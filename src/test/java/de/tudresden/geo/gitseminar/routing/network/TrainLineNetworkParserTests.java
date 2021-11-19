package de.tudresden.geo.gitseminar.routing.network;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import de.tudresden.geo.gitseminar.routing.util.GraphVisualizationService;

public class TrainLineNetworkParserTests {

	private static final Logger log = LoggerFactory.getLogger(TrainLineNetworkParserTests.class);
	private static final boolean visualize = true;

	@Test
	public void deserializationProvidesCorrectObject() throws IOException {
		Resource jsonResource = new ClassPathResource("timetables.json");
		var parser = new TrainLineNetworkParser();

		var result = parser.deserializeJsonFile(jsonResource.getFile());

		assertThat(result).isPresent();

		var lineInfos = result.get();
		assertThat(lineInfos.getData()).isNotEmpty();

		var firstLineInfo = lineInfos.getData().get(0);
		assertThat(firstLineInfo.getTrainType()).isEqualTo("VBG");

		log.debug("First line info: {}", firstLineInfo);
	}

	@Test
	public void testCompleteWorkflow() throws IOException {
		Resource jsonResource = new ClassPathResource("timetables.json");
		var parser = new TrainLineNetworkParser();

		var result = parser.loadFromJsonFile(jsonResource.getFile());
		assertThat(result).isPresent();

		var trainLineNetwork = result.get();

		if (visualize) {
			log.debug("Tests finished, visualizing results now.");
			GraphVisualizationService.saveTrainLineNetwork(trainLineNetwork, "fullgraph.png");
		}

	}

	@Test
	public void detectsOberzentrum() {
		var stationName = "Dresden (Hbf)";
		var parser = new TrainLineNetworkParser();

		var station = parser.generateTrainStation(stationName);
		assertThat(station.isOberzentrum()).isTrue();

		// Oberzentrum should also be considered Mittelzentrum
		assertThat(station.qualifiesAsMittelzentrum()).isTrue();
		assertThat(station.isMittelzentrum()).isFalse();
	}

	@Test
	public void detectsMittelzentrum() {
		var stationName = "Freital-Hainsberg";
		var parser = new TrainLineNetworkParser();

		var station = parser.generateTrainStation(stationName);
		assertThat(station.isMittelzentrum()).isTrue();
		assertThat(station.qualifiesAsMittelzentrum()).isTrue();

		assertThat(station.isOberzentrum()).isFalse();
	}

	@Test
	public void detectsNormalStations() {
		var stationName = "Oberwiesenthal";
		var parser = new TrainLineNetworkParser();

		var station = parser.generateTrainStation(stationName);
		assertThat(station.isMittelzentrum()).isFalse();
		assertThat(station.qualifiesAsMittelzentrum()).isFalse();
		assertThat(station.isOberzentrum()).isFalse();
	}

}
