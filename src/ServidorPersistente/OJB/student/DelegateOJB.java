/*
 * Created on Feb 18, 2004
 *  
 */
package ServidorPersistente.OJB.student;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IDegree;
import Dominio.IExecutionYear;
import Dominio.IStudent;
import Dominio.student.Delegate;
import Dominio.student.IDelegate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.student.IPersistentDelegate;
import Util.DelegateYearType;

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