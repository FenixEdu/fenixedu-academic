package net.sourceforge.fenixedu.domain;

import java.util.List;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class EnrolmentEquivalence extends EnrolmentEquivalence_Base {

    private IEnrolment enrolment;

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
    public IEnrolment getEnrolment() {
        return enrolment;
    }

    /**
     * Sets the enrolment.
     * 
     * @param enrolment
     *            The enrolment to set
     */
    public void setEnrolment(IEnrolment enrolment) {
        this.enrolment = enrolment;
    }

    public List getEquivalenceRestrictions() {
        return equivalenceRestrictions;
    }

    public void setEquivalenceRestrictions(List list) {
        equivalenceRestrictions = list;
    }

}