/*
 * Created on Feb 18, 2004
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.student;

import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.student.Delegate;
import net.sourceforge.fenixedu.domain.student.Delegate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentDelegate;
import net.sourceforge.fenixedu.util.DelegateYearType;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public class DelegateOJB extends PersistentObjectOJB implements IPersistentDelegate {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.student.IPersistentDelegate#readByDegreeAndExecutionYear(Dominio.Degree,
     *      Dominio.ExecutionYear)
     */
    public List readByDegreeAndExecutionYear(Degree degree, ExecutionYear executionYear)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degree.idInternal", degree.getIdInternal());
        criteria.addEqualTo("executionYear.idInternal", executionYear.getIdInternal());
        return queryList(Delegate.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.student.IPersistentDelegate#readByDegreeAndExecutionYearAndType(Dominio.Degree,
     *      Dominio.ExecutionYear, Util.DelegateType)
     */
    public List readByDegreeAndExecutionYearAndYearType(Degree degree, ExecutionYear executionYear,
            DelegateYearType yearType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degree.idInternal", degree.getIdInternal());
        criteria.addEqualTo("executionYear.idInternal", executionYear.getIdInternal());
        criteria.addEqualTo("yearType", yearType);
        return queryList(Delegate.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.student.IPersistentDelegate#readByStudent(Dominio.Student)
     */
    public Delegate readByStudent(Student student) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("student.idInternal", student.getIdInternal());
        return (Delegate) queryObject(Delegate.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.student.IPersistentDelegate#readDegreeDelegateByDegreeAndExecutionYear(Dominio.Degree,
     *      Dominio.ExecutionYear)
     */
    public List readDegreeDelegateByDegreeAndExecutionYear(Degree degree, ExecutionYear executionYear)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degree.idInternal", degree.getIdInternal());
        criteria.addEqualTo("executionYear.idInternal", executionYear.getIdInternal());
        criteria.addEqualTo("type", Boolean.TRUE);
        return queryList(Delegate.class, criteria);
    }
}