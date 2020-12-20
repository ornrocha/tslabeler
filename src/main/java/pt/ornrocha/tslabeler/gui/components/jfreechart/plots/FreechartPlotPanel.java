package pt.ornrocha.tslabeler.gui.components.jfreechart.plots;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import pt.ornrocha.tslabeler.core.containers.ContainerState;
import pt.ornrocha.tslabeler.core.containers.FileInfo;
import pt.ornrocha.tslabeler.core.containers.TimeSeriesContainer;
import pt.ornrocha.tslabeler.core.containers.TypeTimeSeries;
import pt.ornrocha.tslabeler.gui.components.jfreechart.renderers.MultivariateTimeseriesRenderer;
import pt.ornrocha.tslabeler.gui.components.jfreechart.renderers.UnivariateTimeseriesRenderer;
import pt.ornrocha.tslabeler.gui.main.Maingui;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

public class FreechartPlotPanel extends JPanel implements ChartMouseListener {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private JScrollPane plotScrollPane;
  private ChartPanel chartPanel;
  private UnivariateTimeseriesRenderer renderer;
  private TimeSeriesContainer currentcontainer;

  static final int pref_height = 500;

  public FreechartPlotPanel() {
    initGUI();
  }

  private void initGUI() {

    setLayout(new GridLayout(0, 1, 0, 0));

    plotScrollPane = new JScrollPane();
    plotScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    add(plotScrollPane);
    chartPanel = new ChartPanel(null);
    chartPanel.addChartMouseListener(this);// {

    plotScrollPane.getViewport().add(chartPanel);
    chartPanel.setMouseWheelEnabled(true);
    chartPanel.setHorizontalAxisTrace(true);
    chartPanel.setVerticalAxisTrace(true);

  }

  public void resetChartState() {
    this.currentcontainer = null;
    chartPanel.setChart(null);
    chartPanel.setMouseWheelEnabled(false);
    chartPanel.setHorizontalAxisTrace(false);
    chartPanel.setVerticalAxisTrace(false);

  }

  public ChartPanel getChartPanel() {
    return this.chartPanel;
  }

  public void showTimeSeries(TimeSeriesContainer container) {

    this.currentcontainer = container;
    Table dataframe = container.getDataframe();

    XYDataset dataset = null;
    JFreeChart chart = null;

    try {
      dataset = createDataset(dataframe);

      String chartname = container.getInfoFile().getBaseName() + " Time Serie";
      chart = ChartFactory.createTimeSeriesChart(chartname, // Chart
          "Date", // X-Axis Label
          "Value", // Y-Axis Label
          dataset, true, true, false);

      XYPlot plot = (XYPlot) chart.getPlot();
      plot.setDomainCrosshairVisible(true);
      plot.setDomainPannable(true);
      plot.setRangePannable(true);


      if (currentcontainer.getType().equals(TypeTimeSeries.UNIVARIATE)) {
        renderer = new UnivariateTimeseriesRenderer(
            (ArrayList<Integer>) dataframe.column(currentcontainer.getAnomalyindex()).asList());

        setUnivariateRendererSettings();
        chart.removeLegend();
      } else {
        renderer = new MultivariateTimeseriesRenderer(
            (ArrayList<Integer>) dataframe.column(currentcontainer.getAnomalyindex()).asList());
        setMultivariateRendererSettings();
      }

      plot.setRenderer(renderer);
      if (chartPanel.getChart() == null) {
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setHorizontalAxisTrace(true);
        chartPanel.setVerticalAxisTrace(true);
      }



    } catch (Exception e) {
      Maingui.getInstance().showMessage(e.getMessage());
    }


    chartPanel.setChart(chart);
    chartPanel.repaint();
  }

  private void setUnivariateRendererSettings() {
    renderer.setUseFillPaint(true);
    renderer.setSeriesPaint(0, Color.BLUE);
    renderer.setDefaultFillPaint(Color.BLUE);
    renderer.setDrawOutlines(true);

  }

  private void setMultivariateRendererSettings() {
    renderer.setDefaultShapesVisible(true);
    renderer.setDefaultShapesFilled(false);
    renderer.setDrawOutlines(true);
    renderer.setUseFillPaint(true);
    renderer.setUseOutlinePaint(true);
  }

