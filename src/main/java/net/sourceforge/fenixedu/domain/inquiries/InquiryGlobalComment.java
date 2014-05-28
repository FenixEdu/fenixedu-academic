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
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;

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
        for (; !getInquiryResultCommentsSet().isEmpty(); getInquiryResultComments().iterator().next().delete()) {
            ;
        }
        setExecutionCourse(null);
        setExecutionDegree(null);
        setExecutionSemester(null);
        setTeacher(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment> getInquiryResultComments() {
        return getInquiryResultCommentsSet();
    }

    @Deprecated
    public boolean hasAnyInquiryResultComments() {
        return !getInquiryResultCommentsSet().isEmpty();
    }

    @Deprecated
    public boolean hasCommentOnTeacher() {
        return getCommentOnTeacher() != null;
    }

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasExecutionDegree() {
        return getExecutionDegree() != null;
    }

    @Deprecated
    public boolean hasExecutionSemester() {
        return getExecutionSemester() != null;
    }

}
