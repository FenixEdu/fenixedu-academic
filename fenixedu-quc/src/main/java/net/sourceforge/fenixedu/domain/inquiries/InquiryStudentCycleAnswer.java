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
package org.fenixedu.academic.domain.inquiries;

import jvstm.cps.ConsistencyPredicate;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;

public class InquiryStudentCycleAnswer extends InquiryStudentCycleAnswer_Base {

    public InquiryStudentCycleAnswer(Registration registration) {
        super();
        setRegistration(registration);
    }

    public InquiryStudentCycleAnswer(PhdIndividualProgramProcess phdProcess) {
        super();
        setPhdProcess(phdProcess);
    }

    @ConsistencyPredicate
    public boolean checkHasRegistrationOrHasPhd() {
        return getRegistration() != null || getPhdProcess() != null;
    }

    public static boolean hasFirstTimeCycleInquiryToRespond(Student student) {
        for (Registration registration : student.getActiveRegistrations()) {
            if (!registration.getDegreeType().isEmpty() && registration.getInquiryStudentCycleAnswer() == null
                    && registration.isFirstTime()) {
                if (registration.getPhdIndividualProgramProcess() != null
                        && registration.getPhdIndividualProgramProcess().getInquiryStudentCycleAnswer() != null) {
                    return false;
                }
                return true;
            }
        }
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        for (final PhdIndividualProgramProcess phdProcess : student.getPerson().getPhdIndividualProgramProcessesSet()) {
            if (phdProcess.getInquiryStudentCycleAnswer() == null && student.isValidAndActivePhdProcess(phdProcess)) {
                if (phdProcess.getRegistration() != null) {
                    if (phdProcess.getRegistration().getInquiryStudentCycleAnswer() != null) {
                        return false;
                    } else {
                        if (currentExecutionYear.containsDate(phdProcess.getWhenStartedStudies())) {
                            return true;
                        }
                    }
                } else {
                    if (currentExecutionYear.containsDate(phdProcess.getWhenStartedStudies())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
