/*
 * Created on 22/Nov/2003
 *
 */
package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.util.OldPublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class OldPublication extends OldPublication_Base {
    private ITeacher teacher;
    private OldPublicationType oldPublicationType;

    /**
     * @return Returns the oldPublicationType.
     */
    public OldPublicationType getOldPublicationType() {
        return oldPublicationType;
    }

    /**
     * @param oldPublicationType
     *            The oldPublicationType to set.
     */
    public void setOldPublicationType(OldPublicationType oldPublicationType) {
        this.oldPublicationType = oldPublicationType;
    }

    /**
     * @return Returns the teacher.
     */
    public ITeacher getTeacher() {
        return teacher;
    }

    /**
     * @param teacher
     *            The teacher to set.
     */
    public void setTeacher(ITeacher teacher) {
        this.teacher = teacher;
    }

}
