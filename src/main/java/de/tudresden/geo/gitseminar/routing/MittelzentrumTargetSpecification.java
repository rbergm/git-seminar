package de.tudresden.geo.gitseminar.routing;

public class MittelzentrumTargetSpecification implements TargetSpecification {

	@Override
	public boolean isSatisfiedBy(TrainStation station) {
		return station.isMittelzentrum();
	}

}
