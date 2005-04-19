/*
 * Created on 2/Abr/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author jpvl
 */
public class EquivalentEnrolmentForEnrolmentEquivalence extends EquivalentEnrolmentForEnrolmentEquivalence_Base {
    private IEnrolmentEquivalence enrolmentEquivalence;

    private IEnrolment equivalentEnrolment;

    /**
     *  
     */
    public EquivalentEnrolmentForEnrolmentEquivalence() {
    }

    public boolean equals(Object obj) {
        boolean resultado = false;

        if (obj instanceof IEquivalentEnrolmentForEnrolmentEquivalence) {
            IEquivalentEnrolmentForEnrolmentEquivalence equivalence = (IEquivalentEnrolmentForEnrolmentEquivalence) obj;

            resultado = (this.getEnrolmentEquivalence().equals(equivalence.getEnrolmentEquivalence()))
                    && (this.getEquivalentEnrolment().equals(equivalence.getEquivalentEnrolment()));
        }
        return resultado;
    }

    public IEnrolmentEquivalence getEnrolmentEquivalence() {
        return enrolmentEquivalence;
    }

    public IEnrolment getEquivalentEnrolment() {
        return equivalentEnrolment;
    }

    public void setEnrolmentEquivalence(IEnrolmentEquivalence equivalence) {
        enrolmentEquivalence = equivalence;
    }

    public void setEquivalentEnrolment(IEnrolment enrolment) {
        equivalentEnrolment = enrolment;
    }
}