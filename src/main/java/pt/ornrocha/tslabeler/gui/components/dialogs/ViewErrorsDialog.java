package pt.ornrocha.tslabeler.gui.components.dialogs;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableColumnModel;

import pt.ornrocha.tslabeler.core.utils.collections.IndexedHashMap;
import pt.ornrocha.tslabeler.gui.components.tables.InformationTable;

public class ViewErrorsDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel panel_cmds;
	private JPanel panel_table;
	private JButton btnClose;
	private JButton btnSave;
	private InformationTable table;
	private Component parent;
	private IndexedHashMap<String, ArrayList<String>> allerrors = new IndexedHashMap<String, ArrayList<String>>();
	private IndexedHashMap<String, String> lasterrors;

	public static String CLOSEPANEL = "close_panel";
	private static String ALLDATA = "show_all_data";
	private JButton btnViewAllErrors;
	private int numberdatasets = 0;

	public ViewErrorsDialog() {
		initGUI();
		initComponents();
	}

	private void initGUI() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 1 };
		gridBagLayout.rowHeights = new int[] { 1, 1 };
		gridBagLayout.columnWeights = new double[] { 1.0 };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.1 };
		getContentPane().setLayout(gridBagLayout);

		panel_table = new JPanel();
		GridBagConstraints gbc_panel_table = new GridBagConstraints();
		gbc_panel_table.insets = new Insets(0, 0, 5, 0);
		gbc_panel_table.fill = GridBagConstraints.BOTH;
		gbc_panel_table.gridx = 0;
		gbc_panel_table.gridy = 0;
		getContentPane().add(panel_table, gbc_panel_table);
		panel_table.setLayout(new GridLayout(1, 0, 0, 0));
		JScrollPane scrollpane = new JScrollPane();
		panel_table.add(scrollpane);

		table = new InformationTable(new String[] { "Filepath", "Error" });
		scrollpane.setViewportView(table);
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setWidth(50);
		columnModel.getColumn(1).setWidth(150);
		columnModel.getColumn(0).setPreferredWidth(50);
		columnModel.getColumn(1).setPreferredWidth(150);

		panel_cmds = new JPanel();
		GridBagConstraints gbc_panel_cmds = new GridBagConstraints();
		gbc_panel_cmds.fill = GridBagConstraints.BOTH;
		gbc_panel_cmds.gridx = 0;
		gbc_panel_cmds.gridy = 1;
		getContentPane().add(panel_cmds, gbc_panel_cmds);
		GridBagLayout gbl_panel_cmds = new GridBagLayout();
		gbl_panel_cmds.columnWidths = new int[] { 1, 1, 1, 1, 1, 1 };
		gbl_panel_cmds.rowHeights = new int[] { 1 };
		gbl_panel_cmds.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0 };
		gbl_panel_cmds.rowWeights = new double[] { 1.0 };
		panel_cmds.setLayout(gbl_panel_cmds);

		btnViewAllErrors = new JButton("View all errors");
		btnViewAllErrors.addActionListener(this);
		btnViewAllErrors.setActionCommand(ALLDATA);
		GridBagConstraints gbc_btnViewAllErrors = new GridBagConstraints();
		gbc_btnViewAllErrors.insets = new Insets(0, 0, 0, 5);
		gbc_btnViewAllErrors.gridx = 0;
		gbc_btnViewAllErrors.gridy = 0;
		panel_cmds.add(btnViewAllErrors, gbc_btnViewAllErrors);

		btnSave = new JButton("Save");
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSave.insets = new Insets(0, 0, 0, 5);
		gbc_btnSave.gridx = 3;
		gbc_btnSave.gridy = 0;
		panel_cmds.add(btnSave, gbc_btnSave);

		btnClose = new JButton("Close");
		GridBagConstraints gbc_btnClose = new GridBagConstraints();
		gbc_btnClose.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnClose.insets = new Insets(0, 0, 0, 5);
		gbc_btnClose.gridx = 4;
		gbc_btnClose.gridy = 0;
		panel_cmds.add(btnClose, gbc_btnClose);

		this.setSize(1200, 600);
	}

	private void initComponents() {
		btnSave.addActionListener(table);
		btnSave.setActionCommand(InformationTable.SAVETABLE);

	}

	public void addActionListeners(ActionListener l) {
		btnClose.addActionListener(l);
		btnClose.setActionCommand(CLOSEPANEL);
	}

	public void showDialog(Component parent) {
		this.parent = parent;
		if (lasterrors != null)
			initErrorsTable();
		if (allerrors.size() > 0 && numberdatasets > 1)
			btnViewAllErrors.setEnabled(true);
		else
			btnViewAllErrors.setEnabled(false);
		setAlwaysOnTop(true);
		// setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		if (parent != null)
			this.setLocationRelativeTo(parent);

		this.setVisible(true);
	}

	public void addErrors(IndexedHashMap<String, String> errors) {

		lasterrors = errors;

		for (Entry<String, String> entry : errors.entrySet()) {
			if (!allerrors.containsKey(entry.getKey())) {
				ArrayList<String> errorkey = new ArrayList<String>();
				errorkey.add(entry.getValue());
				allerrors.put(entry.getKey(), errorkey);
			} else {
				if (!allerrors.get(entry.getKey()).contains(entry.getValue()))
					allerrors.get(entry.getKey()).add(entry.getValue());
			}
		}

		this.numberdatasets++;
	}

	private void initErrorsTable() {
		List<Object[]> errors = new ArrayList<>();
		for (Entry<String, String> entry : lasterrors.entrySet()) {
			Object[] obj = new Object[2];
			obj[0] = entry.getKey();
			obj[1] = entry.getValue();
			errors.add(obj);
		}
		table.setData(errors);
	}

	private void viewAllErrors() {
		List<Object[]> errors = getAllErrorsList();
		table.clearTable();
		table.setData(errors);
	}

	private List<Object[]> getAllErrorsList() {

		List<Object[]> errors = new ArrayList<>();

		for (String key : allerrors.keySet()) {

			for (String msg : allerrors.get(key)) {
				Object[] obj = new Object[2];
				obj[0] = key;
				obj[1] = msg;
				errors.add(obj);
			}
		}

		return errors;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if (cmd.equals(ALLDATA)) {
			viewAllErrors();
		}

	}

}
