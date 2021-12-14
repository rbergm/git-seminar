package de.tudresden.geo.gitseminar.geoserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
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

  private final RestTemplate restTemplate;
  private final GeoServerURLBuilder urlBuilder;

  public GeoServer(RestTemplate restTemplate, @Value("${geoserver.root}") String geoserverBaseUrl,
      @Value("${geoserver.workspace}") String geoserverWorkspace) {
    this.restTemplate = restTemplate;
    this.urlBuilder = GeoServerURLBuilder.forInstance(geoserverBaseUrl, geoserverWorkspace);
  }

  public boolean hasCoverageStore(String storeName) {
    var coverageStoreUrl = urlBuilder.coverageStores();
    var coverageStores = restTemplate.getForObject(coverageStoreUrl, CoverageStores.class);
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

}
