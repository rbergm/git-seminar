package de.tudresden.geo.gitseminar.routing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jgrapht.graph.DefaultWeightedEdge;

public class StationConnection extends DefaultWeightedEdge {

	private static final long serialVersionUID = -2163683886019819520L;

	public static StationConnection serving(List<TrainLine> lines) {
		return new StationConnection(lines);
	}

	public static StationConnection serving(TrainLine... lines) {
		return new StationConnection(Arrays.asList(lines));
	}

	private List<TrainLine> lines;

	private StationConnection(List<TrainLine> servedLines) {
		this.lines = new ArrayList<>(servedLines);
	}

	public List<TrainLine> getLines() {
		return lines;
	}

	public void setLines(List<TrainLine> lines) {
		this.lines = lines;
	}

	public void addLineIfNotPresent(TrainLine line) {
		if (!this.lines.contains(line)) {
			this.lines.add(line);
		}
	}

	@Override
	public String toString() {
		return "" + lines;
	}

}
