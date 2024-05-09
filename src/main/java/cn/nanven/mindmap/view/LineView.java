package cn.nanven.mindmap.view;

import cn.nanven.mindmap.entity.LineEntity;
import cn.nanven.mindmap.entity.NodeEntity;
import javafx.scene.Node;

public abstract class LineView {
    protected NodeEntity head;
    protected NodeEntity tail;
    public abstract Node render();

    public NodeEntity getHead() {
        return head;
    }

    public void setHead(NodeEntity head) {
        this.head = head;
    }

    public NodeEntity getTail() {
        return tail;
    }

    public void setTail(NodeEntity tail) {
        this.tail = tail;
    }
}
