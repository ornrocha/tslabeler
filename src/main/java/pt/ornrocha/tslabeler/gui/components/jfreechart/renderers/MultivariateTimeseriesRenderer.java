package pt.ornrocha.tslabeler.gui.components.jfreechart.renderers;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class MultivariateTimeseriesRenderer extends UnivariateTimeseriesRenderer {

	private static final long serialVersionUID = 1L;

	public MultivariateTimeseriesRenderer() {
		super();
	}

	public MultivariateTimeseriesRenderer(ArrayList<Integer> listanomalies, boolean lines, boolean shapes) {
		super(listanomalies, lines, shapes);
	}

	public MultivariateTimeseriesRenderer(ArrayList<Integer> listanomalies) {
		super(listanomalies);
	}

	public MultivariateTimeseriesRenderer(boolean lines, boolean shapes) {
		super(lines, shapes);
	}

	@Override
	public boolean getItemShapeVisible(int series, int item) {

		if (this.listanomalies.get(item) == 1) {
			return true;
		} else {
			return super.getItemShapeVisible(series, item);
		}
	}

	@Override
	public boolean getItemShapeFilled(int series, int item) {
		if (this.listanomalies.get(item) == 1) {
			return true;
		} else
			return super.getItemShapeFilled(series, item);
	}

	@Override
	public Paint getItemFillPaint(int row, int column) {
		if (listanomalies.get(column) == 1)
			return Color.BLACK;
		else
			return super.getItemFillPaint(row, column);
	}

	@Override
	public Paint getItemOutlinePaint(int row, int column) {
		if (listanomalies.get(column) == 1)
			return Color.BLACK;
		else
			return super.getItemOutlinePaint(row, column);
	}

	@Override
	public Shape getItemShape(int row, int column) {

		if (listanomalies.get(column) == 1)
			return new Rectangle2D.Double(-3, -3, 5, 5);
		else if (listanomalies.get(column) == 0)
			return new Ellipse2D.Double(-1.5, -1.5, 3, 3);
		else
			return super.getItemShape(row, column);
	}

}
