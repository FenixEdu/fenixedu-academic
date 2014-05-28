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
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.exception.MissingObjectException;

public class DeleteProfessorshipResultsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long professorshipOID;
    private ShiftType shiftType;
    private Long inquiryQuestionOID;
    private Long executionCourseOID;

    public boolean deleteResults() {
        Professorship professorship = null;
        try {
            professorship = FenixFramework.getDomainObject(getProfessorshipOID().toString());
        } catch (ClassCastException cce) {
            throw new DomainException("error.professorship.dontExist", cce.getCause());
        } catch (MissingObjectException moe) {
            throw new DomainException("error.professorship.dontExist", moe.getCause());
        }
        setExecutionCourseOID(null);
        if (getShiftType() != null) {
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
            return professorship.deleteInquiryResults(getShiftType(), inquiryQuestion);
        } else {
            setInquiryQuestionOID(null);
            return professorship.deleteInquiryResults();
        }
    }

    public boolean deleteAllTeachersResults() {
        ExecutionCourse executionCourse = null;
        try {
            executionCourse = FenixFramework.getDomainObject(getExecutionCourseOID().toString());
        } catch (ClassCastException cce) {
            throw new DomainException("error.executionCourse.dontExist", cce.getCause());
        } catch (MissingObjectException moe) {
            throw new DomainException("error.executionCourse.dontExist", moe.getCause());
        }
        setProfessorshipOID(null);
        setShiftType(null);
        setInquiryQuestionOID(null);
        return executionCourse.deleteAllTeachersResults();
    }

    public Long getProfessorshipOID() {
        return professorshipOID;
    }

    public void setProfessorshipOID(Long professorshipOID) {
        this.professorshipOID = professorshipOID;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    public Long getInquiryQuestionOID() {
        return inquiryQuestionOID;
    }

    public void setInquiryQuestionOID(Long inquiryQuestionOID) {
        this.inquiryQuestionOID = inquiryQuestionOID;
    }

    public void setExecutionCourseOID(Long executionCourseOID) {
        this.executionCourseOID = executionCourseOID;
    }

    public Long getExecutionCourseOID() {
        return executionCourseOID;
    }
}
