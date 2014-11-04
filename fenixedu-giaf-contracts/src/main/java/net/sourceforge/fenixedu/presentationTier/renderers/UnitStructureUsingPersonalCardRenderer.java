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
package org.fenixedu.academic.ui.renderers;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.fenixedu.academic.domain.Person;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.contexts.PresentationContext;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;
import pt.ist.fenixWebFramework.renderers.utils.RenderMode;

public class UnitStructureUsingPersonalCardRenderer extends UnitStructureRenderer {

    private final Map<String, String> properties = new HashMap<String, String>();

    public void setCardProperty(String name, String value) {
        properties.put(name, value);
    }

    public String getCardProperty(String name) {
        return properties.get(name);
    }

    private Properties getPropertyMap() {
        Properties properties = new Properties();
        for (String key : this.properties.keySet()) {
            properties.put(key, this.properties.get(key));
        }
        return properties;
    }

    @Override
    protected HtmlComponent generatePerson(Person person) {
        PresentationContext newContext = getContext().createSubContext(getContext().getMetaObject());
        newContext.setProperties(getPropertyMap());
        newContext.setRenderMode(RenderMode.OUTPUT);

        return RenderKit.getInstance().renderUsing(new PersonalCardRenderer(), newContext, person, Person.class);
    }
}
