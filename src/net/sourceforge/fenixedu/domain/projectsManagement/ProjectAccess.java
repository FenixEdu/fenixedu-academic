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
        Calendar result = Calendar.getInstance();
        result.setTime(this.getBegin());
        return result;
    }

    /**
     * @param beginDate
     *            The beginDate to set.
     */
    public void setBeginDate(Calendar beginDate) {
        this.setBegin(beginDate.getTime());
    }

    /**
     * @return Returns the endDate.
     */
    public Calendar getEndDate() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getEnd());
        return result;
    }

    /**
     * @param endDate
     *            The endDate to set.
     */
    public void setEndDate(Calendar endDate) {
        this.setEnd(endDate.getTime());
    }

}
