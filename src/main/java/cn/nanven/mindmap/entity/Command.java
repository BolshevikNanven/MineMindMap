package cn.nanven.mindmap.entity;

public interface Command {
    void execute();
    void undo();
}
