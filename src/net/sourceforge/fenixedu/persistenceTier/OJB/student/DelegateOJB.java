/*
 * Created on Feb 18, 2004
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.student;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.student.Delegate;
import net.sourceforge.fenixedu.domain.student.IDelegate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentDelegate;
import net.sourceforge.fenixedu.util.DelegateYearType;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public class DelegateOJB extends PersistentObjectOJB implements IPersistentDelegate {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.student.IPersistentDelegate#readByDegreeAndExecutionYear(Dominio.IDegree,
     *      Dominio.IExecutionYear)
     */
    public List readByDegreeAndExecutionYear(IDegree degree, IExecutionYear executionYear)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degree.idInternal", degree.getIdInternal());
        criteria.addEqualTo("executionYear.idInternal", executionYear.getIdInternal());
        return queryList(Delegate.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.student.IPersistentDelegate#readByDegreeAndExecutionYearAndType(Dominio.IDegree,
     *      Dominio.IExecutionYear, Util.DelegateType)
     */
    public List readByDegreeAndExecutionYearAndYearType(IDegree degree, IExecutionYear executionYear,
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
     * @see ServidorPersistente.student.IPersistentDelegate#readByStudent(Dominio.IStudent)
     */
    public IDelegate readByStudent(IStudent student) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("student.idInternal", student.getIdInternal());
        return (IDelegate) queryObject(Delegate.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.student.IPersistentDelegate#readDegreeDelegateByDegreeAndExecutionYear(Dominio.IDegree,
     *      Dominio.IExecutionYear)
     */
    public List readDegreeDelegateByDegreeAndExecutionYear(IDegree degree, IExecutionYear executionYear)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degree.idInternal", degree.getIdInternal());
        criteria.addEqualTo("executionYear.idInternal", executionYear.getIdInternal());
        criteria.addEqualTo("type", Boolean.TRUE);
        return queryList(Delegate.class, criteria);
    }
}