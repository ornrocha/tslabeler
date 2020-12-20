package pt.ornrocha.tslabeler.gui.components.tables;

import java.util.List;

public class FilesTableModel extends GenericTableViewerModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FilesTableModel(String[] colNames, boolean isValueCellsEditable) {
		super(colNames, isValueCellsEditable);
	}

	public FilesTableModel(List<Object[]> listobj, String[] colNames, boolean isValueCellsEditable) {
		super(listobj, colNames, isValueCellsEditable);
	}

}
