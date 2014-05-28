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
package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ExecutionDegreeListBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurricularCoursesForDegreeCurricularPlan implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        final ExecutionDegreeListBean executionDegreeListBean = (ExecutionDegreeListBean) source;
        final List<DegreeModule> result = new ArrayList<DegreeModule>();
        if (executionDegreeListBean.getDegree() != null && executionDegreeListBean.getDegreeCurricularPlan() != null) {

            if (executionDegreeListBean.getDegree().getDegreeCurricularPlansSet()
                    .contains(executionDegreeListBean.getDegreeCurricularPlan())) {
                if (executionDegreeListBean.getDegree().isBolonhaDegree()) {
                    result.addAll(executionDegreeListBean.getDegreeCurricularPlan().getDcpDegreeModules(CurricularCourse.class));
                } else {
                    result.addAll(executionDegreeListBean.getDegreeCurricularPlan().getCurricularCourses());
                }
            } else {
                executionDegreeListBean.setDegreeCurricularPlan(null);
                executionDegreeListBean.setCurricularCourse(null);
            }
        }

        Collections.sort(result, DegreeModule.COMPARATOR_BY_NAME);
        Collections.reverseOrder();
        return result;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
