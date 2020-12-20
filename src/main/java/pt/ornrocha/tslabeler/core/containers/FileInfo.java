package pt.ornrocha.tslabeler.core.containers;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;

public class FileInfo {

	protected String filepath;

	/** The name. */
	protected String name;

	/** The parent dirs. */
	protected String parentdir;

	/** The extension. */
	protected String extension;

	/** The path. */
	protected Path path = null;

	/** The Base name. */
	protected String BaseName;

	public FileInfo(String filepath) {

		if (filepath != null) {
			this.filepath = filepath;
			this.path = Paths.get(filepath);
			this.name = this.path.getFileName().toString();
			this.parentdir = this.path.getParent().toString();
			this.extension = FilenameUtils.getExtension(this.filepath);
			this.BaseName = FilenameUtils.getBaseName(this.filepath);
		}
	}

	public static FileInfo load(String filepath) {
		return new FileInfo(filepath);
	}

	public String getFilepath() {
		return filepath;
	}

	public String getName() {
		return name;
	}

	public String getParentdir() {
		return parentdir;
	}

	public String getExtension() {
		return extension;
	}

	public Path getPath() {
		return path;
	}

	public String getBaseName() {
		return BaseName;
	}

}
