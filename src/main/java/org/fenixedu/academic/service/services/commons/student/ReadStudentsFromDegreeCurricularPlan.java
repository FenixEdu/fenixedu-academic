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

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.dto.InfoStudentCurricularPlan;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadStudentsFromDegreeCurricularPlan {

    protected List run(String degreeCurricularPlanID) throws FenixServiceException {
        // Read the Students
        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);

        Collection students = degreeCurricularPlan.getStudentCurricularPlansSet();

        if ((students == null) || (students.isEmpty())) {
            throw new NonExistingServiceException();
        }

        return (List) CollectionUtils.collect(students, new Transformer() {
            @Override
            public Object transform(Object arg0) {
                StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) arg0;
                return InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan);
            }

        });
    }

    // Service Invokers migrated from Berserk

    private static final ReadStudentsFromDegreeCurricularPlan serviceInstance = new ReadStudentsFromDegreeCurricularPlan();

    @Atomic
    public static List runReadStudentsFromDegreeCurricularPlan(String degreeCurricularPlanID) throws FenixServiceException,
            NotAuthorizedException {
        return serviceInstance.run(degreeCurricularPlanID);
    }

}