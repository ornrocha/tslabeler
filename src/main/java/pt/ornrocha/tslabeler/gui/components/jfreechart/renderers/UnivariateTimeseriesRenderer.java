package pt.ornrocha.tslabeler.gui.components.jfreechart.renderers;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class UnivariateTimeseriesRenderer extends XYLineAndShapeRenderer {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected ArrayList<Integer> listanomalies = null;

    public UnivariateTimeseriesRenderer() {
	super();
    }

    public UnivariateTimeseriesRenderer(ArrayList<Integer> listanomalies) {
	super();
	this.listanomalies = listanomalies;
    }

    public UnivariateTimeseriesRenderer(boolean lines, boolean shapes) {
	super(lines, shapes);
    }

    public UnivariateTimeseriesRenderer(ArrayList<Integer> listanomalies, boolean lines, boolean shapes) {
	this(lines, shapes);
	this.listanomalies = listanomalies;
    }

    public UnivariateTimeseriesRenderer addListAnomalies(ArrayList<Integer> listanomalies) {
	this.listanomalies = listanomalies;
	return this;
    }

    public ArrayList<Integer> getListanomalies() {
	return listanomalies;
    }

    @Override
    public Shape getItemShape(int row, int column) {

	if (listanomalies.get(column) == 1)
	    return new Rectangle2D.Double(-3, -3, 6, 6);
	else if (listanomalies.get(column) == 0)
	    return new Ellipse2D.Double(-2.5, -2.5, 5, 5);
	else
	    return super.getItemShape(row, column);
    }

    @Override
    public Paint getItemFillPaint(int row, int column) {
	if (listanomalies.get(column) == 1)
	    return Color.RED;
	else
	    return super.getItemFillPaint(row, column);
    }

    @Override
    public Paint getItemOutlinePaint(int row, int column) {
	if (listanomalies.get(column) == 1)
	    return Color.RED;
	else
	    return super.getItemOutlinePaint(row, column);
    }

    public void changeStateItem(int item) {
	int currentstate = this.listanomalies.get(item);
	if (currentstate == 0)
	    this.listanomalies.set(item, 1);
	else
	    this.listanomalies.set(item, 0);
    }

    public void markPointsAsAnomalies(List<Integer> indexes) {

	for (Integer index : indexes) {

	    listanomalies.set(index, 1);

	}
	fireChangeEvent();
    }

    public void unmarkPointsAsAnomalies(List<Integer> indexes) {

	for (Integer index : indexes) {

	    listanomalies.set(index, 0);

	}
	fireChangeEvent();
    }

}
