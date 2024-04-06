package cn.nanven.mindmap.util;

import cn.nanven.mindmap.entity.NodeEntity;

@FunctionalInterface
public interface MapEventHandler {
    void handle(NodeEntity node);
}
