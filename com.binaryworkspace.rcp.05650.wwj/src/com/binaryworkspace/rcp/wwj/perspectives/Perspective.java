package com.binaryworkspace.rcp.wwj.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.binaryworkspace.rcp.wwj.views.ElevatedPathView;
import com.binaryworkspace.rcp.wwj.views.ElevationContoursView;
import com.binaryworkspace.rcp.wwj.views.LayerSelectionView;
import com.binaryworkspace.rcp.wwj.views.PointRandomizationView;
import com.binaryworkspace.rcp.wwj.views.PointTessellationView;
import com.binaryworkspace.rcp.wwj.views.SurfaceImageEditorView;
import com.binaryworkspace.rcp.wwj.views.SurfaceImageView;
import com.binaryworkspace.rcp.wwj.views.SurfacePathView;
import com.binaryworkspace.rcp.wwj.views.SurfacePolygonsView;

public class Perspective implements IPerspectiveFactory {

	public static final String ID = Perspective.class.getName();

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		IFolderLayout folderReport = layout.createFolder("Perspective.folder", IPageLayout.LEFT, 1.0f, layout.getEditorArea());
		folderReport.addView(LayerSelectionView.ID);
		folderReport.addView(ElevationContoursView.ID);
		folderReport.addView(ElevatedPathView.ID);
		folderReport.addView(PointRandomizationView.ID);
		folderReport.addView(PointTessellationView.ID);
		folderReport.addView(SurfaceImageView.ID);
		folderReport.addView(SurfaceImageEditorView.ID);
		folderReport.addView(SurfacePathView.ID);
		folderReport.addView(SurfacePolygonsView.ID);
	}
}
