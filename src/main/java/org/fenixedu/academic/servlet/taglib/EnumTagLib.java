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
/*
 * Created on Apr 18, 2005
 */
package net.sourceforge.fenixedu.presentationTier.TagLib;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.Globals;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

public class EnumTagLib extends BodyTagSupport {

    private String id;

    private String enumeration;

    private String locale = Globals.LOCALE_KEY;

    private String bundle;

    private String excludedFields;

    private String includedFields;

    protected static MessageResources messages = MessageResources
            .getMessageResources("org.apache.struts.taglib.bean.LocalStrings");

    @Override
    public int doStartTag() throws JspException {
        Collection<LabelValueBean> labelValueBeans = getLabelValues();
        pageContext.getRequest().setAttribute(id, labelValueBeans);

        return super.doStartTag();
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setExcludedFields(String excludedFields) {
        this.excludedFields = excludedFields;
    }

    public void setIncludedFields(String includedFields) {
        this.includedFields = includedFields;
    }

    public Collection<LabelValueBean> getLabelValues() {

        return getLabelValuesLookup();
    }

    public Collection<LabelValueBean> getLabelValuesLookup() {
        try {
            Class clazz = Class.forName(this.enumeration);
            if (!clazz.isEnum()) {
                throw new IllegalArgumentException("Expected an enum type, got: " + this.enumeration);
            }

            if (this.excludedFields != null && this.includedFields != null) {
                throw new IllegalArgumentException("includedFields and excludedFields are both not empty");
            }

            final Method method = clazz.getMethod("values", (Class[]) null);
            final Object[] objects = (Object[]) method.invoke(clazz, (Object[]) null);
            final Collection<LabelValueBean> labelValueBeans = new ArrayList<LabelValueBean>(objects.length);

            final Set<String> excludedFieldsNames = new HashSet();
            fillSetWithSplittedString(excludedFieldsNames, this.excludedFields);
            final Set<String> includedFieldsNames = new HashSet();
            fillSetWithSplittedString(includedFieldsNames, this.includedFields);

            for (Object object : objects) {
                String value = object.toString();

                if ((!excludedFieldsNames.isEmpty() && !excludedFieldsNames.contains(value))
                        || (!includedFieldsNames.isEmpty() && includedFieldsNames.contains(value))
                        || (includedFieldsNames.isEmpty() && excludedFieldsNames.isEmpty())) {
                    String message = getMessage(clazz, value);
                    labelValueBeans.add(new LabelValueBean(message, value));
                }
            }

            return labelValueBeans;
        } catch (Exception e) {
            throw new IllegalArgumentException("Expected an enum type, got: " + this.enumeration);
        }

    }

    private String getMessage(Class clazz, String key) throws JspException {
        String message = null;

        message = getMessageFromBundle(clazz.getName() + "." + key);

        if (message == null) {
            message = getMessageFromBundle(clazz.getSimpleName() + "." + key);
        }

        if (message == null) {
            message = getMessageFromBundle(key);
        }

        return (message != null) ? message : key;
    }

    private String getMessageFromBundle(String key) throws JspException {
        return (TagUtils.getInstance().present(this.pageContext, this.bundle, this.locale, key)) ? TagUtils.getInstance()
                .message(this.pageContext, this.bundle, this.locale, key) : null;
    }

    private void fillSetWithSplittedString(final Set<String> fieldsNamesSet, String fieldNamesString) {
        if (fieldNamesString != null && fieldNamesString.length() > 0) {
            String[] fieldsNamesArray = fieldNamesString.split(",");
            for (String fieldName : fieldsNamesArray) {
                fieldsNamesSet.add(fieldName.trim());
            }
        }
    }

}
