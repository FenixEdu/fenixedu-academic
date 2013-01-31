package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;

public class TeacherShiftTypeGeneralResultBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Professorship professorship;
	private ShiftType shiftType;
	private InquiryResult inquiryResult;

	public TeacherShiftTypeGeneralResultBean(Professorship professorship, ShiftType shiftType, InquiryResult inquiryResult) {
		setProfessorship(professorship);
		setShiftType(shiftType);
		setInquiryResult(inquiryResult);
	}

	public String getTeacherId() {
		if (getProfessorship().getPerson().getTeacher() != null) {
			String identifier;
			if (getProfessorship().getPerson().getEmployee() != null) {
				identifier = getProfessorship().getPerson().getEmployee().getEmployeeNumber().toString();
			} else {
				identifier = getProfessorship().getPerson().getIstUsername();
			}
			return " (" + identifier + ") ";
		}
		return "";
	}

	public Professorship getProfessorship() {
		return professorship;
	}

	public void setProfessorship(Professorship professorship) {
		this.professorship = professorship;
	}

	public ShiftType getShiftType() {
		return shiftType;
	}

	public void setShiftType(ShiftType shiftType) {
		this.shiftType = shiftType;
	}

	public InquiryResult getInquiryResult() {
		return inquiryResult;
	}

	public void setInquiryResult(InquiryResult inquiryResult) {
		this.inquiryResult = inquiryResult;
	}

}
