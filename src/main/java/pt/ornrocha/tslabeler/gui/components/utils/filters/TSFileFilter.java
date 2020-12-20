package pt.ornrocha.tslabeler.gui.components.utils.filters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class TSFileFilter extends FileFilter {

	private final String[] allowedExtensions = new String[] { ".csv" };

	public boolean accept(File file) {
		for (String extension : allowedExtensions) {
			if (file.getName().toLowerCase().endsWith(extension)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isFileValid(File file) {
		TSFileFilter filter = new TSFileFilter();
		return filter.accept(file);
	}

	@Override
	public String getDescription() {
		return "Supported formats";
	}

}
