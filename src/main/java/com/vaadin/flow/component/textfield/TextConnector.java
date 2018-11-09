/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.component.textfield;

import com.vaadin.flow.component.AttachNotifier;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.data.value.HasValueChangeMode;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.io.Serializable;

/**
 * Utility class for text field mixin web components to access JS connector
 */
class TextConnector<F extends HasValueChangeMode & HasElement & AttachNotifier>
        implements Serializable {

    private final F field;
    private LazyChange lazyChange;
    private Validation validation;

    TextConnector(F field) {
        this.field = field;
    }

    LazyChange getLazyChange() {
        if (lazyChange == null) {
            lazyChange = new LazyChange();
        }
        return lazyChange;
    }

    Validation getValidation() {
        if (validation == null) {
            validation = new Validation();
        }
        return validation;
    }

    class LazyChange implements Serializable {

        private LazyChange() {
            executeJavaScript("window.Vaadin.Flow.textConnector.initLazyChange($0)");
            setValueChangeTimeout(field.getValueChangeTimeout());
        }

        void updateLazyMode(ValueChangeMode valueChangeMode) {
            if (valueChangeMode == ValueChangeMode.LAZY) {
                callFunction("$lazyChange.enableLazyChange");
            } else {
                callFunction("$lazyChange.disableLazyChange");
            }
        }

        void setValueChangeTimeout(int timeout) {
            callFunction("$lazyChange.setLazyChangeTimeout", timeout);
        }

    }

    class Validation implements Serializable {

        private Validation() {
            executeJavaScript("window.Vaadin.Flow.textConnector.initValidation($0)");
        }

        void updateClientValidation(boolean requiredIndicatorVisible) {
            if (requiredIndicatorVisible) {
                callFunction("$validation.disableClientValidation");
            } else {
                callFunction("$validation.enableClientValidation");
            }
        }

    }

    private void executeJavaScript(String javaScript) {
        field.addAttachListener(attachEvent -> UI.getCurrent().getPage()
                .executeJavaScript(javaScript, field.getElement()));
    }

    private void callFunction(String functionName, Serializable... arguments) {
        field.getElement().callFunction(functionName, arguments);
    }

}
