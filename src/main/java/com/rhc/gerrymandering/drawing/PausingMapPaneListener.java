package com.rhc.gerrymandering.drawing;

import org.geotools.swing.JMapPane;
import org.geotools.swing.event.MapPaneEvent;
import org.geotools.swing.event.MapPaneListener;

public class PausingMapPaneListener implements MapPaneListener {
	
	private JMapPane pane;
	
	public PausingMapPaneListener(JMapPane pane) {
		this.pane = pane;
	}

	@Override
	public void onDisplayAreaChanged(MapPaneEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNewMapContent(MapPaneEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRenderingStarted(MapPaneEvent event) {
		pane.setIgnoreRepaint(true);		
	}

	@Override
	public void onRenderingStopped(MapPaneEvent event) {
		pane.setIgnoreRepaint(false);		
	}

}
