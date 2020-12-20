package pt.ornrocha.tslabeler.gui.components.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.pmw.tinylog.Logger;

import pt.ornrocha.tslabeler.core.containers.ContainerState;
import pt.ornrocha.tslabeler.core.containers.FileInfo;
import pt.ornrocha.tslabeler.core.containers.TimeSeriesContainer;
import pt.ornrocha.tslabeler.core.processors.FilesProcessor;
import pt.ornrocha.tslabeler.core.utils.collections.IndexedHashMap;
import pt.ornrocha.tslabeler.gui.components.background.RunUtils;
import pt.ornrocha.tslabeler.gui.components.dialogs.MsgDialog;
import pt.ornrocha.tslabeler.gui.components.jfreechart.plots.TimeseriesPlotPanel;
import pt.ornrocha.tslabeler.gui.components.utils.filters.TSFileFilter;
import pt.ornrocha.tslabeler.gui.main.Maingui;

public class FilesMainPanel extends JPanel implements ListSelectionListener, ActionListener {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private FilesScrollPanel filesScrollPanel;
	private TimeseriesPlotPanel plotPanel;
	private IndexedHashMap<String, TimeSeriesContainer> timeseries = null;
	private IndexedHashMap<String, String> errors;
	private PropertyChangeListener loadingerrorlistener;

	private boolean reseting = false;
	private boolean loading = false;
	private boolean started = false;

	private static String BUTTON_LOAD_FILES = "button_load_files";
	private static String BUTTON_REMOVE_FILES = "button_remove_files";
	private static String BUTTON_CLEAR_FILES = "button_clear_files";
	private static String BUTTON_SAVE_ALL_CHANGES = "button_save_all_changes";
	private static String BUTTON_DELETE_FILE = "button_delete_file";

	private int currentselectedrow = -1;

	public FilesMainPanel() {
		setBorder(new TitledBorder(null, "Files to process", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		initGUI();
		initComponents();
	}

	private void initGUI() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 7, 0, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 };
		gridBagLayout.rowHeights = new int[] { 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 1.0, 0.1 };
		setLayout(gridBagLayout);

		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridwidth = 13;
		gbc_panel_1.gridheight = 11;
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		add(panel_1, gbc_panel_1);

		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 1 };
		gbl_panel_1.rowHeights = new int[] { 1 };
		gbl_panel_1.columnWeights = new double[] { 1.0 };
		gbl_panel_1.rowWeights = new double[] { 1.0 };
		panel_1.setLayout(gbl_panel_1);

