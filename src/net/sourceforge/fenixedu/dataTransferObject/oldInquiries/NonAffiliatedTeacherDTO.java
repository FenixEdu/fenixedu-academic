/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class NonAffiliatedTeacherDTO extends TeacherDTO {

    private NonAffiliatedTeacher nonAffiliatedTeacher;

    public NonAffiliatedTeacherDTO(NonAffiliatedTeacher nonAffiliatedTeacher) {
        this.nonAffiliatedTeacher = nonAffiliatedTeacher;
    }

    @Override
    public NonAffiliatedTeacher getTeacher() {
        return nonAffiliatedTeacher;
    }

    @Override
    public String getName() {
        return getTeacher().getName();
    }

}
