package com.SerenityBDD.execute;

import com.SerenityBDD.state.VerifyStateOf;
import com.SerenityBDD.support.DataObjectOperations;
import com.microsoft.playwright.Dialog;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import com.SerenityBDD.pages.gemfin.dialogs.Calendar;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggingEventBuilder;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Perform extends PageObject {
    public final LoggingEventBuilder LOGGER_INFO = LoggerFactory.getLogger(Perform.class).atInfo();
    final VerifyStateOf verifyStateOf = new VerifyStateOf();
    final Page page = Serenity.sessionVariableCalled("page");

    /**
     * Retrieves a String locator object associated with a given field within a specified page class.
     *
     * @param field     The field for which to retrieve the String locator.
     * @param pageClass The page class containing the field.
     * @return String   The String locator for the specified field.
     * @throws RuntimeException If there is an IllegalAccessException when accessing the field.
     */
    public String fieldToInteract(Field field, Class<?> pageClass) {
        try {
            return (String) field.get(pageClass);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets a specified value to an HTML element identified by a String locator.
     *
     * @param field     The field associated with the element to interact with.
     * @param pageClass The page class containing the field.
     * @param value     The value to be set in the HTML element.
     */
    public void settingFieldValue(Field field, Class<?> pageClass, String value) {
        String fieldToFill = fieldToInteract(field, pageClass);
        page.waitForSelector(fieldToFill);
        settingFieldValue(fieldToFill, value);
    }

    /**
     * Sets a specified value to an HTML element identified by a String locator.
     *
     * @param field The String locator for the HTML element.
     * @param value   The value to be set in the HTML element.
     */
    public void settingFieldValue(String field, String value) {
        page.fill(field, value);
    }

    /**
     * Clicks on an HTML element identified by a String locator.
     *
     * @param field     The field associated with the element to click.
     * @param pageClass The page class containing the field.
     */
    public void clickOn(Field field, Class<?> pageClass) {
        String fieldToClick = fieldToInteract(field, pageClass);
        clickOn(fieldToClick);
    }

    /**
     * Clicks on an HTML element identified by a String locator.
     *
     * @param field The String locator for the HTML element.
     */
    public void clickOn(String field) {
        page.click(field);
    }

    /**
     * Clicks on a provided ElementHandle.
     *
     * @param elementToClick The ElementHandle to click on.
     */
    public void clickOn(ElementHandle elementToClick) {
        elementToClick.click();
    }

    /**
     * Retrieves a list of ElementHandles based on a provided #{@link String} locator.
     *
     * @param element The String locator to locate the list of ElementHandles.
     * @return List<ElementHandle> A list of ElementHandle identified by the provided String locator.
     */
    public List<ElementHandle> getElementHandles(String element) {
        return page.querySelectorAll(element);
    }

    /**
     * Retrieves the value of a text field identified by a String locator.
     *
     * @param field The String locator for the text field.
     * @return String The value of the text field identified by the provided String locator.
     */
    public String gettingTextFieldValue(String field) {
        ElementHandle fieldValueToGet = page.querySelector(field);
        return fieldValueToGet.getAttribute("value");
    }

    /**
     * Retrieves the selected option's text from a dropdown element identified by a String locator.
     *
     * @param field The String locator for the dropdown element.
     * @return String The text of the selected option in the dropdown identified by the provided String locator.
     */
    public String gettingDropdownValue(String field) {
        ElementHandle dropdownElement = page.querySelector(field);
        return dropdownElement.getProperty("value").toString();
    }

    /**
     * Retrieves the status (Selected or Unselected) of a radio button element identified by a String locator.
     *
     * @param field The String locator for the radio button element.
     * @return String The status ("Selected" or "Unselected") of the radio button identified by the provided String locator.
     */
    public String gettingRadioStatus(String field) {
        if (page.querySelector(field).isChecked()) {
            return "Selected";
        } else {
            return "Unselected";
        }
    }

    /**
     * Retrieves the status (Selected or Unselected) of a radio button element.
     *
     * @param element The ElementHandle representing the radio button element.
     * @return String The status ("Selected" or "Unselected") of the radio button represented by the provided ElementHandle.
     */
    public String gettingRadioStatus(ElementHandle element) {
        if (element.isChecked()) {
            return "Selected";
        } else {
            return "Unselected";
        }
    }

    /**
     * Retrieves the status (Checked or Unchecked) of a checkbox element identified by a String locator.
     *
     * @param field The String locator for the checkbox element.
     * @return String The status ("Checked" or "Unchecked") of the checkbox identified by the provided String locator.
     */
    public String gettingCheckboxStatus(String field) {
        if (gettingRadioStatus(field).equalsIgnoreCase("selected")) return "Checked";
        else return "Unchecked";
    }

    /**
     * Retrieves the status (Checked or Unchecked) of a checkbox element.
     *
     * @param element The ElementHandle representing the checkbox element.
     * @return String The status ("Checked" or "Unchecked") of the checkbox represented by the provided ElementHandle.
     */
    public String gettingCheckboxStatus(ElementHandle element) {
        if (gettingRadioStatus(element).equalsIgnoreCase("selected")) return "Checked";
        else return "Unchecked";
    }

    /**
     * Retrieves the state of a field (visibility, read-only, selected) identified by a String locator.
     *
     * @param field     The field for which to retrieve the state.
     * @param pageClass The page class containing the field.
     * @return HashMap<String, Boolean> A HashMap containing field states, including "not visible," "visible," "readonly," and "selected."
     */
    public HashMap<String, Boolean> gettingFieldState(Field field, Class<?> pageClass) {
        String fieldForState = fieldToInteract(field, pageClass);
        return gettingFieldState(fieldForState);
    }

    /**
     * Retrieves the state of a field (visibility, read-only, selected) identified by a String locator.
     *
     * @param field The String locator for the field.
     * @return HashMap<String, Boolean> A HashMap containing field states, including "not visible," "visible," "readonly," and "selected."
     */
    public HashMap<String, Boolean> gettingFieldState(String field) {
        HashMap<String, Boolean> fieldState = new HashMap<>();
        ElementHandle element = null;
        try {
            element = page.querySelector(field);
            fieldState.put("not visible", !element.isVisible());
            fieldState.put("visible", element.isVisible());
        } catch (NoSuchElementException e) {
            fieldState.put("not visible", true);
        }
        if (element != null) {
            fieldState.put("visible", element.isVisible());
            try {
                fieldState.put("readonly", !element.isEnabled());
                fieldState.put("selected", element.isChecked());
            } catch (Exception e) {
                LOGGER_INFO.log("Attempt to perform an unsupported operation for the field.");
            }
        }
        return fieldState;
    }

    /**
     * Accepts the currently displayed browser alert.
     */
    public void acceptingBrowserAlert() {
        page.onDialog(Dialog::accept);
    }

    /**
     * Dismisses the currently displayed browser alert.
     */
    public void dismissingBrowserAlert() {
        page.onDialog(Dialog::dismiss);
    }

    /**
     * Retrieves the value of a field based on its type and the associated String locator.
     *
     * @param field     The field for which to retrieve the value.
     * @param pageClass The page class containing the field.
     * @param fieldType The type of the field, such as TEXT_FIELD, TEXTAREA, DATE_FIELD, LABEL, DATE_LABEL, DROPDOWN, RADIO, or CHECKBOX.
     * @return String   The value of the field as a String.
     * @throws RuntimeException If an unsupported field type is provided.
     */
    public String gettingFieldValue(Field field, Class<?> pageClass, FieldType fieldType) {
        String fieldValueToGet = fieldToInteract(field, pageClass);
        switch (fieldType) {
            case TEXT_FIELD, TEXTAREA, DATE_FIELD -> {
                return gettingTextFieldValue(fieldValueToGet);
            }
            case LABEL, DATE_LABEL -> {
                return gettingFieldValue(fieldValueToGet);
            }
            case DROPDOWN -> {
                return gettingDropdownValue(fieldValueToGet);
            }
            case RADIO -> {
                return gettingRadioStatus(fieldValueToGet);
            }
            case CHECKBOX -> {
                return gettingCheckboxStatus(fieldValueToGet);
            }
        }
        throw new RuntimeException("Unsupported field type: " + fieldType);
    }

    /**
     * Retrieves the value of an HTML element identified by a String locator.
     *
     * @param field The String locator for the HTML element.
     * @return String The value of the HTML element identified by the provided String locator.
     */
    public String gettingFieldValue(String field) {
        ElementHandle fieldValueToGet = page.querySelector(field);
        String value = fieldValueToGet.getAttribute("value");
        value = value == null ? fieldValueToGet.innerText() : value;
        return value;
    }

    /**
     * Sets the value of a date field identified by a Field and Class in the page object.
     *
     * @param field     The field representing the date input.
     * @param pageClass The page class containing the date field.
     * @param value     The date value to set in the date field.
     */
    public void settingDateFieldValue(Field field, Class<?> pageClass, String value) {
        String fieldToFill = fieldToInteract(field, pageClass);
        page.waitForSelector(fieldToFill);
        settingDateFieldValue(fieldToFill, value);
    }

    /**
     * Sets the value of a date field identified by a String locator.
     *
     * @param field The String locator for the date field.
     * @param value   The date value to set in the date field.
     */
    public void settingDateFieldValue(String field, String value) {
        // Clear the date field to ensure no previous values are present.
        page.fill(field, "");

        // Attempt to parse the date using different date formatters.
        ArrayList<DateTimeFormatter> formatters = DataObjectOperations.DateTimeFormatters.getFormattersList();
        String separator = "";
        LocalDate date;
        for (DateTimeFormatter formatter : formatters) {
            try {
                date = LocalDate.parse(value, formatter);
                String formattedDate = formatter.format(date);
                separator = formattedDate.contains("/") ? "/" : "-";
                if (separator.equals("-")) separator = formattedDate.contains("-") ? "-" : "";
                break;
            } catch (Exception ignored) {
            }
        }

        // Split the date value based on the determined separator and enter each part.
        String[] splitValue = value.split(separator);
        for (String val : splitValue) {
            page.fill(field, value);
        }

        // Send the ESCAPE key to exit date entry mode if needed.
        page.querySelector(field).press("Escape");

        // If the entered date does not contain the determined separator, clear and set the value again.
        if (!gettingFieldValue(field).contains(separator)) {
            page.fill(field, "");
            settingFieldValue(field, value);
        }
    }

    /**
     * Sets the selected value of a dropdown element identified by a Field and Class in the page object.
     *
     * @param field     The field representing the dropdown input.
     * @param pageClass The page class containing the dropdown field.
     * @param value     The value to select from the dropdown.
     */
    public void settingDropdownValue(Field field, Class<?> pageClass, String value) {
        String fieldToSelect = fieldToInteract(field, pageClass);
        page.waitForSelector(fieldToSelect);
        settingDropdownValue(fieldToSelect, value);
    }

    /**
     * Sets the selected value of a dropdown element identified by a String locator.
     *
     * @param field The String locator for the dropdown element.
     * @param value   The value to select from the dropdown.
     */
    public void settingDropdownValue(String field, String value) {
        ElementHandle elementHandle = page.querySelector(field);
        elementHandle.selectOption(value);
    }

    /**
     * Sets the state of a checkbox element identified by a Field and Class in the page object.
     *
     * @param field     The field representing the checkbox input.
     * @param pageClass The page class containing the checkbox field.
     * @param value     The desired state for the checkbox ("checked" or "unchecked").
     */
    public void settingCheckboxAs(Field field, Class<?> pageClass, String value) {
        String checkboxField = fieldToInteract(field, pageClass);
        page.waitForSelector(checkboxField);
        settingCheckboxAs(checkboxField, value);
    }

    /**
     * Sets the state of a checkbox element identified by a String locator.
     *
     * @param field The String locator for the checkbox element.
     * @param value   The desired state for the checkbox ("checked" or "unchecked").
     */
    public void settingCheckboxAs(String field, String value) {
        ElementHandle checkbox = page.querySelector(field);
        settingCheckboxAs(checkbox, value);
    }

    /**
     * Sets the state of a checkbox element represented by a ElementHandle.
     *
     * @param checkbox The ElementHandle representing the checkbox.
     * @param value    The desired state for the checkbox ("checked" or "unchecked").
     */
    public void settingCheckboxAs(ElementHandle checkbox, String value) {
        boolean checkboxState = verifyStateOf.checkboxIsSelected(checkbox);
        boolean expectedState = value.equalsIgnoreCase("checked");
        if ((!checkboxState && expectedState) || (checkboxState && !expectedState)) {
            checkbox.click();
        } else if (checkboxState == expectedState) {
            LOGGER_INFO.log("Checkbox state is already as expected i.e.: " + value);
        } else if (!value.equalsIgnoreCase("checked") && !value.equalsIgnoreCase("unchecked")) {
            throw new IllegalArgumentException("Incorrect value provided for setting the checkbox. It should " +
                    "either be 'Checked' or 'Unchecked' (Case-Insensitive)");
        } else {
            throw new RuntimeException("Unable to " + value.replace("ed", "") + " checkbox");
        }
    }

    /**
     * Refreshes the current page in the web browser.
     */
    public void pageRefresh() {
        page.reload();
    }

    /**
     * Navigates to the previous page in the web browser's history.
     */
    public void headingToPreviousPage() {
        page.goBack();
    }

    /**
     * Retrieves a set of window handles currently open in the web browser.
     *
     * @return Set<String> A set of window handles as strings.
     */
    public List<Page> gettingWindowHandles() {
        try {
            // Wait for new window to open if expected
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return page.context().pages();
    }

    /**
     * Switches to a window handle based on the specified classification type and matching parameter.
     *
     * @param type              The classification type for identifying the window handle (TITLE_MATCH or URL_SUBSTRING).
     * @param matchingParameter The parameter to match for identifying the window handle.
     */
    public void switchToWindowHandle(WindowHandleClassificationType type, String matchingParameter) {
        List<Page> pages = page.context().pages();
        if (pages.size() == 1) {
            page.bringToFront();
        }
        boolean windowFound = false;

        for (int attempt = 1; attempt <= 20; attempt++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (type.equals(WindowHandleClassificationType.TITLE_MATCH)) {
                for (Page p : pages) {
                    if (p.title().contains(matchingParameter)) {
                        p.bringToFront();
                        windowFound = true;
                        break;
                    }
                }
            } else if (type.equals(WindowHandleClassificationType.URL_SUBSTRING)) {
                for (Page p : pages) {
                    if (p.url().contains(matchingParameter)) {
                        p.bringToFront();
                        windowFound = true;
                        break;
                    }
                }
            }

            if (windowFound) {
                LOGGER_INFO.log("Window found: TRUE. Switched to window with title: " + page.title());
                break;
            }

            LOGGER_INFO.log("Attempt No." + attempt + " to locate window with handle of type: " + type);
        }
        if (!windowFound) {
            throw new RuntimeException("Unable to find window with type: " + type +
                    " and value: " + matchingParameter);
        }
    }

    /**
     * Closes the current window based on the specified classification type and matching parameter.
     *
     * @param windowHandleClassificationType The classification type for identifying the window handle (TITLE_MATCH or URL_SUBSTRING).
     * @param matchingParameter              The parameter used to match the window handle.
     */
    public void closingWindow(WindowHandleClassificationType windowHandleClassificationType, String matchingParameter) {
        boolean rightWindowFocused = false;
        if (windowHandleClassificationType.equals(WindowHandleClassificationType.TITLE_MATCH)) {
            rightWindowFocused = page.title().equals(matchingParameter);
        } else if (windowHandleClassificationType.equals(WindowHandleClassificationType.URL_SUBSTRING)) {
            rightWindowFocused = page.url().contains(matchingParameter);
        }
        if (!rightWindowFocused) {
            switchToWindowHandle(windowHandleClassificationType, matchingParameter);
        }
        page.close();
    }

    public void settingAngularDropdownAs(Field angularDropdownElement, Class<?> pageClass, String value) {
        String fieldToSelect = fieldToInteract(angularDropdownElement, pageClass);
        page.waitForSelector(fieldToSelect);
        settingAngularDropdownAs(fieldToSelect, value);
    }

    public void settingAngularDropdownAs(String field, String value) {
        clickOn(field);
        page.waitForSelector("div[role='listbox']");
        List<ElementHandle> options = getElementHandles("mat-option[role='option'] span");
        boolean valueFound = false;
        for (ElementHandle option : options) {
            if (option.innerText().equalsIgnoreCase(value)) {
                valueFound = true;
                option.click();
                break;
            }
        }
        if (!valueFound) {
            throw new RuntimeException("Unable to find the value: " + value + "in the angular dropdown.");
        }
    }

    public void settingDateOnCalendar(String dateToSet) {
        Calendar calendar = new Calendar();
        String[] distribution = dateToSet.split(" ");
        String currentMonthYearOnCalendar = gettingFieldValue(Calendar.MONTH_YEAR);
        if (!currentMonthYearOnCalendar.equalsIgnoreCase(distribution[0].substring(0, 3) + " " + distribution[2])) {
            clickOn(Calendar.MONTH_YEAR_ARROW);
            calendar.setYear(distribution[2]);
            calendar.setMonth(distribution[0]);
        }
        calendar.setDate(distribution[1].replace(",", ""));
    }

    public void sendingSpecialKeys(Field field, Class<?> pageClass, String value) {
        String fieldToFill = fieldToInteract(field, pageClass);
        page.waitForSelector(fieldToFill);
        sendingSpecialKeys(fieldToFill, value);
    }

    public void sendingSpecialKeys(String byField, String value) {
        ElementHandle element = getElementHandle(byField);
        sendingSpecialKeys(element, value);
    }

    public void sendingSpecialKeys(ElementHandle element, String value) {
        element.press(value);
    }

    public ElementHandle getElementHandle(String byField) {
        return page.querySelector(byField);
    }

    public void scrollingToTheBottomOfThePage() {
        page.evaluate("window.scrollTo(0, 360)");
    }

    public void scrollingToTheMiddleOfThePage() {
        page.evaluate("window.scrollTo(0, 180)");
    }

    public String gettingAttribute(String byField, String attributeName) {
        ElementHandle fieldElement = getElementHandle(byField);
        return gettingAttribute(fieldElement, attributeName);
    }

    public String gettingAttribute(ElementHandle fieldElement, String attributeName) {
        return fieldElement.getAttribute(attributeName);
    }

    /**
     * An enumeration representing different window handle classification types.
     */
    public enum WindowHandleClassificationType {
        TITLE_MATCH,
        URL_SUBSTRING
    }

    /**
     * An enumeration representing different field types.
     */
    public enum FieldType {
        TEXT_FIELD,
        DATE_FIELD,
        DATE_LABEL,
        DROPDOWN,
        CHECKBOX,
        RADIO,
        LABEL,
        TEXTAREA;

        /**
         * Resolves a FieldType based on a given field type string.
         *
         * @param fieldType The field type string.
         * @return FieldType The resolved FieldType.
         * @throws RuntimeException If an unsupported field type is provided.
         */
        public static FieldType resoluteFieldType(String fieldType) {
            return switch (fieldType.toLowerCase()) {
                case "text field" -> TEXT_FIELD;
                case "date field" -> DATE_FIELD;
                case "date label" -> DATE_LABEL;
                case "radio" -> RADIO;
                case "checkbox" -> CHECKBOX;
                case "dropdown" -> DROPDOWN;
                case "label" -> LABEL;
                case "textarea" -> TEXTAREA;
                default -> throw new RuntimeException("Unsupported field type: " + fieldType);
            };
        }

        /**
         * Resolves the field name associated with a FieldType.
         *
         * @param fieldType The FieldType.
         * @return String The resolved field name.
         */
        public static String resoluteFieldName(FieldType fieldType) {
            return switch (fieldType) {
                case TEXT_FIELD -> "text field";
                case DATE_FIELD -> "date field";
                case DATE_LABEL -> "date label";
                case RADIO -> "radio";
                case CHECKBOX -> "checkbox";
                case DROPDOWN -> "dropdown";
                case LABEL -> "label";
                case TEXTAREA -> "textarea";
            };
        }
    }
}
