package de.tudresden.geo.gitseminar.processing;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.util.factory.Hints;
import org.springframework.stereotype.Service;

@Service
public class CoverageReader {

  public Optional<GridCoverage2D> loadCoverage(File source) {
    try {
      AbstractGridFormat format = GridFormatFinder.findFormat(source);
      Hints hints = null;
      if (format instanceof GeoTiffFormat) {
        hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
      }

      GridCoverage2DReader gridReader = format.getReader(source, hints);
      GridCoverage2D gridCoverage = gridReader.read(null);

      return Optional.of(gridCoverage);

    } catch (IOException e) {
      return Optional.empty();
    }
  }

}
