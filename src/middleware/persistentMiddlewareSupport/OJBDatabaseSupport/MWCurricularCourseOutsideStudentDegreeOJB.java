
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import middleware.middlewareDomain.MWCurricularCourseOutsideStudentDegree;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourseOutsideStudentDegree;

import org.apache.ojb.broker.query.Criteria;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;


/**
 * @author David Santos
 * 20/Out/2003
 */

public class MWCurricularCourseOutsideStudentDegreeOJB extends ObjectFenixOJB implements IPersistentMWCurricularCourseOutsideStudentDegree {
    
    public MWCurricularCourseOutsideStudentDegreeOJB() {
    }

	public MWCurricularCourseOutsideStudentDegree readByCourseCodeAndDegreeCode(String courseCode, Integer degreeCode) throws ExcepcaoPersistencia {

		Criteria criteria = new Criteria();
		
		criteria.addEqualTo("courseCode", courseCode);
		criteria.addEqualTo("degreeCode", degreeCode);
		
		return (MWCurricularCourseOutsideStudentDegree) queryObject(MWCurricularCourseOutsideStudentDegree.class, criteria);
	}
}