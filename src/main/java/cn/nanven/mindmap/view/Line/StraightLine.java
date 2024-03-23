package cn.nanven.mindmap.view.Line;

import cn.nanven.mindmap.modal.LineEntity;
import cn.nanven.mindmap.service.LineService;
import cn.nanven.mindmap.util.StyleUtil;
import cn.nanven.mindmap.view.LineView;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

public class StraightLine extends LineView {
    private Line line;

    private StraightLine() {

    }

    public StraightLine(LineEntity lineEntity) {
        this.lineEntity = lineEntity;
        line = new Line();
        line.startXProperty().bind(lineEntity.getHead().xProperty().add(lineEntity.getHead().actualWidthProperty()));
        line.startYProperty().bind(lineEntity.getHead().yProperty().add(lineEntity.getHead().actualHeightProperty().divide(2)));
        line.endXProperty().bind(lineEntity.getTail().xProperty());
        line.endYProperty().bind(lineEntity.getTail().yProperty().add(lineEntity.getTail().actualHeightProperty().divide(2)));

        line.setStroke(Color.valueOf(StyleUtil.getBackgroundColor(lineEntity.getTail().getBackground())));
        line.setStrokeWidth(2.0);

        addListener();
    }

    private void addListener() {
        this.lineEntity.getTail().backgroundProperty().addListener((observableValue, background, t1) -> {
            line.setStroke(Color.valueOf(StyleUtil.getBackgroundColor(t1)));
        });
        this.lineEntity.getTail().deleteSymbolProperty().addListener(e -> {
            LineService.getInstance().deleteLine(line,lineEntity);
        });
    }

    public Node render() {
        return this.line;
    }

}