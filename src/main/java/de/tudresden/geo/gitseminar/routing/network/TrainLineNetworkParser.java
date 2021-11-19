package de.tudresden.geo.gitseminar.routing.network;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tudresden.geo.gitseminar.routing.RoutingProperties;
import de.tudresden.geo.gitseminar.routing.StationConnection;
import de.tudresden.geo.gitseminar.routing.TrainLine;
import de.tudresden.geo.gitseminar.routing.TrainStation;
import de.tudresden.geo.gitseminar.routing.network.LineInfoJSON.LineInfo;
import de.tudresden.geo.gitseminar.util.SlidingWindow;

@Service
public class TrainLineNetworkParser {

	private static final Logger log = LoggerFactory.getLogger(TrainLineNetworkParser.class);

	public Optional<Graph<TrainStation, StationConnection>> loadFromJsonFile(File file) {
		var jsonData = deserializeJsonFile(file);
		Optional<Graph<TrainStation, StationConnection>> graphFromLineInfo =
				jsonData.map(this::convertLineInfoToStationNetwork);
		return graphFromLineInfo;
	}

	Optional<LineInfoJSON> deserializeJsonFile(File file) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			var jsonData = mapper.readValue(file, LineInfoJSON.class);
			return Optional.of(jsonData);
		} catch (IOException e) {
			log.warn("{}", e);
			return Optional.empty();
		}
	}

	Graph<TrainStation, StationConnection> convertLineInfoToStationNetwork(LineInfoJSON lineData) {
		Graph<TrainStation, StationConnection> trainLineNetwork =
				new DefaultUndirectedGraph<>(StationConnection.class);

		for (var lineJson : lineData.getData()) {
			var trainLine = parseTrainLine(lineJson);
			var rawRoute = lineJson.getRouteNormalized();

			for (var connectionJson : SlidingWindow.over(rawRoute, 2)) {
				if (connectionJson.size() != 2) {
					continue;
				}

				var start = generateTrainStation(connectionJson.get(0));
				var end = generateTrainStation(connectionJson.get(1));
				var addedStart = trainLineNetwork.addVertex(start);
				var addedEnd = trainLineNetwork.addVertex(end);

				if (addedStart) {
					log.debug("Creating new node for start station {}", start);
				}
				if (addedEnd) {
					log.debug("Creating new node for end station {}", end);
				}

				var connection = trainLineNetwork.getEdge(start, end);

				if (connection != null) {
					connection.addLineIfNotPresent(trainLine);
				} else {
					var edgeAdded =
							trainLineNetwork.addEdge(start, end, StationConnection.serving(trainLine));
					if (!edgeAdded) {
						log.warn("Could not add edge for line {} between stations {} and {}", trainLine, start,
								end);
					}
				}
			}

		}

		return trainLineNetwork;
	}

	TrainLine parseTrainLine(LineInfo rawData) {
		if (rawData.getLine() == null) {
			return TrainLine.of(rawData.getTrainType());
		}

		String lineNumber = "";
		if (rawData.getTrainType().equals("S")) {
			lineNumber = "S" + rawData.getLine();
		} else {
			lineNumber = rawData.getLine();
		}
		return TrainLine.of(lineNumber);
	}

	TrainStation generateTrainStation(String name) {
		var station = TrainStation.ofName(name);

		// Heuristic to check whether a station is Oberzentrum or Mittelzentrum
		if (RoutingProperties.Oberzentren.stream().anyMatch(name::startsWith)) {
			station.oberzentrum();
			station.mittelzentrum();
		} else if (RoutingProperties.Mittelzentren.stream().anyMatch(name::startsWith)) {
			station.mittelzentrum();
		}

		return station.get();
	}

}
