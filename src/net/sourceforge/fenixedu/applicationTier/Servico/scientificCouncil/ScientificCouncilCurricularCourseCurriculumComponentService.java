/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Factory.ScientificCouncilCurricularCourseCurriculumComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 *  
 */
public class ScientificCouncilCurricularCourseCurriculumComponentService implements IServico {

    private static ScientificCouncilCurricularCourseCurriculumComponentService _servico = new ScientificCouncilCurricularCourseCurriculumComponentService();

    /**
     * The actor of this class.
     */

    private ScientificCouncilCurricularCourseCurriculumComponentService() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "ScientificCouncilCurricularCourseCurriculumComponentService";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadExecutionCourse
     */
    public static ScientificCouncilCurricularCourseCurriculumComponentService getService() {
        return _servico;
    }

    public SiteView run(ISiteComponent bodyComponent, Integer curricularCourseId, Integer curriculumId)
            throws FenixServiceException {

        ScientificCouncilCurricularCourseCurriculumComponentBuilder componentBuilder = ScientificCouncilCurricularCourseCurriculumComponentBuilder
                .getInstance();
        bodyComponent = componentBuilder.getComponent(bodyComponent, curricularCourseId, curriculumId);
        SiteView siteView = new SiteView();
        siteView.setComponent(bodyComponent);

        return siteView;
    }

}