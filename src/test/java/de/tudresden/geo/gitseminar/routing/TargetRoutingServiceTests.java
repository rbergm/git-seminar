package de.tudresden.geo.gitseminar.routing;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import org.jgrapht.Graph;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import de.tudresden.geo.gitseminar.routing.network.TrainLineNetworkParser;
import de.tudresden.geo.gitseminar.util.Count;

public class TargetRoutingServiceTests {

	private static final Logger log = LoggerFactory.getLogger(TargetRoutingServiceTests.class);

	private TargetRoutingService routingService = new TargetRoutingService();
	private Graph<TrainStation, StationConnection> network = ExampleRoutingData.get();

	@Test
	public void findsDirectlyConnectedOberzentrum() {
		var start = ExampleRoutingData.niederwiesa();
		var goalSpec = new OberzentrumTargetSpecification();
		var goal = ExampleRoutingData.chemnitzHbf();

		var result = routingService.findTarget(network, start, goalSpec);

		assertThat(result).isPresent();
		assertThat(result.get().getFinalStop()).isEqualTo(goal);
		assertThat(result.get().countEffectiveChanges()).isEqualTo(Count.of(0));
	}

	@Test
	public void findsDistantOberzentrum() {
		var start = ExampleRoutingData.freiberg();
		var goalSpec = new OberzentrumTargetSpecification();

		var result = routingService.findTarget(network, start, goalSpec);

		assertThat(result).isPresent();
	}

	@Test
	public void providesNothingIfNoMatchingStation() {
		var start = ExampleRoutingData.niederwiesa();
		var goalSpec = new ImpossibleTargetSpecification();

		var result = routingService.findTarget(network, start, goalSpec);

		assertThat(result).isEmpty();
	}

	@Test
	public void findsClosestNodeOnLargeGraphs() throws IOException {
		Resource networkDescription = new ClassPathResource("timetables.json");
		var networkParser = new TrainLineNetworkParser();

		var network = networkParser.loadFromJsonFile(networkDescription.getFile());
		assertThat(network).isPresent();


		var start = TrainStation.ofName("Großheringen").get();
		var goalSpec = new OberzentrumTargetSpecification();

		var result = routingService.findTarget(network.get(), start, goalSpec);
		log.debug("First route: {}", result);
		assertThat(result).isPresent();

		start = TrainStation.ofName("Kurort Oberwiesenthal").get();
		result = routingService.findTarget(network.get(), start, goalSpec);
		log.debug("Second route: {}", result);
		assertThat(result).isPresent();
	}


	private static class ImpossibleTargetSpecification implements TargetSpecification {

		@Override
		public boolean isSatisfiedBy(TrainStation station) {
			return false;
		}

	}

}
