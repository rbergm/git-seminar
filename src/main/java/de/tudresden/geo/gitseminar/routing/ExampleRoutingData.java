package de.tudresden.geo.gitseminar.routing;

import java.util.Arrays;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedGraph;

public class ExampleRoutingData {

	public static TrainStation dresdenHbf() {
		return TrainStation.ofName("Dresden Hbf").oberzentrum().get();
	}

	public static TrainStation chemnitzHbf() {
		return TrainStation.ofName("Chemnitz Hbf").oberzentrum().get();
	}

	public static TrainStation leipzigHbf() {
		return TrainStation.ofName("Leipzig Hbf").oberzentrum().get();
	}

	public static TrainStation freiberg() {
		return TrainStation.ofName("Freiberg (Sachs)").mittelzentrum().get();
	}

	public static TrainStation niederwiesa() {
		return TrainStation.ofName("Niederwiesa").get();
	}

	public static TrainLine rb30() {
		return TrainLine.of("RB30");
	}

	public static TrainLine re3() {
		return TrainLine.of("RE3");
	}

	public static TrainLine re6() {
		return TrainLine.of("RE6");
	}

	public static TrainLine re50() {
		return TrainLine.of("RE50");
	}

	public static Graph<TrainStation, StationConnection> get() {
		TrainStation ddHbf = dresdenHbf();
		TrainStation leHbf = leipzigHbf();
		TrainStation chHbf = chemnitzHbf();
		TrainStation freiberg = freiberg();
		TrainStation tharandt = TrainStation.ofName("Tharandt").get();
		TrainStation niederwiesa = niederwiesa();
		TrainStation floeha = TrainStation.ofName("Flöha").get();

		TrainLine re3 = re3(); // Dresden - Hof
		TrainLine rb30 = rb30(); // Dresden - Zwickau
		TrainLine re50 = re50(); // Leipzig - Dresden
		TrainLine ic2444 = TrainLine.of("IC2444"); // Leipzig - Dresden
		TrainLine s3 = TrainLine.of("S3"); // Dresden - Freiberg
		TrainLine re6 = re6(); // Chemnitz - Leipzig

		StationConnection ddhbf_tharandt = StationConnection.serving(re3, rb30, s3);
		StationConnection tharandt_freiberg = StationConnection.serving(re3, rb30, s3);
		StationConnection freiberg_floeha = StationConnection.serving(re3);
		StationConnection floeha_niederwiesa = StationConnection.serving(rb30);
		StationConnection niederwiesa_chemnitz = StationConnection.serving(rb30);
		StationConnection floeha_chemnitz = StationConnection.serving(re3);

		StationConnection ddhbf_lehbf = StationConnection.serving(re50, ic2444);

		StationConnection chhbf_lehbf = StationConnection.serving(re6);

		Graph<TrainStation, StationConnection> network =
				new DefaultUndirectedGraph<>(StationConnection.class);
		for (var ts : Arrays.asList(ddHbf, leHbf, chHbf, freiberg, tharandt, niederwiesa, floeha)) {
			network.addVertex(ts);
		}
		network.addEdge(ddHbf, tharandt, ddhbf_tharandt);
		network.addEdge(tharandt, freiberg, tharandt_freiberg);
		network.addEdge(freiberg, floeha, freiberg_floeha);
		network.addEdge(niederwiesa, chHbf, niederwiesa_chemnitz);
		network.addEdge(floeha, niederwiesa, floeha_niederwiesa);
		network.addEdge(floeha, chHbf, floeha_chemnitz);
		network.addEdge(ddHbf, leHbf, ddhbf_lehbf);
		network.addEdge(chHbf, leHbf, chhbf_lehbf);

		return network;
	}

}
