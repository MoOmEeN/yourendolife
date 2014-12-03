package com.moomeen.utils.chart;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.plot.DefaultDrawingSupplier;

@SuppressWarnings("serial")
public class ChartDrawingSupplier extends DefaultDrawingSupplier {

	public Paint[] paintSequence;
	public int paintIndex;
	public int fillPaintIndex;

	{
		paintSequence = new Paint[] { 
				new Color(74, 112, 147),
				new Color(73, 207, 229), 
				new Color(255, 188, 65),
				new Color(194, 71, 88), 
				new Color(119, 170, 84),
				new Color(112, 113, 200), 
				new Color(197, 143, 186),
				new Color(181, 185, 198), 
				};
	}

	@Override
	public Paint getNextPaint() {
		Paint result = paintSequence[paintIndex % paintSequence.length];
		paintIndex++;
		return result;
	}

	@Override
	public Paint getNextFillPaint() {
		Paint result = paintSequence[fillPaintIndex % paintSequence.length];
		fillPaintIndex++;
		return result;
	}
}