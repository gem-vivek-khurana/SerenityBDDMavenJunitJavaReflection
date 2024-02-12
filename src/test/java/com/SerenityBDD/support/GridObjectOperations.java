package com.SerenityBDD.support;

import com.SerenityBDD.execute.Perform;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggingEventBuilder;

import java.lang.reflect.Field;
import java.util.*;

public class GridObjectOperations extends PageObjectOperations {

    public final LoggingEventBuilder LOGGER_INFO = LoggerFactory.getLogger(GridObjectOperations.class).atInfo();
    final Page page = Serenity.sessionVariableCalled("page");

    @Steps
    Perform perform;

    @Steps
    PageObjectOperations pageObjectOperations;

    /**
     * Returns all table headers, including empty headings, for a given table element.
     *
     * @param table The locator for the table element.
     * @return List of table headers as strings.
     */
    public List<String> getAllTableHeaders(String table) {
        ElementHandle tableElement = page.querySelector(table);
        return getAllTableHeaders(tableElement);
    }

    /**
     * Returns all table headers, including empty headings, for a given table element.
     *
     * @param tableElement The table ElementHandle.
     * @return List of table headers as strings.
     */
    public List<String> getAllTableHeaders(ElementHandle tableElement) {
        return tableElement.querySelectorAll("th").stream().map(ElementHandle::innerText)
                .toList().stream().map(String::trim).toList();
    }

    /**
     * Returns the visible table headers for a given table locator.
     *
     * @param table The locator for the table element.
     * @return List of visible table headers as strings.
     */
    public List<String> getTableHeaders(String table) {
        return getAllTableHeaders(table).stream().filter(r -> !r.isEmpty()).toList();
    }

    /**
     * Get the total number of rows in a table's body for a given table locator.
     *
     * @param table The locator for the table element.
     * @return The number of rows in the table.
     */
    public int getRowCount(String table) {
        ElementHandle tableElement = page.querySelector(table);
        return tableElement.querySelectorAll("tbody tr").size();
    }

    /**
     * Get data of the rows as a List present on the UI under a column in the table.
     *
     * @param columns List of Columns to fetch the data
     * @param table {@link String} locator of the table
     * @return HashMap<String, ArrayList<String>>
     */
    public HashMap<String, ArrayList<String>> getRowsDataForColumns(List<String> columns, String table) {
        HashMap<String, ArrayList<String>> rowsForColumnsMap = new HashMap<>();
        List<String> tableHeaders = getAllTableHeaders(table);
        ElementHandle tableElement = page.querySelector(table);
        for (String column : columns) {
            int colIndex = tableHeaders.indexOf(column);
            List<ElementHandle> rowElements = tableElement.querySelectorAll("tbody tr");
            rowsForColumnsMap.put(column, new ArrayList<>(){{
                for (ElementHandle rowElement : rowElements) {
                    add(rowElement.querySelector("td:nth-child(" + (colIndex + 1) + ")").innerText());
                }
            }});
        }
        return rowsForColumnsMap;
    }

    /**
     * Get values for a specified row and column as a Map for a given table locator.
     *
     * @param tableRow The Map containing row and column data.
     * @param table The locator for the table element.
     * @return Map where keys are column names, and values are the corresponding row values.
     */
    public Map<String, String> getValueForRowColumn(Map<String, String> tableRow, String table) {
        Map<String, String> results = new HashMap<>();
        List<String> columns = new ArrayList<>(tableRow.keySet());
        if (columns.indexOf("row") != 0) {
            int tempIndex = columns.indexOf("row");
            columns.set(tempIndex, columns.get(0));
            columns.set(0, "row");
        }
        results.put("row", tableRow.get("row"));
        for (int i = 1; i < columns.size(); i++) {
            int rowNumber = Integer.parseInt(tableRow.get("row")) - 1;
            LOGGER_INFO.log("Getting Data for: " + columns.get(i));
            String tdSelector = "td:nth-child(" + (getAllTableHeaders(table).indexOf(columns.get(i)) + 1) + ")";
            String result = page.querySelector(table).querySelectorAll("tbody tr").get(rowNumber)
                    .querySelector(tdSelector).innerText();
            if (Objects.equals(result, "")) {
                result = null;
            }
            results.put(columns.get(i), result);
        }
        return results;
    }

    /**
     * Get the locator for a focused row in a grid for a specified row number and grid class name.
     *
     * @param rowNumber     The row number to focus on.
     * @param gridClassName The class name of the grid.
     * @return HashMap containing the selector style and locator.
     */
    public HashMap<String, String> getRowFocusedLocator(int rowNumber, String gridClassName) {
        Field elementLoaded = pageObjectOperations.poeFieldClass("GRID_LOADED", gridClassName);
        Class<?> gridClass = pageObjectOperations.getPageClass(gridClassName);
        String field = perform.fieldToInteract(elementLoaded, gridClass);
        if (field.toLowerCase().contains("//")) {
            return constructFieldSelector("xpath", "table", field, "/tbody/tr[" + rowNumber + "]");
        } else {
            return constructFieldSelector("cssSelector", "table", field, ">tbody tr:nth-child(" + rowNumber + ")");
        }
    }

    /**
     * Get the ElementHandle for a focused row in a grid for a specified row number and grid page.
     *
     * @param rowNumber The row number to focus on.
     * @param gridPage  The grid page name.
     * @return ElementHandle representing the focused row.
     */
    public ElementHandle getRowFocusedElementHandle(int rowNumber, String gridPage) {
        HashMap<String, String> rowElementMap = getRowFocusedLocator(rowNumber, gridPage);
        String locator = rowElementMap.values().stream().toList().get(0);
        return page.querySelector(locator);
    }

    /**
     * Construct a field selector for a given main tag, field, and additional selector.
     *
     * @param mainTag            The main HTML tag for the field.
     * @param field              The field locator.
     * @param additionalSelector Additional selector (if any).
     * @return HashMap containing the selector style and locator.
     */
    public HashMap<String, String> constructFieldSelector(String selectorType, String mainTag, String field, String... additionalSelector) {
        HashMap<String, String> selector = new HashMap<>();
        if (selectorType.equals("xpath") || selectorType.equals("cssSelector")) {
            selector.put(selectorType, field + additionalSelector[0]);
        } else {
            selector.put(selectorType, mainTag + field + "\"]" + additionalSelector[0]);
        }
        return selector;
    }
}
