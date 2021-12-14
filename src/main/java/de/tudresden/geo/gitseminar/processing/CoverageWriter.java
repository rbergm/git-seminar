package de.tudresden.geo.gitseminar.processing;

import java.io.File;
import java.io.IOException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.springframework.stereotype.Service;

@Service
public class CoverageWriter {


  public void storeCoverage(GridCoverage2D raster, String outName) throws IOException {
    storeCoverage(raster, outName, new GeoTiffFormat());
  }

  public void storeCoverage(GridCoverage2D raster, String outName, AbstractGridFormat format)
      throws IOException {
    File outputRaster = new File(outName);
    format.getWriter(outputRaster).write(raster);
  }

}
