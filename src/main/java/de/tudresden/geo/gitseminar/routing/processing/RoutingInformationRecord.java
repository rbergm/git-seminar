package de.tudresden.geo.gitseminar.routing.processing;

import java.util.Objects;

public class RoutingInformationRecord {

  public static RoutingInformationRecord noRouteNecessary(String station) {
    return new RoutingInformationRecord(station, station, 0, 0);
  }

  public static RoutingInformationRecord noRouteAvailable(String station) {
    return new RoutingInformationRecord(station, "", -1, -1);
  }

  public final String trainStation;
  public final String targetStation;
  public final int numberOfIntermediateStops;
  public final int numberOfChanges;

  public RoutingInformationRecord(String trainStation, String targetStation,
      int numberOfIntermediateStops, int numberOfChanges) {
    this.trainStation = trainStation;
    this.targetStation = targetStation;
    this.numberOfIntermediateStops = numberOfIntermediateStops;
    this.numberOfChanges = numberOfChanges;
  }

  @Override
  public int hashCode() {
    return Objects.hash(numberOfChanges, numberOfIntermediateStops, targetStation, trainStation);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    RoutingInformationRecord other = (RoutingInformationRecord) obj;
    return numberOfChanges == other.numberOfChanges
        && numberOfIntermediateStops == other.numberOfIntermediateStops
        && Objects.equals(targetStation, other.targetStation)
        && Objects.equals(trainStation, other.trainStation);
  }

  @Override
  public String toString() {
    return "RoutingInformationRecord [trainStation=" + trainStation + ", targetStation="
        + targetStation + ", numberOfIntermediateStops=" + numberOfIntermediateStops
        + ", numberOfChanges=" + numberOfChanges + "]";
  }

}
