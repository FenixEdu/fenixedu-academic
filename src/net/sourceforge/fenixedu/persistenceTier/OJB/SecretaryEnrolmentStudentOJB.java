/*
 * Created on Feb 3, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.ISecretaryEnrolmentStudent;
import net.sourceforge.fenixedu.domain.SecretaryEnrolmentStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSecretaryEnrolmentStudent;

import org.apache.ojb.broker.query.Criteria;

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
