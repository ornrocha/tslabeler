package pt.ornrocha.tslabeler.gui.main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import org.pmw.tinylog.Logger;

import pt.ornrocha.tslabeler.gui.components.jfreechart.plots.TimeseriesPlotPanel;
import pt.ornrocha.tslabeler.gui.components.panels.ErrorsWarningPanel;
import pt.ornrocha.tslabeler.gui.components.panels.FilesMainPanel;

public class Maingui extends JFrame {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private TimeseriesPlotPanel plot_panel;
	private FilesMainPanel filesMainPanel;
	private ErrorsWarningPanel errorspanel;
	// private JPanel contentPane;
	private static Maingui instance;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Maingui frame = new Maingui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static Maingui getInstance() {
		if (instance == null)
			instance = new Maingui();
		return instance;
	}

	/**
	 * Create the frame.
	 */
	private Maingui() {

		initGui();

	}

	private void initGui() {
		try {
			GridBagLayout mainlayout = new GridBagLayout();
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 947, 663);
			setSize(1200, 800);
			setPreferredSize(new Dimension(1200, 800));

			mainlayout.columnWidths = new int[] { 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 };
			mainlayout.rowHeights = new int[] { 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 };
			mainlayout.columnWeights = new double[] { 1.0, 1.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1 };
			mainlayout.rowWeights = new double[] { 1.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.0,
					0.1 };

			getContentPane().setLayout(mainlayout);

			JSplitPane splitPane = new JSplitPane();
			GridBagConstraints gbc_splitPane = new GridBagConstraints();
			gbc_splitPane.gridwidth = 12;
			gbc_splitPane.gridheight = 14;
			gbc_splitPane.insets = new Insets(0, 0, 5, 5);
			gbc_splitPane.fill = GridBagConstraints.BOTH;
			gbc_splitPane.gridx = 0;
			gbc_splitPane.gridy = 0;
			getContentPane().add(splitPane, gbc_splitPane);

			filesMainPanel = new FilesMainPanel();
			JPanel rightpanel = new JPanel();
			splitPane.setLeftComponent(rightpanel);
			rightpanel.setLayout(new GridLayout(0, 1, 0, 0));
			rightpanel.add(filesMainPanel);

			plot_panel = new TimeseriesPlotPanel();
			splitPane.setRightComponent(plot_panel);
			filesMainPanel.setTimeseriesPlotPanel(plot_panel);

			JPanel rightdownpanel = new JPanel();
			rightpanel.add(rightdownpanel);
			GridBagLayout gbl_rightdownpanel = new GridBagLayout();
			gbl_rightdownpanel.columnWidths = new int[] { 0 };
			gbl_rightdownpanel.rowHeights = new int[] { 0 };
			gbl_rightdownpanel.columnWeights = new double[] { 1.0 };
			gbl_rightdownpanel.rowWeights = new double[] { 1.0 };
			rightdownpanel.setLayout(gbl_rightdownpanel);

			JSplitPane splitPane_info = new JSplitPane();
			splitPane_info.setOrientation(JSplitPane.VERTICAL_SPLIT);
			GridBagConstraints gbc_splitPane_info = new GridBagConstraints();
			gbc_splitPane_info.fill = GridBagConstraints.BOTH;
			gbc_splitPane_info.gridx = 0;
			gbc_splitPane_info.gridy = 0;
			rightdownpanel.add(splitPane_info, gbc_splitPane_info);

			errorspanel = new ErrorsWarningPanel();
			splitPane_info.setLeftComponent(errorspanel);
			filesMainPanel.setFileLoadingErrorsListener(errorspanel);
			errorspanel.linkFilesMainPanel(filesMainPanel);

			JPanel logspanel = new JPanel();
			splitPane_info.setRightComponent(logspanel);
			logspanel.setLayout(new GridLayout(1, 0, 0, 0));

			JTextArea textArea = new JTextArea();
			logspanel.add(textArea);
			setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/bigicon.png")));

		} catch (Exception e) {
			Logger.error(e);
		}
	}

   public void showMessage(String msg) {
     JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
   }

}
