package de.tudresden.geo.gitseminar.routing;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import org.jgrapht.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import de.tudresden.geo.gitseminar.util.Count;

@Service
public class TargetRoutingService {

	private static final Logger log = LoggerFactory.getLogger(TargetRoutingService.class);

	public Optional<Route> findTarget(Graph<TrainStation, StationConnection> trainlineNetwork,
			TrainStation start, TargetSpecification target) {
		if (target.isSatisfiedBy(start)) {
			throw new StartStationMatchesTargetSpecificationException();
		}

		Queue<Route> routeQueue = new LinkedList<>();

		Route bestRoute = null;
		Count bestMaxChanges = Count.of(Integer.MAX_VALUE);

		for (var connection : trainlineNetwork.edgesOf(start)) {
			for (var line : connection.getLines()) {
				try {
					var destination = trainlineNetwork.getEdgeTarget(connection);
					routeQueue.add(Route.generate(start, destination, line));
				} catch (CyclicRouteException e) {
					log.trace(e.getMessage());
				}
			}
		}

		while (!routeQueue.isEmpty()) {
			// Poll retrieves *and removes* the first element in our queue
			Route candidateRoute = routeQueue.poll();

			if (target.isSatisfiedBy(candidateRoute.getFinalStop())) {
				if (candidateRoute.countEffectiveChanges().isLessThan(bestMaxChanges)) {
					bestRoute = candidateRoute;
					bestMaxChanges = bestRoute.countEffectiveChanges();
				} else {
					// Although we found a matching target station, its route is already worse
					// than the best route. Therefore, we can simply drop this route.
				}
			} else if (candidateRoute.countEffectiveChanges().isLessThan(bestMaxChanges)) {
				var neighborConnections = trainlineNetwork.edgesOf(candidateRoute.getFinalStop());
				for (var connection : neighborConnections) {
					for (var line : connection.getLines()) {
						// TODO: don't consider the backward link to our previous station
						try {
							var destination = trainlineNetwork.getEdgeTarget(connection);
							routeQueue.add(candidateRoute.expandBy(destination, line));
						} catch (CyclicRouteException e) {
							log.trace(e.getMessage());
						}
					}
				}
			} else {
				// The candidate is already worse than our best route. Therefore, it can never lead to an
				// improvement. We can simply drop this route.
			}
		}

		if (bestRoute != null) {
			return Optional.of(bestRoute);
		} else {
			return Optional.empty();
		}
	}

}
