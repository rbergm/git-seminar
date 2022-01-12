package de.tudresden.geo.gitseminar.routing.rest;

import java.util.Objects;
import de.tudresden.geo.gitseminar.routing.Route;

public class RoutingResponse {

  public enum RoutingResponseType {
    OK("ok"), NoRoute("no-route"), StartMatchesTarget("start-matches-target"), StationNotFound(
        "station-not-found");

    private String type;

    RoutingResponseType(String type) {
      this.type = type;
    }

    public String getType() {
      return type;
    }
  }

  public static RoutingResponse ok(Route route) {
    return new RoutingResponse(RoutingResponseType.OK.getType(), route);
  }

  public static RoutingResponse noRoute() {
    return new RoutingResponse(RoutingResponseType.NoRoute.getType(), null);
  }

  public static RoutingResponse startMatchesTarget() {
    return new RoutingResponse(RoutingResponseType.StartMatchesTarget.getType(), null);
  }

  public static RoutingResponse stationNotFound() {
    return new RoutingResponse(RoutingResponseType.StationNotFound.getType(), null);
  }

  private final String status;
  private final Route route;

  private RoutingResponse(String status, Route route) {
    this.status = status;
    this.route = route;
  }

  public String getStatus() {
    return status;
  }

  public Route getRoute() {
    return route;
  }

  @Override
  public int hashCode() {
    return Objects.hash(route, status);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    RoutingResponse other = (RoutingResponse) obj;
    return Objects.equals(route, other.route) && Objects.equals(status, other.status);
  }

  @Override
  public String toString() {
    return "RoutingResponse [status=" + status + ", route=" + route + "]";
  }

}
