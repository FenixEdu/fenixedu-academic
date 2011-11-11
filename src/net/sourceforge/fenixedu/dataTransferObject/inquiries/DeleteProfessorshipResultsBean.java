package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import pt.ist.fenixframework.pstm.MissingObjectException;

public class DeleteProfessorshipResultsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long professorshipOID;
    private ShiftType shiftType;
    private Long inquiryQuestionOID;
    private Long executionCourseOID;

    public boolean deleteResults() {
	Professorship professorship = null;
	try {
	    professorship = Professorship.fromExternalId(getProfessorshipOID().toString());
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
		    inquiryQuestion = InquiryQuestion.fromExternalId(getInquiryQuestionOID().toString());
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
	    executionCourse = ExecutionCourse.fromExternalId(getExecutionCourseOID().toString());
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
