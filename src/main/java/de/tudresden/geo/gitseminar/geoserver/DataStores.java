package de.tudresden.geo.gitseminar.geoserver;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.tudresden.geo.gitseminar.geoserver.DataStores.DataStore.DataStoreEntry;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataStores {

  private DataStore dataStores;

  public DataStores() {}

  public DataStore getDataStores() {
    return dataStores;
  }

  public void setDataStores(DataStore dataStores) {
    this.dataStores = dataStores;
  }

  public List<DataStoreEntry> flatten() {
    return dataStores.dataStore;
  }

  public DataStoreEntry flattenOne() {
    return dataStores.dataStore.get(0);
  }

  @Override
  public String toString() {
    return "DataStores [dataStores=" + dataStores + "]";
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class DataStore {

    private List<DataStoreEntry> dataStore;

    public DataStore() {}

    public List<DataStoreEntry> getDataStore() {
      return dataStore;
    }

    public void setDataStore(List<DataStoreEntry> dataStore) {
      this.dataStore = dataStore;
    }

    @Override
    public String toString() {
      return "DataStore [dataStore=" + dataStore + "]";
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataStoreEntry {
      private String name;
      private String href;

      public DataStoreEntry() {}

      public String getName() {
        return name;
      }

      public void setName(String name) {
        this.name = name;
      }

      public String getHref() {
        return href;
      }

      public void setHref(String href) {
        this.href = href;
      }

      @Override
      public String toString() {
        return "DataStoreEntry [href=" + href + ", name=" + name + "]";
      }

    }

  }

}
