package com.vaadin.flow.component.textfield.demo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableConsumer;

import static com.vaadin.flow.component.textfield.TextFieldVariant.LUMO_ALIGN_RIGHT;
import static com.vaadin.flow.component.textfield.TextFieldVariant.LUMO_SMALL;

class TimeoutFieldProvider {

    private final TextField timeoutField;
    private SerializableConsumer<Integer> commitTimeoutConsumer;

    TimeoutFieldProvider(SerializableConsumer<Integer> commitTimeoutConsumer) {
        this.commitTimeoutConsumer = commitTimeoutConsumer;

        timeoutField = new TextField();
        timeoutField.setTitle("Leave blank for default change event behavior.");
        timeoutField.addValueChangeListener(event -> this.onChange());
        timeoutField.setPrefixComponent(new Span("'lazy' timeout"));
        timeoutField.setSuffixComponent(new Span("msec"));
        timeoutField.setPattern("[0-9]*");
        timeoutField.setMaxLength(4);
        timeoutField.setPreventInvalidInput(true);
        timeoutField.addThemeVariants(LUMO_ALIGN_RIGHT, LUMO_SMALL);
        timeoutField.getStyle().set("margin-left", "1em");

        timeoutField.setValue("500");
        onChange();
    }

    private void onChange() {
        try {
            commitTimeoutConsumer.accept(new Integer(timeoutField.getValue()));
        } catch (NumberFormatException e) {
            commitTimeoutConsumer.accept(0);
        }
    }

    void onValueModeChange(ValueChangeMode valueChangeMode) {
        timeoutField.setEnabled(valueChangeMode.equals(ValueChangeMode.ON_CHANGE));
    }

    Component getField() {
        return timeoutField;
    }

}
