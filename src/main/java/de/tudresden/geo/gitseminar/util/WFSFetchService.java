package de.tudresden.geo.gitseminar.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Basic utility to load geodata via WFS.
 *
 * @deprecated WFS connections are currently not supported by GeoTools
 * @author Rico Bergmann
 *
 */
@Service
@Deprecated
public class WFSFetchService {

  private static final Logger log = LoggerFactory.getLogger(WFSFetchService.class);

  @Value("${geoserver.root}")
  private String geoserverRootPath;

  public void loadWFS() {
    log.debug("Trying to load WFS data from " + geoserverRootPath);

    log.debug("Done with loading");
  }

}
