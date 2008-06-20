/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.ShiftType;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class NonAffiliatedTeacherInquiryDTO extends TeacherInquiryDTO {

    private DomainReference<NonAffiliatedTeacher> teacher;

    NonAffiliatedTeacherInquiryDTO(final NonAffiliatedTeacher teacher, final ExecutionCourse executionCourse, ShiftType shiftType) {
	super(executionCourse, shiftType);
	this.teacher = new DomainReference<NonAffiliatedTeacher>(teacher);
    }

    public NonAffiliatedTeacher getTeacher() {
	return teacher.getObject();
    }

    @Override
    public String getTeacherName() {
	return getTeacher().getName();
    }

}
