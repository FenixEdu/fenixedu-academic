/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class NonAffiliatedTeacherDTO extends TeacherDTO {

    private DomainReference<NonAffiliatedTeacher> nonAffiliatedTeacher;

    public NonAffiliatedTeacherDTO(NonAffiliatedTeacher nonAffiliatedTeacher) {
	this.nonAffiliatedTeacher = new DomainReference<NonAffiliatedTeacher>(nonAffiliatedTeacher);
    }

    @Override
    public NonAffiliatedTeacher getTeacher() {
	return nonAffiliatedTeacher.getObject();
    }

    @Override
    public String getName() {
	return getTeacher().getName();
    }

}
