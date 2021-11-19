package de.tudresden.geo.gitseminar.routing;

public class MittelzentrumTargetSpecification implements TargetSpecification {

	private final boolean treatOberzentrumAsMittelzentrum;

	public MittelzentrumTargetSpecification() {
		this(true);
	}

	public MittelzentrumTargetSpecification(boolean treatOberzentrumAsMittelzentrum) {
		this.treatOberzentrumAsMittelzentrum = treatOberzentrumAsMittelzentrum;
	}

	@Override
	public boolean isSatisfiedBy(TrainStation station) {
		return treatOberzentrumAsMittelzentrum ? station.qualifiesAsMittelzentrum()
				: station.isMittelzentrum();
	}

}
