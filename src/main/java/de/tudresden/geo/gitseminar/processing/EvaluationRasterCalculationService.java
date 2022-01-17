package de.tudresden.geo.gitseminar.processing;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.geotools.coverage.grid.GridCoverage2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class EvaluationRasterCalculationService {

  public enum Rasters {
    StationEvaluation("station_departures.tif"), //
    MittelzentrumRoutes("routes_mittelzentren.tif"), //
    OberzentrumRoutes("routes_oberzentrum.tif"), //
    Population("population.tif"), //
    TrainStationDistance("station_distance.tif");

    private final String name;

    private Rasters(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

  }

  public static void setDefaultWeight(double newDefault) {
    defaultWeight = newDefault;
  }

  public static Map<Rasters, Double> generateWeights(double stationEvaluationWeight,
      double mittelzentrumRoutesWeight, double oberzentrumRoutesWeight, double populationWeight,
      double stationDistanceWeight) {
    Map<Rasters, Double> weights = new HashMap<>();
    weights.put(Rasters.StationEvaluation, stationEvaluationWeight);
    weights.put(Rasters.MittelzentrumRoutes, mittelzentrumRoutesWeight);
    weights.put(Rasters.OberzentrumRoutes, oberzentrumRoutesWeight);
    weights.put(Rasters.Population, populationWeight);
    weights.put(Rasters.TrainStationDistance, stationDistanceWeight);
    return weights;
  }

  private static final Logger log =
      LoggerFactory.getLogger(EvaluationRasterCalculationService.class);

  private static double defaultWeight = 5.0;

  private CoverageReader coverageReader;
  private CoverageWriter coverageWriter;
  Resource stationEvalRasterResource;
  Resource mittelzentrumRoutesRasterResource;
  Resource oberzentrumRoutesRasterResource;
  Resource populationRasterResource;
  Resource trainStationDistanceRasterResource;

  public EvaluationRasterCalculationService(CoverageReader coverageReader,
      CoverageWriter coverageWriter,
      @Value("${raster.src.stationeval}") String stationEvalRasterPath,
      @Value("${raster.src.mzroutes}") String mittelzentrumRoutesRasterPath,
      @Value("${raster.src.ozroutes}") String oberzentrumRoutesRasterPath,
      @Value("${raster.src.population}") String populationRasterPath,
      @Value("${raster.src.distance}") String trainStationDistanceRasterPath) {
    this.coverageReader = coverageReader;
    this.coverageWriter = coverageWriter;

    log.debug("Setup with raster sources stationEval={}, mzRoutes={}, ozRoutes={}, pop={}, dist={}",
        stationEvalRasterPath, mittelzentrumRoutesRasterPath, oberzentrumRoutesRasterPath,
        populationRasterPath, trainStationDistanceRasterPath);

    this.stationEvalRasterResource = new ClassPathResource(stationEvalRasterPath);
    this.mittelzentrumRoutesRasterResource = new ClassPathResource(mittelzentrumRoutesRasterPath);
    this.oberzentrumRoutesRasterResource = new ClassPathResource(oberzentrumRoutesRasterPath);
    this.populationRasterResource = new ClassPathResource(populationRasterPath);
    this.trainStationDistanceRasterResource = new ClassPathResource(trainStationDistanceRasterPath);
  }

  public GridCoverage2D compute(Map<Rasters, Double> weights) throws IOException {
    GridCoverage2D statEvalRaster =
        coverageReader.loadCoverage(stationEvalRasterResource.getFile()).orElseThrow();
    GridCoverage2D mzRouteRaster =
        coverageReader.loadCoverage(mittelzentrumRoutesRasterResource.getFile()).orElseThrow();
    GridCoverage2D ozRouteRaster =
        coverageReader.loadCoverage(oberzentrumRoutesRasterResource.getFile()).orElseThrow();
    GridCoverage2D popRaster =
        coverageReader.loadCoverage(populationRasterResource.getFile()).orElseThrow();
    GridCoverage2D distRaster =
        coverageReader.loadCoverage(trainStationDistanceRasterResource.getFile()).orElseThrow();

    var operatorBuilder = WeightedSumOperator.create();

    if (weights.containsKey(Rasters.StationEvaluation)) {
      operatorBuilder.raster(statEvalRaster).weight(weights.get(Rasters.StationEvaluation));
    } else {
      operatorBuilder.raster(statEvalRaster).weight(defaultWeight);
    }

    if (weights.containsKey(Rasters.MittelzentrumRoutes)) {
      operatorBuilder.raster(mzRouteRaster).weight(weights.get(Rasters.MittelzentrumRoutes));
    } else {
      operatorBuilder.raster(mzRouteRaster).weight(defaultWeight);
    }

    if (weights.containsKey(Rasters.OberzentrumRoutes)) {
      operatorBuilder.raster(ozRouteRaster).weight(weights.get(Rasters.OberzentrumRoutes));
    } else {
      operatorBuilder.raster(ozRouteRaster).weight(defaultWeight);
    }

    if (weights.containsKey(Rasters.Population)) {
      operatorBuilder.raster(popRaster).weight(weights.get(Rasters.Population));
    } else {
      operatorBuilder.raster(popRaster).weight(defaultWeight);
    }

    if (weights.containsKey(Rasters.TrainStationDistance)) {
      operatorBuilder.raster(distRaster).weight(weights.get(Rasters.TrainStationDistance));
    } else {
      operatorBuilder.raster(distRaster).weight(defaultWeight);
    }

    operatorBuilder.normalizeWeights();

    WeightedSumOperator weightedSumOperator = operatorBuilder.build();
    log.debug("Weights: {}", weightedSumOperator.getWeights());

    return weightedSumOperator.calculate();
  }

  public void computeAndStore(Map<Rasters, Double> weights, String outName) throws IOException {
    var raster = compute(weights);
    coverageWriter.storeCoverage(raster, outName);
  }

}
