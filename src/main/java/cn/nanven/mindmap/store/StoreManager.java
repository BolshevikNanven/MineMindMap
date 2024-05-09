package cn.nanven.mindmap.store;

import cn.nanven.mindmap.entity.Command;
import cn.nanven.mindmap.entity.NodeEntity;
import cn.nanven.mindmap.view.AuxiliaryNodeView;
import cn.nanven.mindmap.view.NodeView;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class StoreManager {
    private static final List<NodeEntity> rootNodeList = new ArrayList<>();
    private static NodeView selectedNode;
    private static AuxiliaryNodeView auxiliaryNode;
    private static final SimpleIntegerProperty canvasScale = new SimpleIntegerProperty(100);
    private static final Stack<Command> undoStack = new Stack<>();
    private static final Stack<Command> redoStack = new Stack<>();
    private static final SimpleObjectProperty<File> file = new SimpleObjectProperty<>();
    private static volatile SimpleBooleanProperty loadingState = new SimpleBooleanProperty(false);

    public static int getCanvasScale() {
        return canvasScale.get();
    }

    public static SimpleIntegerProperty canvasScaleProperty() {
        return canvasScale;
    }

    public static void setCanvasScale(int canvasScale) {
        StoreManager.canvasScale.set(canvasScale);
    }

    public static Stack<Command> getUndoStack() {
        return undoStack;
    }

    public static Stack<Command> getRedoStack() {
        return redoStack;
    }

    public static List<NodeEntity> getRootNodeList() {
        return rootNodeList;
    }

    public static AuxiliaryNodeView getAuxiliaryNode() {
        return auxiliaryNode;
    }

    public static void setAuxiliaryNode(AuxiliaryNodeView auxiliaryNode) {
        StoreManager.auxiliaryNode = auxiliaryNode;
    }

    public static NodeView getSelectedNode() {
        return selectedNode;
    }

    public static void setSelectedNode(NodeView selectedNode) {
        StoreManager.selectedNode = selectedNode;
    }

    public static void setFile(File file) {
        StoreManager.file.set(file);
    }

    public static File getFile() {
        return StoreManager.file.get();
    }
    public static SimpleObjectProperty<File> file(){
        return StoreManager.file;
    }

    public static boolean isLoadingState() {
        return loadingState.get();
    }

    public static SimpleBooleanProperty loadingStateProperty() {
        return loadingState;
    }

    public static void setLoadingState(boolean loadingState) {
        StoreManager.loadingState.set(loadingState);
    }
}
