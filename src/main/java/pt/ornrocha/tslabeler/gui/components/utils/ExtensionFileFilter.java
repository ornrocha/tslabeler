package pt.ornrocha.tslabeler.gui.components.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FilenameUtils;

public class ExtensionFileFilter extends FileFilter {

	private String ext;
	private String description;

	public ExtensionFileFilter(String allowedextension) {
		this.ext = allowedextension;
	}

	public ExtensionFileFilter(String description, String allowedextension) {
		this.ext = allowedextension;
		this.description = description;
	}

	@Override
	public boolean accept(File f) {
		String fileext = FilenameUtils.getExtension(f.getAbsolutePath());
		if (fileext.toLowerCase().equals(ext.toLowerCase()))
			return true;
		else if (f.isDirectory())
			return true;
		return false;
	}

	@Override
	public String getDescription() {
		if (description == null)
			return "*." + ext;
		else
			return description + " (*." + ext + ")";
	}

	public String getSingleDescription() {
		if (description == null)
			return ext;
		else
			return description;
	}

	public String getExtension() {
		return ext;
	}

}
