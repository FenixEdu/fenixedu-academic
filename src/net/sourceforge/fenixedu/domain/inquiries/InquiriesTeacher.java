/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesTeacher;
import net.sourceforge.fenixedu.domain.INonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;


/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InquiriesTeacher extends InquiriesTeacher_Base {
	public InquiriesTeacher() {
		super();
	}
	
	protected InquiriesTeacher(IInquiriesCourse inquiriesCourse, ITeacher teacher, ShiftType shiftType, InfoInquiriesTeacher infoInquiriesTeacher) {
		if((inquiriesCourse == null) || (teacher == null) || (shiftType == null))
			throw new DomainException("The inquiriesCourse, teacher and shiftType should not be null!");
				
		this.setInquiriesCourse(inquiriesCourse);
		this.setTeacher(teacher);
		this.setBasicProperties(shiftType, infoInquiriesTeacher);
	}
	
	protected InquiriesTeacher(IInquiriesCourse inquiriesCourse, INonAffiliatedTeacher nonAffiliatedTeacher, ShiftType shiftType, InfoInquiriesTeacher infoInquiriesTeacher) {
		if((inquiriesCourse == null) || (nonAffiliatedTeacher == null) || (shiftType == null))
			throw new DomainException("The inquiriesCourse, nonAffiliatedTeacher and shiftType should not be null!");

		this.setInquiriesCourse(inquiriesCourse);
		this.setNonAffiliatedTeacher(nonAffiliatedTeacher);
		this.setBasicProperties(shiftType, infoInquiriesTeacher);
	}
	
	private void setBasicProperties(ShiftType shiftType, InfoInquiriesTeacher infoInquiriesTeacher) {
        this.setShiftType(shiftType);
        this.setStudentAssiduity(infoInquiriesTeacher.getStudentAssiduity());
        this.setTeacherAssiduity(infoInquiriesTeacher.getTeacherAssiduity());
        this.setTeacherPunctuality(infoInquiriesTeacher.getTeacherPunctuality());
        this.setTeacherClarity(infoInquiriesTeacher.getTeacherClarity());
        this.setTeacherAssurance(infoInquiriesTeacher.getTeacherAssurance());
        this.setTeacherInterestStimulation(infoInquiriesTeacher.getTeacherInterestStimulation());
        this.setTeacherAvailability(infoInquiriesTeacher.getTeacherAvailability());
        this.setTeacherReasoningStimulation(infoInquiriesTeacher.getTeacherReasoningStimulation());
        this.setGlobalAppreciation(infoInquiriesTeacher.getGlobalAppreciation());
		
	}
}
