package pt.ornrocha.tslabeler.gui.components.panels;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.pmw.tinylog.Logger;

import pt.ornrocha.tslabeler.core.containers.TimeSeriesContainer;
import pt.ornrocha.tslabeler.gui.components.tables.FilesTableModel;
import pt.ornrocha.tslabeler.gui.components.utils.filters.TSFileFilter;

public class FilesScrollPanel extends JScrollPane implements DropTargetListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FilesMainPanel parentPanel = null;
	protected JTable table;
	protected FilesTableModel tablemodel;
	private DropTarget dropTarget;

	public FilesScrollPanel(FilesMainPanel parentPanel) {
		this.parentPanel = parentPanel;
		initGUI();
		initTableModel();
		dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this, true);
		this.setToolTipText(
				"<html><font size=\"4\", color=\"red\"><b>" + "Drop your files over the table</b></font></html>");

	}

	protected void initTableModel() {
		String[] colnames = { "Filename", "Filepath" };
		this.tablemodel = new FilesTableModel(colnames, true);
		this.tablemodel.seteditfromcolumn(1);
		this.table = new JTable();
		this.table.setModel(this.tablemodel);

		this.getViewport().add(table);
	}

	private void initGUI() {
		try {
			{
				this.setPreferredSize(new java.awt.Dimension(300, 260));
			}
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	public JTable getTable() {
		return this.table;
	}

	public void resetTable() {
		tablemodel.resetTable();
	}

	public void removeRowsAtPosition(int[] rows) {
		tablemodel.removeRowsAtPos(rows);
	}

	public void removeRowAtPos(int row) {
		tablemodel.removeRowAtPos(row);
	}

	@Override
	public void dragEnter(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragExit(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragOver(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drop(DropTargetDropEvent dd) {
		try {
			if (dd.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				dd.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
				Transferable t = dd.getTransferable();

				List<File> data = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);

				ArrayList<String> allowedfiles = new ArrayList<>();

				TSFileFilter filter = new TSFileFilter();

				for (File file : data) {

					if (filter.accept(file)) {
						allowedfiles.add(file.getAbsolutePath());
					}
				}

				if (allowedfiles.size() > 0)
					addFileFromDragAndDrop(allowedfiles);
			}
		} catch (UnsupportedFlavorException | IOException e) {
			dd.rejectDrop();
		}

	}

	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void addFilesToTable(ArrayList<TimeSeriesContainer> timeseries) {
		List<Object[]> listfiles = new ArrayList<>();
		for (TimeSeriesContainer container : timeseries) {
			Object[] obj = new Object[2];
			obj[0] = container.getInfoFile().getName();
			obj[1] = container.getInfoFile().getFilepath();
			listfiles.add(obj);
		}
		tablemodel.insertListOfData(listfiles);

	}

	private void addFileFromDragAndDrop(ArrayList<String> files) {
		parentPanel.processFileList(files);

	}

}
