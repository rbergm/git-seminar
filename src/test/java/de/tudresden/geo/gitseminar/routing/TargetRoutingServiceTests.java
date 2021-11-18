package de.tudresden.geo.gitseminar.routing;

import static org.assertj.core.api.Assertions.assertThat;
import org.jgrapht.Graph;
import org.junit.jupiter.api.Test;
import de.tudresden.geo.gitseminar.util.Count;

public class TargetRoutingServiceTests {

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


	// TODO more extensive testing on larger datasets

	private static class ImpossibleTargetSpecification implements TargetSpecification {

		@Override
		public boolean isSatisfiedBy(TrainStation station) {
			return false;
		}

	}

}
