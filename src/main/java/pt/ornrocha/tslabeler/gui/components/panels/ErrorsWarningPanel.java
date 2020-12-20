package pt.ornrocha.tslabeler.gui.components.panels;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pt.ornrocha.tslabeler.gui.components.dialogs.ViewErrorsDialog;
import pt.ornrocha.tslabeler.gui.main.Maingui;

public class ErrorsWarningPanel extends JPanel implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private JLabel lblWarning;
	private JButton btnNewButton;
	private ViewErrorsDialog viewdialog;
	private FilesMainPanel filespanel;

	private static String VIEW_ERRORS = "view_errors";

	public ErrorsWarningPanel() {
		initGUI();
		initComponents();
	}

	private void initGUI() {
		setLayout(new GridLayout(0, 1, 0, 0));

		lblWarning = new JLabel("");
		lblWarning.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblWarning);

		btnNewButton = new JButton("Check Errors");
		add(btnNewButton);

	}

	private void initComponents() {
		btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(this);
		btnNewButton.setActionCommand(VIEW_ERRORS);
		viewdialog = new ViewErrorsDialog();
		viewdialog.addActionListeners(this);
	}

	public void linkFilesMainPanel(FilesMainPanel filespanel) {
		this.filespanel = filespanel;
	}

	private void launchErrorsDialog() {
		if (filespanel != null)
			viewdialog.addErrors(filespanel.getErrors());
		viewdialog.showDialog(Maingui.getInstance());
		// viewdialog.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if (cmd.equals(VIEW_ERRORS)) {
			launchErrorsDialog();
		} else if (cmd.equals(ViewErrorsDialog.CLOSEPANEL)) {
			closeErrorsDialog();
		}

	}

	private void closeErrorsDialog() {
		viewdialog.dispose();
		lblWarning.setText("");
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String eventname = evt.getPropertyName();

		if (eventname.equals("newcontent"))
			if (lblWarning.getText() == "") {
				lblWarning.setText("Errors occurred while reading the files!");
				lblWarning.setForeground(Color.RED);
				btnNewButton.setEnabled(true);
			}

	}

}
