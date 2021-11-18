package de.tudresden.geo.gitseminar.routing.network;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LineInfoJSON {

	private List<LineInfo> data;

	public List<LineInfo> getData() {
		return data;
	}

	public void setData(List<LineInfo> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "LineInfoJSON [data=" + data + "]";
	}

	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class LineInfo {

		private String trainType;

		private String extractedFrom;

		private String line;

		private List<String> routeNormalized;

		public String getTrainType() {
			return trainType;
		}

		public void setTrainType(String trainType) {
			this.trainType = trainType;
		}

		public String getExtractedFrom() {
			return extractedFrom;
		}

		public void setExtractedFrom(String extractedFrom) {
			this.extractedFrom = extractedFrom;
		}

		public String getLine() {
			return line;
		}

		public void setLine(String line) {
			this.line = line;
		}

		public List<String> getRouteNormalized() {
			return routeNormalized;
		}

		public void setRouteNormalized(List<String> routeNormalized) {
			this.routeNormalized = routeNormalized;
		}

		@Override
		public String toString() {
			return "LineInfo [trainType=" + trainType + ", extractedFrom=" + extractedFrom + ", line="
					+ line + ", routeNormalized=" + routeNormalized + "]";
		}

	}

}
