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
package net.sourceforge.fenixedu.presentationTier.TagLib;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.struts.Globals;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.taglib.html.SelectTag;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

/**
 * @author Fernanda Quitério & Tania Pousão
 */
public class OptionsTag extends TagSupport {

    /**
     * The message resources for this package.
     */
    protected static MessageResources messages = MessageResources
            .getMessageResources("org.apache.struts.taglib.html.LocalStrings");

    /**
     * The name of the collection containing beans that have properties to
     * provide both the values and the labels (identified by the <code>property</code> and <code>labelProperty</code> attributes).
     */
    protected String collection = null;

    public String getCollection() {
        return (this.collection);
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    /**
     * The name of the bean containing the labels collection.
     */
    protected String labelName = null;

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    /**
     * The bean property containing the labels collection.
     */
    protected String labelProperty = null;

    public String getLabelProperty() {
        return labelProperty;
    }

    public void setLabelProperty(String labelProperty) {
        this.labelProperty = labelProperty;
    }

    /**
     * The name of the bean containing the values collection.
     */
    protected String name = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The name of the property to use to build the values collection.
     */
    protected String property = null;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * The style associated with this tag.
     */
    private String style = null;

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * The named style class associated with this tag.
     */
    private String styleClass = null;

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /* Acrescentado por Fernanda Quitério & Tania Pousão */
    /**
     * The servlet context attribute key for our resources.
     */
    protected String bundle = Globals.MESSAGES_KEY;

    public String getBundle() {
        return (this.bundle);
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    /**
     * The session scope key under which our Locale is stored.
     */
    protected String localeKey = Globals.LOCALE_KEY;

    public String getLocale() {
        return (this.localeKey);
    }

    public void setLocale(String localeKey) {
        this.localeKey = localeKey;
    }

    /* até aqui */

    /**
     * Process the start of this tag.
     */

    @Override
    public int doStartTag() {
        return SKIP_BODY;
    }

    /**
     * Process the end of this tag.
     * 
     * @exception JspException
     *                if a JSP exception has occurred
     */
    @Override
    public int doEndTag() throws JspException {

        // Acquire the select tag we are associated with
        SelectTag selectTag = (SelectTag) pageContext.getAttribute(Constants.SELECT_KEY);
        if (selectTag == null) {
            throw new JspException(messages.getMessage("optionsTag.select"));
        }
        StringBuilder sb = new StringBuilder();

        // If a collection was specified, use that mode to render options
        if (collection != null) {
            Iterator collIterator = getIterator(collection, null);
            while (collIterator.hasNext()) {

                Object bean = collIterator.next();
                Object value = null;
                Object label = null;

                try {
                    value = PropertyUtils.getProperty(bean, property);
                    if (value == null) {
                        value = "";
                    }
                } catch (IllegalAccessException e) {
                    throw new JspException(messages.getMessage("getter.access", property, collection));
                } catch (InvocationTargetException e) {
                    Throwable t = e.getTargetException();
                    throw new JspException(messages.getMessage("getter.result", property, t.toString()));
                } catch (NoSuchMethodException e) {
                    throw new JspException(messages.getMessage("getter.method", property, collection));
                }

                try {
                    if (labelProperty != null) {
                        label = PropertyUtils.getProperty(bean, labelProperty);
                    } else {
                        label = value;
                    }
                    if (label == null) {
                        label = "";
                    }
                } catch (IllegalAccessException e) {
                    throw new JspException(messages.getMessage("getter.access", labelProperty, collection));
                } catch (InvocationTargetException e) {
                    Throwable t = e.getTargetException();
                    throw new JspException(messages.getMessage("getter.result", labelProperty, t.toString()));
                } catch (NoSuchMethodException e) {
                    throw new JspException(messages.getMessage("getter.method", labelProperty, collection));
                }

                String stringValue = value.toString();
                addOption(sb, stringValue, label.toString(), selectTag.isMatched(stringValue));

            }

        }

        // Otherwise, use the separate iterators mode to render options
        else {

            // Construct iterators for the values and labels collections
            Iterator valuesIterator = getIterator(name, property);
            Iterator labelsIterator = null;
            if ((labelName == null) && (labelProperty == null)) {
                labelsIterator = getIterator(name, property); // Same coll.
            } else {
                labelsIterator = getIterator(labelName, labelProperty);
            }

            // Render the options tags for each element of the values coll.
            while (valuesIterator.hasNext()) {
                String value = valuesIterator.next().toString();
                String label = value;
                if (labelsIterator.hasNext()) {
                    label = labelsIterator.next().toString();
                }
                addOption(sb, value, label, selectTag.isMatched(value));
            }
        }

        // Render this element to our writer
        ResponseUtils.write(pageContext, sb.toString());

        // Evaluate the remainder of this page
        return EVAL_PAGE;

    }

    /**
     * Release any acquired resources.
     */
    @Override
    public void release() {

        super.release();
        collection = null;
        labelName = null;
        labelProperty = null;
        name = null;
        property = null;
        style = null;
        styleClass = null;

    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Add an option element to the specified StringBuilder based on the
     * specified parameters.
     * 
     * @param sb
     *            StringBuilder accumulating our results
     * @param value
     *            Value to be returned to the server for this option
     * @param label
     *            Value to be shown to the user for this option
     * @param matched
     *            Should this value be marked as selected?
     */
    protected void addOption(StringBuilder sb, String value, String label, boolean matched) throws JspException {

        sb.append("<option value=\"");
        sb.append(value);
        sb.append("\"");
        if (matched) {
            sb.append(" selected=\"selected\"");
        }
        if (style != null) {
            sb.append(" style=\"");
            sb.append(style);
            sb.append("\"");
        }
        if (styleClass != null) {
            sb.append(" class=\"");
            sb.append(styleClass);
            sb.append("\"");
        }
        sb.append(">");

        /* acrescentado por Nanda & Tania */
        // String locale = Action.LOCALE_KEY;
        // String bundle = Action.MESSAGES_KEY;
        String key = label;

        // Retrieve the message string we are looking for
        String message = RequestUtils.message(pageContext, this.bundle, this.localeKey, key, null);

        if (message == null) {
            JspException e = new JspException(messages.getMessage("message.message", key));
            RequestUtils.saveException(pageContext, e);
            throw e;
        }
        label = message;
        /* ate aqui!! */

        sb.append(ResponseUtils.filter(label));
        sb.append("</option>\r\n");

    }

    /**
     * Return an iterator for the option labels or values, based on our
     * configured properties.
     * 
     * @param name
     *            Name of the bean attribute (if any)
     * @param property
     *            Name of the bean property (if any)
     * 
     * @exception JspException
     *                if an error occurs
     */
    protected Iterator getIterator(String name, String property) throws JspException {

        // Identify the bean containing our collection
        String beanName = name;
        if (beanName == null) {
            beanName = Constants.BEAN_KEY;
        }
        Object bean = pageContext.findAttribute(beanName);
        if (bean == null) {
            throw new JspException(messages.getMessage("getter.bean", beanName));
        }

        // Identify the collection itself
        Object collection = bean;
        if (property != null) {
            try {
                collection = PropertyUtils.getProperty(bean, property);
                if (collection == null) {
                    throw new JspException(messages.getMessage("getter.property", property));
                }
            } catch (IllegalAccessException e) {
                throw new JspException(messages.getMessage("getter.access", property, name));
            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();
                throw new JspException(messages.getMessage("getter.result", property, t.toString()));
            } catch (NoSuchMethodException e) {
                throw new JspException(messages.getMessage("getter.method", property, name));
            }
        }

        // Construct and return an appropriate iterator
        if (collection.getClass().isArray()) {
            collection = Arrays.asList((Object[]) collection);
        }
        if (collection instanceof Collection) {
            return (((Collection) collection).iterator());
        } else if (collection instanceof Iterator) {
            return ((Iterator) collection);
        } else if (collection instanceof Map) {
            return (((Map) collection).entrySet().iterator());
        } else if (collection instanceof Enumeration) {
            return (IteratorUtils.asIterator((Enumeration) collection));
        } else {
            throw new JspException(messages.getMessage("optionsTag.iterator", collection.toString()));
        }

    }

}