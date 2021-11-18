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
import com.google.common.collect.Lists;
import de.tudresden.geo.gitseminar.routing.StationConnection;
import de.tudresden.geo.gitseminar.routing.TrainLine;
import de.tudresden.geo.gitseminar.routing.TrainStation;
import de.tudresden.geo.gitseminar.routing.network.LineInfoJSON.LineInfo;

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

			for (var connectionJson : Lists.partition(lineJson.getRouteNormalized(), 2)) {
				if (connectionJson.size() != 2) {
					continue;
				}

				var start = TrainStation.ofName(connectionJson.get(0)).get();
				var end = TrainStation.ofName(connectionJson.get(1)).get();
				trainLineNetwork.addVertex(start);
				trainLineNetwork.addVertex(end);

				var connection = trainLineNetwork.getEdge(start, end);

				if (connection != null) {
					connection.addLineIfNotPresent(trainLine);
				} else {
					trainLineNetwork.addEdge(start, end, StationConnection.serving(trainLine));
				}
			}
		}

		return trainLineNetwork;
	}

	TrainLine parseTrainLine(LineInfo rawData) {
		String lineNumber = "";
		if (rawData.getTrainType().equals("S")) {
			lineNumber = "S" + rawData.getLine();
		} else {
			lineNumber = rawData.getLine();
		}
		return TrainLine.of(lineNumber);
	}

}
