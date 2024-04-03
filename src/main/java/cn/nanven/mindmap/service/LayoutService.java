package cn.nanven.mindmap.service;

import cn.nanven.mindmap.modal.NodeEntity;

public interface LayoutService {
    void layout();//布局
    void indicate(NodeEntity node,double x,double y);//吸附指示
    void snap(NodeEntity node);//吸附操作
}
