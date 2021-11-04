package de.tudresden.geo.gitseminar.routing;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import org.jgrapht.Graph;
import org.springframework.stereotype.Service;

@Service
public class TargetRoutingService {

	public Optional<TrainStation> findTarget(Graph<TrainStation, StationConnection> trainlineNetwork,
			TrainStation start, TargetSpecification target) {
		if (target.isSatisfiedBy(start)) {
			return Optional.of(start);
		}

		// expand queue to also keep track of the route chosen
		Queue<TrainStation> stationQueue = new LinkedList<>();
		Set<TrainStation> visitedStations = new HashSet<>();

		TrainStation bestStation = null;
		int bestMaxChanges = Integer.MAX_VALUE;

		for (var connection : trainlineNetwork.edgesOf(start)) {
			stationQueue.add(trainlineNetwork.getEdgeTarget(connection));
		}
		visitedStations.add(start);

		while (!stationQueue.isEmpty()) {
			TrainStation currentStation = stationQueue.poll();
			if (visitedStations.contains(currentStation)) {
				// expand test to check for shorter routes
				continue;
			}

			if (target.isSatisfiedBy(currentStation)) {
				// check if matching node actually constitutes an improvement
				return Optional.of(currentStation);
			}

			var neighborConnections = trainlineNetwork.edgesOf(currentStation);
			for (var connection : neighborConnections) {
				// query line information here. If multiple lines serve a connection, add them all
				stationQueue.add(trainlineNetwork.getEdgeTarget(connection));
			}
			visitedStations.add(currentStation);
		}

		return Optional.empty();
	}

}
