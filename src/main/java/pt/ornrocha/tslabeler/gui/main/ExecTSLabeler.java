package pt.ornrocha.tslabeler.gui.main;

import javax.swing.SwingUtilities;

public class ExecTSLabeler {

	class MainGUIStarter implements Runnable {

		@Override
		public void run() {
			Maingui inst = Maingui.getInstance();

			inst.setLocationRelativeTo(null);
			inst.setVisible(true);
			inst.setSize(1600, 900);

		}

	}

	void run() {

		Thread run = new Thread() {
			public void run() {
				SwingUtilities.invokeLater(new MainGUIStarter());
			}
		};

		run.start();

	}

	public static void main(String[] args) {
		ExecTSLabeler exec = new ExecTSLabeler();
		exec.run();

	}

}
