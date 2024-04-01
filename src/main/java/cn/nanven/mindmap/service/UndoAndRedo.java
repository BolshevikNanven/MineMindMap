package cn.nanven.mindmap.service;

import cn.nanven.mindmap.modal.NodeEntity;

import java.util.Stack;

public class UndoAndRedo {
    private Stack<NodeEntity> undoStack = new Stack<>();
    private Stack<NodeEntity> redoStack = new Stack<>();
    private static UndoAndRedo instance;

    public static synchronized UndoAndRedo getInstance() {
        if (instance == null) {
            instance = new UndoAndRedo();
        }
        return instance;
    }

    public void saveState(NodeEntity state) {
        undoStack.push(state.clone()); // 假设NodeEntity有一个克隆方法。
    }

    public NodeEntity undo() {
        if (!undoStack.isEmpty()) {
            NodeEntity prevState = undoStack.pop();
            redoStack.push(prevState.clone());
            return prevState;
        }
        return null;
    }

    public NodeEntity redo() {
        if (!redoStack.isEmpty()) {
            NodeEntity nextState = redoStack.pop();
            undoStack.push(nextState.clone());
            return nextState;
        }
        return null;
    }
}
