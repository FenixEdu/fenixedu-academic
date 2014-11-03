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
package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.student.Registration;

abstract class PrescriptionRuleGenericMoment extends AbstractPrescriptionRule {

    public PrescriptionRuleGenericMoment() {
    }

    @Override
    public boolean isPrescript(Registration registration, BigDecimal ects, int numberOfEntriesStudentInSecretary,
            ExecutionYear executionYear) {
        return ects.compareTo(getMinimumEcts()) < 0
                && numberOfEntriesStudentInSecretary == getNumberOfEntriesStudentInSecretary()
                && isForAdmission(registration.getIngression()) && registration.isFullRegime(executionYear);
    }

    protected boolean isForAdmission(Ingression ingression) {
        return ingression != null
                && (ingression.equals(Ingression.CNA01) || ingression.equals(Ingression.CNA02)
                        || ingression.equals(Ingression.CNA03) || ingression.equals(Ingression.CNA04)
                        || ingression.equals(Ingression.CNA05) || ingression.equals(Ingression.CNA06) || ingression
                            .equals(Ingression.CNA07));
    }

}
