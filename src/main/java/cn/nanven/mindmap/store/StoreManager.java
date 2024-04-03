package cn.nanven.mindmap.store;

import cn.nanven.mindmap.modal.NodeEntity;
import cn.nanven.mindmap.view.AuxiliaryNodeView;
import cn.nanven.mindmap.view.NodeView;

import java.util.ArrayList;
import java.util.List;

public class StoreManager {
    private static final List<NodeEntity> rootNodeList = new ArrayList<>();
    private static NodeView selectedNode;
    private static AuxiliaryNodeView auxiliaryNode;

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
