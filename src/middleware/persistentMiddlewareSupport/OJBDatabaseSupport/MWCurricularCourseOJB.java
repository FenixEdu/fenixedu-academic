
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import middleware.middlewareDomain.MwCurricularCourse;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourse;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;

import org.apache.ojb.broker.query.Criteria;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * 
 */
public class MWCurricularCourseOJB extends ObjectFenixOJB implements IPersistentMWCurricularCourse {
    
    public MWCurricularCourseOJB() {
    }


	public MwCurricularCourse readByCode(String code) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("coursecode", code);
		return (MwCurricularCourse) queryObject(MwCurricularCourse.class, criteria);
	}
    
}