package pt.ornrocha.tslabeler.core.utils.files;

import org.apache.commons.io.FilenameUtils;

public class FileUtils {

	public static String createFilePathWithExtension(String filepath, String extension) {
		String filename = null;

		if (FilenameUtils.getExtension(filepath).toLowerCase().equals(extension))
			filename = filepath;
		else {
			String parentpath = FilenameUtils.getFullPath(filepath);
			String basename = FilenameUtils.getBaseName(filepath);
			String ext = FilenameUtils.getExtension(filepath);

			if (ext != null && !ext.isEmpty()) {
				if (extension.startsWith("."))
					filename = parentpath + basename + extension;
				else
					filename = parentpath + basename + "." + extension;
			} else {
				if (extension.startsWith("."))
					filename = filepath + extension;
				else
					filename = filepath + "." + extension;

			}
		}

		return filename;
	}

}
