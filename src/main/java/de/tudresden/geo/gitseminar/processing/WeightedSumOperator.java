package de.tudresden.geo.gitseminar.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.Operations;
import org.opengis.coverage.Coverage;
import org.springframework.util.Assert;

public class WeightedSumOperator {

  public static WeightedSumOperatorBuilder create() {
    return new WeightedSumOperatorBuilder();
  }

  private Operations operations;
  private Map<GridCoverage2D, Double> inputs;

  private WeightedSumOperator(Map<GridCoverage2D, Double> inputs, boolean normalizeWeights) {
    this.operations = new Operations(null);

    if (normalizeWeights) {
      this.inputs = normalizeWeights(inputs);

    } else {
      this.inputs = inputs;
    }

  }

  public GridCoverage2D calculate() {
    List<Coverage> weightedRasters = new ArrayList<>(inputs.size());
    for (var inputRaster : inputs.entrySet()) {
      double[] weight = {inputRaster.getValue()};
      weightedRasters.add(operations.add(inputRaster.getKey(), weight));
    }

    if (weightedRasters.size() == 1) {
      return (GridCoverage2D) weightedRasters.get(0);
    }

    Coverage intermediateSum = weightedRasters.get(0);
    for (int i = 1; i < weightedRasters.size(); ++i) {
      intermediateSum = operations.add(intermediateSum, weightedRasters.get(i));
    }

    return (GridCoverage2D) intermediateSum;
  }

  private Map<GridCoverage2D, Double> normalizeWeights(Map<GridCoverage2D, Double> inputs) {
    Map<GridCoverage2D, Double> normalizedWeights = new HashMap<>(inputs.size());
    var total = inputs.values().stream().mapToDouble(Double::doubleValue).sum();

    for (var entry : inputs.entrySet()) {
      normalizedWeights.put(entry.getKey(), entry.getValue() / total);
    }

    return normalizedWeights;
  }

  public static class WeightedSumOperatorBuilder {

    private Map<GridCoverage2D, Double> inputs;
    private boolean normalizeWeights = false;

    public WeightedSumOperatorBuilder() {
      this.inputs = new HashMap<>();
    }

    public WeightedSumOperatorWeightBuilder raster(GridCoverage2D inputRaster) {
      return new WeightedSumOperatorWeightBuilder(this, inputRaster);
    }

    public WeightedSumOperatorBuilder input(GridCoverage2D inputRaster, double weight) {
      inputs.put(inputRaster, weight);
      return this;
    }

    public WeightedSumOperatorBuilder normalizeWeights() {
      normalizeWeights = true;
      return this;
    }

    public WeightedSumOperatorBuilder normalizeWeights(boolean doNormalize) {
      normalizeWeights = doNormalize;
      return this;
    }

    public WeightedSumOperator build() {
      Assert.isTrue(!inputs.isEmpty(), "A weighted sum over zero rasters is not defined");
      return new WeightedSumOperator(inputs, normalizeWeights);
    }

  }

  public static class WeightedSumOperatorWeightBuilder {

    private WeightedSumOperatorBuilder parent;
    private GridCoverage2D rasterToWeight;

    private WeightedSumOperatorWeightBuilder(WeightedSumOperatorBuilder parent,
        GridCoverage2D rasterToWeight) {
      super();
      this.parent = parent;
      this.rasterToWeight = rasterToWeight;
    }

    public WeightedSumOperatorBuilder weight(double weight) {
      parent.inputs.put(rasterToWeight, weight);
      return parent;
    }

  }
}
