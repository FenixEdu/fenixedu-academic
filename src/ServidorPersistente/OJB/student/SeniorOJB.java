/*
 * Created on Dec 22, 2004
 *
 */
package ServidorPersistente.OJB.student;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IStudent;
import Dominio.student.ISenior;
import Dominio.student.Senior;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.student.IPersistentSenior;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 *
 */
public class SeniorOJB extends PersistentObjectOJB implements IPersistentSenior {

    /* (non-Javadoc)
     * @see ServidorPersistente.student.IPersistentSenior#readByStudent(Dominio.IStudent)
     */
    public ISenior readByStudent(IStudent student) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("student.idInternal", student.getIdInternal());
        return (ISenior) queryObject(Senior.class, criteria);
    }

}
