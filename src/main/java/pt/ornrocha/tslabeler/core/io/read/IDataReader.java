package pt.ornrocha.tslabeler.core.io.read;

import pt.ornrocha.tslabeler.core.containers.TimeSeriesContainer;

public interface IDataReader {

	public TimeSeriesContainer getTimeseriesContainer();

	public void readFile(String filepath);

}
