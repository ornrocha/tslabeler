package pt.ornrocha.tslabeler.gui.components.jfreechart.plots;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import pt.ornrocha.tslabeler.core.containers.TimeSeriesContainer;

public class TimeseriesPlotPanel extends JPanel implements ActionListener {

    /**
    * 
    */
    private static final long serialVersionUID = 1L;

    private FreechartPlotPanel plotpanel;
    private JComboBox comboBox_trace;
    private JButton btnAutoZoom;
    private JButton btnmarkanomalies;
    private JButton btndeselectanomalies;

    private static String COMBOBOX_TRACE = "combox_trace_cmd";
    private static String BUTTON_AUTO_ZOOM = "button_autozoom_cmd";
    private static String BUTTON_SAVE_CHANGES = "button_save_changes";
    private static String BUTTON_UPDATE_CHANGES = "button_update_changes";
    private static String BUTTON_MARK_ANOMALIES = "button_mark_anomalies";
    private static String BUTTON_UNMARK_ANOMALIES = "button_unmark_anomalies";

    public TimeseriesPlotPanel() {
	setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Chart", TitledBorder.LEADING,
		TitledBorder.TOP, null, Color.DARK_GRAY));
	initGUI();
	initComponents();
    }

    private void initGUI() {
	GridBagLayout gridBagLayout = new GridBagLayout();
	this.setPreferredSize(new Dimension(914, 574));
	gridBagLayout.columnWidths = new int[] { 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 };
	gridBagLayout.rowHeights = new int[] { 7, 7, 7, 7, 7, 7, 7, 7 };
	gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
	gridBagLayout.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
	setLayout(gridBagLayout);

	plotpanel = new FreechartPlotPanel();
	GridBagConstraints gbc_plotpanel = new GridBagConstraints();
	gbc_plotpanel.gridwidth = 10;
	gbc_plotpanel.gridheight = 8;
	gbc_plotpanel.insets = new Insets(0, 0, 5, 5);
	gbc_plotpanel.fill = GridBagConstraints.BOTH;
	gbc_plotpanel.gridx = 0;
	gbc_plotpanel.gridy = 0;
	add(plotpanel, gbc_plotpanel);
	plotpanel.setLayout(new GridLayout(1, 0, 0, 0));

	JPanel controlspanel = new JPanel();
	GridBagConstraints gbc_controlspanel = new GridBagConstraints();
	gbc_controlspanel.anchor = GridBagConstraints.SOUTH;
	gbc_controlspanel.gridheight = 2;
	gbc_controlspanel.gridwidth = 10;
	gbc_controlspanel.fill = GridBagConstraints.HORIZONTAL;
	gbc_controlspanel.gridx = 0;
	gbc_controlspanel.gridy = 8;
	add(controlspanel, gbc_controlspanel);
	GridBagLayout gbl_controlspanel = new GridBagLayout();
	gbl_controlspanel.columnWidths = new int[] { 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 };
	gbl_controlspanel.rowHeights = new int[] { 7, 7 };
	gbl_controlspanel.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
	gbl_controlspanel.rowWeights = new double[] { 0.0, 0.0 };
	controlspanel.setLayout(gbl_controlspanel);

	comboBox_trace = new JComboBox();
	comboBox_trace.addActionListener(this);
	comboBox_trace.setActionCommand(COMBOBOX_TRACE);
	GridBagConstraints gbc_comboBox_trace = new GridBagConstraints();
	gbc_comboBox_trace.insets = new Insets(0, 0, 5, 5);
	gbc_comboBox_trace.fill = GridBagConstraints.HORIZONTAL;
	gbc_comboBox_trace.gridx = 1;
	gbc_comboBox_trace.gridy = 0;
	controlspanel.add(comboBox_trace, gbc_comboBox_trace);

	JButton btnNewButton_update = new JButton("Save current changes");
	btnNewButton_update.addActionListener(this);

	btnmarkanomalies = new JButton("Label all visible points as anomalies");
	btnmarkanomalies.addActionListener(this);
	btnmarkanomalies.setActionCommand(BUTTON_MARK_ANOMALIES);
	GridBagConstraints gbc_btnmarkanomalies = new GridBagConstraints();
	gbc_btnmarkanomalies.fill = GridBagConstraints.HORIZONTAL;
	gbc_btnmarkanomalies.gridwidth = 2;
	gbc_btnmarkanomalies.insets = new Insets(0, 0, 5, 5);
	gbc_btnmarkanomalies.gridx = 3;
	gbc_btnmarkanomalies.gridy = 0;
	controlspanel.add(btnmarkanomalies, gbc_btnmarkanomalies);
	btnNewButton_update.setActionCommand(BUTTON_UPDATE_CHANGES);
	GridBagConstraints gbc_btnNewButton_update = new GridBagConstraints();
	gbc_btnNewButton_update.fill = GridBagConstraints.HORIZONTAL;
	gbc_btnNewButton_update.gridwidth = 2;
	gbc_btnNewButton_update.insets = new Insets(0, 0, 5, 5);
	gbc_btnNewButton_update.gridx = 7;
	gbc_btnNewButton_update.gridy = 0;
	controlspanel.add(btnNewButton_update, gbc_btnNewButton_update);

	btnAutoZoom = new JButton("Default zoom");
	btnAutoZoom.addActionListener(this);
	btnAutoZoom.setActionCommand(BUTTON_AUTO_ZOOM);
	GridBagConstraints gbc_btnAutoZoom = new GridBagConstraints();
	gbc_btnAutoZoom.fill = GridBagConstraints.HORIZONTAL;
	gbc_btnAutoZoom.insets = new Insets(0, 0, 0, 5);
	gbc_btnAutoZoom.gridx = 1;
	gbc_btnAutoZoom.gridy = 1;
	controlspanel.add(btnAutoZoom, gbc_btnAutoZoom);

	JButton btnNewButton_save = new JButton("Save to file");
	btnNewButton_save.addActionListener(this);

	btndeselectanomalies = new JButton("Unlabel all visible points as anomalies");
	btndeselectanomalies.addActionListener(this);
	btndeselectanomalies.setActionCommand(BUTTON_UNMARK_ANOMALIES);
	GridBagConstraints gbc_btndeselectanomalies = new GridBagConstraints();
	gbc_btndeselectanomalies.fill = GridBagConstraints.HORIZONTAL;
	gbc_btndeselectanomalies.gridwidth = 2;
	gbc_btndeselectanomalies.insets = new Insets(0, 0, 0, 5);
	gbc_btndeselectanomalies.gridx = 3;
	gbc_btndeselectanomalies.gridy = 1;
	controlspanel.add(btndeselectanomalies, gbc_btndeselectanomalies);
	btnNewButton_save.setActionCommand(BUTTON_SAVE_CHANGES);
	GridBagConstraints gbc_btnNewButton_save = new GridBagConstraints();
	gbc_btnNewButton_save.fill = GridBagConstraints.BOTH;
	gbc_btnNewButton_save.gridwidth = 2;
	gbc_btnNewButton_save.insets = new Insets(0, 0, 0, 5);
	gbc_btnNewButton_save.gridx = 7;
	gbc_btnNewButton_save.gridy = 1;
	controlspanel.add(btnNewButton_save, gbc_btnNewButton_save);

    }

    private void initComponents() {
	final String[] traceCmds = { "Enable Trace", "Disable Trace" };
	comboBox_trace.setModel(new DefaultComboBoxModel(traceCmds));
    }

    public void setTimeseriesContainer(TimeSeriesContainer container) {
	plotpanel.showTimeSeries(container);
    }

    public FreechartPlotPanel getChartPanel() {
	return plotpanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	String cmd = e.getActionCommand();

	if (cmd.equals(COMBOBOX_TRACE) && plotpanel.isActive()) {
	    String tracecmd = (String) comboBox_trace.getSelectedItem();
	    if (tracecmd.equals("Enable Trace")) {
		plotpanel.getChartPanel().setHorizontalAxisTrace(true);
		plotpanel.getChartPanel().setVerticalAxisTrace(true);
		plotpanel.getChartPanel().repaint();
	    } else {
		plotpanel.getChartPanel().setHorizontalAxisTrace(false);
		plotpanel.getChartPanel().setVerticalAxisTrace(false);
		plotpanel.getChartPanel().repaint();
	    }
	} else if (cmd.equals(BUTTON_AUTO_ZOOM)) {
	    if (plotpanel.isActive())
		plotpanel.getChartPanel().restoreAutoBounds();
	} else if (cmd.equals(BUTTON_UPDATE_CHANGES)) {
	    if (plotpanel.isActive())
		plotpanel.updateTimeserie();
	} else if (cmd.equals(BUTTON_SAVE_CHANGES)) {
	    if (plotpanel.isActive())
		plotpanel.SaveTimeserie();
	}else if (cmd.equals(BUTTON_MARK_ANOMALIES)) {
	    if(plotpanel.isActive())
		plotpanel.markVisiblePointsAsAnomalies();
	}else if (cmd.equals(BUTTON_UNMARK_ANOMALIES)) {
	    if(plotpanel.isActive())
		plotpanel.unmarkVisiblePointsAsAnomalies();
	}

    }

}
