package de.tudresden.geo.gitseminar.geoserver;

import java.util.List;

public class CoverageStores {

  private CoverageStore coverageStores;

  public CoverageStores() {}

  public CoverageStore getCoverageStores() {
    return coverageStores;
  }

  public void setCoverageStores(CoverageStore coverageStores) {
    this.coverageStores = coverageStores;
  }

  @Override
  public String toString() {
    return "CoverageStores [coverageStores=" + coverageStores + "]";
  }

  public static class CoverageStore {
    private List<CoverageStoreEntry> coverageStore;

    public CoverageStore() {}

    public List<CoverageStoreEntry> getCoverageStore() {
      return coverageStore;
    }

    public void setCoverageStore(List<CoverageStoreEntry> coverageStore) {
      this.coverageStore = coverageStore;
    }

    @Override
    public String toString() {
      return "CoverageStore [coverageStore=" + coverageStore + "]";
    }

  }

  public static class CoverageStoreEntry {
    private String name;
    private String href;

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
      return "CoverageStoreEntry [name=" + name + ", href=" + href + "]";
    }
  }

}
