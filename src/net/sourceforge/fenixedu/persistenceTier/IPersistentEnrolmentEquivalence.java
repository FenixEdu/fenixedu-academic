package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEquivalence;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public interface IPersistentEnrolmentEquivalence extends IPersistentObject {

    public void delete(IEnrolmentEquivalence enrolment) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public IEnrolmentEquivalence readByEnrolment(IEnrolment enrolment) throws ExcepcaoPersistencia;
}