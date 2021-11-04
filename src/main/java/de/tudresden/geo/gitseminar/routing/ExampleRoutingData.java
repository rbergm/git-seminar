package de.tudresden.geo.gitseminar.routing;

import java.util.Arrays;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedGraph;

public class ExampleRoutingData {

	public static Graph<TrainStation, StationConnection> get() {
		TrainStation ddHbf = TrainStation.ofName("Dresden Hbf").oberzentrum().get();
		TrainStation leHbf = TrainStation.ofName("Leipzig Hbf").oberzentrum().get();
		TrainStation chHbf = TrainStation.ofName("Chemnitz Hbf").oberzentrum().get();
		TrainStation freiberg = TrainStation.ofName("Freiberg (Sachs)").mittelzentrum().get();
		TrainStation tharandt = TrainStation.ofName("Tharandt").get();
		TrainStation niederwiesa = TrainStation.ofName("Niederwiesa").get();

		TrainLine re3 = TrainLine.of("RE3"); // Dresden - Hof
		TrainLine rb30 = TrainLine.of("RB30"); // Dresden - Zwickau
		TrainLine re50 = TrainLine.of("RE50"); // Leipzig - Dresden
		TrainLine ic2444 = TrainLine.of("IC2444"); // Leipzig - Dresden
		TrainLine s3 = TrainLine.of("S3"); // Dresden - Freiberg
		TrainLine re6 = TrainLine.of("RE6"); // Chemnitz - Leipzig

		StationConnection ddhbf_tharandt = StationConnection.serving(re3, rb30, s3);
		StationConnection tharandt_freiberg = StationConnection.serving(re3, rb30, s3);
		StationConnection freiberg_niederwiesa = StationConnection.serving(rb30);
		StationConnection niederwiesa_chemnitz = StationConnection.serving(rb30);
		StationConnection freiberg_chemnitz = StationConnection.serving(re3);

		StationConnection ddhbf_lehbf = StationConnection.serving(re50, ic2444);

		StationConnection chhbf_lehbf = StationConnection.serving(re6);

		Graph<TrainStation, StationConnection> network =
				new DefaultUndirectedGraph<>(StationConnection.class);
		for (var ts : Arrays.asList(ddHbf, leHbf, chHbf, freiberg, tharandt, niederwiesa)) {
			network.addVertex(ts);
		}
		network.addEdge(ddHbf, tharandt, ddhbf_tharandt);
		network.addEdge(tharandt, freiberg, tharandt_freiberg);
		network.addEdge(freiberg, niederwiesa, freiberg_niederwiesa);
		network.addEdge(niederwiesa, chHbf, niederwiesa_chemnitz);
		network.addEdge(freiberg, chHbf, freiberg_chemnitz);
		network.addEdge(ddHbf, leHbf, ddhbf_lehbf);
		network.addEdge(chHbf, leHbf, chhbf_lehbf);

		return network;
	}

}
