package de.tudresden.geo.gitseminar.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationController {

  private static final Logger log = LoggerFactory.getLogger(ApplicationController.class);

  @GetMapping("/test/routing")
  public String routingTest() {
    log.debug("Serving routing test");
    return "routing_test";
  }

  @GetMapping(value = {"/", "/index", "/raster_page", "/raster_page.html"})
  public String rasterPage() {
    return "raster_page";
  }

  @GetMapping(value = {"/station_page", "/station_page.html"})
  public String stationPage() {
    return "station_page";
  }

  @GetMapping(value = {"/reset", "/reset.html"})
  public String resetWebStorage() {
    return "reset";
  }

}
