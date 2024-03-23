package cn.nanven.mindmap.util;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class StyleUtil {
    public static Font mergeFont(Font font, FontWeight fontWeight) {
        double finalSize = font.getSize();
        String style = font.getStyle().toLowerCase();
        FontPosture finalFontPosture = style.contains("italic") ? FontPosture.ITALIC : FontPosture.REGULAR;
        ;

        return Font.font(font.getFamily(), fontWeight, finalFontPosture, finalSize);
    }

    public static Font mergeFont(Font font, Double size) {
        String style = font.getStyle().toLowerCase();
        FontWeight finalFontWeight = style.contains("bold") ? FontWeight.BOLD : FontWeight.NORMAL;
        FontPosture finalFontPosture = style.contains("italic") ? FontPosture.ITALIC : FontPosture.REGULAR;
        ;

        return Font.font(font.getFamily(), finalFontWeight, finalFontPosture, size);
    }

    public static Font mergeFont(Font font, FontPosture posture) {
        double finalSize = font.getSize();
        String style = font.getStyle().toLowerCase();
        FontWeight finalFontWeight = style.contains("bold") ? FontWeight.BOLD : FontWeight.NORMAL;

        return Font.font(font.getFamily(), finalFontWeight, posture, finalSize);
    }
    public static Background newBackground(String color){
       return new Background(new BackgroundFill(Paint.valueOf(color), new CornerRadii(8), null));
    }
    public static String getBackgroundColor(Background background){
        return background.getFills().get(0).getFill().toString();
    }
}
