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
package net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

import org.joda.time.YearMonthDay;

public class PhdRegistrationConclusionBean extends RegistrationConclusionBean {

    static private final long serialVersionUID = 1L;

    public PhdRegistrationConclusionBean(Registration registration) {
        super(registration);
        setCycleCurriculumGroup(registration.getLastStudentCurricularPlan().getCycle(CycleType.THIRD_CYCLE));
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
        return isConclusionProcessed() ? getCycleCurriculumGroup().getConclusionDate() : null;
    }

    @Override
    public Integer getFinalAverage() {
        return isConclusionProcessed() ? getCycleCurriculumGroup().getFinalAverage() : null;
    }

}
