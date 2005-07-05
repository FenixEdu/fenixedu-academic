/*
 * Created on 2/Abr/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author jpvl
 */
public class EquivalentEnrolmentForEnrolmentEquivalence extends
        EquivalentEnrolmentForEnrolmentEquivalence_Base {

    public boolean equals(Object obj) {
        if (obj instanceof IEquivalentEnrolmentForEnrolmentEquivalence) {
            final IEquivalentEnrolmentForEnrolmentEquivalence equivalentEnrolmentForEnrolmentEquivalence = (IEquivalentEnrolmentForEnrolmentEquivalence) obj;
            return this.getIdInternal().equals(
                    equivalentEnrolmentForEnrolmentEquivalence.getIdInternal());
        }
        return false;
    }

}
