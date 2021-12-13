package de.tudresden.geo.gitseminar.processing.rest;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
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

    String id = requestIdentifier.toIdentifier(requestData);

    if (geoserver.hasDatastore(id)) {
      String wcsPath = geoserver.getWCSForDatastore(id);
      return ResponseEntity.status(HttpStatus.OK).body(RasterCalculationResponse.cached(wcsPath));
    } else {
      Map<Rasters, Double> weightMap = new EnumMap<>(Rasters.class);
      weightMap.put(Rasters.StationEvaluation, requestData.getDepartureFrequencyWeight());
      weightMap.put(Rasters.MittelzentrumRoutes, requestData.getMittelzentrumDistanceWeight());
      weightMap.put(Rasters.OberzentrumRoutes, requestData.getOberzentrumDistanceWeight());
      weightMap.put(Rasters.TrainStationDistance, requestData.getStationDistanceWeight());
      weightMap.put(Rasters.Population, requestData.getPopulationDataWeight());

      var resultingRaster = calculationService.compute(weightMap);
      geoserver.createDatastore(id);
      geoserver.uploadToDatastore(id, resultingRaster);

      String wcsPath = geoserver.getWCSForDatastore(id);
      return ResponseEntity.status(HttpStatus.OK).body(RasterCalculationResponse.created(wcsPath));
    }
  }

}
