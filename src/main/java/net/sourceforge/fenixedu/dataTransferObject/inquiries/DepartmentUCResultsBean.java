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
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGlobalComment;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;

import org.apache.commons.beanutils.BeanComparator;

public class DepartmentUCResultsBean extends CoordinatorResultsBean {

    private static final long serialVersionUID = 1L;

    public DepartmentUCResultsBean(ExecutionCourse executionCourse, ExecutionDegree executionDegree, Person president,
            boolean backToResume) {
        super(executionCourse, executionDegree, president, backToResume);
    }

    public List<InquiryResultComment> getAllCourseComments() {
        List<InquiryResultComment> commentsMade = new ArrayList<InquiryResultComment>();
        for (InquiryGlobalComment globalComment : getExecutionCourse().getInquiryGlobalComments()) {
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
