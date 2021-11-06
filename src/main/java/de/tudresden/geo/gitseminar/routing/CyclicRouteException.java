package de.tudresden.geo.gitseminar.routing;

public class CyclicRouteException extends RuntimeException {

	private static final long serialVersionUID = -3545542414731175568L;

	public CyclicRouteException() {
		super();
	}

	public CyclicRouteException(String msg) {
		super(msg);
	}

}
