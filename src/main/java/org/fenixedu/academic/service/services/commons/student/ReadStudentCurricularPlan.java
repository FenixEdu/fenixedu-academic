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
/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package org.fenixedu.academic.service.services.commons.student;

import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.dto.InfoStudentCurricularPlan;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadStudentCurricularPlan {

    @Atomic
    public static InfoStudentCurricularPlan run(final String studentCurricularPlanID) throws FenixServiceException {
        if (studentCurricularPlanID == null) {
            throw new FenixServiceException("Persistence layer error");
        }

        final StudentCurricularPlan studentCurricularPlan = FenixFramework.getDomainObject(studentCurricularPlanID);
        if (studentCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        return InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan);
    }

}