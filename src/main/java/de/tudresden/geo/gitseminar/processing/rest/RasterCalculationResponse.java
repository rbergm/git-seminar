package de.tudresden.geo.gitseminar.processing.rest;

import java.util.Objects;

public class RasterCalculationResponse {

  public static RasterCalculationResponse cached(String geoserverUrl) {
    return new RasterCalculationResponse(STATUS_CACHED, geoserverUrl);
  }

  public static RasterCalculationResponse created(String geoserverUrl) {
    return new RasterCalculationResponse(STATUS_CREATED, geoserverUrl);
  }

  private static final String STATUS_CACHED = "cached";
  private static final String STATUS_CREATED = "created";

  private final String status;
  private final String geoserverUrl;

  private RasterCalculationResponse(String status, String geoserverUrl) {
    this.status = status;
    this.geoserverUrl = geoserverUrl;
  }

  public String getStatus() {
    return status;
  }

  public String getGeoserverUrl() {
    return geoserverUrl;
  }

  public boolean created() {
    return status.equals(STATUS_CREATED);
  }

  public boolean cached() {
    return status.equals(STATUS_CACHED);
  }

  @Override
  public int hashCode() {
    return Objects.hash(geoserverUrl, status);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    RasterCalculationResponse other = (RasterCalculationResponse) obj;
    return Objects.equals(geoserverUrl, other.geoserverUrl) && Objects.equals(status, other.status);
  }

  @Override
  public String toString() {
    return "RasterCalculationResponse [status=" + status + ", geoserverUrl=" + geoserverUrl + "]";
  }

}
