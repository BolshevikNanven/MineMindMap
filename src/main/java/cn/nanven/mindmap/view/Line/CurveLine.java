package cn.nanven.mindmap.view.Line;

import cn.nanven.mindmap.entity.NodeEntity;
import cn.nanven.mindmap.service.LineService;
import cn.nanven.mindmap.util.StyleUtil;
import cn.nanven.mindmap.view.LineView;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class CurveLine extends LineView {
    private Path line;

    public CurveLine(NodeEntity head, NodeEntity tail, SimpleDoubleProperty[] headBindings, SimpleDoubleProperty[] tailBindings) {
        line = new Path();

        this.head = head;
        this.tail = tail;
        boolean right = tailBindings[0].get() > headBindings[0].get();

        MoveTo moveTo = new MoveTo();
        moveTo.xProperty().bind(headBindings[0]);
        moveTo.yProperty().bind(headBindings[1]);

        // 贝塞尔曲线控制点和结束点
        CubicCurveTo cubicCurveTo = new CubicCurveTo();
        cubicCurveTo.setControlX1(right ? headBindings[0].get() + 8 : headBindings[0].get() - 8);
        headBindings[0].addListener((observableValue, number, t1) -> {
            boolean right1 = tailBindings[0].get() > headBindings[0].get();
            cubicCurveTo.setControlX1(right1 ? headBindings[0].get() + 8 : headBindings[0].get() - 8);
        });
        tailBindings[0].addListener((observableValue, number, t1) -> {
            boolean right1 = tailBindings[0].get() > headBindings[0].get();
            cubicCurveTo.setControlX1(right1 ? headBindings[0].get() + 8 : headBindings[0].get() - 8);
        });
        cubicCurveTo.controlY1Property().bind(headBindings[1]);

        cubicCurveTo.controlX2Property().bind(headBindings[0]);
        cubicCurveTo.controlY2Property().bind(tailBindings[1]);
        cubicCurveTo.xProperty().bind(tailBindings[0]);
        cubicCurveTo.yProperty().bind(tailBindings[1]);

        line.getElements().add(moveTo);
        line.getElements().add(cubicCurveTo);

        line.setStroke(Color.valueOf(StyleUtil.getBackgroundColor(tail.getBackground())));
        line.setStrokeWidth(2.0);
        line.setFill(null); // 确保路径是透明的，没有填充色

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

    @Override
    public Node render() {
        return line;
    }
}
