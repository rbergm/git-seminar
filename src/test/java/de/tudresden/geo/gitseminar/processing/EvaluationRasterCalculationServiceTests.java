package de.tudresden.geo.gitseminar.processing;

import java.io.IOException;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EvaluationRasterCalculationServiceTests {

  @Autowired
  private EvaluationRasterCalculationService calculationService;

  @Test
  public void allDistanceRasters() throws IOException {
    setAllRastersToDistance(calculationService);
    var resultGrid = calculationService.compute(new HashMap<>());
  }

  @Test
  public void mittelzentrumRoutesRaster() throws IOException {
    useRealMittelzentrumRaster(calculationService);
    var resulGrid = calculationService.compute(new HashMap<>());
  }

  @Test
  public void allRealRasters() throws IOException {
    useAllREalRasters(calculationService);
    var resulGrid = calculationService.compute(new HashMap<>());
  }

  @Test
  public void toleratesZeroWeight() throws IOException {
    useAllREalRasters(calculationService);
    var weights = EvaluationRasterCalculationService.generateWeights(0, 0, 0, 0, 1);
    var resulGrid = calculationService.compute(weights);
  }


  private void setAllRastersToDistance(EvaluationRasterCalculationService calculationService) {
    calculationService.stationEvalRasterResource = new ClassPathResource("dist_scaled_raster.tif");
    calculationService.mittelzentrumRoutesRasterResource =
        new ClassPathResource("dist_scaled_raster.tif");
    calculationService.oberzentrumRoutesRasterResource =
        new ClassPathResource("dist_scaled_raster.tif");
    calculationService.populationRasterResource = new ClassPathResource("dist_scaled_raster.tif");
    calculationService.trainStationDistanceRasterResource =
        new ClassPathResource("dist_scaled_raster.tif");
  }

  private void useRealMittelzentrumRaster(EvaluationRasterCalculationService calculationService) {
    setAllRastersToDistance(calculationService);
    calculationService.mittelzentrumRoutesRasterResource =
        new ClassPathResource("mittelzentren_scored_raster.tif");
  }

  private void useAllREalRasters(EvaluationRasterCalculationService calculationService) {
    calculationService.stationEvalRasterResource =
        new ClassPathResource("departures_scaled_raster.tif");
    calculationService.mittelzentrumRoutesRasterResource =
        new ClassPathResource("mittelzentren_scored_raster.tif");
    calculationService.oberzentrumRoutesRasterResource =
        new ClassPathResource("oberzentren_scored_raster.tif");

    // TODO: the real population raster does not yet exist
    calculationService.populationRasterResource = new ClassPathResource("dist_scaled_raster.tif");
    calculationService.trainStationDistanceRasterResource =
        new ClassPathResource("dist_scaled_raster.tif");
  }

}
