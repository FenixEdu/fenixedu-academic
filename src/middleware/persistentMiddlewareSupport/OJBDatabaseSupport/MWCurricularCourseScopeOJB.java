
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import java.util.List;

import middleware.middlewareDomain.MWCurricularCourseScope;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourseScope;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;

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


	public List readAll() throws PersistentMiddlewareSupportException, ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		return queryList(MWCurricularCourseScope.class, criteria);
	}
    
}