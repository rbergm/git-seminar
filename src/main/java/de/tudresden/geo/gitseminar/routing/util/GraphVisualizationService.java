package de.tudresden.geo.gitseminar.routing.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.util.mxCellRenderer;
import de.tudresden.geo.gitseminar.routing.StationConnection;
import de.tudresden.geo.gitseminar.routing.TrainStation;


public class GraphVisualizationService {

	public static void displayTrainLineNetwork(Graph<TrainStation, StationConnection> network) {
		var image = graphToImage(network);

		ImageIcon icon = new ImageIcon(image);
		JLabel label = new JLabel(icon);

		JOptionPane.showMessageDialog(null, label);
	}

	public static void saveTrainLineNetwork(Graph<TrainStation, StationConnection> network,
			String outFile) throws IOException {
		var image = graphToImage(network);
		File file = new File(outFile);
		ImageIO.write(image, "PNG", file);
	}

	private static BufferedImage graphToImage(Graph<TrainStation, StationConnection> network) {
		JGraphXAdapter<TrainStation, StationConnection> graphAdapter = new JGraphXAdapter<>(network);
		mxIGraphLayout layout = new mxOrganicLayout(graphAdapter);
		layout.execute(graphAdapter.getDefaultParent());
		BufferedImage image =
				mxCellRenderer.createBufferedImage(graphAdapter, null, 1, Color.WHITE, true, null);
		return image;
	}

}
