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
import java.util.List;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.DegreeCurricularPlanEquivalencePlan;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.ui.struts.action.coordinator.StudentSearchBean;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeCurricularPlanForStudentEquivalenciesProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final StudentSearchBean studentSearchBean = (StudentSearchBean) source;
        return studentSearchBean.getDegreeCurricularPlan() == studentSearchBean.getOldDegreeCurricularPlan() ? getAllDegreeCurricularPlans() : getDestinationsDegreeCurricularPlans(studentSearchBean
                .getOldDegreeCurricularPlan());
    }

    private List<DegreeCurricularPlan> getDestinationsDegreeCurricularPlans(DegreeCurricularPlan oldDegreeCurricularPlan) {
        final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
        for (final DegreeCurricularPlanEquivalencePlan degreeCurricularPlanEquivalencePlan : oldDegreeCurricularPlan
                .getTargetEquivalencePlans()) {
            result.add(degreeCurricularPlanEquivalencePlan.getDegreeCurricularPlan());
        }

        return result;

    }

    private List<DegreeCurricularPlan> getAllDegreeCurricularPlans() {
        return new ArrayList<DegreeCurricularPlan>(DegreeCurricularPlan.getDegreeCurricularPlans(DegreeType.oneOf(
                DegreeType::isBolonhaDegree, DegreeType::isBolonhaMasterDegree, DegreeType::isIntegratedMasterDegree)));
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
