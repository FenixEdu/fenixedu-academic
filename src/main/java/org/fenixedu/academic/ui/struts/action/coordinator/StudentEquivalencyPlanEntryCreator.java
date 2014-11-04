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
package org.fenixedu.academic.ui.struts.action.coordinator;

import org.fenixedu.academic.domain.DegreeCurricularPlanEquivalencePlan;
import org.fenixedu.academic.domain.StudentCurricularPlanEquivalencePlan;

public class StudentEquivalencyPlanEntryCreator extends EquivalencePlanEntryCreator {

    private final DegreeCurricularPlanEquivalencePlan degreeCurricularPlanEquivalencePlan;

    public StudentEquivalencyPlanEntryCreator(final StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan,
            final DegreeCurricularPlanEquivalencePlan degreeCurricularPlanEquivalencePlan) {
        super(studentCurricularPlanEquivalencePlan);
        this.degreeCurricularPlanEquivalencePlan = degreeCurricularPlanEquivalencePlan;
    }

    public DegreeCurricularPlanEquivalencePlan getDegreeCurricularPlanEquivalencePlan() {
        return degreeCurricularPlanEquivalencePlan;
    }

    public StudentCurricularPlanEquivalencePlan getStudentCurricularPlanEquivalencePlan() {
        return (StudentCurricularPlanEquivalencePlan) getEquivalencePlan();
    }

}
