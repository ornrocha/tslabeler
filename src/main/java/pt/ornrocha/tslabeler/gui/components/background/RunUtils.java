package pt.ornrocha.tslabeler.gui.components.background;

import javax.swing.SwingWorker;

import org.pmw.tinylog.Logger;

import pt.ornrocha.tslabeler.core.processors.FilesProcessor;
import pt.ornrocha.tslabeler.gui.components.dialogs.MsgDialog;

public class RunUtils {

	public static void ProcessFilesInBackground(final MsgDialog msgpanel, final FilesProcessor processor) {

		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {

				processor.processFiles();

				while (processor.isInprocessing()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						Logger.error(e);
					}
				}
				return null;
			}

			@Override
			protected void done() {
				if (msgpanel != null)
					msgpanel.dispose();
			}
		};
		worker.execute();
		if (msgpanel != null)
			msgpanel.setVisible(true);
	}

}
