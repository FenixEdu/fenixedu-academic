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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.util.search.StudentSearchBean;
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
        final Set<DegreeType> degreeTypes = new HashSet<DegreeType>();
        degreeTypes.add(DegreeType.BOLONHA_DEGREE);
        degreeTypes.add(DegreeType.BOLONHA_MASTER_DEGREE);
        degreeTypes.add(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
        return new ArrayList<DegreeCurricularPlan>(DegreeCurricularPlan.getDegreeCurricularPlans(degreeTypes));
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
