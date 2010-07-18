package net.brokentrain.bandsaw.log4j;

import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.ArrayList;

import net.brokentrain.bandsaw.util.BandsawUtilities;

import org.apache.log4j.spi.LoggingEvent;

public class ColumnList {

    ArrayList<Integer> columns = new ArrayList<Integer>();
    ArrayList<Integer> defaultCols = new ArrayList<Integer>();

    private static boolean instance = false;

    private static ColumnList cl;

    public static final int COL_COUNT = 10;

    public static final char delimiter = ':';

    private ColumnList() {
    }

    public static ColumnList getInstance() {
        if (!instance) {
            cl = new ColumnList();
            instance = true;
        }
        return cl;
    }

    public String getText(int col, LoggingEvent le) {
        return BandsawUtilities.handleNull(BandsawUtilities.Log4jItemFactory(
                (columns.get(col)).intValue(), le).getText());
    }

    public void add(int col) {
        columns.add(new Integer(col));
    }

    public void clear() {
        columns.clear();
    }

    public void remove(int index) {
        columns.remove(index);
    }

    public Iterator<Integer> getList() {
        return columns.iterator();
    }

    public int getColumnCount() {
        return columns.size();
    }

    public ArrayList<Integer> getDefaultColumns() {
        return defaultCols;
    }

    public int[] getCols() {
        int size = getColumnCount();
        int[] list = new int[size];
        for (int i = 0; i < size; i++) {
            list[i] = (columns.get(i)).intValue();
        }
        return list;
    }

    public static int[] deSerialize(String object) {
        StringTokenizer st = new StringTokenizer(object,
                Character.toString(delimiter));
        int size = st.countTokens();
        int[] j = new int[size];
        int i = 0;
        while (st.hasMoreTokens()) {
            int thisVal = 99;
            thisVal = Integer.parseInt(st.nextToken());
            j[i++] = thisVal;
        }
        return j;
    }

    public static String serialize(int[] columns) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < columns.length; i++) {
            sb.append(String.valueOf(columns[i]));
            if (i != columns.length - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public void setColList(int[] list) {
        columns = new ArrayList<Integer>();
        for (int i = 0; i < list.length; i++) {
            columns.add(new Integer(list[i]));
        }
        defaultCols = (ArrayList<Integer>) columns.clone();
    }

}
