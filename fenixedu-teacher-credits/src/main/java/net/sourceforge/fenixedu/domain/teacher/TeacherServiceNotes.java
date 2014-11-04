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
package org.fenixedu.academic.domain.teacher;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.time.calendarStructure.TeacherCreditsFillingCE;

public class TeacherServiceNotes extends TeacherServiceNotes_Base {

    public TeacherServiceNotes(TeacherService teacherService) {
        super();
        if (teacherService == null) {
            throw new DomainException("arguments can't be null");
        }
        if (teacherService.getTeacherServiceNotes() != null) {
            throw new DomainException("There is already one Teacher Service Notes, there can't be more");
        }
        setTeacherService(teacherService);
    }

    public void editNotes(String managementFunctionNote, String serviceExemptionNote, String otherNote,
            String masterDegreeTeachingNote, String functionsAccumulation, String thesisNote, RoleType roleType) {

        TeacherCreditsFillingCE.checkValidCreditsPeriod(getTeacherService().getExecutionPeriod(), roleType);

        if (managementFunctionNote != null) {
            setManagementFunctionNotes(managementFunctionNote);
        }
        if (serviceExemptionNote != null) {
            setServiceExemptionNotes(serviceExemptionNote);
        }
        if (masterDegreeTeachingNote != null) {
            setMasterDegreeTeachingNotes(masterDegreeTeachingNote);
        }
        if (otherNote != null) {
            setOthersNotes(otherNote);
        }
        if (functionsAccumulation != null) {
            setFunctionsAccumulation(functionsAccumulation);
        }
        if (thesisNote != null) {
            setThesisNote(thesisNote);
        }
    }

}
