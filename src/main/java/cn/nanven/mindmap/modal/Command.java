package cn.nanven.mindmap.modal;

public interface Command {
    public void execute();

    public void undo();
}
