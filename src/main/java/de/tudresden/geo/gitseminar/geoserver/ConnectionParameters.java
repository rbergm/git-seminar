package de.tudresden.geo.gitseminar.geoserver;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectionParameters {

	private List<ConnectionParameterEntry> entry;

	public ConnectionParameters() {}

	public List<ConnectionParameterEntry> getEntry() {
		return entry;
	}

	public void setEntry(List<ConnectionParameterEntry> entry) {
		this.entry = entry;
	}

	@Override
	public String toString() {
		return "ConnectionParameters [entry=" + entry + "]";
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ConnectionParameterEntry {
		@JsonAlias("@key")
		private String key;

		@JsonAlias("$")
		private String value;

		public ConnectionParameterEntry() {}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "ConnectionParameterEntry [key=" + key + ", value=" + value + "]";
		}
	}

}
