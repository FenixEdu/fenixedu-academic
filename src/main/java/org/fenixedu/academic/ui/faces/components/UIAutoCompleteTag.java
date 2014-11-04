/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.jsf.components;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

import net.sourceforge.fenixedu.presentationTier.jsf.components.util.JsfTagUtils;

public class UIAutoCompleteTag extends UIComponentTag {

    private static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.UIAutoComplete";

    private String inputTextArgName;

    private String labelField;

    private String valueField;

    private String serviceName;

    private String serviceArgs;

    private String autoCompleteStyleClass;

    private String textFieldStyleClass;

    private String value;

    private String className;

    private String required;

    private String autoCompleteItemsStyleClass;

    private String size;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAutoCompleteItemsStyleClass() {
        return autoCompleteItemsStyleClass;
    }

    public void setAutoCompleteItemsStyleClass(String autoCompleteItemsStyleClass) {
        this.autoCompleteItemsStyleClass = autoCompleteItemsStyleClass;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAutoCompleteStyleClass() {
        return autoCompleteStyleClass;
    }

    public void setAutoCompleteStyleClass(String autoCompleteStyleClass) {
        this.autoCompleteStyleClass = autoCompleteStyleClass;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getInputTextArgName() {
        return inputTextArgName;
    }

    public void setInputTextArgName(String inputTextArgName) {
        this.inputTextArgName = inputTextArgName;
    }

    public String getLabelField() {
        return labelField;
    }

    public void setLabelField(String labelField) {
        this.labelField = labelField;
    }

    public String getServiceArgs() {
        return serviceArgs;
    }

    public void setServiceArgs(String serviceArgs) {
        this.serviceArgs = serviceArgs;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getTextFieldStyleClass() {
        return textFieldStyleClass;
    }

    public void setTextFieldStyleClass(String textFieldStyleClass) {
        this.textFieldStyleClass = textFieldStyleClass;
    }

    public String getValueField() {
        return valueField;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }

    @Override
    public String getComponentType() {

        return COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return null;
    }

    @Override
    protected void setProperties(UIComponent component) {

        super.setProperties(component);

        JsfTagUtils.setString(component, "autoCompleteStyleClass", this.autoCompleteStyleClass);
        JsfTagUtils.setString(component, "autoCompleteItemsStyleClass", this.autoCompleteItemsStyleClass);
        JsfTagUtils.setString(component, "className", this.className);
        JsfTagUtils.setString(component, "inputTextArgName", this.inputTextArgName);
        JsfTagUtils.setString(component, "labelField", this.labelField);
        JsfTagUtils.setString(component, "valueField", this.valueField);
        JsfTagUtils.setString(component, "serviceArgs", this.serviceArgs);
        JsfTagUtils.setString(component, "serviceName", this.serviceName);
        JsfTagUtils.setString(component, "textFieldStyleClass", this.textFieldStyleClass);
        JsfTagUtils.setInteger(component, "value", this.value);
        JsfTagUtils.setBoolean(component, "required", this.required);
        JsfTagUtils.setInteger(component, "size", this.size);

    }

    @Override
    public void release() {
        super.release();

        this.value = null;
        this.required = null;
        this.serviceArgs = null;
        this.serviceName = null;
        this.textFieldStyleClass = null;
        this.valueField = null;
        this.labelField = null;
        this.inputTextArgName = null;
        this.className = null;
        this.autoCompleteStyleClass = null;

    }

}
