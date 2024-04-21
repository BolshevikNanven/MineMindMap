package cn.nanven.mindmap.util.handler;

import cn.nanven.mindmap.entity.NodeEntity;

@FunctionalInterface
public interface MapEventHandler {
    void handle(NodeEntity node);
}
