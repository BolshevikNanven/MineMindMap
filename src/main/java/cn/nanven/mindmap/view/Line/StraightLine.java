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

public class StraightLine extends LineView {
    private Line line;

    private StraightLine() {

    }

    public StraightLine(NodeEntity head, NodeEntity tail, SimpleDoubleProperty[] headBindings, SimpleDoubleProperty[] tailBindings) {
        line = new Line();

        this.head = head;
        this.tail = tail;

        line.startXProperty().bind(headBindings[0]);
        line.startYProperty().bind(headBindings[1]);
        line.endXProperty().bind(tailBindings[0]);
        line.endYProperty().bind(tailBindings[1]);

        line.setStroke(Color.valueOf(StyleUtil.getBackgroundColor(tail.getBackground())));
        line.setStrokeWidth(2.0);

        addListener();
    }

    private void addListener() {
        this.tail.backgroundProperty().addListener((observableValue, prev, background) -> {
            line.setStroke(Color.valueOf(StyleUtil.getBackgroundColor(background)));
        });
        this.tail.deleteSymbolProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) LineService.getInstance().deleteLine(this);
        });
    }

    public Node render() {
        return this.line;
    }

}
