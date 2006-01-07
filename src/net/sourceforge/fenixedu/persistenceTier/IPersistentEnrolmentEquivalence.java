package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.EnrolmentEquivalence;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public interface IPersistentEnrolmentEquivalence extends IPersistentObject {

    public EnrolmentEquivalence readByEnrolment(Integer enrolmentId) throws ExcepcaoPersistencia;
}