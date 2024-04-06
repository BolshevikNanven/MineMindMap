package cn.nanven.mindmap.store;

import cn.nanven.mindmap.entity.Command;
import cn.nanven.mindmap.entity.NodeEntity;
import cn.nanven.mindmap.view.AuxiliaryNodeView;
import cn.nanven.mindmap.view.NodeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class StoreManager {
    private static final List<NodeEntity> rootNodeList = new ArrayList<>();
    private static NodeView selectedNode;
    private static AuxiliaryNodeView auxiliaryNode;
    private static Stack<Command> undoStack = new Stack<>();
    private static Stack<Command> redoStack = new Stack<>();

    public static Stack<Command> getUndoStack() {
        return undoStack;
    }
    public static Stack<Command> getRedoStack() {
        return redoStack;
    }

    public static List<NodeEntity> getRootNodeList() {
        return rootNodeList;
    }

    public static AuxiliaryNodeView getAuxiliaryNode() {
        return auxiliaryNode;
    }

    public static void setAuxiliaryNode(AuxiliaryNodeView auxiliaryNode) {
        StoreManager.auxiliaryNode = auxiliaryNode;
    }

    public static NodeView getSelectedNode() {
        return selectedNode;
    }

    public static void setSelectedNode(NodeView selectedNode) {
        StoreManager.selectedNode = selectedNode;
    }
}
