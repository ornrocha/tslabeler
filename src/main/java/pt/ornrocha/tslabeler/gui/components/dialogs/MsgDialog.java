package pt.ornrocha.tslabeler.gui.components.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.pmw.tinylog.Logger;

public class MsgDialog extends JDialog implements Runnable {

	private static final long serialVersionUID = 1L;
	private JTextPane jTextPanemsg;
	private boolean setVisible = true;

	public MsgDialog(String message, Component comp) {
		initGUI();
		setMessage(message);
		setTextAtributes();
		this.setLocationRelativeTo(comp);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setModal(true);
	}

	public void setMessage(String msg) {
		this.jTextPanemsg.setText(msg);
	}

	public boolean maintainVisible() {
		return setVisible;
	}

	public void close() {
		this.setVisible = false;
	}

	private void setTextAtributes() {
		StyledDocument doc = jTextPanemsg.getStyledDocument();

		SimpleAttributeSet bSet = new SimpleAttributeSet();

		StyleConstants.setAlignment(bSet, StyleConstants.ALIGN_CENTER);
		StyleConstants.setFontFamily(bSet, "Courier");
		StyleConstants.setFontSize(bSet, 18);
		StyleConstants.setBold(bSet, true);

		doc.setParagraphAttributes(0, doc.getLength() - 1, bSet, false);

	}

	@Override
	public void run() {
		this.setVisible(true);
	}

	private void initGUI() {
		try {
			{
			}
			BorderLayout thisLayout = new BorderLayout();
			getContentPane().setLayout(thisLayout);
			{
				jTextPanemsg = new JTextPane();
				getContentPane().add(jTextPanemsg, BorderLayout.CENTER);
			}
			{
				this.setSize(391, 82);
			}
		} catch (Exception e) {
			Logger.error(e);
		}
	}
}
