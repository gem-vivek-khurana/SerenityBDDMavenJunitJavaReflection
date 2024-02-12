package com.SerenityBDD.state;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class VerifyStateOf extends PageObject {
    final Page page = Serenity.sessionVariableCalled("page");

    /**
     * Wait for the visibility of a web element identified by a field and page class.
     *
     * @param field     The field representing the element.
     * @param pageClass The class of the page where the element is located.
     * @throws IllegalAccessException If there is an issue accessing the field.
     */
    public void theVisibilityOf(Field field, Class<?> pageClass) throws IllegalAccessException {
        String elementForVisibility = (String) field.get(pageClass);
        theVisibilityOf(elementForVisibility);
    }

    /**
     * Wait for the visibility of a web element identified by a locator.
     *
     * @param field The locator of the element in CssSelector or xPath format.
     */
    public void theVisibilityOf(String field) {
        try {
            page.waitForSelector(field);
        } catch (Exception e) {
            throw new RuntimeException("Element " + field + " is not visible due to: " + e);
        }
    }

    /**
     * Check if a web element identified by a locator is visible on the page.
     *
     * @param field The locator of the element.
     * @return True if the element is visible, false otherwise.
     */
    public boolean elementIsVisible(String field) {
        return page.isVisible(field);
    }

    /**
     * Wait for the presence of a web element identified by a field and page class.
     *
     * @param field     The field representing the element.
     * @param pageClass The class of the page where the element is located.
     * @throws IllegalAccessException If there is an issue accessing the field.
     */
    public void thePresenceOf(Field field, Class<?> pageClass) throws IllegalAccessException {
        String elementForAvailability = (String) field.get(pageClass);
        thePresenceOf(elementForAvailability);
    }

    /**
     * Wait for the presence of a web element identified by a locator.
     *
     * @param field The locator of the element in String format of CssSelector or xPath.
     */
    public void thePresenceOf(String field) {
        page.waitForSelector(field);
    }

    public boolean visibilityOfElementInViewPort(Field field, Class<?> pageClass) throws IllegalAccessException {
        return visibilityOfElementInViewPort((String) field.get(pageClass));
    }

    /**
     * Check if a web element is visible within the viewport.
     *
     * @param field The selector to be checked for visibility in viewport.
     * @return True if the element is visible in the viewport, false otherwise.
     */
    public boolean visibilityOfElementInViewPort(String field) {
        ElementHandle element = page.querySelector(field);
        Map<String, ElementHandle> arg = new HashMap<>();
        arg.put("element", element);

        String script = "({ element }) => {\n" +
                "  const rect = element.getBoundingClientRect();\n" +
                "  return (\n" +
                "    rect.top >= 0 &&\n" +
                "    rect.left >= 0 &&\n" +
                "    rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&\n" +
                "    rect.right <= (window.innerWidth || document.documentElement.clientWidth)\n" +
                "  );\n" +
                "}";

        return (boolean) page.evaluate(script, arg);

    }

    /**
     * Wait for the invisibility of a web element identified by a field and page class.
     *
     * @param field     The field representing the element.
     * @param pageClass The class of the page where the element is located.
     */
    public void theInvisibilityOf(Field field, Class<?> pageClass) {
        String elementForInvisibility;
        try {
            elementForInvisibility = (String) field.get(pageClass);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        theInvisibilityOf(elementForInvisibility);
    }

    /**
     * Wait for the invisibility of a web element identified by a locator.
     *
     * @param field The locator of the element.
     */
    public void theInvisibilityOf(String field) {
        page.waitForSelector(field, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }

    /**
     * Check if a checkbox ElementHandle is selected.
     *
     * @param checkbox The checkbox ElementHandle to check.
     * @return True if the checkbox is selected, false otherwise.
     */
    public boolean checkboxIsSelected(ElementHandle checkbox) {
        if (page.evaluate("e => e.tagName", checkbox).toString().contains("input")) {
            return checkbox.isChecked();
        }
        return checkbox.querySelector("mat-checkbox-input").getAttribute("aria-checked").equals("true");
    }
}
