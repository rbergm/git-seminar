package de.tudresden.geo.gitseminar.routing.processing;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import de.tudresden.geo.gitseminar.routing.StartStationMatchesTargetSpecificationException;
import de.tudresden.geo.gitseminar.routing.TargetRoutingService;
import de.tudresden.geo.gitseminar.routing.TargetSpecification;
import de.tudresden.geo.gitseminar.routing.network.TrainNetworkSupplier;

@Service
public class StaticRoutingDataGenerator {

  private TargetRoutingService routingService;
  private TrainNetworkSupplier networkSupplier;

  public StaticRoutingDataGenerator(TargetRoutingService routingService,
      TrainNetworkSupplier networkSupplier) {
    super();
    this.routingService = routingService;
    this.networkSupplier = networkSupplier;
  }

  public void run(String outFile, TargetSpecification target) throws IOException {
    var routingData = generateRoutingData(target);
    writeOutRoutingData(routingData, outFile);
  }

  private List<RoutingInformationRecord> generateRoutingData(TargetSpecification target) {
    var stations = networkSupplier.getAllTrainStations();
    var resultRecords = new ArrayList<RoutingInformationRecord>(stations.size());
    for (var station : stations) {
      RoutingInformationRecord resultRecord;
      try {
        var routingResult =
            routingService.findTarget(networkSupplier.getNetwork(), station, target);

        if (routingResult.isPresent()) {
          var route = routingResult.get();
          resultRecord =
              new RoutingInformationRecord(station.getName(), route.getFinalStop().getName(),
                  route.countPassedStations().get(), route.countEffectiveChanges().get());
        } else {
          resultRecord = RoutingInformationRecord.noRouteAvailable(station.getName());
        }

      } catch (StartStationMatchesTargetSpecificationException e) {
        resultRecord = RoutingInformationRecord.noRouteNecessary(station.getName());
      }
      resultRecords.add(resultRecord);
    }
    return resultRecords;
  }

  private void writeOutRoutingData(List<RoutingInformationRecord> records, String outfile)
      throws IOException {
    var out = new FileWriter(outfile);
    try (var printer = new CSVPrinter(out,
        CSVFormat.Builder.create().setHeader("station", "target", "changes", "stops").build())) {
      for (var routingRecord : records) {
        printer.printRecord(routingRecord.trainStation, routingRecord.targetStation,
            routingRecord.numberOfChanges, routingRecord.numberOfIntermediateStops);
      }
    }
  }

}
