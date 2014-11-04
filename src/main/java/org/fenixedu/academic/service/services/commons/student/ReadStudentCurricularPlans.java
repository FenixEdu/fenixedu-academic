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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.InfoStudentCurricularPlan;
import org.fenixedu.academic.service.services.ExcepcaoInexistente;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;

import pt.ist.fenixframework.Atomic;

public class ReadStudentCurricularPlans {

    @Atomic
    public static List run(Integer studentNumber, DegreeType degreeType) throws ExcepcaoInexistente, FenixServiceException {

        Registration registration = Registration.readStudentByNumberAndDegreeType(studentNumber, degreeType);
        if (registration == null) {
            throw new NonExistingServiceException("student does not exist");
        }
        Collection<StudentCurricularPlan> studentCurricularPlans = registration.getStudentCurricularPlansSet();

        if ((studentCurricularPlans == null) || (studentCurricularPlans.size() == 0)) {
            throw new NonExistingServiceException();
        }

        Iterator iterator = studentCurricularPlans.iterator();
        List result = new ArrayList();

        // FIXME: There's a problem with data of the Graduation Students
        // For now only Master Degree Students can view their Curriculum

        while (iterator.hasNext()) {
            StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iterator.next();

            result.add(InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan));
        }

        if (result.size() == 0) {
            throw new NonExistingServiceException();
        }

        return result;
    }
}