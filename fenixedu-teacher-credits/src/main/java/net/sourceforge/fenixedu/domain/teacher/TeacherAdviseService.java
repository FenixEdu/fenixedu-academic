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

public class TeacherAdviseService extends TeacherAdviseService_Base {

    public TeacherAdviseService(TeacherService teacherService, Advise advise, Double percentage, RoleType roleType) {
        super();

        if (teacherService == null || advise == null || percentage == null) {
            throw new DomainException("arguments can't be null");
        }

        setTeacherService(teacherService);
        TeacherCreditsFillingCE.checkValidCreditsPeriod(getTeacherService().getExecutionPeriod(), roleType);
        checkPercentage(percentage);
        advise.checkPercentageCoherenceWithOtherAdvises(teacherService.getExecutionPeriod(), percentage.doubleValue());
        setPercentage(percentage);
        setAdvise(advise);
    }

    public void delete(RoleType roleType) {
        TeacherCreditsFillingCE.checkValidCreditsPeriod(getTeacherService().getExecutionPeriod(), roleType);
        setAdvise(null);
        setTeacherService(null);
        super.delete();
    }

    public void updatePercentage(Double percentage, RoleType roleType) {
        TeacherCreditsFillingCE.checkValidCreditsPeriod(getTeacherService().getExecutionPeriod(), roleType);
        checkPercentage(percentage);
        getAdvise().checkPercentageCoherenceWithOtherAdvises(getTeacherService().getExecutionPeriod(), percentage.doubleValue());
        setPercentage(percentage);
    }

    private void checkPercentage(Double percentage) {
        if (percentage == null || percentage > 100 || percentage < 0) {
            throw new DomainException("error.invalid.advise.service.percentage");
        }
    }

}
