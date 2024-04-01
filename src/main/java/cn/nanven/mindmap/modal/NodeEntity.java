package cn.nanven.mindmap.modal;

import com.google.gson.Gson;
import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.util.List;

public class NodeEntity implements Cloneable{
    private NodeEntity parent;
    private List<NodeEntity> children;
    private Double bounds;
    private final SimpleBooleanProperty deleteSymbol = new SimpleBooleanProperty();
    private final SimpleBooleanProperty disabled = new SimpleBooleanProperty();
    private final SimpleObjectProperty<Pos> alignment = new SimpleObjectProperty<>();
    private final SimpleStringProperty content = new SimpleStringProperty();
    private final SimpleDoubleProperty x = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y = new SimpleDoubleProperty();
    private final SimpleDoubleProperty width = new SimpleDoubleProperty();
    private final SimpleDoubleProperty actualWidth = new SimpleDoubleProperty();
    private final SimpleDoubleProperty height = new SimpleDoubleProperty();
    private final SimpleDoubleProperty actualHeight = new SimpleDoubleProperty();
    private final SimpleObjectProperty<Background> background = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Paint> color = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Font> font = new SimpleObjectProperty<>();
    private final SimpleBooleanProperty fontUnderline = new SimpleBooleanProperty();

    public void delete() {
        this.deleteSymbol.set(true);

    }

    public boolean getDisabled() {
        return disabled.get();
    }

    public SimpleBooleanProperty disabledProperty() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled.set(disabled);
    }

    public Background getBackground() {
        return background.get();
    }

    public SimpleObjectProperty<Background> backgroundProperty() {
        return background;
    }

    public void setBackground(Background background) {
        this.background.set(background);
    }

    public SimpleBooleanProperty deleteSymbolProperty() {
        return deleteSymbol;
    }

    public Double getBounds() {
        return bounds;
    }

    public void setBounds(Double bounds) {
        this.bounds = bounds;
    }

    public NodeEntity getParent() {
        return parent;
    }

    public void setParent(NodeEntity parent) {
        this.parent = parent;
    }

    public List<NodeEntity> getChildren() {
        return children;
    }

    public void setChildren(List<NodeEntity> children) {
        this.children = children;
    }

    public Pos getAlignment() {
        return alignment.get();
    }

    public SimpleObjectProperty<Pos> alignmentProperty() {
        return alignment;
    }

    public void setAlignment(Pos alignment) {
        this.alignment.set(alignment);
    }

    public String getContent() {
        return content.get();
    }

    public SimpleStringProperty contentProperty() {
        return content;
    }

    public void setContent(String content) {
        this.content.set(content);
    }

    public double getX() {
        return x.get();
    }

    public SimpleDoubleProperty xProperty() {
        return x;
    }

    public void setX(double x) {
        this.x.set(x);
    }

    public double getY() {
        return y.get();
    }

    public SimpleDoubleProperty yProperty() {
        return y;
    }

    public void setY(double y) {
        this.y.set(y);
    }

    public double getActualWidth() {
        return actualWidth.get();
    }

    public SimpleDoubleProperty actualWidthProperty() {
        return actualWidth;
    }

    public double getActualHeight() {
        return actualHeight.get();
    }

    public SimpleDoubleProperty actualHeightProperty() {
        return actualHeight;
    }

    public double getWidth() {
        return width.get();
    }

    public SimpleDoubleProperty widthProperty() {
        return width;
    }

    public void setWidth(double width) {
        this.width.set(width);
    }

    public double getHeight() {
        return height.get();
    }

    public SimpleDoubleProperty heightProperty() {
        return height;
    }

    public void setHeight(double height) {
        this.height.set(height);
    }

    public Paint getColor() {
        return color.get();
    }

    public SimpleObjectProperty<Paint> colorProperty() {
        return color;
    }

    public void setColor(Paint color) {
        this.color.set(color);
    }

    public Font getFont() {
        return font.get();
    }

    public SimpleObjectProperty<Font> fontProperty() {
        return font;
    }

    public void setFont(Font font) {
        this.font.set(font);
    }

    public boolean isFontUnderline() {
        return fontUnderline.get();
    }

    public SimpleBooleanProperty fontUnderlineProperty() {
        return fontUnderline;
    }

    public void setFontUnderline(boolean fontUnderline) {
        this.fontUnderline.set(fontUnderline);
    }


    @Override
    public NodeEntity clone() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        NodeEntity node = gson.fromJson(json, NodeEntity.class);
        return node;
    }
}
