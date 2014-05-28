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

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.exception.MissingObjectException;

public class DeleteExecutionCourseResultsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long executionCourseOID;
    private Long executionDegreeOID;
    private Long inquiryQuestionOID;

    public boolean deleteResults() {
        ExecutionCourse executionCourse = null;
        try {
            executionCourse = FenixFramework.getDomainObject(getExecutionCourseOID().toString());
        } catch (ClassCastException cce) {
            throw new DomainException("error.executionCourse.dontExist", cce.getCause());
        } catch (MissingObjectException moe) {
            throw new DomainException("error.executionCourse.dontExist", moe.getCause());
        }
        if (getExecutionDegreeOID() != null) {
            ExecutionDegree executionDegree = null;
            try {
                executionDegree = FenixFramework.getDomainObject(getExecutionDegreeOID().toString());
            } catch (ClassCastException cce) {
                throw new DomainException("error.executionDegree.dontExist", cce.getCause());
            } catch (MissingObjectException moe) {
                throw new DomainException("error.executionDegree.dontExist", moe.getCause());
            }
            InquiryQuestion inquiryQuestion = null;
            if (getInquiryQuestionOID() != null) {
                try {
                    inquiryQuestion = FenixFramework.getDomainObject(getInquiryQuestionOID().toString());
                } catch (ClassCastException cce) {
                    throw new DomainException("error.inquiryQuestion.dontExist", cce.getCause());
                } catch (MissingObjectException moe) {
                    throw new DomainException("error.inquiryQuestion.dontExist", moe.getCause());
                }
            }
            return executionCourse.deleteInquiryResults(executionDegree, inquiryQuestion);
        } else {
            setInquiryQuestionOID(null);
            return executionCourse.deleteInquiryResults();
        }
    }

    public void setExecutionCourseOID(Long executionCourseOID) {
        this.executionCourseOID = executionCourseOID;
    }

    public Long getExecutionCourseOID() {
        return executionCourseOID;
    }

    public void setExecutionDegreeOID(Long executionDegreeOID) {
        this.executionDegreeOID = executionDegreeOID;
    }

    public Long getExecutionDegreeOID() {
        return executionDegreeOID;
    }

    public Long getInquiryQuestionOID() {
        return inquiryQuestionOID;
    }

    public void setInquiryQuestionOID(Long inquiryQuestionOID) {
        this.inquiryQuestionOID = inquiryQuestionOID;
    }
}
