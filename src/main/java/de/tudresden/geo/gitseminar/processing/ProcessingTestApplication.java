package de.tudresden.geo.gitseminar.processing;

import java.io.File;
import java.io.IOException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.Operations;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.util.factory.Hints;

public class ProcessingTestApplication {

  public static void main(String... args) throws IOException {

    // see http://docs.geotools.org/latest/userguide/tutorial/coverage/coverage.html

    File inputRaster = new File("src/main/resources/dist_raster.tif");
    AbstractGridFormat format = GridFormatFinder.findFormat(inputRaster);
    Hints hints = null;
    if (format instanceof GeoTiffFormat) {
      hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
    }

    GridCoverage2DReader gridReader = format.getReader(inputRaster, hints);
    GridCoverage2D gridCoverage = gridReader.read(null);

    CoverageProcessor coverageProcessor = CoverageProcessor.getInstance();

    for (var op : coverageProcessor.getOperations()) {
      System.out.println(op.getName() + " - " + op.getDescription());
    }

    coverageProcessor.getOperation("MultiplyConst");

    Operations ops = new Operations(null);

    double[] weight = {0.4};
    var weightedCoverage = ops.multiply(gridCoverage, weight);

    var resultCoverage = (GridCoverage2D) ops.add(weightedCoverage, gridCoverage);

    File outputRaster = new File("src/main/resources/out.tif");
    format.getWriter(outputRaster).write(resultCoverage, null);

    System.out.println("Done");
  }

}
