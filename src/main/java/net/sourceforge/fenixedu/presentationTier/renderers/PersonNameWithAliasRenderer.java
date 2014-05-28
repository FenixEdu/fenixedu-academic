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
package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * This renderer shows a person's name like the PersonNameRenderer but it also
 * so the person's most import role. If the person is and external person then
 * the renderer allows ou to choose the message appearing instead of the role.
 * 
 * Example: John Doe (X12345), Jane Doe (External)
 * 
 * @author cfgi
 */
public class PersonNameWithAliasRenderer extends PersonNameRenderer {

    private String label;
    private String bundle;
    private boolean key;

    private String labelClass;
    private String labelStyle;

    public PersonNameWithAliasRenderer() {
        super();

        setKey(true);
    }

    public String getBundle() {
        return this.bundle;
    }

    /**
     * Chooses the bundle used when the label is interpreted as a resource
     * message.
     * 
     * @property
     */
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public boolean isKey() {
        return this.key;
    }

    /**
     * Indicates if the label is a resource message.
     * 
     * @property
     */
    public void setKey(boolean key) {
        this.key = key;
    }

    public String getLabel() {
        return this.label;
    }

    /**
     * The label to use when a person is external. If this is not defined then
     * no label is shown in that case. By default the value of this property is
     * treated as a key to a resource message.
     * 
     * @property
     */
    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabelClass() {
        return this.labelClass;
    }

    /**
     * The css class to apply to the label.
     * 
     * @property
     */
    public void setLabelClass(String labelClass) {
        this.labelClass = labelClass;
    }

    public String getLabelStyle() {
        return this.labelStyle;
    }

    /**
     * The css style to apply to the label.
     * 
     * @property
     */
    public void setLabelStyle(String labelStyle) {
        this.labelStyle = labelStyle;
    }

    @Override
    public HtmlComponent render(Object object, Class type) {
        Person person = (Person) object;

        if (person == null) {
            return super.render(object, type);
        }

        String name = person.getName();
        HtmlComponent component = super.render(name, String.class);

        if (person.hasExternalContract()) {
            return addLabel(person, component, getExternalLabel());
        } else {
            return addLabel(person, component, person.getUsername());
        }
    }

    private String getExternalLabel() {
        if (!isKey()) {
            return getLabel();
        } else {
            return RenderUtils.getResourceString(getBundle(), getLabel());
        }
    }

    private HtmlComponent addLabel(Person person, HtmlComponent component, String label) {
        if (label == null) {
            return component;
        } else {
            HtmlInlineContainer container = new HtmlInlineContainer();

            container.addChild(component);
            container.addChild(getStyledLabel(label));

            return container;
        }
    }

    private HtmlText getStyledLabel(String label) {
        HtmlText text = new HtmlText("(" + label + ")");

        text.setClasses(getLabelClass());
        text.setStyle(getLabelStyle());

        return text;
    }

}
