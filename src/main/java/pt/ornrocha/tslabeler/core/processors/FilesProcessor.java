package pt.ornrocha.tslabeler.core.processors;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import pt.ornrocha.tslabeler.core.containers.FileInfo;
import pt.ornrocha.tslabeler.core.containers.TimeSeriesContainer;
import pt.ornrocha.tslabeler.core.io.read.CsvDataReader;
import pt.ornrocha.tslabeler.core.utils.collections.IndexedHashMap;

public class FilesProcessor {

	private ArrayList<String> files;
	private boolean inprocessing = false;
	private IndexedHashMap<String, String> loadingerrors = new IndexedHashMap<String, String>();
	private ArrayList<TimeSeriesContainer> timeseries = new ArrayList<TimeSeriesContainer>();

	public FilesProcessor(ArrayList<String> files) {
		this.files = files;
	}

	public ArrayList<TimeSeriesContainer> getTimeseries() {
		return timeseries;
	}

	public void addFileLoadingErrorsListener(PropertyChangeListener l) {
		loadingerrors.addPropertyChangeListener(l);
	}

	public boolean isInprocessing() {
		return inprocessing;
	}

	public IndexedHashMap<String, String> getLoadingerrors() {
		return loadingerrors;
	}

	public void processFiles() {

		if (files != null) {
			inprocessing = true;

			for (String filepath : files) {

				FileInfo info = FileInfo.load(filepath);
				String extension = info.getExtension().toLowerCase();

				switch (extension) {
				case "csv":
					loadCsv(filepath);
					break;

				default:
					break;

				}

			}
			inprocessing = false;
		}
	}

	private void loadCsv(String file) {

		try {
			TimeSeriesContainer container = CsvDataReader.loadFromFile(file);
			timeseries.add(container);
		} catch (Exception e) {
			loadingerrors.put(file, e.getMessage());
		}

	}

}
