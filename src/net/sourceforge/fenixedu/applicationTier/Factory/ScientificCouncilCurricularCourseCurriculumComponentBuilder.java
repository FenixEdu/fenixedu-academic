/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Factory;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
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
            Integer curriculumId) throws FenixServiceException {
        if (component instanceof InfoSiteCurriculum) {
            return getInfoSiteCurriculum((InfoSiteCurriculum) component, curricularCourseId);
        }
        return null;

    }

    /**
     * @param curriculum
     * @param curricularCourseId
     * @return
     */
    private ISiteComponent getInfoSiteCurriculum(InfoSiteCurriculum component, Integer curricularCourseId)
            throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseId);
            ICurriculum curriculum = persistentCurriculum
                    .readCurriculumByCurricularCourse(curricularCourse);
            if (curriculum != null) {
                InfoCurriculum infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);
                component.setInfoCurriculum(infoCurriculum);
            }
            component.setInfoCurricularCourse(Cloner
                    .copyCurricularCourse2InfoCurricularCourse(curricularCourse));
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return component;
    }

}