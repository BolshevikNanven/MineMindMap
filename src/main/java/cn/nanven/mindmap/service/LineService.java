package cn.nanven.mindmap.service;

import cn.nanven.mindmap.entity.LineEntity;
import cn.nanven.mindmap.entity.NodeEntity;
import cn.nanven.mindmap.store.SettingStore;
import cn.nanven.mindmap.view.Line.StraightLine;
import cn.nanven.mindmap.view.Line.TwoPolyLine;
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
        if (head == null) return;

        LayoutService layoutService = SettingStore.getLayoutService();
        if (tail.getLine() != null) {
            deleteLine(tail.getLine());
            tail.setLine(null);
        }

        LineView lineView = new TwoPolyLine(head, tail, layoutService.getLineHead(head), layoutService.getLineTail(tail));
        tail.setLine(lineView);

        canvas.getChildren().add(lineView.render());

    }

    public void deleteLine(LineView lineView) {
        canvas.getChildren().remove(lineView.render());
        lineView.setHead(null);
        lineView.setTail(null);
    }

}
