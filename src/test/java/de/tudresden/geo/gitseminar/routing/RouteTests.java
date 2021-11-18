package de.tudresden.geo.gitseminar.routing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RouteTests {

	private Route route;

	@BeforeEach
	public void setup() {
		route = Route.generate(ExampleRoutingData.chemnitzHbf(), ExampleRoutingData.dresdenHbf(),
				ExampleRoutingData.re3());
	}

	@Test
	public void assumesChangeIfNewLine() {
		var initialChanges = route.countEffectiveChanges();

		var nextStation = ExampleRoutingData.leipzigHbf();
		var nextLine = ExampleRoutingData.re6();

		route = route.expandBy(nextStation, nextLine);

		assertThat(route.countEffectiveChanges()).isEqualTo(initialChanges.increasedBy(1));
		assertThat(route.getFinalStop()).isEqualTo(nextStation);
	}

	@Test
	public void doesNotChangeIfLineStaysTheSame() {
		var lineTaken = ExampleRoutingData.rb30();
		route =
				Route.generate(ExampleRoutingData.chemnitzHbf(), ExampleRoutingData.freiberg(), lineTaken);
		var initialChanges = route.countEffectiveChanges();

		route = route.expandBy(ExampleRoutingData.dresdenHbf(), lineTaken);
		assertThat(route.countEffectiveChanges()).isEqualTo(initialChanges);
		assertThat(route.getFinalStop()).isEqualTo(ExampleRoutingData.dresdenHbf());
	}

	@Test
	public void rejectsDirectBackwardLinks() {
		assertThrows(CyclicRouteException.class, () -> {
			route.expandBy(ExampleRoutingData.chemnitzHbf(), ExampleRoutingData.re3());
		});

		assertThrows(CyclicRouteException.class, () -> {
			route.expandBy(ExampleRoutingData.chemnitzHbf(), ExampleRoutingData.rb30());
		});
	}

	@Test
	public void detectsFullCycles() {
		route = route.expandBy(ExampleRoutingData.leipzigHbf(), ExampleRoutingData.re50());

		assertThrows(CyclicRouteException.class, () -> {
			route.expandBy(ExampleRoutingData.chemnitzHbf(), ExampleRoutingData.re6());
		});
	}

	@Test
	public void detectsTransitiveCycles() {
		route = Route.generate(ExampleRoutingData.chemnitzHbf(), ExampleRoutingData.freiberg(),
				ExampleRoutingData.rb30());
		route = route.expandBy(ExampleRoutingData.dresdenHbf(), ExampleRoutingData.re3());

		assertThrows(CyclicRouteException.class, () -> {
			route.expandBy(ExampleRoutingData.freiberg(), ExampleRoutingData.re3());
		});
	}

	// TODO: test wiggling changes and passed station resilience

}
