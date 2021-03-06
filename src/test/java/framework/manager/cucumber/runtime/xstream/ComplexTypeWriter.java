package test.java.framework.manager.cucumber.runtime.xstream;

import test.java.framework.manager.cucumber.runtime.table.CamelCaseStringConverter;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class ComplexTypeWriter extends CellWriter {
    private final List<String> columnNames;
    private final List<String> fieldNames = new ArrayList<String>();
    private final List<String> fieldValues = new ArrayList<String>();

    private int nodeDepth = 0;

    public ComplexTypeWriter(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    @Override
    public List<String> getHeader() {
        return columnNames.isEmpty() ? fieldNames : columnNames;
    }

    @Override
    public List<String> getValues() {
        CamelCaseStringConverter converter = new CamelCaseStringConverter();
        if (!columnNames.isEmpty()) {
            String[] explicitFieldValues = new String[columnNames.size()];
            int n = 0;
            for (String columnName : columnNames) {
                int index = fieldNames.indexOf(converter.map(columnName));
                if (index == -1) {
                    explicitFieldValues[n] = "";
                } else {
                    explicitFieldValues[n] = fieldValues.get(index);
                }
                n++;
            }
            return asList(explicitFieldValues);
        } else {
            return fieldValues;
        }
    }

    @Override
    public void startNode(String name) {
        if (nodeDepth == 1) {
            this.fieldNames.add(name);
        }
        nodeDepth++;
    }

    @Override
    public void addAttribute(String name, String value) {
    }

    @Override
    public void setValue(String value) {
        fieldValues.add(value == null ? "" : value);
    }

    @Override
    public void endNode() {
        nodeDepth--;
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }
}
