package net.brokentrain.bandsaw.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;

public class WidgetUtil {

    public static ArrayList<StyleRange> calculateStyleRanges(
            StyledText textField, List<String> words, Color foreground,
            Color background, int fontstyle, boolean caseSensitive,
            boolean underline) {

        ArrayList<StyleRange> styleRanges = new ArrayList<StyleRange>();

        String text = textField.getText();

        if (!caseSensitive) {
            text = textField.getText().toLowerCase();
        }

        for (int a = 0; a < words.size(); a++) {
            int start = 0;
            String curWord = words.get(a);

            if (!caseSensitive) {
                curWord = curWord.toLowerCase();
            }

            int pos;

            while ((pos = text.indexOf(curWord, start)) > -1) {

                StyleRange styleRange = new StyleRange();
                styleRange.start = pos;
                styleRange.length = (curWord.length());
                styleRange.fontStyle = fontstyle;
                styleRange.foreground = foreground;
                styleRange.background = background;
                styleRange.underline = underline;
                styleRanges.add(styleRange);

                start = styleRange.start + styleRange.length;
            }
        }

        return styleRanges;
    }

    public static void createWildCardMenu(final Text text, String[] wildcards) {
        Menu wildCardMenu = new Menu(text);

        for (String element : wildcards) {
            final String wildcard = element;
            MenuItem menuItem = new MenuItem(wildCardMenu, SWT.POP_UP);
            menuItem.setText(element);
            menuItem.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    text.insert(wildcard);
                }
            });
        }
        text.setMenu(wildCardMenu);
    }

    public static void initMnemonics(Button buttons[]) {

        ArrayList<String> chars = new ArrayList<String>();

        for (Button element : buttons) {
            String name = element.getText();

            name = name.replaceAll("&", "");

            for (int b = 0; b < name.length(); b++) {

                if ((name.substring(b, b + 1) != null)
                        && !name.substring(b, b + 1).equals(" ")) {

                    if (!chars.contains(name.substring(b, b + 1).toLowerCase())) {

                        StringBuffer buttonText = new StringBuffer(
                                name.substring(0, b));
                        buttonText.append("&").append(
                                name.substring(b, name.length()));

                        element.setText(buttonText.toString());

                        chars.add(name.substring(b, b + 1).toLowerCase());
                        break;
                    }
                }
            }
        }
    }

    public static void initMnemonics(ToolItem items[]) {

        ArrayList<String> chars = new ArrayList<String>();

        for (ToolItem element : items) {
            String name = element.getText();

            name = name.replaceAll("&", "");

            for (int b = 0; b < name.length(); b++) {

                if ((name.substring(b, b + 1) != null)
                        && !name.substring(b, b + 1).equals(" ")) {

                    if (!chars.contains(name.substring(b, b + 1).toLowerCase())) {

                        StringBuffer itemText = new StringBuffer(
                                name.substring(0, b));
                        itemText.append("&").append(
                                name.substring(b, name.length()));

                        element.setText(itemText.toString());

                        chars.add(name.substring(b, b + 1).toLowerCase());
                        break;
                    }
                }
            }
        }
    }

    public static boolean isset(Widget widget) {
        return ((widget != null) && !widget.isDisposed());
    }

    public static boolean isWidget(Object obj) {
        return ((obj != null) && (obj instanceof Widget) && isset((Widget) obj));
    }

    private WidgetUtil() {
    }
}
