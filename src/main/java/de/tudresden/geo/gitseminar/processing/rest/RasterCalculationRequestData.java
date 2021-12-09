package de.tudresden.geo.gitseminar.processing.rest;

import java.util.Objects;

public class RasterCalculationRequestData {

  private double departureFrequencyWeight;
  private double mittelzentrumDistanceWeight;
  private double oberzentrumDistanceWeight;
  private double stationDistanceWeight;
  private double populationDataWeight;

  public RasterCalculationRequestData(double departureFrequencyWeight,
      double mittelzentrumDistanceWeight, double oberzentrumDistanceWeight,
      double stationDistanceWeight, double populationDataWeight) {
    this.departureFrequencyWeight = departureFrequencyWeight;
    this.mittelzentrumDistanceWeight = mittelzentrumDistanceWeight;
    this.oberzentrumDistanceWeight = oberzentrumDistanceWeight;
    this.stationDistanceWeight = stationDistanceWeight;
    this.populationDataWeight = populationDataWeight;
  }

  public double getDepartureFrequencyWeight() {
    return departureFrequencyWeight;
  }

  public void setDepartureFrequencyWeight(double departureFrequencyWeight) {
    this.departureFrequencyWeight = departureFrequencyWeight;
  }

  public double getMittelzentrumDistanceWeight() {
    return mittelzentrumDistanceWeight;
  }

  public void setMittelzentrumDistanceWeight(double mittelzentrumDistanceWeight) {
    this.mittelzentrumDistanceWeight = mittelzentrumDistanceWeight;
  }

  public double getOberzentrumDistanceWeight() {
    return oberzentrumDistanceWeight;
  }

  public void setOberzentrumDistanceWeight(double oberzentrumDistanceWeight) {
    this.oberzentrumDistanceWeight = oberzentrumDistanceWeight;
  }

  public double getStationDistanceWeight() {
    return stationDistanceWeight;
  }

  public void setStationDistanceWeight(double stationDistanceWeight) {
    this.stationDistanceWeight = stationDistanceWeight;
  }

  public double getPopulationDataWeight() {
    return populationDataWeight;
  }

  public void setPopulationDataWeight(double populationDataWeight) {
    this.populationDataWeight = populationDataWeight;
  }

  @Override
  public int hashCode() {
    return Objects.hash(departureFrequencyWeight, mittelzentrumDistanceWeight,
        oberzentrumDistanceWeight, populationDataWeight, stationDistanceWeight);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    RasterCalculationRequestData other = (RasterCalculationRequestData) obj;
    return Double.doubleToLongBits(departureFrequencyWeight) == Double
        .doubleToLongBits(other.departureFrequencyWeight)
        && Double.doubleToLongBits(mittelzentrumDistanceWeight) == Double
            .doubleToLongBits(other.mittelzentrumDistanceWeight)
        && Double.doubleToLongBits(oberzentrumDistanceWeight) == Double
            .doubleToLongBits(other.oberzentrumDistanceWeight)
        && Double.doubleToLongBits(populationDataWeight) == Double
            .doubleToLongBits(other.populationDataWeight)
        && Double.doubleToLongBits(stationDistanceWeight) == Double
            .doubleToLongBits(other.stationDistanceWeight);
  }

  @Override
  public String toString() {
    return "RasterCalculationRequestData [departureFrequencyWeight=" + departureFrequencyWeight
        + ", mittelzentrumDistanceWeight=" + mittelzentrumDistanceWeight
        + ", oberzentrumDistanceWeight=" + oberzentrumDistanceWeight + ", stationDistanceWeight="
        + stationDistanceWeight + ", populationDataWeight=" + populationDataWeight + "]";
  }

}
