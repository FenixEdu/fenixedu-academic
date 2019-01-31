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
package org.fenixedu.academic.domain.student;

import java.util.Comparator;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentDataByExecutionYear extends StudentDataByExecutionYear_Base {

    static final public Comparator<StudentDataByExecutionYear> COMPARATOR_BY_EXECUTION_YEAR =
            new Comparator<StudentDataByExecutionYear>() {
                @Override
                public int compare(StudentDataByExecutionYear o1, StudentDataByExecutionYear o2) {
                    return o1.getExecutionYear().compareTo(o2.getExecutionYear());
                }
            };

    private StudentDataByExecutionYear() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setWorkingStudent(false);
    }

    public StudentDataByExecutionYear(final Student student) {
        this();
        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();

        checkParameters(student, executionYear);
        checkConditions(student, executionYear);

        setStudent(student);
        setExecutionYear(executionYear);
    }

    public StudentDataByExecutionYear(final Student student, final ExecutionYear executionYear) {
        this();

        checkParameters(student, executionYear);
        checkConditions(student, executionYear);

        setStudent(student);
        setExecutionYear(executionYear);
    }

    private void checkParameters(final Student student, final ExecutionYear executionYear) {
        if (student == null) {
            throw new RuntimeException("error.StudentDataByExecutionYear.student.cannot.be.null");
        }
        if (executionYear == null) {
            throw new RuntimeException("error.StudentDataByExecutionYear.executionYear.cannot.be.null");
        }
    }

    private void checkConditions(final Student student, final ExecutionYear executionYear) {
        if (student.getStudentDataByExecutionYear(executionYear) != null) {
            throw new DomainException("error.StudentDataByExecutionYear.student.already.has.data.for.given.executionYear");
        }

    }

    public void delete() {
        setExecutionYear(null);
        setStudent(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

}
