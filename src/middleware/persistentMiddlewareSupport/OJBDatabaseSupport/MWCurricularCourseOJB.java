
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import middleware.middlewareDomain.MWCurricularCourse;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourse;

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


	public MWCurricularCourse readByCode(String code) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("coursecode", code);
		return (MWCurricularCourse) queryObject(MWCurricularCourse.class, criteria);
	}
    
}