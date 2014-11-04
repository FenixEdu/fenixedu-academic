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

import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.exceptions.DomainException;

public class TeacherPastService extends TeacherPastService_Base {

    public TeacherPastService(TeacherService teacherService, Double credits) {
        super();
        if (teacherService == null || credits == null) {
            throw new DomainException("arguments can't be null");
        }
        if (countPastServices(teacherService.getTeacher()) >= 1) {
            throw new DomainException("There is already one Past Service, there can't be more");
        }
        setTeacherService(teacherService);
        setCredits(credits);
    }

    @Override
    public void setCredits(Double credits) {
        if (credits == null) {
            throw new DomainException("arguments can't be null");
        }
        super.setCredits(credits);
    }

    private int countPastServices(Teacher teacher) {
        int count = 0;
        for (TeacherService teacherService : teacher.getTeacherServicesSet()) {
            if (teacherService.getPastService() != null) {
                count++;
            }
        }
        return count;
    }

}
