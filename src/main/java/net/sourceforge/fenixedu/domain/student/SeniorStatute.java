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
package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class SeniorStatute extends SeniorStatute_Base {

    private SeniorStatute() {
        super();
    }

    public SeniorStatute(Student student, Registration registration, StudentStatuteType statuteType,
            ExecutionSemester beginExecutionPeriod, ExecutionSemester endExecutionPeriod) {
        this();
        setBeginExecutionPeriod(beginExecutionPeriod);
        setEndExecutionPeriod(endExecutionPeriod);
        setStatuteType(statuteType);

        for (StudentStatute statute : student.getStudentStatutes()) {
            if (!statute.overlapsWith(this)) {
                continue;
            }
            if (!statute.hasSeniorStatuteForRegistration(getRegistration())) {
                continue;
            }

            throw new DomainException("error.studentStatute.alreadyExistsOneOverlapingStatute");

        }

        setStudent(student);

        if (registration == null) {
            throw new DomainException("error.studentStatute.mustDefineValidRegistrationMatchingSeniorStatute");
        }

        setRegistration(registration);
    }

    @Override
    public void delete() {
        checkRules();
        setBeginExecutionPeriod(null);
        setEndExecutionPeriod(null);
        setStudent(null);
        setRegistration(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Override
    public boolean hasSeniorStatuteForRegistration(Registration registration) {
        return (this.getRegistration() == registration);
    }

    @Deprecated
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

}
