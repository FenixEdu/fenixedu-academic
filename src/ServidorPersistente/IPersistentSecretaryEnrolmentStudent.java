/*
 * Created on Feb 3, 2005
 *
 */
package ServidorPersistente;

import Dominio.ISecretaryEnrolmentStudent;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 *
 */
public interface IPersistentSecretaryEnrolmentStudent {
    public abstract ISecretaryEnrolmentStudent readByStudentNumber(Integer studentNumber)
            throws ExcepcaoPersistencia;
}