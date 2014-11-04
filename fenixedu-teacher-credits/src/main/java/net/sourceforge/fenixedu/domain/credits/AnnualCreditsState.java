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
package org.fenixedu.academic.domain.credits;

import org.fenixedu.academic.domain.ExecutionYear;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

public class AnnualCreditsState extends AnnualCreditsState_Base {

    public AnnualCreditsState(ExecutionYear executionYear) {
        super();
        setExecutionYear(executionYear);
        setOrientationsCalculationDate(new LocalDate(executionYear.getBeginCivilYear(), 12, 31));
        setIsOrientationsCalculated(false);
        setIsFinalCreditsCalculated(false);
        setIsCreditsClosed(false);
        setRootDomainObject(Bennu.getInstance());
    }

    @Atomic
    public static AnnualCreditsState getAnnualCreditsState(ExecutionYear executionYear) {
        AnnualCreditsState annualCreditsState = executionYear.getAnnualCreditsState();
        if (annualCreditsState == null) {
            annualCreditsState = new AnnualCreditsState(executionYear);
        }
        return annualCreditsState;
    }

}
