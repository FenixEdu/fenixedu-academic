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
package org.fenixedu.academic.ui.struts.action.phd.academicAdminOffice;

import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.joda.time.YearMonthDay;

public class PhdRegistrationConclusionBean extends RegistrationConclusionBean {

    static private final long serialVersionUID = 1L;

    public PhdRegistrationConclusionBean(Registration registration) {
        super(registration);
        setCurriculumGroup(registration.getLastStudentCurricularPlan().getCycle(CycleType.THIRD_CYCLE));
    }

    public RegistrationStateType getActiveStateType() {
        return getRegistration().getActiveStateType();
    }

    public YearMonthDay getStartDate() {
        return getRegistration().getStartDate();
    }

    public String getDegreeNameWithDescription() {
        return getRegistration().getDegreeNameWithDescription();
    }

    @Override
    public YearMonthDay getConclusionDate() {
        return isConclusionProcessed() ? getCurriculumGroup().getConclusionDate() : null;
    }

    @Override
    public Grade getFinalGrade() {
        return isConclusionProcessed() ? getCurriculumGroup().getFinalGrade() : Grade.createEmptyGrade();
    }

}
