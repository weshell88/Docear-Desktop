package org.freeplane.core.ui.ribbon;

import java.util.ArrayList;
import java.util.List;

import org.freeplane.features.map.IMapChangeListener;
import org.freeplane.features.map.IMapLifeCycleListener;
import org.freeplane.features.map.INodeChangeListener;
import org.freeplane.features.map.INodeSelectionListener;
import org.freeplane.features.map.MapChangeEvent;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeChangeEvent;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.ui.CloseAction;
import org.freeplane.features.url.mindmapmode.OpenAction;

public class RibbonMapChangeAdapter implements INodeSelectionListener, INodeChangeListener, IMapChangeListener, IMapLifeCycleListener {
	private List<IChangeObserver> listeners = new ArrayList<IChangeObserver>();
	
	public void clear() {
		listeners.clear();
	}
	
	public void addListener(IChangeObserver listener) {
		synchronized (listeners) {
			if(!listeners.contains(listener)) {
				listeners.add(listener);
			}
		}
	}
	
	public void removeListener(IChangeObserver listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	
	public void selectionChanged(Object selection) {
		CurrentState state = new CurrentState();
		if(selection != null) {
			state.set(selection.getClass(), selection);
		}
		fireStateChanged(state);
	}
	

	public void mapChanged(MapChangeEvent event) {
		CurrentState state = new CurrentState();
		state.set(NodeModel.class, event.getMap().getRootNode());
		fireStateChanged(state);		
	}

	public void onNodeDeleted(NodeModel parent, NodeModel child, int index) {
		// TODO Auto-generated method stub
		
	}

	public void onNodeInserted(NodeModel parent, NodeModel child, int newIndex) {
		// TODO Auto-generated method stub
		
	}

	public void onNodeMoved(NodeModel oldParent, int oldIndex, NodeModel newParent, NodeModel child, int newIndex) {
		// TODO Auto-generated method stub
		
	}

	public void onPreNodeMoved(NodeModel oldParent, int oldIndex, NodeModel newParent, NodeModel child, int newIndex) {
		// TODO Auto-generated method stub
		
	}

	public void onPreNodeDelete(NodeModel oldParent, NodeModel selectedNode, int index) {
		// TODO Auto-generated method stub
		
	}

	public void onCreate(MapModel map) {
		CurrentState state = new CurrentState();
		state.set(OpenAction.class, map);
		fireStateChanged(state);
	}

	public void onRemove(MapModel map) {
		CurrentState state = new CurrentState();
		state.set(CloseAction.class, map);
		fireStateChanged(state);
		
	}

	public void onSavedAs(MapModel map) {
	}

	public void onSaved(MapModel map) {		
	}

	public void nodeChanged(NodeChangeEvent event) {
		CurrentState state = new CurrentState();
		state.set(NodeModel.class, event.getNode());
		fireStateChanged(state);
	}

	public void onDeselect(NodeModel node) {
	}

	public void onSelect(NodeModel node) {
		CurrentState state = new CurrentState();
		state.set(NodeModel.class, node);
		fireStateChanged(state);
		
	}
	
	protected void fireStateChanged(CurrentState state) {
		synchronized (listeners) {
			for (IChangeObserver observer : listeners) {
				observer.updateState(state);
			}
		}
	}
}