/*
 * Created on 22/Nov/2003
 *
 */
package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.util.OldPublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class OldPublication extends OldPublication_Base {
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

}
