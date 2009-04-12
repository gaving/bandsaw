package net.brokentrain.bandsaw.log4j;

import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import net.brokentrain.bandsaw.BandsawUtilities;

import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * @author Brandon
 */
public class ColumnList
{

    Vector<Integer> columns = new Vector<Integer>();

    private static boolean notInit = true;

    private static ColumnList cl;

    public static final int COL_COUNT = 10;

    public static final char delimiter = ':';

    private ColumnList()
    {
    }

    public static ColumnList getInstance()
    {
        if (notInit)
        {
            cl = new ColumnList();
            notInit = false;
        }
        return cl;
    }

    public String getText(int col, LoggingEvent le)
    {
        return BandsawUtilities.handleNull(
                BandsawUtilities
                .Log4jItemFactory(((Integer) columns.get(col)).intValue(), le)
                .getText());
    }

    public void add(int col)
    {
        columns.add(new Integer(col));
        if (BandsawUtilities.isShowing())
        {
            new TableColumn(BandsawUtilities.getTable(), SWT.NONE);
        }
    }

    public void remove(int index)
    {
        columns.remove(index);
        if (BandsawUtilities.isShowing())
        {
            Table table = BandsawUtilities.getTable();
            TableColumn col = table.getColumn(table.getColumnCount() - 1);
            col.dispose();
        }
    }

    public void moveUp(int index)
    {
        if (index == 0 || index < 0)
        {
            return;
        } // border condition
        Integer i = (Integer) columns.get(index);
        columns.remove(index);
        columns.insertElementAt(i, index - 1);
    }

    public void moveDown(int index)
    {
        if (index == columns.size() || index < 0)
        {
            return;
        } // border condition
        Integer i = (Integer) columns.get(index);
        columns.remove(index);
        columns.insertElementAt(i, index + 1);
    }

    public Iterator getList()
    {
        return columns.iterator();
    }

    public int getColumnCount()
    {
        return columns.size();
    }

    public int getColType(int index)
    {
        return ((Integer) columns.get(index)).intValue();
    }

    //TODO: Make this thread safe
    public int[] getCols()
    {
        int size = getColumnCount();
        int[] list = new int[size];
        for (int i = 0; i < size; i++)
        {
            list[i] = ((Integer) columns.get(i)).intValue();
        }
        return list;
    }

    public static int[] deSerialize(String object)
    {
        StringTokenizer st =
            new StringTokenizer(object, Character.toString(delimiter));
        int size = st.countTokens();
        int[] j = new int[size];
        int i = 0;
        while (st.hasMoreTokens())
        {
            int thisVal = 99;
            thisVal = Integer.parseInt(st.nextToken());
            j[i++] = thisVal;
        }
        return j;
    }

    public static String serialize(int[] columns)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < columns.length; i++)
        {
            sb.append(String.valueOf(columns[i]));
            if (i != columns.length - 1)
            {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public void setColList(int[] list)
    {
        columns = new Vector<Integer>();
        for (int i = 0; i < list.length; i++)
        {
            columns.add(new Integer(list[i]));
        }
    }

}
