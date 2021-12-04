package de.tudresden.geo.gitseminar.routing.rest;

import javax.validation.constraints.NotBlank;

public class RoutingRequest {

	@NotBlank
	private String startStation;

	@NotBlank
	private String target;

	public RoutingRequest(@NotBlank String startStation, @NotBlank String target) {
		super();
		this.startStation = startStation;
		this.target = target;
	}

	public String getStartStation() {
		return startStation;
	}

	public void setStartStation(String startStation) {
		this.startStation = startStation;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((startStation == null) ? 0 : startStation.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof RoutingRequest))
			return false;
		RoutingRequest other = (RoutingRequest) obj;
		if (startStation == null) {
			if (other.startStation != null)
				return false;
		} else if (!startStation.equals(other.startStation))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RoutingRequest [startStation=" + startStation + ", target=" + target + "]";
	}

}
