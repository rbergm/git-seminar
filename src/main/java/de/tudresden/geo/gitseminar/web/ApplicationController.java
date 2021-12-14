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

}
