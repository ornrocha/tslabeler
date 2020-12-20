package pt.ornrocha.tslabeler.core.io.read;

import pt.ornrocha.tslabeler.core.containers.TimeSeriesContainer;

public abstract class AbstractDataReader {

	protected TimeSeriesContainer datacontainer = null;

	public AbstractDataReader() {
	}

	public void readFile(String filepath) throws Exception {
		readAndValidate(filepath);
	}

	public TimeSeriesContainer getTimeseriesContainer() {
		return datacontainer;
	}

	protected abstract boolean readAndValidate(String filepath) throws Exception;

}
