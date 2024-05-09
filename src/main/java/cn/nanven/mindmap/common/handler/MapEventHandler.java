package cn.nanven.mindmap.common.handler;

import cn.nanven.mindmap.entity.NodeEntity;

@FunctionalInterface
public interface MapEventHandler {
    void handle(NodeEntity parent,NodeEntity node);
}
