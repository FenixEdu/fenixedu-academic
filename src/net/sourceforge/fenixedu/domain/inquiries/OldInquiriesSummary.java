/*
 * Created on Nov 15, 2004
 *
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class OldInquiriesSummary extends OldInquiriesSummary_Base {
    private IDegree degree;

    private IExecutionPeriod executionPeriod;

    /**
     * @return Returns the degree.
     */
    public IDegree getDegree() {
        return degree;
    }

    /**
     * @param degree
     *            The degree to set.
     */
    public void setDegree(IDegree degree) {
        this.degree = degree;
    }

    /**
     * @return Returns the executionPeriod.
     */
    public IExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }

    /**
     * @param executionPeriod
     *            The executionPeriod to set.
     */
    public void setExecutionPeriod(IExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod;
    }

}
