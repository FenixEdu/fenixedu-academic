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
package org.fenixedu.academic.domain.curricularRules;

import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;

public abstract class PrecedenceRule extends PrecedenceRule_Base {

    protected PrecedenceRule() {
        super();
    }

    @Override
    public boolean appliesToContext(final Context context) {
        return (super.appliesToContext(context) && appliesToPeriod(context));
    }

    protected boolean appliesToPeriod(final Context context) {
        return (hasNoCurricularPeriodOrder() || (this.getAcademicPeriod().equals(
                context.getCurricularPeriod().getAcademicPeriod()) && this.getCurricularPeriodOrder().equals(
                context.getCurricularPeriod().getChildOrder())));
    }

    protected boolean hasNoCurricularPeriodOrder() {
        return (this.getAcademicPeriod() == null || this.getCurricularPeriodOrder() == null || (this.getAcademicPeriod().equals(
                AcademicPeriod.SEMESTER) && this.getCurricularPeriodOrder().equals(0)));
    }

    @Override
    protected void removeOwnParameters() {
        setPrecedenceDegreeModule(null);
    }

}
