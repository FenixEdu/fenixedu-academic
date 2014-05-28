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
/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult;
import net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentInquiriesCourseResultBean implements Serializable {

    private StudentInquiriesCourseResult studentInquiriesCourseResult;

    private List<StudentInquiriesTeachingResult> studentInquiriesTeachingResults =
            new ArrayList<StudentInquiriesTeachingResult>();

    public StudentInquiriesCourseResultBean(final StudentInquiriesCourseResult studentInquiriesCourseResult) {
        super();
        this.studentInquiriesCourseResult = studentInquiriesCourseResult;
    }

    public ExecutionDegree getExecutionDegree() {
        return getStudentInquiriesCourseResult().getExecutionDegree();
    }

    public StudentInquiriesCourseResult getStudentInquiriesCourseResult() {
        return studentInquiriesCourseResult;
    }

    public List<StudentInquiriesTeachingResult> getStudentInquiriesTeachingResults() {
        List<StudentInquiriesTeachingResult> result = new ArrayList<StudentInquiriesTeachingResult>();
        for (StudentInquiriesTeachingResult domainReference : studentInquiriesTeachingResults) {
            result.add(domainReference);
        }
        return result;
    }

    public void setStudentInquiriesTeachingResults(List<StudentInquiriesTeachingResult> studentInquiriesTeachingResults) {
        for (StudentInquiriesTeachingResult studentInquiriesTeachingResult : studentInquiriesTeachingResults) {
            this.studentInquiriesTeachingResults.add(studentInquiriesTeachingResult);
        }
    }

    public void addStudentInquiriesTeachingResult(StudentInquiriesTeachingResult studentInquiriesTeachingResult) {
        this.studentInquiriesTeachingResults.add(studentInquiriesTeachingResult);
    }

    public boolean isToImproove() {
        if (getStudentInquiriesCourseResult().isUnsatisfactory()) {
            return true;
        }

        for (StudentInquiriesTeachingResult studentInquiriesTeachingResult : getStudentInquiriesTeachingResults()) {
            if (studentInquiriesTeachingResult.isUnsatisfactory()) {
                return true;
            }
        }

        return false;
    }

}
