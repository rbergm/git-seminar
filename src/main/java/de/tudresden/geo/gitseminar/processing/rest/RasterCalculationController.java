package de.tudresden.geo.gitseminar.processing.rest;

import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import de.tudresden.geo.gitseminar.processing.EvaluationRasterCalculationService;

@RestController
public class RasterCalculationController {

  private EvaluationRasterCalculationService calculationService;

  public RasterCalculationController(EvaluationRasterCalculationService calculationService) {
    this.calculationService = calculationService;
  }


  @PostMapping("/raster/calculate")
  public ResponseEntity<RasterCalculationResponse> calculateResultRaster(
      @RequestBody RasterCalculationRequestData requestData) throws IOException {

    // TODO: check if existing datastore exists. If so, reuse it. Otherwise, create result raster
    // and upload it

    return null;
  }

}
