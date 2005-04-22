package net.sourceforge.fenixedu.domain;


/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class EnrolmentEquivalence extends EnrolmentEquivalence_Base {

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
        result += "enrolment = " + getEnrolment() + "; ";
        return result;
    }
}