/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.renderers;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.ui.renderers.converters.PersonUsernameConverter;

import pt.ist.fenixWebFramework.renderers.StringInputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlFormComponent;

import com.google.common.base.Predicate;

public class PersonUsernameStringInputRenderer extends StringInputRenderer {

    @Override
    protected HtmlComponent createTextField(Object object, Class type) {

        Person person = (Person) object;
        String username = (person != null) ? person.getUsername() : null;

        final HtmlComponent container = super.createTextField(username, type);
        final HtmlFormComponent formComponent = (HtmlFormComponent) container.getChild(new Predicate<HtmlComponent>() {
            @Override
            public boolean apply(HtmlComponent input) {
                return input instanceof HtmlFormComponent;
            }
        });
        formComponent.setConverter(new PersonUsernameConverter());

        return formComponent;
    }
}
