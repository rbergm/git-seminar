package de.tudresden.geo.gitseminar.geoserver;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.tudresden.geo.gitseminar.geoserver.Workspaces.Workspace.WorkspaceEntry;

/**
 * Value object matching the GeoServer workspace data.
 *
 * @author Rico Bergmann
 * @see https://docs.geoserver.org/latest/en/api/#1.0.0/workspaces.yaml
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Workspaces {

  private Workspace workspaces;

  public Workspaces() {}

  public Workspace getWorkspaces() {
    return workspaces;
  }

  public void setWorkspaces(Workspace workspaces) {
    this.workspaces = workspaces;
  }

  public List<WorkspaceEntry> flatten() {
    return workspaces.workspace;
  }

  public WorkspaceEntry flattenOne() {
    return workspaces.workspace.get(0);
  }


  @Override
  public String toString() {
    return "Workspaces [workspaces=" + workspaces + "]";
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Workspace {

    private List<WorkspaceEntry> workspace;

    public Workspace() {}

    public List<WorkspaceEntry> getWorkspace() {
      return workspace;
    }

    public void setWorkspace(List<WorkspaceEntry> workspace) {
      this.workspace = workspace;
    }

    @Override
    public String toString() {
      return "Workspace [workspace=" + workspace + "]";
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WorkspaceEntry {

      private String name;
      private String href;

      public WorkspaceEntry() {}

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
        return "WorkspaceEntry [name=" + name + ", href=" + href + "]";
      }

    }

  }

}
