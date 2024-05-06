package cn.nanven.mindmap.service;

import cn.nanven.mindmap.entity.Command;
import cn.nanven.mindmap.store.StoreManager;

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
        if (StoreManager.getUndoStack().size() < 10) {
            StoreManager.getUndoStack().push(command);
        }else {
            Command[] temp = new Command[10];
            for (int i = 0; i < StoreManager.getUndoStack().size(); i++)
                temp[i] = StoreManager.getUndoStack().pop();
            for (int i = StoreManager.getUndoStack().size() - 1; i > 0; i--)
                StoreManager.getUndoStack().push(temp[i]);
            StoreManager.getUndoStack().push(command);
        }
    }

    private void pushRedoStack(Command command) {

        if (flagOfRedo)//可以入栈
        {
            StoreManager.getRedoStack().push(command);
        } else {//清空Redo栈
            for (int i = 0; i < StoreManager.getRedoStack().size(); i++) {
                StoreManager.getRedoStack().pop();
            }
        }
    }

    public void execute(Command command) {
        pushUndoStack(command);
        command.execute();
    }

    public void undo() {
        if (!StoreManager.getUndoStack().isEmpty()) {
            flagOfRedo = true;
            Command pop = StoreManager.getUndoStack().pop();
            pushRedoStack(pop);
            pop.undo();
        }
    }

    public void redo() {
        if (!StoreManager.getRedoStack().isEmpty()) {
            Command pop = StoreManager.getRedoStack().pop();
            pushUndoStack(pop);
            pop.execute();
        }
    }
}
