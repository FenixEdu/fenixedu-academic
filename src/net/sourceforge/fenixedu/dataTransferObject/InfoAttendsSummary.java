/*
 * Attends.java Created on 20 de Outubro de 2002, 14:42
 */

package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author João Mota
 */
public class InfoAttendsSummary extends InfoObject implements Serializable {

    protected List attends;

    protected Map enrollmentDistribution;

    protected List numberOfEnrollments;

    public InfoAttendsSummary() {

    }

    /**
     * @return Returns the attends.
     */
    public List getAttends() {
        return attends;
    }

    /**
     * @param attends
     *            The attends to set.
     */
    public void setAttends(List attends) {
        this.attends = attends;
    }

    /**
     * @return Returns the enrollmentDistribution.
     */
    public Map getEnrollmentDistribution() {
        return enrollmentDistribution;
    }

    /**
     * @param enrollmentDistribution
     *            The enrollmentDistribution to set.
     */
    public void setEnrollmentDistribution(Map enrollmentDistribution) {
        this.enrollmentDistribution = enrollmentDistribution;
    }

    /**
     * @return Returns the numberOfEnrollments.
     */
    public List getNumberOfEnrollments() {
        return numberOfEnrollments;
    }

    /**
     * @param numberOfEnrollments
     *            The numberOfEnrollments to set.
     */
    public void setNumberOfEnrollments(List numberOfEnrollments) {
        this.numberOfEnrollments = numberOfEnrollments;
    }
}