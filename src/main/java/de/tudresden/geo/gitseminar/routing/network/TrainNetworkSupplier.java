package de.tudresden.geo.gitseminar.routing.network;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import de.tudresden.geo.gitseminar.routing.StationConnection;
import de.tudresden.geo.gitseminar.routing.TrainStation;

@Service
public class TrainNetworkSupplier {

  private static Logger log = LoggerFactory.getLogger(TrainNetworkSupplier.class);

  private Graph<TrainStation, StationConnection> network;
  private Map<String, TrainStation> trainStations;

  private TrainLineNetworkParser parser;
  private String networkDescription;

  public TrainNetworkSupplier(TrainLineNetworkParser parser,
      @Value("${trainnetwork.file}") String networkDescription) {
    this.parser = parser;
    this.networkDescription = networkDescription;
  }

  public Graph<TrainStation, StationConnection> getNetwork() {
    if (network == null) {
      initializeNetwork();
    }

    return network;
  }

  public TrainStation getTrainStation(String name) {
    if (network == null) {
      initializeNetwork();
    }
    return trainStations.get(name);
  }

  public Collection<TrainStation> getAllTrainStations() {
    if (network == null) {
      initializeNetwork();
    }
    return trainStations.values();
  }

  private void initializeNetwork() {
    try {
      Resource networkFile = new ClassPathResource(networkDescription);
      var parsingResult = parser.loadFromJsonFile(networkFile.getFile());

      if (parsingResult.isEmpty()) {
        log.error("Train network is empty");
        setupDummyNetwork();
      }

      network = parsingResult.get().network;
      trainStations = parsingResult.get().trainStations;

    } catch (IOException e) {
      log.error("Could not load train network from file: {}", e.getMessage());
      setupDummyNetwork();
    }
  }

  private void setupDummyNetwork() {
    network = new DefaultUndirectedGraph<>(StationConnection.class);
  }

}
