package de.tudresden.geo.gitseminar.processing.rest;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import de.tudresden.geo.gitseminar.geoserver.GeoServer;
import de.tudresden.geo.gitseminar.processing.EvaluationRasterCalculationService;
import de.tudresden.geo.gitseminar.processing.EvaluationRasterCalculationService.Rasters;

@RestController
public class RasterCalculationController {

  private static final Logger log = LoggerFactory.getLogger(RasterCalculationController.class);

  private EvaluationRasterCalculationService calculationService;
  private RasterCalculationRequestIdentifier requestIdentifier;
  private GeoServer geoserver;

  public RasterCalculationController(EvaluationRasterCalculationService calculationService,
      RasterCalculationRequestIdentifier requestIdentifier, GeoServer geoserver) {
    this.calculationService = calculationService;
    this.requestIdentifier = requestIdentifier;
    this.geoserver = geoserver;
  }

  @PostMapping("/raster/calculate")
  public ResponseEntity<RasterCalculationResponse> calculateResultRaster(
      @RequestBody RasterCalculationRequestData requestData) throws IOException {

    log.debug("Received request for weights {}", requestData);

    String id = requestIdentifier.toIdentifier(requestData);

    if (geoserver.hasCoverageStore(id)) {
      log.debug("Coverage with ID {} exists, reusing", id);
      String wcsPath = geoserver.getWCSForCoverageStore(id);
      return ResponseEntity.status(HttpStatus.OK)
          .body(RasterCalculationResponse.cached(wcsPath, geoserver.getWorkspace()));
    } else {
      log.debug("No coverage with ID {} exists, creating", id);
      Map<Rasters, Double> weightMap = new EnumMap<>(Rasters.class);
      weightMap.put(Rasters.StationEvaluation, requestData.getDepartureFrequencyWeight());
      weightMap.put(Rasters.MittelzentrumRoutes, requestData.getMittelzentrumDistanceWeight());
      weightMap.put(Rasters.OberzentrumRoutes, requestData.getOberzentrumDistanceWeight());
      weightMap.put(Rasters.TrainStationDistance, requestData.getStationDistanceWeight());
      weightMap.put(Rasters.Population, requestData.getPopulationDataWeight());

      calculationService.computeAndStore(weightMap, id + ".tif");
      geoserver.uploadToCoverageStore(id, id + ".tif");

      String wcsPath = geoserver.getWCSForCoverageStore(id);
      return ResponseEntity.status(HttpStatus.OK)
          .body(RasterCalculationResponse.created(wcsPath, geoserver.getWorkspace()));
    }
  }

}
