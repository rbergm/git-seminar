package de.tudresden.geo.gitseminar.routing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import de.tudresden.geo.gitseminar.util.Count;

public class Route {

	public static Route generate(TrainStation startStation, TrainStation destination,
			TrainLine line) {
		var initialChange = new Change(startStation, line);
		return new Route(startStation, Arrays.asList(initialChange), destination);
	}

	private final TrainStation startStation;
	private final List<Change> changes;
	private final TrainStation finalStop;

	private Route(TrainStation startStation, List<Change> changes, TrainStation finalStop) {
		super();
		this.startStation = startStation;
		this.changes = changes;
		this.finalStop = finalStop;
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
		List<Change> expandedChanges = new ArrayList<>(this.changes);

		// check for cycles in our route
		for (var change : changes) {
			if (change.station.equals(nextStop)) {
				throw new CyclicRouteException("Cycle at change " + change);
			}
		}

		// there will always be at least one change: the one at the first station
		var lastChangeIdx = this.changes.size() - 1;
		var lastChange = this.changes.get(lastChangeIdx);
		if (!lastChange.targetLine.equals(lineTaken)) {
			expandedChanges.add(new Change(this.finalStop, lineTaken));
		}

		return new Route(this.startStation, expandedChanges, nextStop);
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
		return "Route [startStation=" + startStation + ", changes=" + changes + ", finalStop="
				+ finalStop + "]";
	}

	public static class Change {
		private final TrainStation station;
		private final TrainLine targetLine;

		public Change(TrainStation station, TrainLine targetLine) {
			this.station = station;
			this.targetLine = targetLine;
		}

		public TrainStation getStation() {
			return station;
		}

		public TrainLine getTargetLine() {
			return targetLine;
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
			return "Change [station=" + station + ", targetLine=" + targetLine + "]";
		}

	}

}
