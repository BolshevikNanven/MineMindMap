package cn.nanven.mindmap.service;

import cn.nanven.mindmap.entity.Command;
import cn.nanven.mindmap.store.SystemStore;

public class UndoAndRedoService {

    private static UndoAndRedoService instance;
    private boolean flagOfRedo = false;

    public static UndoAndRedoService getInstance() {
        return instance;
    }

    public static void init() {
        if (instance == null)
            instance = new UndoAndRedoService();
    }

    private void pushUndoStack(Command command) {
        flagOfRedo = false;
        if (SystemStore.getUndoStack().size() < 10) {
            SystemStore.getUndoStack().push(command);
        }else {
            Command[] temp = new Command[10];
            for (int i = 0; i < SystemStore.getUndoStack().size(); i++)
                temp[i] = SystemStore.getUndoStack().pop();
            for (int i = SystemStore.getUndoStack().size() - 1; i > 0; i--)
                SystemStore.getUndoStack().push(temp[i]);
            SystemStore.getUndoStack().push(command);
        }
    }

    private void pushRedoStack(Command command) {

        if (flagOfRedo)//可以入栈
        {
            SystemStore.getRedoStack().push(command);
        } else {//清空Redo栈
            for (int i = 0; i < SystemStore.getRedoStack().size(); i++) {
                SystemStore.getRedoStack().pop();
            }
        }
    }

    public void execute(Command command) {
        pushUndoStack(command);
        command.execute();
    }

    public void undo() {
        if (!SystemStore.getUndoStack().isEmpty()) {
            flagOfRedo = true;
            Command pop = SystemStore.getUndoStack().pop();
            pushRedoStack(pop);
            pop.undo();
        }
    }

    public void redo() {
        if (!SystemStore.getRedoStack().isEmpty()) {
            Command pop = SystemStore.getRedoStack().pop();
            pushUndoStack(pop);
            pop.execute();
        }
    }
}
