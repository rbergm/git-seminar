package de.tudresden.geo.gitseminar.util;

import org.springframework.util.Assert;

public class Count implements Comparable<Count> {

	public static Count of(int value) {
		return new Count(value);
	}

	// TODO: find a better method name
	public static Count generateFrom(int value) {
		return new Count(value < 0 ? 0 : value);
	}

	private final int value;

	private Count(int value) {
		Assert.isTrue(value >= 0, "Count may not be negative, but was " + value);
		this.value = value;
	}

	public int get() {
		return value;
	}

	public int getValue() {
		return value;
	}

	public boolean isLessThan(Count other) {
		return this.value < other.value;
	}

	public Count increasedBy(int value) {
		return Count.of(this.value + value);
	}

	public Count plus(Count other) {
		return Count.of(this.value + other.value);
	}


	@Override
	public int compareTo(Count other) {
		return this.value - other.value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Count))
			return false;
		Count other = (Count) obj;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "" + value;
	}

}
