/**
 * author: Marcel Genzmehr
 * 14.12.2011
 */
package org.freeplane.plugin.workspace.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;

import org.freeplane.core.ui.AFreeplaneAction;
import org.freeplane.core.util.Compat;
import org.freeplane.core.util.LogUtils;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.mindmapmode.MMapModel;
import org.freeplane.features.mapio.MapIO;
import org.freeplane.features.mapio.mindmapmode.MMapIO;
import org.freeplane.features.mode.mindmapmode.MModeController;
import org.freeplane.plugin.workspace.URIUtils;
import org.freeplane.plugin.workspace.WorkspaceController;
import org.freeplane.plugin.workspace.model.project.AWorkspaceProject;

/**
 * FIX for issue that a new mindmap is always set to <code>saved</code> by
 * default. This Action is used to set the new mindmap to <code>unsaved</code>
 * right after its creation.
 */
public class WorkspaceNewMapAction extends AFreeplaneAction {

	public static final String KEY = "NewMapAction";
	private static final long serialVersionUID = 1L;

	/***********************************************************************************
	 * CONSTRUCTORS
	 **********************************************************************************/
	/**
	 * 
	 */
	public WorkspaceNewMapAction() {
		super(KEY);
	}

	/***********************************************************************************
	 * METHODS
	 **********************************************************************************/
	public static MapModel createNewMap() {
		return createNewMap(null, null, null, false);
	}
	
	public static MapModel createNewMap(AWorkspaceProject project) {
		return createNewMap(project, null, null, false);
	}
	
	public static MapModel createNewMap(final URI uri, String name, boolean save) {		
		return createNewMap(null, uri, name, save);
	}
	
	public static MapModel createNewMap(AWorkspaceProject project, URI uri, String name, boolean save) {
		if (uri == null) {
			save = false;
		}

		File f = URIUtils.getAbsoluteFile(uri);
		if (save) {
			if (!createFolderStructure(f)) {
				return null;
			}
		}
		final MMapIO mapIO = (MMapIO) MModeController.getMModeController().getExtension(MapIO.class);
		
		final MapModel map = new MMapModel();
		map.createNewRoot();

		if (name != null) {
			map.getRootNode().setText(name);
		}
		
		if(project != null) {
			WorkspaceController.getMapModelExtension(map).setProject(project);
		}
		 
		if (save) {
			mapIO.save(map, f);
		}
		else {
			if(f != null) {
				try {
					map.setURL(Compat.fileToUrl(f));
				} catch (MalformedURLException e) {
					LogUtils.warn(WorkspaceNewMapAction.class + ": " + e.getMessage());
				}
			}
			//Controller.getCurrentModeController().getMapController().setSaved(map, false);
		}
		
			
		//WORKSPACE - todo: remove the following when the "fixme" above has been fixed
//		if(f != null) {
//			Controller.getCurrentController().close(true);
//			try {
//				mapIO.newMap(Compat.fileToUrl(f));
//			} catch (Exception e) {
//				LogUtils.severe(e);
//			}
//		}		
		return map;
	}

	private static boolean createFolderStructure(final File f) {
		final File folder = f.getParentFile();
		if (folder.exists()) {
			return true;
		}
		return folder.mkdirs();
	}

	/***********************************************************************************
	 * REQUIRED METHODS FOR INTERFACES
	 **********************************************************************************/
	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		createNewMap();
		
	}
}
