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
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.FileContent.EducationalResourceType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class EducationalResourceProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        List<EducationalResourceType> types = new ArrayList<EducationalResourceType>();

        for (EducationalResourceType type : EducationalResourceType.values()) {
            if (!type.equals(EducationalResourceType.PROJECT_SUBMISSION) && !type.equals(EducationalResourceType.SITE_CONTENT)) {
                types.add(type);
            }
        }

        Collections.sort(types, new Comparator<Enum>() {

            @Override
            public int compare(Enum o1, Enum o2) {
                return RenderUtils.getEnumString(o1).compareTo(RenderUtils.getEnumString(o2));
            }
        });

        return types;
    }

    @Override
    public Converter getConverter() {
        return new Converter() {

            @Override
            public Object convert(Class type, Object value) {
                List<EducationalResourceType> types = new ArrayList<EducationalResourceType>();
                String[] flatTypes = (String[]) value;
                for (String flatType : flatTypes) {
                    types.add(EducationalResourceType.valueOf(flatType));
                }
                return types;

            }

        };
    }

}
