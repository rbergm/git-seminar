package de.tudresden.geo.gitseminar.util;

import org.jgrapht.Graph;

public class Graphs {

  public static <NodeType, EdgeType> NodeType edgeTarget(Graph<NodeType, EdgeType> graph,
      NodeType node, EdgeType edge) {
    NodeType theoreticalTarget = graph.getEdgeTarget(edge);
    if (theoreticalTarget.equals(node)) {
      return graph.getEdgeSource(edge);
    } else {
      return theoreticalTarget;
    }
  }

}
