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
package org.fenixedu.academic.dto.inquiries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.inquiries.InquiryGlobalComment;
import org.fenixedu.academic.domain.inquiries.InquiryResultComment;
import org.fenixedu.academic.domain.inquiries.ResultPersonCategory;

import org.apache.commons.beanutils.BeanComparator;

public class DepartmentUCResultsBean extends CoordinatorResultsBean {

    private static final long serialVersionUID = 1L;

    public DepartmentUCResultsBean(ExecutionCourse executionCourse, ExecutionDegree executionDegree, Person president,
            boolean backToResume) {
        super(executionCourse, executionDegree, president, backToResume);
    }

    public List<InquiryResultComment> getAllCourseComments() {
        List<InquiryResultComment> commentsMade = new ArrayList<InquiryResultComment>();
        for (InquiryGlobalComment globalComment : getExecutionCourse().getInquiryGlobalCommentsSet()) {
            commentsMade.addAll(globalComment.getInquiryResultCommentsSet());
        }
        Collections.sort(commentsMade, new BeanComparator("person.name"));
        return commentsMade;
    }

    @Override
    protected ResultPersonCategory getPersonCategory() {
        return ResultPersonCategory.DEPARTMENT_PRESIDENT;
    }
}
