package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.IEquivalentEnrolmentForEnrolmentEquivalence;

/**
 * @author dcs-rjao 17/Jul/2003
 */
public interface IPersistentEquivalentEnrolmentForEnrolmentEquivalence extends IPersistentObject {

    public IEquivalentEnrolmentForEnrolmentEquivalence readByEnrolmentEquivalenceAndEquivalentEnrolment(
            Integer enrolmentEquivalenceId, Integer equivalentEnrolmentId)
            throws ExcepcaoPersistencia;
}