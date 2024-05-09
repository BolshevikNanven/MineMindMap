package cn.nanven.mindmap.service;

import cn.nanven.mindmap.entity.NodeEntity;
import cn.nanven.mindmap.view.NodeView;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.MouseEvent;

public interface LayoutService {
    void layout();//布局

    void indicate(NodeEntity node, double x, double y);//吸附指示

    void snap(NodeEntity node, double x, double y, double prevX, double prevY);//吸附操作

    SimpleDoubleProperty[] getLineHead(NodeEntity node);//获取线头
    SimpleDoubleProperty[] getLineTail(NodeEntity node);//获取线尾
}
