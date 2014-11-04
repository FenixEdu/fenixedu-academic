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
package org.fenixedu.academic.ui.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.dto.resourceAllocationManager.ContextSelectionBean;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurricularYearForExecutionDegreeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        ContextSelectionBean contextSelectionBean = (ContextSelectionBean) source;
        List<CurricularYear> result = new ArrayList<CurricularYear>();
        ExecutionDegree executionDegree = contextSelectionBean.getExecutionDegree();

        if (executionDegree != null) {
            Integer index = executionDegree.getDegreeCurricularPlan().getDegreeDuration();

            result.addAll(Bennu.getInstance().getCurricularYearsSet());
            Collections.sort(result, CurricularYear.CURRICULAR_YEAR_COMPARATORY_BY_YEAR);
            return index == null ? result : result.subList(0, index);
        }

        return result;
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
