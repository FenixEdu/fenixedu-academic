/**
 * Copyright © 2002 Instituto Superior Técnico
 * <p>
 * This file is part of FenixEdu Academic.
 * <p>
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.renderers.providers.serviceRequests;

import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

import java.util.HashSet;
import java.util.Set;

public class CycleTypesByRegistrationToCreateDocumentRequestProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new EnumConverter();
    }

    @Override
    public Object provide(final Object source, final Object currentValue) {
        final DocumentRequestCreateBean documentRequestCreateBean = (DocumentRequestCreateBean) source;

        final Set<CycleType> result = new HashSet<>();
        documentRequestCreateBean.getRegistration().getStudentCurricularPlanStream()
                .flatMap(scp -> scp.getInternalCycleCurriculumGrops().stream())
                .forEach(gr -> result.add(gr.getCycleType()));

        return result;
    }
}
