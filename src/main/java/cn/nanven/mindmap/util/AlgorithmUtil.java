package cn.nanven.mindmap.util;

import cn.nanven.mindmap.entity.NodeEntity;

public class AlgorithmUtil {
    public static void headMapNode(NodeEntity node, MapEventHandler handler) {
        handler.handle(node);
        for (NodeEntity child : node.getChildren()) {
            headMapNode(child, handler);
        }
    }

    public static void tailMapNode(NodeEntity node, MapEventHandler handler) {
        for (NodeEntity child : node.getChildren()) {
            tailMapNode(child, handler);
        }
        handler.handle(node);
    }

    public static NodeEntity findRoot(NodeEntity target) {
        NodeEntity node = target;
        while (true){
            if (node.getParent()==null){
                return node;
            }
            node=node.getParent();
        }
    }
    //向根节点判断节点是否存在
    public static boolean checkExistParent(NodeEntity start, NodeEntity target) {
        NodeEntity node = start;
        while (true) {
            if (node.getParent() == null) {
                return false;
            }

            if (target == node.getParent()) {
                return true;
            }
            node = node.getParent();
        }
    }
}
