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
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.ContextSelectionBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

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
