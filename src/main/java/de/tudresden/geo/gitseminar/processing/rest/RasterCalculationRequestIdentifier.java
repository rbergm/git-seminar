package de.tudresden.geo.gitseminar.processing.rest;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RasterCalculationRequestIdentifier {

  @Value("${geoserver.raster.datastore.format}")
  private String identifierFormat;

  public String toIdentifier(RasterCalculationRequestData requestData) {
    Map<String, Double> values = new HashMap<>(5);
    values.put("depf", requestData.getDepartureFrequencyWeight());
    values.put("mz", requestData.getMittelzentrumDistanceWeight());
    values.put("oz", requestData.getOberzentrumDistanceWeight());
    values.put("statdist", requestData.getStationDistanceWeight());
    values.put("pop", requestData.getPopulationDataWeight());

    return StringSubstitutor.replace(identifierFormat, values, "{", "}");
  }

}
