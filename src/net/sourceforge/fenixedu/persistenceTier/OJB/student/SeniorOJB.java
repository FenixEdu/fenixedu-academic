/*
 * Created on Dec 22, 2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.student;

import net.sourceforge.fenixedu.domain.Student;
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
     * @see ServidorPersistente.student.IPersistentSenior#readByStudent(Dominio.Student)
     */
    public Senior readByStudent(Student student) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("student.idInternal", student.getIdInternal());
        return (Senior) queryObject(Senior.class, criteria);
    }

}
