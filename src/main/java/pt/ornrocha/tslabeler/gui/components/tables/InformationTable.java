package pt.ornrocha.tslabeler.gui.components.tables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.pmw.tinylog.Logger;

import pt.ornrocha.tslabeler.core.io.write.ExcelWriterUtils;
import pt.ornrocha.tslabeler.core.io.write.WriteUtils;
import pt.ornrocha.tslabeler.gui.components.utils.ExtensionFileFilter;

// TODO: Auto-generated Javadoc
/**
 * The Class InformationTable.
 */
public class InformationTable extends JTable implements MouseListener, ActionListener{


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The filter header. */
	
	/** The tablemodel. */
	protected GenericTableViewerModel tablemodel;
	
	/** The pop menu. */
	protected JPopupMenu popMenu = new JPopupMenu();
	
	/** The export. */
	protected JMenuItem export;
	
	/** The savetable. */
	public static String SAVETABLE="savetable";
	
	/**
	 * Instantiates a new information table.
	 *
	 * @param columnames the columnames
	 */
	public InformationTable(String[] columnames) {
		tablemodel=new GenericTableViewerModel(columnames, false);
		setModel(tablemodel);
		setPopMenu();
		addMouseListener(this);		
	}
	
	/**
	 * Sets the pop menu.
	 */
	protected void setPopMenu(){
		export=new JMenuItem("Export table");
		export.setActionCommand(SAVETABLE);
		export.addActionListener(this);
		popMenu.add(export);
		popMenu.setInvoker(this);
		setComponentPopupMenu(popMenu);
	}
	
	/**
	 * Pop menu.
	 *
	 * @param event the event
	 */
	protected void popMenu(MouseEvent event) {
	    popMenu.show(event.getComponent(), event.getX(), event.getY());
	}
	
	
	/**
	 * Sets the data.
	 *
	 * @param listobj the new data
	 */
	public void setData(List<Object[]> listobj) {
		clearTable();
		for (int i = 0; i < listobj.size(); i++) {
			tablemodel.addRow(listobj.get(i));
		}
	}
	
	/**
	 * Adds the row.
	 *
	 * @param row the row
	 */
	public void addRow(Object[] row) {
		tablemodel.addRow(row);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JTable#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex){
		tablemodel.setValueAt(value, rowIndex, columnIndex);
	}
	
	
	/**
	 * Clear table.
	 */
	public void clearTable(){
		tablemodel.resetTable();
	}
	
	
	/**
	 * Append data.
	 *
	 * @param row the row
	 */
	public void appendData(Object[] row) {
		tablemodel.addRow(row);
	}
	

	
	
	
	
	/* (non-Javadoc)
	 * @see javax.swing.JTable#getToolTipText(java.awt.event.MouseEvent)
	 */
	@Override
	public String getToolTipText(MouseEvent e) {
		
    	String tip = null;
        java.awt.Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);
        
       Object val= tablemodel.getValueAt(rowIndex, colIndex);
       if(!(val instanceof String))
    	   tip=String.valueOf(val);
       else
    	   tip=(String) val;
    	
    	return tip;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JTable#getModel()
	 */
	@Override
	 public TableModel getModel() {
	        return tablemodel;
	    }



	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
	/*	if (e.isPopupTrigger()) {
			popMenu(e);
		}*/
		
	}



	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
			popMenu(e);
		}

	}



	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
       String cmd=e.getActionCommand();
       
       if(cmd.equals(SAVETABLE))
		  saveTable();
	}
	
	/**
	 * Save table.
	 */
	protected void saveTable() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new ExtensionFileFilter("Comma-separated values","csv"));
		fileChooser.addChoosableFileFilter(new ExtensionFileFilter("Excel", "xlsx"));
		int tag=fileChooser.showSaveDialog(this);
		
		if(tag==JFileChooser.APPROVE_OPTION) {
			ExtensionFileFilter filter=(ExtensionFileFilter) fileChooser.getFileFilter();
			try {
				if(filter.getExtension().equals("csv"))
					saveToCSV(fileChooser.getSelectedFile().getAbsolutePath());
				else
					saveToExcel(fileChooser.getSelectedFile().getAbsolutePath());
			} catch (IOException e) {
					Logger.error(e);
				}
		}
	}
	
	/**
	 * Save to CSV.
	 *
	 * @param filename the filename
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void saveToCSV(String filename) throws IOException {
		
		StringBuilder str=new StringBuilder();
		
		String[] header=tablemodel.getColumnNames();
		
		for (int i = 0; i < header.length; i++) {
			str.append(header[i]);
			if(i<(header.length-1))
				str.append("\t");
		}
		str.append("\n");
		
		for (int i = 0; i < getRowCount(); i++) {
			Object[] row=tablemodel.getRowAt(i);
			
			for (int j = 0; j < row.length; j++) {
				str.append(String.valueOf(row[j]));	
				if(j<(row.length-1))
					str.append("\t");
			}
			str.append("\n");
			
		}
		
		String filepath=filename+".csv";
		WriteUtils.writeDataTofile(filepath, str.toString());
		
	}
	
	
	/**
	 * Save to excel.
	 *
	 * @param filepath the filepath
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void saveToExcel(String filepath) throws IOException {
		
		ArrayList<ArrayList<Object>> intputdata=new ArrayList<>();
		
		ArrayList<Object> headerexcel=new ArrayList<>();
		String[] header=tablemodel.getColumnNames();

		for (int i = 0; i < header.length; i++) {
			headerexcel.add(header[i]);
		}
		intputdata.add(headerexcel);
		
		for (int i = 0; i < getRowCount(); i++) {
			ArrayList<Object> rowexcel=new ArrayList<>();
			Object[] row=tablemodel.getRowAt(i);
			for (int j = 0; j < row.length; j++) {
				rowexcel.add(row[j]);
			}
			intputdata.add(rowexcel);
		}
		
		ExcelWriterUtils.WriteDataToNewExcelFile(filepath, "xlsx", intputdata, "Exported Data");
	}

}
