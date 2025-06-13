package view;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.lang.reflect.Field;
import java.util.List;

public class TableUI<T> {
    private static final String BOLD = "\033[1m";
    public void getTableDisplay(List<T> tList) {
        T t = tList.get(0);
        Field[] fields = t.getClass().getDeclaredFields();
        String[] columns = new String[fields.length];

        Table table = new Table(columns.length, BorderStyle.UNICODE_ROUND_BOX, ShownBorders.ALL);
        for (int i = 0; i < fields.length; i++) {
            columns[i] = convertToUpperCaseWithSpace(fields[i].getName());
            table.setColumnWidth(i, 15, 45);
        }

        CellStyle center = new CellStyle(CellStyle.HorizontalAlign.center);
        for (String column : columns) {
            table.addCell(BOLD + BOLD + column, center);
        }

        for (T t1 : tList) {
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(t1);
                    table.addCell(String.valueOf(value), center);
                } catch (IllegalAccessException e) {
                    table.addCell("ERR");
                }
            }
        }
        System.out.println(table.render());
    }

    private String convertToUpperCaseWithSpace(String fieldName){
        return fieldName
                .replaceAll("([a-z])([A-Z])", "$1 $2")
                .toUpperCase();
    }
}