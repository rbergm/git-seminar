package de.tudresden.geo.gitseminar.processing.rest;

import java.util.Objects;

public class RasterCalculationResponse {

  public static RasterCalculationResponse cached(String geoserverUrl, String workspace) {
    return new RasterCalculationResponse(STATUS_CACHED, geoserverUrl, workspace);
  }

  public static RasterCalculationResponse created(String geoserverUrl, String workspace) {
    return new RasterCalculationResponse(STATUS_CREATED, geoserverUrl, workspace);
  }

  private static final String STATUS_CACHED = "cached";
  private static final String STATUS_CREATED = "created";

  private final String status;
  private final String geoserverUrl;
  private final String workspace;

  private RasterCalculationResponse(String status, String geoserverUrl, String workspace) {
    this.status = status;
    this.geoserverUrl = geoserverUrl;
    this.workspace = workspace;
  }

  public String getStatus() {
    return status;
  }

  public String getGeoserverUrl() {
    return geoserverUrl;
  }

  public String getWorkspace() {
    return workspace;
  }

  public boolean created() {
    return status.equals(STATUS_CREATED);
  }

  public boolean cached() {
    return status.equals(STATUS_CACHED);
  }

  @Override
  public int hashCode() {
    return Objects.hash(geoserverUrl, status, workspace);
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
    return Objects.equals(geoserverUrl, other.geoserverUrl) && Objects.equals(status, other.status)
        && Objects.equals(workspace, other.workspace);
  }

  @Override
  public String toString() {
    return "RasterCalculationResponse [status=" + status + ", geoserverUrl=" + geoserverUrl
        + ", workspace=" + workspace + "]";
  }

}
