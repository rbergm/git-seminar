package de.tudresden.geo.gitseminar.geoserver;

import org.geotools.coverage.grid.GridCoverage2D;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import de.tudresden.geo.gitseminar.geoserver.DataStores.DataStore.DataStoreEntry;

@Service
public class GeoServer {

  private final RestTemplate restTemplate;
  private final String geoserverBaseUrl;
  private String geoserverWorkspace;

  public GeoServer(RestTemplate restTemplate, @Value("${geoserver.root}") String geoserverBaseUrl,
      @Value("${geoserver.workspace}") String geoserverWorkspace) {
    this.restTemplate = restTemplate;
    this.geoserverBaseUrl = geoserverBaseUrl;
    this.geoserverWorkspace = geoserverWorkspace;
  }

  public boolean hasDatastore(String datastoreName) {
    var datastoreUrl =
        GeoServerURLBuilder.forInstance(geoserverBaseUrl, geoserverWorkspace).datastores();
    var datastores = restTemplate.getForObject(datastoreUrl, DataStores.class);
    boolean datastoreExists = datastores.flatten().stream().map(DataStoreEntry::getName)
        .anyMatch(n -> n.equals(datastoreName));
    return datastoreExists;
  }

  public void createDatastore(String datastoreName) {

  }

  public void uploadToDatastore(String datastoreName, GridCoverage2D raster) {
    uploadToDatastore(datastoreName, raster, true);
  }

  public void uploadToDatastore(String datastoreName, GridCoverage2D raster,
      boolean createIfNecessary) {

  }

  public String getWCSForDatastore(String datastoreName) {
    return "";
  }

}
