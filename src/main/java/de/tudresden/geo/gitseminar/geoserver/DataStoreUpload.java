package de.tudresden.geo.gitseminar.geoserver;

import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataStoreUpload {

	public enum UploadMethod {
		URL, FILE, EXTERNAL;
	}

	public enum DataType {
		SHP;
	}

	public static DataStoreUpload create(String dsName, String dsPath) {
		var dsContainer = new DataStoreUpload();
		var ds = new DataStore();
		ds.setName(dsName);

		var fileParamContainer = new ConnectionParameters();
		var fileParam = new ConnectionParameters.ConnectionParameterEntry();
		fileParam.setKey("url");
		fileParam.setValue("file:" + dsPath);
		fileParamContainer.setEntry(Arrays.asList(fileParam));

		ds.setConnectionParameters(fileParamContainer);

		dsContainer.setDataStore(ds);
		return dsContainer;
	}

	private DataStore dataStore;

	public DataStoreUpload() {}

	public DataStore getDataStore() {
		return dataStore;
	}

	public void setDataStore(DataStore dataStore) {
		this.dataStore = dataStore;
	}

	@Override
	public String toString() {
		return "DataStoreUpload [dataStore=" + dataStore + "]";
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DataStore {
		private String name;
		private ConnectionParameters connectionParameters;

		public DataStore() {}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public ConnectionParameters getConnectionParameters() {
			return connectionParameters;
		}

		public void setConnectionParameters(ConnectionParameters connectionParameters) {
			this.connectionParameters = connectionParameters;
		}

		@Override
		public String toString() {
			return "DataStore [name=" + name + ", connectionParameters=" + connectionParameters + "]";
		}
	}


}
