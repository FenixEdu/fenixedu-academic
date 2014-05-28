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
package net.sourceforge.fenixedu.domain.curricularRules;

import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

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

    @Deprecated
    public boolean hasPrecedenceDegreeModule() {
        return getPrecedenceDegreeModule() != null;
    }

    @Deprecated
    public boolean hasAcademicPeriod() {
        return getAcademicPeriod() != null;
    }

    @Deprecated
    public boolean hasCurricularPeriodOrder() {
        return getCurricularPeriodOrder() != null;
    }

}
