package cn.nanven.mindmap.view;

import cn.nanven.mindmap.dao.NodeDao;
import cn.nanven.mindmap.modal.NodeEntity;
import cn.nanven.mindmap.service.NodeService;
import cn.nanven.mindmap.store.StoreManager;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.concurrent.atomic.AtomicReference;

public class NodeView extends AnchorPane {
    private static final String BASE_CLASS = "node-box";
    private static final String TEXT_CLASS = "node-text";

    private NodeEntity nodeEntity;
    private TextField textField;

    private NodeView() {
    }

    public NodeView(NodeEntity nodeEntity) {
        this.nodeEntity = nodeEntity;
        textField = new TextField();

        this.getStyleClass().add(BASE_CLASS);
        this.layoutXProperty().bind(nodeEntity.xProperty());
        this.layoutYProperty().bind(nodeEntity.yProperty());
        this.prefWidthProperty().bind(nodeEntity.widthProperty());
        this.prefHeightProperty().bind(nodeEntity.heightProperty());
        this.backgroundProperty().bind(nodeEntity.backgroundProperty());

        nodeEntity.actualWidthProperty().bind(this.widthProperty());
        nodeEntity.actualHeightProperty().bind(this.heightProperty());


        textField.getStyleClass().add(TEXT_CLASS);
        AnchorPane.setTopAnchor(textField, 0.0);
        AnchorPane.setRightAnchor(textField, 0.0);
        AnchorPane.setBottomAnchor(textField, 0.0);
        AnchorPane.setLeftAnchor(textField, 0.0);
        textField.setPrefWidth(-1);
        textField.textProperty().bindBidirectional(nodeEntity.contentProperty());
        textField.alignmentProperty().bind(nodeEntity.alignmentProperty());
        textField.setStyle("-fx-text-fill:#" + nodeEntity.getColor().toString().substring(2));
        textField.fontProperty().bind(nodeEntity.fontProperty());
        textField.disableProperty().bind(nodeEntity.disabledProperty());
        nodeEntity.fontUnderlineProperty().addListener((e, prev, value) -> {
            if (value) {
                textField.getStyleClass().add("underline");
            } else textField.getStyleClass().remove("underline");
        });

        this.getChildren().add(textField);
        addListener();
    }

    private void addListener() {
        //定义偏移量
        final double[] mouseAnchor = new double[2];
        AtomicReference<Boolean> isDrag = new AtomicReference<>(false);

        nodeEntity.deleteSymbolProperty().addListener(e -> {
            this.nodeEntity = null;
            NodeService.getInstance().removeNode(this);
        });
        nodeEntity.colorProperty().addListener(e -> {
            textField.setStyle("-fx-text-fill:#" + nodeEntity.getColor().toString().substring(2));
        });

        this.setOnMouseClicked(e -> {
            this.requestFocus();
            if (e.getClickCount() == 2) {
                this.nodeEntity.setDisabled(false);
                textField.requestFocus();
            }
            NodeService.getInstance().selectNode(this);
            e.consume();
        });
        this.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> NodeService.getInstance().addBroNode();
                case TAB -> NodeService.getInstance().addSubNode();
                case DELETE -> NodeDao.deleteNode(this.getNodeEntity());
                case ESCAPE -> NodeService.getInstance().selectNode(null);
                default -> focusText();
            }
        });
        textField.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case TAB -> {
                    NodeService.getInstance().addSubNode();
                    e.consume();
                }
            }
        });
        this.setOnMouseMoved(e -> {
            if (e.getX() > this.getWidth() - 8 && e.getX() < this.getWidth() + 8 && e.getY() > this.getHeight() - 8 && e.getY() < this.getHeight() + 8) {
                this.setCursor(Cursor.SE_RESIZE);
            } else if (e.getX() > this.getWidth() - 8 && e.getX() < this.getWidth() + 8) {
                this.setCursor(Cursor.E_RESIZE);
            } else if (e.getY() > this.getHeight() - 8 && e.getY() < this.getHeight() + 8) {
                this.setCursor(Cursor.S_RESIZE);
            } else {
                this.setCursor(Cursor.DEFAULT);
            }
        });
        this.setOnMousePressed(e -> {
            StoreManager.getAuxiliaryNode().render(nodeEntity);
            mouseAnchor[0] = e.getSceneX() - this.getLayoutX();
            mouseAnchor[1] = e.getSceneY() - this.getLayoutY();
        });
        this.setOnMouseDragged(e -> {
            isDrag.set(true);
            NodeService.getInstance().dragNode(this, mouseAnchor[0], mouseAnchor[1], e);
        });
        this.setOnMouseReleased(e -> {
            if (isDrag.get()) {
                NodeService.getInstance().dragDoneNode(this, mouseAnchor[0], mouseAnchor[1], e);
            }
            isDrag.set(false);
        });
    }

    public NodeEntity getNodeEntity() {
        return nodeEntity;
    }

    public void focusText() {
        this.nodeEntity.setDisabled(false);
        this.textField.requestFocus();
    }
}