  private XYDataset createDataset(Table dataframe) {

    TimeSeriesCollection dataset = new TimeSeriesCollection();

    int[] vcolsindex = currentcontainer.getValuesindex();

    List coldate = dataframe.column(currentcontainer.getDateindex()).asList();

    for (int i = 0; i < vcolsindex.length; i++) {

      TimeSeries series = new TimeSeries("Serie_" + dataframe.columnNames().get(vcolsindex[i]));

      List colval = dataframe.column(vcolsindex[i]).asList();

      for (int z = 0; z < coldate.size(); z++) {

        Instant instant = null;
        Object currentdate = coldate.get(z);
        if (currentdate instanceof LocalDate) {
          LocalDateTime datetime = ((LocalDate) currentdate).atStartOfDay();
          instant = datetime.toInstant(ZoneOffset.UTC);
        } else {
          instant = ((LocalDateTime) currentdate).toInstant(ZoneOffset.UTC);
        }

        Date date = Date.from(instant);
        Millisecond time = new Millisecond(date, TimeZone.getTimeZone("UTC"), Locale.getDefault());
        series.add(time, (Number) colval.get(z));
      }
      dataset.addSeries(series);
    }

    return dataset;

  }

  @Override
  public void chartMouseClicked(ChartMouseEvent event) {
    ChartEntity entity = event.getEntity();

    if (entity != null && entity instanceof XYItemEntity) {
      int item = ((XYItemEntity) entity).getItem();
      renderer.changeStateItem(item);

    }

  }

  @Override
  public void chartMouseMoved(ChartMouseEvent event) {
    // TODO Auto-generated method stub

  }

  public void markVisiblePointsAsAnomalies() {
    ArrayList<Integer> indexes = getItemsInVisibleArea();
    renderer.markPointsAsAnomalies(indexes);
    chartPanel.repaint();
  }
  
    public void unmarkVisiblePointsAsAnomalies() {
	ArrayList<Integer> indexes = getItemsInVisibleArea();
	renderer.unmarkPointsAsAnomalies(indexes);
	chartPanel.repaint();
  }


  private ArrayList<Integer> getItemsInVisibleArea() {

    ArrayList<Integer> indexes = new ArrayList<Integer>();

    EntityCollection collection = chartPanel.getChartRenderingInfo().getEntityCollection();

    for (int i = 0; i < collection.getEntityCount(); i++) {
      ChartEntity entity = collection.getEntity(i);

      if (entity instanceof XYItemEntity) {
        int itemindex = ((XYItemEntity) entity).getItem();
        indexes.add(itemindex);
      }

    }
    
    return indexes;

  }




  public boolean isActive() {
    return this.currentcontainer != null;
  }

  public void updateTimeserie() {

    if (renderer != null) {
      ArrayList<Integer> newlistanomalies = renderer.getListanomalies();

      Table dataframe = currentcontainer.getDataframe();

      boolean markupdate = false;

      for (int i = 0; i < dataframe.rowCount(); i++) {
        Row r = dataframe.row(i);
        int currentval = r.getInt(currentcontainer.getAnomalyindex());
        // int currentval = r.getInt("anomaly");
        int newval = newlistanomalies.get(i);
        if (currentval != newval) {
          r.setInt(currentcontainer.getAnomalyindex(), newval);
          markupdate = true;
          // r.setInt("anomaly", newval);
        }
      }

      if (markupdate)
        currentcontainer.setState(ContainerState.UPDATED);

    }

  }

  public void SaveTimeserie() {

    updateTimeserie();

    if (currentcontainer.getInfoFile().getExtension().equals("csv")) {

      Object[] options = {"Replace original file", "Save to a new file", "Cancel"};
      int n = JOptionPane.showOptionDialog(this, "How would you prefer to save your changes?",
          "Save question", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
          options, options[0]);

      if (n == 0) {
        String filepath = currentcontainer.getInfoFile().getFilepath();
        try {
          currentcontainer.getDataframe().write().csv(filepath);
          currentcontainer.setState(ContainerState.SAVED);
        } catch (IOException e) {
          JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          currentcontainer.setState(ContainerState.UPDATED);
        }
      } else if (n == 1) {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
          FileInfo finfo = currentcontainer.getInfoFile();
          File fileToSave = fileChooser.getSelectedFile();
          String filepath = null;
          if (fileToSave.isDirectory()) {
            filepath = fileToSave + File.separator + finfo.getName();
          } else {
            if (!fileToSave.getAbsolutePath().endsWith(".csv"))
              filepath = fileToSave.getAbsolutePath() + ".csv";
            else
              filepath = fileToSave.getAbsolutePath();
          }
          if (filepath != null)
            try {
              currentcontainer.getDataframe().write().csv(filepath);
              currentcontainer.setState(ContainerState.SAVED);
            } catch (IOException e) {
              JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
                  JOptionPane.ERROR_MESSAGE);
              currentcontainer.setState(ContainerState.UPDATED);
            }
        }
      }
    } else {

    }

  }

}
