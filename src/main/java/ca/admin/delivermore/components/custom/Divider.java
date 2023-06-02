package ca.admin.delivermore.components.custom;

import com.vaadin.flow.component.html.Span;

public class Divider extends Span {
    public Divider() {
        getStyle().set("background-color", "var(--lumo-primary-color)");
        getStyle().set("flex", "0 0 2px");
        getStyle().set("align-self", "stretch");
    }
}
