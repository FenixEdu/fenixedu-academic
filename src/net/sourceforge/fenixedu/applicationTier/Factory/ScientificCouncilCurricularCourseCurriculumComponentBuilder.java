/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Factory;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCurriculum;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head ServidorAplicacao.Factory
 * 
 */
public class ScientificCouncilCurricularCourseCurriculumComponentBuilder {

	private static ScientificCouncilCurricularCourseCurriculumComponentBuilder instance = null;

	public ScientificCouncilCurricularCourseCurriculumComponentBuilder() {
	}

	public static ScientificCouncilCurricularCourseCurriculumComponentBuilder getInstance() {
		if (instance == null) {
			instance = new ScientificCouncilCurricularCourseCurriculumComponentBuilder();
		}
		return instance;
	}

	public ISiteComponent getComponent(ISiteComponent component, Integer curricularCourseId,
			Integer curriculumId) throws FenixServiceException, ExcepcaoPersistencia {
		if (component instanceof InfoSiteCurriculum) {
			return getInfoSiteCurriculum((InfoSiteCurriculum) component, curricularCourseId);
		}
		return null;

	}

	/**
	 * @param curriculum
	 * @param curricularCourseId
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	private ISiteComponent getInfoSiteCurriculum(InfoSiteCurriculum component, Integer curricularCourseId)
			throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
		IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
		ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(
				CurricularCourse.class, curricularCourseId);
		ICurriculum curriculum = persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse
				.getIdInternal());
		if (curriculum != null) {
			InfoCurriculum infoCurriculum = InfoCurriculum.newInfoFromDomain(curriculum);
			component.setInfoCurriculum(infoCurriculum);
		}
		component.setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curricularCourse));

		return component;
	}

}