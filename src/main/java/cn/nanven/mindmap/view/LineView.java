package cn.nanven.mindmap.view;

import cn.nanven.mindmap.modal.LineEntity;
import javafx.scene.Node;

public abstract class LineView {
    protected LineEntity lineEntity;
    public abstract Node render();
    public LineEntity getEntity() {
        return lineEntity;
    }
}
