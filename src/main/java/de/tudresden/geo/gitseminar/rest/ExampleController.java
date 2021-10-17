package de.tudresden.geo.gitseminar.rest;

import java.io.IOException;
import java.util.Optional;
import org.geotools.data.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import de.tudresden.geo.gitseminar.geoserver.Workspaces;
import de.tudresden.geo.gitseminar.util.ShapefileLoadService;

@RestController
public class ExampleController {

	private static final Logger log = LoggerFactory.getLogger(ExampleController.class);

	private final ShapefileLoadService shapeLoader;
	private final RestTemplate restTemplate;

	@Value("${geoserver.root}")
	private String geoserverRoot;

	public ExampleController(ShapefileLoadService shapeLoader, RestTemplate restTemplate) {
		super();
		this.shapeLoader = shapeLoader;
		this.restTemplate = restTemplate;
	}

	@GetMapping("/example")
	public String example() {
		Resource exampleShape = new ClassPathResource("atkis_strassen.shp");
		try {
			Optional<DataStore> store = shapeLoader.fromPath(exampleShape.getFile().getAbsolutePath());
			if (!store.isPresent()) {
				log.error("Could not load shapefile from storage");
				throw new IOException("Could not load shapefile from storage");
			}

			var ws = restTemplate.getForObject(geoserverRoot + "/workspaces", Workspaces.class);
			log.debug(ws.toString());

			return "Success!";
		} catch (IOException e) {
			return "IO error :(";
		}
	}

}
