/*
 * Created on 23/Jul/2003
 *
 * 
 */
package ServidorAplicacao.Factory;

import DataBeans.ISiteComponent;
import DataBeans.InfoCurriculum;
import DataBeans.InfoSiteCurriculum;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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

    public ISiteComponent getComponent(ISiteComponent component,
            Integer curricularCourseId, Integer curriculumId)
            throws FenixServiceException {
        if (component instanceof InfoSiteCurriculum) {
            return getInfoSiteCurriculum((InfoSiteCurriculum) component,
                    curricularCourseId);
        } else {
            System.out.println("dei bronca aqui");
            return null;
        }

    }

    /**
     * @param curriculum
     * @param curricularCourseId
     * @return
     */
    private ISiteComponent getInfoSiteCurriculum(InfoSiteCurriculum component,
            Integer curricularCourseId) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCurriculum persistentCurriculum = sp
                    .getIPersistentCurriculum();
            IPersistentCurricularCourse persistentCurricularCourse = sp
                    .getIPersistentCurricularCourse();
            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseId);
            ICurriculum curriculum = persistentCurriculum
                    .readCurriculumByCurricularCourse(curricularCourse);
            if (curriculum != null) {
                InfoCurriculum infoCurriculum = Cloner
                        .copyICurriculum2InfoCurriculum(curriculum);
                component.setInfoCurriculum(infoCurriculum);
            }
            component
                    .setInfoCurricularCourse(Cloner
                            .copyCurricularCourse2InfoCurricularCourse(curricularCourse));
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return component;
    }

}