package pt.ornrocha.tslabeler.core.io.read;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pt.ornrocha.tslabeler.core.containers.FileInfo;
import pt.ornrocha.tslabeler.core.containers.TimeSeriesContainer;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;
import tech.tablesaw.selection.Selection;

public class CsvDataReader extends AbstractDataReader {

	private Table finaldataframe;
	private int dateindex;
	private int anomalyindex;
	private int[] valuesindex;

	public CsvDataReader() {
		super();
	}

	@Override
	protected boolean readAndValidate(String filepath) throws Exception {

		finaldataframe = Table.create();

		Table dataframe = Table.read().csv(filepath);

		ArrayList<String> columnnames = (ArrayList<String>) dataframe.columnNames().stream().map(s -> s.toLowerCase())
				.collect(Collectors.toList());
		if (!columnnames.contains("date"))
			throw new Exception("Date column is missing!");

		checkAndSetDateColumn(dataframe);
		checkAndSetFeaturesColumns(dataframe);
		checkAndSetAnomalyColumn(dataframe);

		FileInfo infofile = FileInfo.load(filepath);

		this.datacontainer = new TimeSeriesContainer(finaldataframe, infofile, dateindex, anomalyindex, valuesindex);

		return true;
	}

	protected void checkAndSetDateColumn(Table table) throws Exception {

		Selection datecolumn = table.structure().stringColumn("Column Type").isEqualTo("LOCAL_DATE_TIME");
		if (datecolumn.size() < 1)
			datecolumn = table.structure().stringColumn("Column Type").isEqualTo("LOCAL_DATE");

		if (datecolumn.size() < 1)
			throw new Exception("It was not possible to parse the date, check if the dates are in ISO 8601 format");
		else {

			List<Column<?>> datecol = table.columns(datecolumn.toArray());
			appendDataToOutputTable(datecol);
			dateindex = datecolumn.toArray()[0];
		}
	}

	protected void checkAndSetFeaturesColumns(Table table) throws Exception {

		Selection valuescols = table.structure().stringColumn("Column Type").isEqualTo("DOUBLE")
				.or(table.structure().stringColumn("Column Type").isEqualTo("INTEGER"))
				.andNot(table.structure().stringColumn("Column Name").lowerCase().isEqualTo("anomaly"));

		if (valuescols.size() < 1)
			throw new Exception("It was not possible to find any column related to the features.");
		else {
			List<Column<?>> cols = table.columns(valuescols.toArray());
			appendDataToOutputTable(cols);
			valuesindex = valuescols.toArray();
		}

	}

	protected void checkAndSetAnomalyColumn(Table table) {

		Selection anomcol = table.structure().stringColumn("Column Name").lowerCase().isEqualTo("anomaly");
		if (anomcol.size() == 1) {
			List<Column<?>> cols = table.columns(anomcol.toArray());
			appendDataToOutputTable(cols);
			anomalyindex = anomcol.toArray()[0];
		} else {

			int nrows = table.rowCount();
			int ncols = table.columnCount();

			int[] newvals = new int[nrows];
			for (int i = 0; i < newvals.length; i++) {
				newvals[i] = 0;
			}

			IntColumn anomaliescol = IntColumn.create("anomaly", newvals);
			List<Column<?>> cols = new ArrayList<Column<?>>();
			cols.add(anomaliescol);
			appendDataToOutputTable(cols);
			anomalyindex = ncols;
		}
	}

	protected void appendDataToOutputTable(List<Column<?>> cols) {

		for (Column<?> col : cols) {
			finaldataframe.addColumns(col);
		}
	}

	public static TimeSeriesContainer loadFromFile(String filepath) throws Exception {
		CsvDataReader reader = new CsvDataReader();
		reader.readFile(filepath);
		return reader.getTimeseriesContainer();
	}

}
