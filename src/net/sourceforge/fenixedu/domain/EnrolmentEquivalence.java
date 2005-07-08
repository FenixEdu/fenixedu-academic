package net.sourceforge.fenixedu.domain;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class EnrolmentEquivalence extends EnrolmentEquivalence_Base {

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "enrolment = " + getEnrolment() + "; ";
        return result;
    }

}
