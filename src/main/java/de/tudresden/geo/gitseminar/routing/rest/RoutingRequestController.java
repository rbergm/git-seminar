package de.tudresden.geo.gitseminar.routing.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import de.tudresden.geo.gitseminar.routing.MittelzentrumTargetSpecification;
import de.tudresden.geo.gitseminar.routing.OberzentrumTargetSpecification;
import de.tudresden.geo.gitseminar.routing.Route;
import de.tudresden.geo.gitseminar.routing.TargetRoutingService;
import de.tudresden.geo.gitseminar.routing.TargetSpecification;
import de.tudresden.geo.gitseminar.routing.TrainStation;
import de.tudresden.geo.gitseminar.routing.network.TrainNetworkSupplier;

@RestController
public class RoutingRequestController {

	private TargetRoutingService routingService;
	private TrainNetworkSupplier networkSupplier;

	public RoutingRequestController(TargetRoutingService routingService,
			TrainNetworkSupplier networkSupplier) {
		this.routingService = routingService;
		this.networkSupplier = networkSupplier;
	}

	public ResponseEntity<Route> calculateRoute(RoutingRequest routingData) {
		var start = TrainStation.ofName(routingData.getStartStation()).get();
		TargetSpecification target = null;
		if (routingData.getTarget().toLowerCase().equals("mittelzentrum")) {
			target = new MittelzentrumTargetSpecification();
		} else if (routingData.getTarget().toLowerCase().equals("oberzentrum")) {
			target = new OberzentrumTargetSpecification();
		} else {
			throw new IllegalArgumentException(
					"Unknown target specification: " + routingData.getTarget());
		}

		var route = routingService.findTarget(networkSupplier.getNetwork(), start, target);

		if (route.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		return ResponseEntity.status(HttpStatus.OK).body(route.get());
	}

}
