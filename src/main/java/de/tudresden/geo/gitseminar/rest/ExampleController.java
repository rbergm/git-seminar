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
		var geoToolsCheck = testBasicGeoToolsIntegration();
		var geoServerCheck = testBasicGeoServerRESTConnection();
		return geoToolsCheck + " \n" + geoServerCheck;
	}

	@GetMapping("/example/ds")
	public String createDatastoreExample() throws IOException {

		// this is the DataStore where we are going to put our example data
		final String exampleDS = "spring-example";

		var dsUrl = GeoServerURLBuilder.forInstance(geoserverRoot, geoserverWorkspace).datastores();

		// First up, we need to check, whether the DataStore already exists
		// To do so, we first request a list of all available DataStores from our GeoServer instance
		var datastores = restTemplate.getForObject(dsUrl, DataStores.class);
		log.debug("Available datastores: {}", datastores.toString());

		// Based on this list, we can simply check, whether on of these stores matches our store.
		boolean exampleDSExists = datastores.flatten().stream().map(DataStoreEntry::getName)
				.anyMatch(n -> n.equals(exampleDS));

		String dataStoreCheckRes;
		if (exampleDSExists) {
			log.debug("Datastore exists already. Check done");
			dataStoreCheckRes = "Datastore exists";
		} else {
			log.debug("Datastore does not yet exist. Creating");

			// If we found, that the DataStore does not yet exist, we need to create it

			// The Shapefile is necessary in order to set the expected filenames correctly
			Resource exampleShape = new ClassPathResource("atkis_strassen.shp");

			final String exampleDSFile =
					GeoServerURLBuilder.forInstance(geoserverRoot, geoserverWorkspace)
							.datastoreFile(exampleDS, exampleShape.getFilename());
			log.debug("Datastore file: {}", exampleDSFile);

			// Once we got all the necessary data, we can request the GeoServer instance to create the
			// DataStore
			String requestRes = restTemplate.postForObject(dsUrl,
					DataStoreUpload.create(exampleDS, exampleDSFile), String.class);
			dataStoreCheckRes = "Datastore created: " + requestRes;
		}

		// We now established, that the DataStore exists. Therefore, we can proceed with the actual
		// upload of the Shapefile.
		var dataStoreUploadRes = testGeoServerFileUpload(exampleDS);

		return dataStoreCheckRes + " \n " + dataStoreUploadRes;
	}

	/**
	 * Checks, whether GeoTools are available and capable of loading a Shapefile.
	 */
	private String testBasicGeoToolsIntegration() {
		log.debug("Testing basic GeoTools setup");
		Resource exampleShape = new ClassPathResource("atkis_strassen.shp");
		try {
			Optional<DataStore> store = shapeLoader.fromPath(exampleShape.getFile().getAbsolutePath());
			if (!store.isPresent()) {
				log.error("Could not load shapefile from storage");
				throw new IOException("Could not load shapefile from storage");
			}
			log.debug("Shapefile loaded successfully");
			return "GeoTools setup success";
		} catch (IOException e) {
			log.debug("Could not load Shapefile: IO error");
			return "GeoTools IO error :(";
		}
	}

	/**
	 * Checks, whether the GeoServer instance is running and responds to basic REST requests.
	 */
	private String testBasicGeoServerRESTConnection() {
		log.debug("Testing REST connection to GeoServer: Querying available workspaces");
		var ws = restTemplate.getForObject(geoserverRoot + "/workspaces", Workspaces.class);
		log.debug(ws.toString());
		return "REST Connection success";
	}

	private String testGeoServerFileUpload(String exampleDS) throws IOException {
		log.debug("Starting upload procedure");
		Resource exampleShapeZip = new ClassPathResource("atkis_strassen.zip");
		final String uploadUrl = GeoServerURLBuilder.forInstance(geoserverRoot, geoserverWorkspace)
				.uploadDatastore(exampleDS, UploadMethod.FILE, DataType.SHP);

		// This process is a bit technical and basically requires to insert the contents of our Shapfile
		// into the request body.
		// This request will then be sent to our GeoServer instance.
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.asMediaType(MimeType.valueOf("application/zip")));
		HttpEntity<byte[]> requestEntity =
				new HttpEntity<>(IOUtils.toByteArray(exampleShapeZip.getInputStream()), headers);

		// Once the request is prepared, we can just send it.
		var uploadRes = restTemplate.exchange(uploadUrl, HttpMethod.PUT, requestEntity, String.class);
		return uploadRes.getBody();
	}

}
