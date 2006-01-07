/*
 * Created on Feb 3, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.SecretaryEnrolmentStudent;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 *
 */
public interface IPersistentSecretaryEnrolmentStudent {
    public abstract SecretaryEnrolmentStudent readByStudentNumber(Integer studentNumber)
            throws ExcepcaoPersistencia;
}