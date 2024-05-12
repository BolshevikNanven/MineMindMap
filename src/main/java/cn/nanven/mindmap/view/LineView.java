package cn.nanven.mindmap.view;

import cn.nanven.mindmap.entity.LineEntity;
import cn.nanven.mindmap.entity.NodeEntity;
import cn.nanven.mindmap.service.LineService;
import cn.nanven.mindmap.util.StyleUtil;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.Objects;

public abstract class LineView {
    protected Shape line;
    protected NodeEntity head;
    protected NodeEntity tail;

    public abstract Node render();

    protected void addListener() {
        this.tail.borderProperty().addListener((observableValue, border, t1) -> {
            String borderColor = StyleUtil.getBorderColor(t1);
            String borderOpacity = borderColor.substring(borderColor.length() - 2);

            if (Objects.equals(borderOpacity, "00")) {
                line.setStroke(Color.valueOf(StyleUtil.getBackgroundColor(tail.getBackground())));
            } else {
                line.setStroke(Color.valueOf(borderColor));
            }
        });
        this.tail.deleteSymbolProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) LineService.getInstance().deleteLine(this);
        });
    }

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
