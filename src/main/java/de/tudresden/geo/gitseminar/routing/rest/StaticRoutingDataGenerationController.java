package de.tudresden.geo.gitseminar.routing.rest;

import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import de.tudresden.geo.gitseminar.routing.MittelzentrumTargetSpecification;
import de.tudresden.geo.gitseminar.routing.OberzentrumTargetSpecification;
import de.tudresden.geo.gitseminar.routing.TargetSpecification;
import de.tudresden.geo.gitseminar.routing.processing.StaticRoutingDataGenerator;

@RestController
public class StaticRoutingDataGenerationController {

  private StaticRoutingDataGenerator routingDataGenerator;

  public StaticRoutingDataGenerationController(StaticRoutingDataGenerator routingDataGenerator) {
    super();
    this.routingDataGenerator = routingDataGenerator;
  }

  @GetMapping("/routing/static/mittelzentrum")
  public String generateMittelzentrumData(
      @RequestParam(defaultValue = "mittelzentren.csv") String outfile) throws IOException {
    TargetSpecification target = new MittelzentrumTargetSpecification();
    routingDataGenerator.run(outfile, target);
    return outfile;
  }

  @GetMapping("/routing/static/oberzentrum")
  public String generateOberzentrumData(
      @RequestParam(defaultValue = "oberzentren.csv") String outfile) throws IOException {
    TargetSpecification target = new OberzentrumTargetSpecification();
    routingDataGenerator.run(outfile, target);
    return outfile;
  }

}
