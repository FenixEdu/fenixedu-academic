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
/*
 * Created on Feb 18, 2005
 *
 */
package org.fenixedu.academic.service.services.manager;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.InfoStudentCurricularPlan;
import pt.ist.fenixframework.Atomic;

/**
 * @author Luis Cruz
 * 
 */
public class ReadStudentCurricularInformation {

    @Atomic
    public static List run(final Integer studentNumber, final DegreeType degreeType) {
        final List<InfoStudentCurricularPlan> infoStudentCurricularPlans = new ArrayList<InfoStudentCurricularPlan>();

        for (Registration registration : Registration.readByNumberAndDegreeType(studentNumber, degreeType)) {
            for (StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
                infoStudentCurricularPlans.add(constructInfoStudentCurricularPlan(studentCurricularPlan));
            }
        }

        return infoStudentCurricularPlans;
    }

    protected static InfoStudentCurricularPlan constructInfoStudentCurricularPlan(
            final StudentCurricularPlan studentCurricularPlan) {
        return InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan);
    }

}