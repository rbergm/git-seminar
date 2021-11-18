package de.tudresden.geo.gitseminar.routing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import de.tudresden.geo.gitseminar.util.Count;

/**
 * A {@code Route} represents a non-cyclic sequence of stops at train stations.
 *
 * Each route starts and ends at specific (and different) stations, which are connected via changes
 * of train lines. Routes are opinionated in that they do not allow cycles, i.e. visiting a station
 * more than once. Similarly, using the same train line multiple times is forbidden as well.
 *
 * Routes can be expanded by adding additional stops, but may never shrink.
 *
 * All changes of train lines are represented by a corresponding {@link Change} object, which
 * provides the station at which the change occurred, as well as the line taken from that station.
 * Importantly, this also includes the very first station, where a train was entered for the first
 * time. Depending on context, this may or may not fit the definition of change (as it does not
 * connect one train line with another). To accommodate these different semantics, routes provide
 * two different kinds of accessor methods for changes: methods such as {@code getChanges} provide
 * the stored changes as-is, i.e. also containing the train-entry change. On the other hand, methods
 * containing the marker {@code effective} (such as {@code getEffectiveChanges}) prune the first
 * change. Furthermore, each change object contains a boolean indicator for whether it represents
 * the train-entry change, or not.
 *
 * @author Rico Bergmann
 *
 */
public class Route {

	/**
	 * Constructs an initial route, which directly connects to stations.
	 */
	public static Route generate(TrainStation startStation, TrainStation destination,
			TrainLine line) {
		var initialChange = new Change(startStation, line, true);
		return new Route(startStation, Arrays.asList(initialChange), destination, new HashSet<>());
	}

	private final TrainStation startStation;
	private final List<Change> changes;
	private final TrainStation finalStop;

	/**
	 * Passed stations represent all stations that are neither start station, nor final station, but
	 * have been visited somewhere on the path. At least, this will include all stations where a
	 * change of train lines took place, but can also include other stations that have been simply
	 * passed-by.
	 *
	 * This attribute is of great importance for detecting all sorts of cycles efficiently.
	 */
	private final Set<TrainStation> passedStations;

	private Route(TrainStation startStation, List<Change> changes, TrainStation finalStop,
			Set<TrainStation> passedStations) {
		super();
		this.startStation = startStation;
		this.changes = changes;
		this.finalStop = finalStop;
		this.passedStations = passedStations;
	}

	public TrainStation getStartStation() {
		return startStation;
	}

	public List<Change> getChanges() {
		return changes;
	}

	public List<Change> getEffectiveChanges() {
		if (changes.size() == 1) {
			return new ArrayList<>();
		}
		var effectiveChanges = new ArrayList<>(this.changes);
		effectiveChanges.remove(0);
		return effectiveChanges;
	}

	public TrainStation getFinalStop() {
		return finalStop;
	}

	public Count countChanges() {
		return Count.of(changes.size());
	}

	public Count countEffectiveChanges() {
		// don't count the initial change, which resembles entering the very first train
		return Count.generateFrom(changes.size() - 1);
	}

	public Route expandBy(TrainStation nextStop, TrainLine lineTaken) {
		// check for cycles in our route

		// a cycle may occur towards the start of our route, towards its end, or to some intermediate
		// station.
		if (this.startStation.equals(nextStop) || this.finalStop.equals(nextStop)
				|| this.passedStations.contains(nextStop)) {
			throw new CyclicRouteException();
		}

		// also check that we do not reuse lines we already took
		int changeIdx = 1;
		int nChanges = changes.size();
		for (var change : changes) {
			if (change.targetLine.equals(lineTaken) && changeIdx < nChanges) {
				throw new WigglingChangeException();
			}
			changeIdx++;
		}

		// now that we know that the route is valid, we can built the necessary data for it
		List<Change> expandedChanges = new ArrayList<>(this.changes);
		Set<TrainStation> expandedPassedStations = new HashSet<>(this.passedStations);

		expandedPassedStations.add(this.finalStop);

		// there will always be at least one change: the one at the first station
		var lastChangeIdx = this.changes.size() - 1;
		var lastChange = this.changes.get(lastChangeIdx);
		if (!lastChange.targetLine.equals(lineTaken)) {
			expandedChanges.add(new Change(this.finalStop, lineTaken, false));
		}

		return new Route(this.startStation, expandedChanges, nextStop, expandedPassedStations);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((changes == null) ? 0 : changes.hashCode());
		result = prime * result + ((finalStop == null) ? 0 : finalStop.hashCode());
		result = prime * result + ((startStation == null) ? 0 : startStation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Route))
			return false;
		Route other = (Route) obj;
		if (changes == null) {
			if (other.changes != null)
				return false;
		} else if (!changes.equals(other.changes))
			return false;
		if (finalStop == null) {
			if (other.finalStop != null)
				return false;
		} else if (!finalStop.equals(other.finalStop))
			return false;
		if (startStation == null) {
			if (other.startStation != null)
				return false;
		} else if (!startStation.equals(other.startStation))
			return false;
		return true;
	}

	@Override
	public String toString() {
		var routeComponents = new StringBuilder(changes.size());
		for (var change : changes) {
			routeComponents.append(
					change.getStation().getName() + " -[" + change.getTargetLine().getName() + "]-> ");
		}
		routeComponents.append(finalStop.getName());

		return routeComponents.toString();
	}

	public static class Change {
		private final TrainStation station;
		private final TrainLine targetLine;
		private final boolean trainEntry;

		public Change(TrainStation station, TrainLine targetLine, boolean trainEntry) {
			this.station = station;
			this.targetLine = targetLine;
			this.trainEntry = trainEntry;
		}

		public TrainStation getStation() {
			return station;
		}

		public TrainLine getTargetLine() {
			return targetLine;
		}

		public boolean isTrainEntry() {
			return trainEntry;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((station == null) ? 0 : station.hashCode());
			result = prime * result + ((targetLine == null) ? 0 : targetLine.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!(obj instanceof Change))
				return false;
			Change other = (Change) obj;
			if (station == null) {
				if (other.station != null)
					return false;
			} else if (!station.equals(other.station))
				return false;
			if (targetLine == null) {
				if (other.targetLine != null)
					return false;
			} else if (!targetLine.equals(other.targetLine))
				return false;
			return true;
		}

		@Override
		public String toString() {
			var prefix = trainEntry ? "TrainEntry " : "Change ";
			return prefix + "[station=" + station + ", targetLine=" + targetLine + "]";
		}

	}

}
