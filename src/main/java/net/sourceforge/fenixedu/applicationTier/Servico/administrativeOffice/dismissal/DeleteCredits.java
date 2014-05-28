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
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.dismissal;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.Credits;
import pt.ist.fenixframework.Atomic;

public class DeleteCredits {

    @Atomic
    public static void run(StudentCurricularPlan studentCurricularPlan, String[] creditsIDs) throws FenixServiceException {
        for (String creditsID : creditsIDs) {
            Credits credits = getCreditsByID(studentCurricularPlan, creditsID);
            if (credits == null) {
                throw new FenixServiceException();
            }
            credits.delete();
        }
    }

    private static Credits getCreditsByID(StudentCurricularPlan studentCurricularPlan, String creditsID) {
        for (Credits credits : studentCurricularPlan.getCreditsSet()) {
            if (credits.getExternalId().equals(creditsID)) {
                return credits;
            }
        }
        return null;
    }

}
