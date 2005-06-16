/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.domain.projectsManagement;

import java.util.Calendar;

/**
 * @author Susana Fernandes
 * 
 */
public class ProjectAccess extends ProjectAccess_Base {

    /**
     * @return Returns the beginDate.
     */
    public Calendar getBeginDate() {
        if (this.getBegin() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getBegin());
            return result;
        }
        return null;
    }

    /**
     * @param beginDate
     *            The beginDate to set.
     */
    public void setBeginDate(Calendar beginDate) {
        if (beginDate.getTime() != null) {
            this.setBegin(beginDate.getTime());    
        } else {
            this.setBegin(null);
        }
    }

    /**
     * @return Returns the endDate.
     */
    public Calendar getEndDate() {
        if (this.getEnd() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnd());
            return result;    
        }
        return null;
    }

    /**
     * @param endDate
     *            The endDate to set.
     */
    public void setEndDate(Calendar endDate) {
        if (endDate.getTime() != null) {
            this.setEnd(endDate.getTime());            
        } else {
            this.setEnd(null);
        }
    }

}