		filesScrollPanel = new FilesScrollPanel(this);
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 0;
		panel_1.add(filesScrollPanel, gbc_panel_2);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 13;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 11;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 7, 7, 7, 7, 7 };
		gbl_panel.rowHeights = new int[] { 7, 7, 7 };
		gbl_panel.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0 };
		gbl_panel.rowWeights = new double[] { 0.1, 0.1, 0.1 };
		panel.setLayout(gbl_panel);

		JButton btnNewButton_openfiles = new JButton("Load files");
		btnNewButton_openfiles.addActionListener(this);
		btnNewButton_openfiles.setActionCommand(BUTTON_LOAD_FILES);
		GridBagConstraints gbc_btnNewButton_openfiles = new GridBagConstraints();
		gbc_btnNewButton_openfiles.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_openfiles.gridwidth = 2;
		gbc_btnNewButton_openfiles.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_openfiles.gridx = 0;
		gbc_btnNewButton_openfiles.gridy = 0;
		panel.add(btnNewButton_openfiles, gbc_btnNewButton_openfiles);

		JButton btnNewButton_removeselected = new JButton("Remove selected");
		btnNewButton_removeselected.addActionListener(this);
		btnNewButton_removeselected.setActionCommand(BUTTON_REMOVE_FILES);
		GridBagConstraints gbc_btnNewButton_removeselected = new GridBagConstraints();
		gbc_btnNewButton_removeselected.gridwidth = 2;
		gbc_btnNewButton_removeselected.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_removeselected.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_removeselected.gridx = 3;
		gbc_btnNewButton_removeselected.gridy = 0;
		panel.add(btnNewButton_removeselected, gbc_btnNewButton_removeselected);

		JButton btnNewButton_clear = new JButton("Clear table");
		btnNewButton_clear.addActionListener(this);
		btnNewButton_clear.setActionCommand(BUTTON_REMOVE_FILES);

		JButton btnDeleteFile = new JButton("Delete file");
		btnDeleteFile.addActionListener(this);
		btnDeleteFile.setActionCommand(BUTTON_DELETE_FILE);
		GridBagConstraints gbc_btnDeleteFile = new GridBagConstraints();
		gbc_btnDeleteFile.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDeleteFile.gridwidth = 2;
		gbc_btnDeleteFile.gridx = 0;
		gbc_btnDeleteFile.gridy = 1;
		panel.add(btnDeleteFile, gbc_btnDeleteFile);
		btnNewButton_clear.setActionCommand(BUTTON_CLEAR_FILES);
		GridBagConstraints gbc_btnNewButton_clear = new GridBagConstraints();
		gbc_btnNewButton_clear.gridwidth = 2;
		gbc_btnNewButton_clear.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_clear.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_clear.gridx = 3;
		gbc_btnNewButton_clear.gridy = 1;
		panel.add(btnNewButton_clear, gbc_btnNewButton_clear);

		JButton btnNewButton_savechanges = new JButton("Record all changes to files");
		btnNewButton_savechanges.addActionListener(this);
		btnNewButton_savechanges.setActionCommand(BUTTON_SAVE_ALL_CHANGES);
		GridBagConstraints gbc_btnNewButton_savechanges = new GridBagConstraints();
		gbc_btnNewButton_savechanges.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_savechanges.gridwidth = 5;
		gbc_btnNewButton_savechanges.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_savechanges.gridx = 0;
		gbc_btnNewButton_savechanges.gridy = 2;
		panel.add(btnNewButton_savechanges, gbc_btnNewButton_savechanges);
	}

	private void initComponents() {
		this.filesScrollPanel.getTable().getSelectionModel().addListSelectionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if (cmd.equals(BUTTON_CLEAR_FILES) && timeseries != null && timeseries.size() > 0) {
			int option = JOptionPane.showConfirmDialog(Maingui.getInstance(), "Do you really want to clear the table?",
					"Confirmation", JOptionPane.YES_NO_OPTION);
			if (option == 0) {
				removeAllFiles();
			}
		} else if (cmd.equals(BUTTON_LOAD_FILES)) {
			loadFiles();
		} else if (cmd.equals(BUTTON_REMOVE_FILES) && timeseries != null && timeseries.size() > 0) {
			removeSelectedFiles();
		} else if (cmd.equals(BUTTON_SAVE_ALL_CHANGES) && timeseries != null && timeseries.size() > 0) {
			saveAllChanges();
		} else if (cmd.equals(BUTTON_DELETE_FILE)) {
			deleteFile();
		}

	}

	private void deleteFile() {
		this.reseting = true;
		int row = filesScrollPanel.getTable().getSelectedRow();
		if (row > -1) {

			int n = JOptionPane.showOptionDialog(Maingui.getInstance(),
					"Your file will be permanently deleted, do you want to continue?", "Delete file",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

			if (n == 0) {

				FileInfo info = timeseries.getValueAt(row).getInfoFile();
				if (info != null) {
					String filepath = info.getFilepath();
					File file = new File(filepath);
					file.delete();
				}

				timeseries.remove(timeseries.getKeyAt(row));
				filesScrollPanel.removeRowAtPos(row);
				selectCloseRow(row);
			}

		}

		this.reseting = false;
	}

	private void saveAllChanges() {

		int numberchanges = 0;

		int n = JOptionPane.showOptionDialog(Maingui.getInstance(),
				"All your files will be replaced, do you want to continue?", "Save question", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (n == 0)
			for (Map.Entry<String, TimeSeriesContainer> entry : timeseries.entrySet()) {
				TimeSeriesContainer timeserie = entry.getValue();
				if (timeserie.getState().equals(ContainerState.UPDATED)) {
					String filepath = timeserie.getInfoFile().getFilepath();
					try {
						timeserie.getDataframe().write().csv(filepath);

					} catch (IOException e) {
						Logger.error(e);
					}
					numberchanges++;
				}
			}

		String msg = "No changes were found";
		if (numberchanges > 0)
			msg = numberchanges + " files were replaced";
		JOptionPane.showMessageDialog(Maingui.getInstance(), msg, "Information", JOptionPane.INFORMATION_MESSAGE);
	}

	private void removeAllFiles() {
		this.reseting = true;
		filesScrollPanel.resetTable();
		timeseries = null;
		plotPanel.getChartPanel().resetChartState();
		started = false;
		this.reseting = false;

	}

	private void loadFiles() {

		JFileChooser chooser = new JFileChooser();
		// chooser.addChoosableFileFilter(filter);
		chooser.addChoosableFileFilter(new TSFileFilter());
		chooser.setMultiSelectionEnabled(true);
		int option = chooser.showOpenDialog(Maingui.getInstance());
		if (option == JFileChooser.APPROVE_OPTION) {

			ArrayList<String> allowedfiles = new ArrayList<>();
			File[] files = chooser.getSelectedFiles();
			for (File file : files) {
				allowedfiles.add(file.getAbsolutePath());
			}
			processFileList(allowedfiles);
		}
	}

	private void removeSelectedFiles() {

		this.reseting = true;
		int[] rows = filesScrollPanel.getTable().getSelectedRows();
		if (rows.length > 0) {
			filesScrollPanel.removeRowsAtPosition(rows);
			ArrayList<String> keys = new ArrayList<String>();
			for (int i = 0; i < rows.length; i++) {
				keys.add(timeseries.getKeyAt(i));
			}
			for (String key : keys) {
				timeseries.remove(key);
			}
			if (timeseries.size() == 0) {
				plotPanel.getChartPanel().resetChartState();
			} else {
				selectCloseRow(rows[0]);
			}
		} else if (timeseries != null)
			JOptionPane.showMessageDialog(Maingui.getInstance(), "Select at least one file!", "Information",
					JOptionPane.INFORMATION_MESSAGE);
		this.reseting = false;

	}

	public void setTimeseriesPlotPanel(TimeseriesPlotPanel plotpanel) {
		this.plotPanel = plotpanel;
	}

	public IndexedHashMap<String, String> getErrors() {
		return errors;
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		int row = filesScrollPanel.getTable().getSelectedRow();
		if (row > -1)
			if (!reseting && !loading) {
				showSelectedTimeSeries(row);
				currentselectedrow = row;
			}
	}

	public void setFileLoadingErrorsListener(PropertyChangeListener l) {
		this.loadingerrorlistener = l;
	}

	private void showSelectedTimeSeries(int index) {

		TimeSeriesContainer container = timeseries.getValueAt(index);
		plotPanel.setTimeseriesContainer(container);

	}

	public void addFiles(ArrayList<TimeSeriesContainer> timeseries) {
		this.loading = true;

		if (timeseries != null && timeseries.size() > 0) {
			if (this.timeseries == null) {
				this.timeseries = new IndexedHashMap<>();
				// jCheckBoxSelectall.setSelected(true);
			}

			ArrayList<TimeSeriesContainer> allowedcontainers = new ArrayList<TimeSeriesContainer>();
			for (TimeSeriesContainer container : timeseries) {
				if (!this.timeseries.containsKey(container.getInfoFile().getFilepath())) {
					this.timeseries.put(container.getInfoFile().getFilepath(), container);
					allowedcontainers.add(container);
				}
			}
			this.filesScrollPanel.addFilesToTable(allowedcontainers);
			if (allowedcontainers.size() > 0)
				checkAndInitPlot();
		}
		this.loading = false;
	}

	private void checkAndInitPlot() {
		if (!started && timeseries.size() > 0) {
			showSelectedTimeSeries(0);
			currentselectedrow = 0;
			this.started = true;
		}
	}

	private void selectCloseRow(int row) {

		if (row == 0 && timeseries.size() > 0) {
			showSelectedTimeSeries(0);
			currentselectedrow = 0;
			filesScrollPanel.getTable().setRowSelectionInterval(0, 0);
		} else if (timeseries == null || timeseries.size() == 0) {
			currentselectedrow = -1;
			plotPanel.getChartPanel().resetChartState();
		} else {
			showSelectedTimeSeries(row - 1);
			currentselectedrow = row - 1;
			filesScrollPanel.getTable().setRowSelectionInterval(row - 1, row - 1);
		}

	}

	public void processFileList(ArrayList<String> files) {

		FilesProcessor processor = new FilesProcessor(files);
		if (loadingerrorlistener != null)
			processor.addFileLoadingErrorsListener(loadingerrorlistener);

		MsgDialog msg = new MsgDialog("Loading files...", Maingui.getInstance());
		RunUtils.ProcessFilesInBackground(msg, processor);
		addFiles(processor.getTimeseries());
		this.errors = processor.getLoadingerrors();

	}

}
