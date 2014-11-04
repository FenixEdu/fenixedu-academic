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
/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.converters;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.enrolment.EnroledCurriculumModuleWrapper;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import pt.ist.fenixWebFramework.renderers.components.converters.ConversionException;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurriculumModuleEnroledWrapperConverter extends Converter {

    @Override
    public Object convert(Class type, Object value) {
        if (value == null) {
            return null;
        }

        final pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter converter =
                new pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter();
        final List<EnroledCurriculumModuleWrapper> result = new ArrayList<EnroledCurriculumModuleWrapper>();
        final String[] values = (String[]) value;
        for (String key : values) {
            String[] parts = key.split(",");
            if (parts.length < 2) {
                throw new ConversionException("invalid key format: " + key);
            }

            final CurriculumModule curriculumModule = (CurriculumModule) converter.convert(type, parts[0]);
            final ExecutionSemester executionSemester = (ExecutionSemester) converter.convert(type, parts[1]);
            result.add(new EnroledCurriculumModuleWrapper(curriculumModule, executionSemester));
        }

        return result;
    }
}