/*
 * Created on Feb 18, 2004
 *  
 */
package ServidorPersistente.OJB.student;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ICurso;
import Dominio.IExecutionYear;
import Dominio.student.Delegate;
import Dominio.student.IDelegate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.student.IPersistentDelegate;
import Util.DelegateType;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida</a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo</a>
 *  
 */
public class DelegateOJB extends ObjectFenixOJB implements IPersistentDelegate
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.student.IPersistentDelegate#readByDegreeAndExecutionYear(Dominio.ICurso,
	 *      Dominio.IExecutionYear)
	 */
    public List readByDegreeAndExecutionYear(ICurso degree, IExecutionYear executionYear)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degree.idInternal", degree.getIdInternal());
        criteria.addEqualTo("executionYear.idInternal", executionYear.getIdInternal());
        return queryList(Delegate.class, criteria);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.student.IPersistentDelegate#readByDegreeAndExecutionYearAndType(Dominio.ICurso,
	 *      Dominio.IExecutionYear, Util.DelegateType)
	 */
    public IDelegate readByDegreeAndExecutionYearAndType(
        ICurso degree,
        IExecutionYear executionYear,
        DelegateType type)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degree.idInternal", degree.getIdInternal());
        criteria.addEqualTo("executionYear.idInternal", executionYear.getIdInternal());
        criteria.addEqualTo("type", type);
        return (IDelegate) queryObject(Delegate.class, criteria);
    }
}
