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
package net.sourceforge.fenixedu.presentationTier.renderers.providers.pedagogicalCouncil;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.commons.delegates.DelegateSearchBean;
import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.delegates.DelegateBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreesGivenDegreeTypeForDelegatesManagement implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        ExecutionYear executionYear;
        DegreeType degreeType;

        if (source instanceof DelegateBean) {
            executionYear = ((DelegateBean) source).getExecutionYear();
            degreeType = ((DelegateBean) source).getDegreeType();
        } else {
            executionYear = ((DelegateSearchBean) source).getExecutionYear();
            degreeType = ((DelegateSearchBean) source).getDegreeType();
        }

        List<Degree> degrees = Degree.readAllByDegreeType(degreeType);
        List<Degree> result = Degree.readAllByDegreeType(degreeType);

        if (executionYear == null) {
            executionYear = ExecutionYear.readCurrentExecutionYear();
        }

        for (Degree degree : degrees) {
            if (degree.getDegreeCurricularPlansForYear(executionYear).isEmpty()) {
                result.remove(degree);
            }
        }
        return result;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}
