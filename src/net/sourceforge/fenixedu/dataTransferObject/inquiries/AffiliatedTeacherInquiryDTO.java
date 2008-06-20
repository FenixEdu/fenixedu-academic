/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class AffiliatedTeacherInquiryDTO extends TeacherInquiryDTO {

    private DomainReference<Teacher> teacher;

    AffiliatedTeacherInquiryDTO(final Teacher teacher, final ExecutionCourse executionCourse, ShiftType shiftType) {
	super(executionCourse, shiftType);
	this.teacher = new DomainReference<Teacher>(teacher);
    }

    public Teacher getTeacher() {
	return teacher.getObject();
    }

    @Override
    public String getTeacherName() {
	return getTeacher().getPerson().getName();
    }

}
