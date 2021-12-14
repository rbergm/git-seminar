package de.tudresden.geo.gitseminar.routing;

public class TrainStation {

  public static TrainStationBuilder ofName(String name) {
    return new TrainStationBuilder(name);
  }

  private final String name;
  private final String eva;
  private final boolean mittelzentrum;
  private final boolean oberzentrum;

  private TrainStation(String name, String eva, boolean mz, boolean oz) {
    this.name = name;
    this.eva = eva;
    this.mittelzentrum = mz;
    this.oberzentrum = oz;
  }

  public String getName() {
    return name;
  }

  public String getEva() {
    return eva;
  }

  public boolean isMittelzentrum() {
    return mittelzentrum && !oberzentrum;
  }

  public boolean qualifiesAsMittelzentrum() {
    return oberzentrum || mittelzentrum;
  }

  public boolean isOberzentrum() {
    return oberzentrum;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof TrainStation))
      return false;
    TrainStation other = (TrainStation) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return name;
  }

  public static class TrainStationBuilder {
    private String name;
    private String eva = "";
    private boolean mittelzentrum = false;
    private boolean oberzentrum = false;

    public TrainStationBuilder(String name) {
      this.name = name;
    }

    public TrainStationBuilder eva(String eva) {
      this.eva = eva;
      return this;
    }

    public TrainStationBuilder mittelzentrum() {
      this.mittelzentrum = true;
      return this;
    }

    public TrainStationBuilder oberzentrum() {
      this.oberzentrum = true;
      return this;
    }

    public TrainStation get() {
      return new TrainStation(name, eva, mittelzentrum, oberzentrum);
    }
  }

}
