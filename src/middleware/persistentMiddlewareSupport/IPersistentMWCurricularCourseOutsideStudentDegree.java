package middleware.persistentMiddlewareSupport;

import middleware.middlewareDomain.MWCurricularCourseOutsideStudentDegree;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author David Santos
 * 20/Out/2003
 */
public interface IPersistentMWCurricularCourseOutsideStudentDegree extends IPersistentObject {

	public MWCurricularCourseOutsideStudentDegree readByCourseCodeAndDegreeCode(String courseCode, Integer degreeCode) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
}
