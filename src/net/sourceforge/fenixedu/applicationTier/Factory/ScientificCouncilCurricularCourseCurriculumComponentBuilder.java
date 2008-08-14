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
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Mota
 * 
 *         23/Jul/2003 fenix-head ServidorAplicacao.Factory
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

    public ISiteComponent getComponent(ISiteComponent component, Integer curricularCourseId, Integer curriculumId)
	    throws FenixServiceException {
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
	    throws FenixServiceException {
	CurricularCourse curricularCourse = (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(
		curricularCourseId);

	Curriculum curriculum = curricularCourse.findLatestCurriculum();

	if (curriculum != null) {
	    InfoCurriculum infoCurriculum = InfoCurriculum.newInfoFromDomain(curriculum);
	    component.setInfoCurriculum(infoCurriculum);
	}
	component.setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curricularCourse));

	return component;
    }

}