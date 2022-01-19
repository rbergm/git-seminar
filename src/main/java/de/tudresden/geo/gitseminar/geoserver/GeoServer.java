package de.tudresden.geo.gitseminar.geoserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.client.RestTemplate;
import de.tudresden.geo.gitseminar.geoserver.CoverageStores.CoverageStoreEntry;

@Service
public class GeoServer {

  private static final Logger log = LoggerFactory.getLogger(GeoServer.class);

  private final RestTemplate restTemplate;
  private final GeoServerURLBuilder urlBuilder;
  private final String workspace;

  public GeoServer(RestTemplate restTemplate, @Value("${geoserver.root}") String geoserverBaseUrl,
      @Value("${geoserver.workspace}") String geoserverWorkspace) {
    this.restTemplate = restTemplate;
    this.urlBuilder = GeoServerURLBuilder.forInstance(geoserverBaseUrl, geoserverWorkspace);
    this.workspace = geoserverWorkspace;
  }

  public boolean hasCoverageStore(String storeName) {
    var coverageStoreUrl = urlBuilder.coverageStores();
    log.debug("Querying GeoServer instance for coverage store {}", storeName);
    var coverageStores = restTemplate.getForObject(coverageStoreUrl, CoverageStores.class);
    log.debug("GeoServer provided store details {}", coverageStores);
    boolean coverageStoreExists = coverageStores.getCoverageStores().getCoverageStore().stream()
        .map(CoverageStoreEntry::getName).anyMatch(n -> n.equals(storeName));
    return coverageStoreExists;
  }

  public void uploadToCoverageStore(String storeName, String rasterFile) throws IOException {
    File raster = new File(rasterFile);
    var uploadUrl = urlBuilder.coverageStoreFile(storeName, ".geotiff");

    try (var rasterData = new FileInputStream(raster)) {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.asMediaType(MimeType.valueOf("image/tif")));
      HttpEntity<byte[]> requestEntity = new HttpEntity<>(IOUtils.toByteArray(rasterData), headers);
      restTemplate.exchange(uploadUrl, HttpMethod.PUT, requestEntity, String.class);
    }
  }

  public String getWCSForCoverageStore(String storeName) {
    return storeName;
  }

  public String getWorkspace() {
    return workspace;
  }

}
