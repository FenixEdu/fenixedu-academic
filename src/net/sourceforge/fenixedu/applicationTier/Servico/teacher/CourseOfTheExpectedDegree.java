package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Tânia Pousão Create on 3/Dez/2003
 */
public class CourseOfTheExpectedDegree implements IService {

	public Boolean run(Integer curricularCourseCode, String degreeCode) throws FenixServiceException {
		boolean result = false;

		try {
			result = CurricularCourseDegree(curricularCourseCode, degreeCode)
					&& CurricularCourseNotBasic(curricularCourseCode);

		} catch (Exception e) {
			e.printStackTrace();
			throw new FenixServiceException(e);
		}

		return new Boolean(result);
	}

	/**
	 * @param argumentos
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	private boolean CurricularCourseDegree(Integer curricularCourseCode, String degreeCode)
			throws FenixServiceException, ExcepcaoPersistencia {
		boolean result = false;

		CurricularCourse curricularCourse = null;
		Degree degree = null;

		ISuportePersistente sp;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

		curricularCourse = (CurricularCourse) persistentCurricularCourse.readByOID(
				CurricularCourse.class, curricularCourseCode);

		degree = curricularCourse.getDegreeCurricularPlan().getDegree();

		result = degree.getSigla().equals(degreeCode);

		return result; // codigo do curso de Aeroespacial
	}

	/**
	 * @param argumentos
	 * @return
	 * @throws ExcepcaoPersistencia 
	 */
	private boolean CurricularCourseNotBasic(Integer curricularCourseCode) throws FenixServiceException, ExcepcaoPersistencia {
		boolean result = false;
		CurricularCourse curricularCourse = null;

		ISuportePersistente sp;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
		curricularCourse = (CurricularCourse) persistentCurricularCourse.readByOID(
				CurricularCourse.class, curricularCourseCode);
		result = curricularCourse.getBasic().equals(Boolean.FALSE);

		return result;
	}
}