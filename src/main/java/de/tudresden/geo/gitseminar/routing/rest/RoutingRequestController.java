package de.tudresden.geo.gitseminar.routing.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import de.tudresden.geo.gitseminar.routing.MittelzentrumTargetSpecification;
import de.tudresden.geo.gitseminar.routing.OberzentrumTargetSpecification;
import de.tudresden.geo.gitseminar.routing.StartStationMatchesTargetSpecificationException;
import de.tudresden.geo.gitseminar.routing.TargetRoutingService;
import de.tudresden.geo.gitseminar.routing.TargetSpecification;
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

  @PostMapping("/routing/calculate")
  public ResponseEntity<RoutingResponse> calculateRoute(@RequestBody RoutingRequest routingData) {

    var start = networkSupplier.getTrainStation(routingData.getStartStation());
    TargetSpecification target = null;
    if (routingData.getTarget().equalsIgnoreCase("mittelzentrum")) {
      target = new MittelzentrumTargetSpecification();
    } else if (routingData.getTarget().equalsIgnoreCase("oberzentrum")) {
      target = new OberzentrumTargetSpecification();
    } else {
      throw new IllegalArgumentException(
          "Unknown target specification: " + routingData.getTarget());
    }

    // TODO: handle IArgE if the vertex does not exist

    RoutingResponse response;
    HttpStatus responseStatus;
    try {
      var route = routingService.findTarget(networkSupplier.getNetwork(), start, target);
      if (route.isEmpty()) {
        response = RoutingResponse.noRoute();
        responseStatus = HttpStatus.NOT_FOUND;
      } else {
        response = RoutingResponse.ok(route.get());
        responseStatus = HttpStatus.OK;
      }
    } catch (StartStationMatchesTargetSpecificationException e) {
      response = RoutingResponse.startMatchesTarget();
      responseStatus = HttpStatus.OK;
    }

    return ResponseEntity.status(responseStatus).body(response);
  }

}
