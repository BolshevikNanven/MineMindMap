package cn.nanven.mindmap.service;

import cn.nanven.mindmap.modal.LineEntity;
import cn.nanven.mindmap.modal.NodeEntity;
import cn.nanven.mindmap.view.Line.StraightLine;
import cn.nanven.mindmap.view.LineView;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class LineService {
    private static LineService instance;
    private Pane canvas;

    private LineService() {

    }

    private LineService(Pane canvas) {
        this.canvas = canvas;
    }

    public static void init(Pane canvas) {
        if (null == instance) {
            instance = new LineService(canvas);
        }
    }

    public static LineService getInstance() {
        return instance;
    }

    public void addLine(NodeEntity head, NodeEntity tail) {
            LineEntity lineEntity = new LineEntity();
            lineEntity.setHead(head);
            lineEntity.setTail(tail);

            tail.setLine(lineEntity);

            LineView lineView = new StraightLine(lineEntity);

            canvas.getChildren().add(lineView.render());

    }
    public void deleteLine(Node line,LineEntity lineEntity){
        canvas.getChildren().remove(line);
        lineEntity.setHead(null);
        lineEntity.setTail(null);
    }

}
