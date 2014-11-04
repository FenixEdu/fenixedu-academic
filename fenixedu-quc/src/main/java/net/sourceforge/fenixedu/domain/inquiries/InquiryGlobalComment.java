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

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Person;

import org.fenixedu.bennu.core.domain.Bennu;

public class InquiryGlobalComment extends InquiryGlobalComment_Base {

    public InquiryGlobalComment(ExecutionCourse executionCourse, ExecutionDegree executionDegree) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setExecutionCourse(executionCourse);
        setExecutionDegree(executionDegree);
        setCommentOnTeacher(false);
    }

    public InquiryGlobalComment(Person teacher, ExecutionSemester executionSemester) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setTeacher(teacher);
        setExecutionSemester(executionSemester);
        setCommentOnTeacher(true);
    }

    public void delete() {
        for (; !getInquiryResultCommentsSet().isEmpty(); getInquiryResultCommentsSet().iterator().next().delete()) {
            ;
        }
        setExecutionCourse(null);
        setExecutionDegree(null);
        setExecutionSemester(null);
        setTeacher(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public static InquiryGlobalComment getInquiryGlobalComment(ExecutionCourse executionCourse, ExecutionDegree executionDegree) {
        for (InquiryGlobalComment globalComment : executionCourse.getInquiryGlobalCommentsSet()) {
            if (globalComment.getExecutionDegree() == executionDegree) {
                return globalComment;
            }
        }
        return null;
    }

    public static InquiryGlobalComment getInquiryGlobalComment(Person person, ExecutionSemester executionSemester) {
        for (final InquiryGlobalComment globalComment : person.getInquiryGlobalCommentsSet()) {
            if (globalComment.getExecutionSemester() == executionSemester) {
                return globalComment;
            }
        }
        return null;
    }

}
