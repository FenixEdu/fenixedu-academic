/*
 * Created on Dec 22, 2004
 *
 */
package ServidorPersistente.student;

import Dominio.IStudent;
import Dominio.student.ISenior;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 *
 */
public interface IPersistentSenior extends IPersistentObject {
    public ISenior readByStudent(IStudent student) throws ExcepcaoPersistencia;
}
