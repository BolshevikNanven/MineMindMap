package cn.nanven.mindmap.entity;

import cn.nanven.mindmap.common.jackson.deserializer.BackgroundDeserializer;
import cn.nanven.mindmap.common.jackson.deserializer.ColorDeserializer;
import cn.nanven.mindmap.common.jackson.deserializer.FontDeserializer;
import cn.nanven.mindmap.common.jackson.serializer.BackgroundSerializer;
import cn.nanven.mindmap.common.jackson.serializer.ColorSerializer;
import cn.nanven.mindmap.util.StyleUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.io.Serializable;
import java.util.List;

public class NodeEntity implements Serializable {
    @JsonIgnore
    private NodeEntity parent;
    private List<NodeEntity> children;
    @JsonIgnore
    private LineEntity line;
    @JsonIgnore
    private Double bounds;
    @JsonIgnore
    private Object param;
    @JsonIgnore
    private final SimpleBooleanProperty deleteSymbol = new SimpleBooleanProperty();
    @JsonIgnore
    private final SimpleBooleanProperty disabled = new SimpleBooleanProperty();
    private final SimpleObjectProperty<Pos> alignment = new SimpleObjectProperty<>();
    private final SimpleStringProperty content = new SimpleStringProperty();
    private final SimpleDoubleProperty x = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y = new SimpleDoubleProperty();
    private final SimpleDoubleProperty width = new SimpleDoubleProperty();
    @JsonIgnore
    private final SimpleDoubleProperty actualWidth = new SimpleDoubleProperty();
    private final SimpleDoubleProperty height = new SimpleDoubleProperty();
    @JsonIgnore
    private final SimpleDoubleProperty actualHeight = new SimpleDoubleProperty();
    @JsonSerialize(using = BackgroundSerializer.class)
    @JsonDeserialize(using = BackgroundDeserializer.class)
    private final SimpleObjectProperty<Background> background = new SimpleObjectProperty<>();
    @JsonSerialize(using = ColorSerializer.class)
    @JsonDeserialize(using = ColorDeserializer.class)
    private final SimpleObjectProperty<Paint> color = new SimpleObjectProperty<>();
    @JsonDeserialize(using = FontDeserializer.class)
    private final SimpleObjectProperty<Font> font = new SimpleObjectProperty<>();
    private final SimpleBooleanProperty fontUnderline = new SimpleBooleanProperty();

    @Override
    public String toString() {
        return "NodeEntity{" +
                "parent=" + parent +
                ", children=" + children +
                ", line=" + line +
                ", bounds=" + bounds +
                ", param=" + param +
                ", deleteSymbol=" + deleteSymbol +
                ", disabled=" + disabled +
                ", alignment=" + alignment +
                ", content=" + content +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", actualWidth=" + actualWidth +
                ", height=" + height +
                ", actualHeight=" + actualHeight +
                ", background=" + background +
                ", color=" + color +
                ", font=" + font +
                ", fontUnderline=" + fontUnderline +
                '}';
    }

    public void delete() {
        this.deleteSymbol.set(true);
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public LineEntity getLine() {
        return line;
    }

    public void setLine(LineEntity line) {
        this.line = line;
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
}
