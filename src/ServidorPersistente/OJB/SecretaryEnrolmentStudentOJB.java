/*
 * Created on Feb 3, 2005
 *
 */
package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ISecretaryEnrolmentStudent;
import Dominio.SecretaryEnrolmentStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSecretaryEnrolmentStudent;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class SecretaryEnrolmentStudentOJB extends PersistentObjectOJB implements IPersistentSecretaryEnrolmentStudent {

    public ISecretaryEnrolmentStudent readByStudentNumber(Integer studentNumber)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("student.number", studentNumber);
        return (ISecretaryEnrolmentStudent) queryObject(SecretaryEnrolmentStudent.class, criteria);
    }

}
