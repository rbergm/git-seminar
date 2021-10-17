package de.tudresden.geo.gitseminar.util;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.geotools.data.DataStore;
import org.geotools.data.FileDataStoreFinder;
import org.springframework.stereotype.Service;

@Service
public class ShapefileLoadService {

	public Optional<DataStore> fromPath(String fPath) {
		var dataFile = new File(fPath);
		try {
			var store = FileDataStoreFinder.getDataStore(dataFile);
			return Optional.of(store);
		} catch (IOException e) {
			return Optional.empty();
		}
	}

}
