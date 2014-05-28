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
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class RegistrationPeriodInDegreeCurricularPlan extends RegistrationPeriodInDegreeCurricularPlan_Base {

    private RegistrationPeriodInDegreeCurricularPlan() {
        super();
    }

    public RegistrationPeriodInDegreeCurricularPlan(final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionYear executionYear, final DateTime startDate, final DateTime endDate) {
        super();
        init(degreeCurricularPlan, executionYear, startDate, endDate);
    }

    protected void init(DegreeCurricularPlan degreeCurricularPlan, ExecutionYear executionYear, DateTime startDate,
            DateTime endDate) {
        checkRuleToCreate(degreeCurricularPlan, executionYear);
        super.init(degreeCurricularPlan, executionYear.getFirstExecutionPeriod(), startDate, endDate);

    }

    private void checkRuleToCreate(final DegreeCurricularPlan degreeCurricularPlan, final ExecutionYear executionYear) {
        if (degreeCurricularPlan.hasRegistrationPeriodFor(executionYear)) {
            throw new DomainException(
                    "error.RegistrationPeriodInDegreeCurricularPlan.degree.curricular.plan.already.contains.registration.period.for.execution.year");
        }

    }

    public ExecutionYear getExecutionYear() {
        return getExecutionPeriod().getExecutionYear();
    }

}
