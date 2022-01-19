package de.tudresden.geo.gitseminar.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class LoggingRestTemplateInterceptor implements ClientHttpRequestInterceptor {

  private static final Logger log = LoggerFactory.getLogger(LoggingRestTemplateInterceptor.class);

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] requestBody,
      ClientHttpRequestExecution execution) throws IOException {
    ClientHttpResponse response = execution.execute(request, requestBody);
    InputStreamReader responseBodyReader =
        new InputStreamReader(response.getBody(), StandardCharsets.UTF_8);
    String responseBody =
        new BufferedReader(responseBodyReader).lines().collect(Collectors.joining("\n"));
    log.debug("Response body: '{}'", responseBody);

    return response;
  }

}
