package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.IEnrolmentEquivalence;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public interface IPersistentEnrolmentEquivalence extends IPersistentObject {

    public IEnrolmentEquivalence readByEnrolment(Integer enrolmentId) throws ExcepcaoPersistencia;
}