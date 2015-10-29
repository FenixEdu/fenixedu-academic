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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.scholarship.utl.report;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.student.Student;

public class ReportStudentsUTLCandidatesForOneStudent extends ReportStudentsUTLCandidates {

    public ReportStudentsUTLCandidatesForOneStudent(final ExecutionYear forExecutionYear, final Student student) {
        super(forExecutionYear);

        getStudentLines(student);
    }

    @Override
    protected void getStudentLines(HSSFSheet sheet) {
        throw new RuntimeException();
    }

    protected void getStudentLines(final Student student) {
        StudentLine studentLine = new StudentLine();
        boolean filledWithSuccess = studentLine.fillWithStudent(forExecutionYear, student);

        if (filledWithSuccess) {
            correctStudentLines.add(studentLine);
        } else {
            erroneousStudentLines.add(studentLine);
        }
    }

}
