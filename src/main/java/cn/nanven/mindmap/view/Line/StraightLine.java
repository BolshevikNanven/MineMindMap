package cn.nanven.mindmap.view.Line;

import cn.nanven.mindmap.entity.LineEntity;
import cn.nanven.mindmap.entity.NodeEntity;
import cn.nanven.mindmap.service.LineService;
import cn.nanven.mindmap.util.StyleUtil;
import cn.nanven.mindmap.view.LineView;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

import java.util.Objects;

public class StraightLine extends LineView {
    private StraightLine() {

    }

    public StraightLine(NodeEntity head, NodeEntity tail, SimpleDoubleProperty[] headBindings, SimpleDoubleProperty[] tailBindings) {
        Line line = new Line();

        this.head = head;
        this.tail = tail;

        line.startXProperty().bind(headBindings[0]);
        line.startYProperty().bind(headBindings[1]);
        line.endXProperty().bind(tailBindings[0]);
        line.endYProperty().bind(tailBindings[1]);

        String borderColor = StyleUtil.getBorderColor(tail.getBorder());
        borderColor = borderColor.substring(borderColor.length() - 2);
        if (Objects.equals(borderColor, "00")) {
            line.setStroke(Color.valueOf(StyleUtil.getBackgroundColor(tail.getBackground())));
        } else {
            line.setStroke(Color.valueOf(borderColor));
        }
        line.setStrokeWidth(2.0);

        this.line=line;
        addListener();
    }

    public Node render() {
        return this.line;
    }

}
