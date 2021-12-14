package de.tudresden.geo.gitseminar.geoserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * The GeoServerRestTemplateBuilder provides a REST template which is suitable for usage on the
 * GeoServer instance connected with this application.
 *
 * The template will have necessary values pre-initialized, such that these steps do not need to be
 * repeated upon each REST call. For example, this includes a specification of the necessary
 * authentication data.
 *
 * @author Rico Bergmann
 *
 */
@Component
public class GeoServerRestTemplateBuilder {

  private static final Logger log = LoggerFactory.getLogger(GeoServerRestTemplateBuilder.class);

  @Value("${geoserver.auth.username}")
  private String authUsername;

  @Value("${geoserver.auth.password}")
  private String authPassword;

  /**
   * Constructs the actual REST template.
   */
  @Bean
  public RestTemplate build(RestTemplateBuilder builder) {
    log.debug("Setting up REST template with auth: " + authUsername + " / " + authPassword);
    builder = builder.basicAuthentication(authUsername, authPassword);
    return builder.build();
  }

}
