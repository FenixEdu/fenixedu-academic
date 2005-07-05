package net.sourceforge.fenixedu.domain;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class EnrolmentEquivalence extends EnrolmentEquivalence_Base {

    public boolean equals(Object obj) {
        if (obj instanceof IEnrolmentEquivalence) {
            final IEnrolmentEquivalence enrolmentEquivalence = (IEnrolmentEquivalence) obj;
            return this.getIdInternal().equals(enrolmentEquivalence.getIdInternal());
        }
        return false;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "enrolment = " + getEnrolment() + "; ";
        return result;
    }

}
