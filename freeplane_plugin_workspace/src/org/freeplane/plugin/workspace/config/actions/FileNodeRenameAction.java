package org.freeplane.plugin.workspace.config.actions;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import org.freeplane.core.ui.components.JFreeplaneMenuItem;
import org.freeplane.core.util.TextUtils;
import org.freeplane.features.mode.Controller;
import org.freeplane.plugin.workspace.io.node.DefaultFileNode;
import org.freeplane.plugin.workspace.model.AWorkspaceTreeNode;
import org.freeplane.plugin.workspace.model.WorkspacePopupMenu;

public class FileNodeRenameAction extends AWorkspaceAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileNodeRenameAction() {
		super("FileNodeRenameAction");
	}

	public void actionPerformed(final ActionEvent e) {
		System.out.println("FileNodeRenameAction: "+e.getActionCommand()+" : "+e.getID());
		AWorkspaceTreeNode node = this.getNodeFromActionEvent(e);
		WorkspacePopupMenu menu = (WorkspacePopupMenu) ((JFreeplaneMenuItem) e.getSource()).getParent();
		JTree tree = (JTree) menu.getInvoker();			
		
		String oldFileName = node.getName();		
		String newFileName = JOptionPane.showInputDialog(Controller.getCurrentController().getViewController().getContentPane(),
				TextUtils.getText("confirm_rename_file_action"), oldFileName);

		if (newFileName != null) {		
			if (node instanceof DefaultFileNode) {
				if (((DefaultFileNode) node).rename(newFileName)) {		
					DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
					
					treeModel.valueForPathChanged(node.getTreePath(), newFileName);
				}
				else {
					JOptionPane.showMessageDialog(Controller.getCurrentController().getViewController().getContentPane(),
							TextUtils.getText("error_rename_file"), TextUtils.getText("error_rename_file_title"), 
							JOptionPane.ERROR_MESSAGE);
				}

			}
		}

	}

}