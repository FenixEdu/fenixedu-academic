/*
 * Created on Dec 22, 2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.student;

import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.student.ISenior;
import net.sourceforge.fenixedu.domain.student.Senior;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentSenior;

import org.apache.ojb.broker.query.Criteria;

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
