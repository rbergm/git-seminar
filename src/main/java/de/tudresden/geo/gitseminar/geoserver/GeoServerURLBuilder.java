package de.tudresden.geo.gitseminar.geoserver;

import de.tudresden.geo.gitseminar.geoserver.DataStoreUpload.DataType;
import de.tudresden.geo.gitseminar.geoserver.DataStoreUpload.UploadMethod;

/**
 * Utility to easily build URLs to different REST interfaces of a GeoServer instance.
 *
 * @author Rico Bergmann
 *
 */
public class GeoServerURLBuilder {


  public static GeoServerURLBuilder forInstance(String geoserverRootPath, String workspace) {
    return new GeoServerURLBuilder(geoserverRootPath, workspace);
  }

  private final String geoserverRootPath;
  private final String workspace;

  private GeoServerURLBuilder(String geoserverRootPath, String workspace) {
    this.geoserverRootPath = geoserverRootPath;
    this.workspace = workspace;
  }

  public String datastores() {
    return geoserverRootPath + "/workspaces/" + workspace + "/datastores";
  }

  public String datastore(String storeName) {
    return datastores() + "/" + storeName;
  }

  public String datastoreFile(String storeName, String fileName) {
    return datastore(storeName) + "/" + fileName;
  }

  public String uploadDatastore(String storeName, UploadMethod method, DataType format) {
    return datastore(storeName) + "/" + method.name().toLowerCase() + "."
        + format.name().toLowerCase();
  }

  public String coverageStores() {
    return coverageStores(false);
  }

  public String coverageStores(boolean asParthOfPath) {
    return geoserverRootPath + "/workspaces/" + workspace + "/coveragestores"
        + (asParthOfPath ? "" : ".json");
  }

  public String coverageStore(String storeName) {
    return coverageStore(storeName, false);
  }

  public String coverageStore(String storeName, boolean asParthOfPath) {
    return coverageStores(true) + "/" + storeName + (asParthOfPath ? "" : ".json");
  }

  public String coverageStoreFile(String storeName, String filetype) {
    return coverageStore(storeName, true) + "/file" + filetype;
  }

}
