package pt.ornrocha.tslabeler.core.containers;

import tech.tablesaw.api.Table;

public class TimeSeriesContainer {

	private Table dataframe;
	private FileInfo infofile;
	private int dateindex;
	private int anomalyindex;
	private int[] valuesindex;

	private ContainerState state = ContainerState.UNCHANGED;

	public TimeSeriesContainer(Table dataframe) {
		this.dataframe = dataframe;
	}

	public TimeSeriesContainer(Table dataframe, FileInfo fileinfo) {
		this(dataframe);
		this.infofile = fileinfo;
	}

	public TimeSeriesContainer(Table dataframe, FileInfo fileinfo, int dateindex, int anomalyindex, int[] valuesindex) {
		this(dataframe, fileinfo);
		this.dateindex = dateindex;
		this.anomalyindex = anomalyindex;
		this.valuesindex = valuesindex;
	}

	public Table getDataframe() {
		return dataframe;
	}

	public FileInfo getInfoFile() {
		return infofile;
	}

	public ContainerState getState() {
		return state;
	}

	public void setState(ContainerState state) {
		this.state = state;
	}

	public TypeTimeSeries getType() {

		if (valuesindex != null && valuesindex.length > 1)
			return TypeTimeSeries.MULTIVARIATE;
		return TypeTimeSeries.UNIVARIATE;
	}

	public int getDateindex() {
		return dateindex;
	}

	public void setDateindex(int dateindex) {
		this.dateindex = dateindex;
	}

	public int getAnomalyindex() {
		return anomalyindex;
	}

	public void setAnomalyindex(int anomalyindex) {
		this.anomalyindex = anomalyindex;
	}

	public int[] getValuesindex() {
		return valuesindex;
	}

	public void setValuesindex(int[] valuesindex) {
		this.valuesindex = valuesindex;
	}

}
