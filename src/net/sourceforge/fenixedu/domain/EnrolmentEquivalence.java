package net.sourceforge.fenixedu.domain;

import java.util.List;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class EnrolmentEquivalence extends DomainObject implements IEnrolmentEquivalence {

    private IEnrollment enrolment;

    private Integer enrolmentKey;

    private List equivalenceRestrictions;

    public EnrolmentEquivalence() {
    }

    public boolean equals(Object obj) {
        boolean resultado = false;

        if (obj instanceof IEnrolmentEquivalence) {
            IEnrolmentEquivalence equivalence = (IEnrolmentEquivalence) obj;

            resultado = (this.getEnrolment().equals(equivalence.getEnrolment()));
        }
        return resultado;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "enrolment = " + this.enrolment + "; ";
        return result;
    }

    /**
     * @return IEnrolment
     */
    public IEnrollment getEnrolment() {
        return enrolment;
    }

    /**
     * @return Integer
     */
    public Integer getEnrolmentKey() {
        return enrolmentKey;
    }

    /**
     * Sets the enrolment.
     * 
     * @param enrolment
     *            The enrolment to set
     */
    public void setEnrolment(IEnrollment enrolment) {
        this.enrolment = enrolment;
    }

    /**
     * Sets the enrolmentKey.
     * 
     * @param enrolmentKey
     *            The enrolmentKey to set
     */
    public void setEnrolmentKey(Integer enrolmentKey) {
        this.enrolmentKey = enrolmentKey;
    }

    public List getEquivalenceRestrictions() {
        return equivalenceRestrictions;
    }

    public void setEquivalenceRestrictions(List list) {
        equivalenceRestrictions = list;
    }

}