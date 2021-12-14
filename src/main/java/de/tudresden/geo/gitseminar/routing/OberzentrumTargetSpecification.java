package de.tudresden.geo.gitseminar.routing;

public class OberzentrumTargetSpecification implements TargetSpecification {

  @Override
  public boolean isSatisfiedBy(TrainStation station) {
    return station.isOberzentrum();
  }

}
