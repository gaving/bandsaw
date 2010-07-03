package net.brokentrain.bandsaw.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Level;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class PaintUtil {

    private static final Class<PaintUtil> clazz = PaintUtil.class;

    public static Image iconInfo;

    public static Image iconError;

    public static Image iconWarning;

    public static void disposeIcons() {
        iconInfo.dispose();
        iconWarning.dispose();
        iconError.dispose();
    }

    public static Image getIcon(int type) {
        switch (type) {
            case Level.ERROR_INT:
                return PaintUtil.iconError;
            case Level.INFO_INT:
                return PaintUtil.iconInfo;
            case Level.WARN_INT:
                return PaintUtil.iconWarning;
            case Level.DEBUG_INT:
                return PaintUtil.iconInfo;
            default:
                return PaintUtil.iconInfo;
        }
    }

    public static Image getFilledImage(Display display, RGB color, int width,
            int height) {
        Image filledImage = new Image(display, width, height);
        Color selectedColor = new Color(display, color);

        GC gc = new GC(filledImage);
        gc.setBackground(selectedColor);
        gc.fillRectangle(0, 0, width, height);
        gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawRectangle(0, 0, width - 1, height - 1);

        gc.dispose();
        selectedColor.dispose();

        return filledImage;
    }

    public static void initIcons() {
        iconInfo = loadImage("/icons/info.png");
        iconWarning = loadImage("/icons/warning.png");
        iconError = loadImage("/icons/error.png");
    }

    public static boolean isset(Image image) {
        return ((image != null) && !image.isDisposed());
    }

    public static Image loadImage(String path) {

        Image image;
        InputStream inS = null;

        try {
            inS = clazz.getResourceAsStream(path);
            image = new Image(Display.getCurrent(), inS);
            inS.close();
        } catch (IOException ioe) {
            image = null;
            ioe.printStackTrace();
        } catch (Exception e) {
            image = null;
            e.printStackTrace();
        } finally {
            if (inS != null) {
                try {
                    inS.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        return image;
    }

    private PaintUtil() {
    }

}
