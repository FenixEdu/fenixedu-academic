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
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

import org.fenixedu.bennu.core.domain.Bennu;

public class DegreeContext extends DegreeContext_Base {

    protected DegreeContext() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public DegreeContext(final DegreeCurricularPlan degreeCurricularPlan, final AcademicPeriod period,
            final AcademicInterval beginInterval, final AcademicInterval endInterval) {
        checkParameters(degreeCurricularPlan, period, beginInterval, endInterval);
        setBeginInterval(beginInterval);
        setEndInterval(endInterval);
        setPeriod(period);
    }

    private void checkParameters(DegreeCurricularPlan degreeCurricularPlan, AcademicPeriod period,
            AcademicInterval beginInterval, AcademicInterval endInterval) {
        if (degreeCurricularPlan == null || period == null || beginInterval == null) {
            throw new DomainException("error.degree.context.wrong.arguments");
        }
        // TODO verificar que beginInterval <= endInterval
        /*
         * if (endInterval != null && beginInterval.isA) { }
         */
    }

    @Deprecated
    public boolean hasPeriod() {
        return getPeriod() != null;
    }

    @Deprecated
    public boolean hasEndInterval() {
        return getEndInterval() != null;
    }

    @Deprecated
    public boolean hasDegreeCurricularPlan() {
        return getDegreeCurricularPlan() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasBeginInterval() {
        return getBeginInterval() != null;
    }

}
