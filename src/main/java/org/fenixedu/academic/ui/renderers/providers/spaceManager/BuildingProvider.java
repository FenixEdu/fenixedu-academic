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
package org.fenixedu.academic.ui.renderers.providers.spaceManager;

import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.dto.spaceManager.FindSpacesBean;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.SpaceClassification;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class BuildingProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        Set<Space> result = new HashSet<Space>();
        FindSpacesBean bean = (FindSpacesBean) source;
        Space campus = bean.getCampus();

        SpaceClassification building = SpaceClassification.getByName("Building");

        if (campus != null) {
            for (Space child : campus.getChildren()) {
                if (child.getClassification().equals(building)) {
                    result.add(child);
                }
            }
        }

        return result;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}
