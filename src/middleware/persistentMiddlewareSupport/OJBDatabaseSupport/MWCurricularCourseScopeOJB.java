
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import java.util.List;

import middleware.middlewareDomain.MWCurricularCourseScope;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourseScope;

import org.apache.ojb.broker.query.Criteria;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * 
 */
public class MWCurricularCourseScopeOJB extends ObjectFenixOJB implements IPersistentMWCurricularCourseScope {
    
    public MWCurricularCourseScopeOJB() {
    }


	public List readAll() throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		return queryList(MWCurricularCourseScope.class, criteria);
	}
    
	/* (non-Javadoc)
	 * @see middleware.persistentMiddlewareSupport.IPersistentMWAluno#readAllBySpan(java.lang.Integer, java.lang.Integer)
	 */
	public List readAllBySpan(Integer spanNumber, Integer numberOfElementsInSpan) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		return readSpan(MWCurricularCourseScope.class, criteria, numberOfElementsInSpan, spanNumber);
	}
	
	/* (non-Javadoc)
	 * @see middleware.persistentMiddlewareSupport.IPersistentMWAluno#countAll()
	 */
	public Integer countAll()
	{
		return new Integer(count(MWCurricularCourseScope.class, new Criteria()));
	}

}