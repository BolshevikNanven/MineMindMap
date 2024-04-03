package cn.nanven.mindmap.util;

import cn.nanven.mindmap.modal.NodeEntity;

@FunctionalInterface
public interface MapEventHandler {
    void handle(NodeEntity node);
}
