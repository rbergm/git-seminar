package de.tudresden.geo.gitseminar.rest;

import java.io.IOException;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.geotools.data.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import de.tudresden.geo.gitseminar.geoserver.DataStoreUpload;
import de.tudresden.geo.gitseminar.geoserver.DataStoreUpload.DataType;
import de.tudresden.geo.gitseminar.geoserver.DataStoreUpload.UploadMethod;
import de.tudresden.geo.gitseminar.geoserver.DataStores;
import de.tudresden.geo.gitseminar.geoserver.DataStores.DataStore.DataStoreEntry;
import de.tudresden.geo.gitseminar.geoserver.GeoServerURLBuilder;
import de.tudresden.geo.gitseminar.geoserver.Workspaces;
import de.tudresden.geo.gitseminar.util.ShapefileLoadService;

/**
 * The ExampleController demonstrates basic workflows necessary throughout the rest of the
 * application.
 *
 * @author Rico Bergmann
 *
 */
@RestController
public class ExampleController {

	private static final Logger log = LoggerFactory.getLogger(ExampleController.class);

	private final ShapefileLoadService shapeLoader;
	private final RestTemplate restTemplate;

	@Value("${geoserver.root}")
	private String geoserverRoot;

	@Value("${geoserver.workspace}")
	private String geoserverWorkspace;

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

	@GetMapping("/example/ds")
	public String createDatastoreExample() throws IOException {
		final String exampleDS = "spring-example";

		var dsUrl = GeoServerURLBuilder.forInstance(geoserverRoot, geoserverWorkspace).datastores();
		var datastores = restTemplate.getForObject(dsUrl, DataStores.class);
		log.debug("Available datastores: {}", datastores.toString());

		boolean exampleDSExists = datastores.flatten().stream().map(DataStoreEntry::getName)
				.anyMatch(n -> n.equals(exampleDS));

		Resource exampleShape = new ClassPathResource("atkis_strassen.shp");

		if (exampleDSExists) {
			log.debug("Datastore exists already. Done");
		} else {
			log.debug("Datastore does not yet exist. Creating");

			final String exampleDSFile =
					GeoServerURLBuilder.forInstance(geoserverRoot, geoserverWorkspace)
							.datastoreFile(exampleDS, exampleShape.getFilename());

			log.debug("Datastore file: {}", exampleDSFile);

			// first up, try to generate the abstract datastore
			String res = restTemplate.postForObject(dsUrl,
					DataStoreUpload.create(exampleDS, exampleDSFile), String.class);
		}

		// now, upload the actual binary file
		log.debug("Starting upload procedure");
		Resource exampleShapeZip = new ClassPathResource("atkis_strassen.zip");
		final String uploadUrl = GeoServerURLBuilder.forInstance(geoserverRoot, geoserverWorkspace)
				.uploadDatastore(exampleDS, UploadMethod.FILE, DataType.SHP);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.asMediaType(MimeType.valueOf("application/zip")));
		HttpEntity<byte[]> requestEntity =
				new HttpEntity<>(IOUtils.toByteArray(exampleShapeZip.getInputStream()), headers);
		var uploadRes = restTemplate.exchange(uploadUrl, HttpMethod.PUT, requestEntity, String.class);

		return "Success: \n" + uploadRes.getBody();
	}

}
