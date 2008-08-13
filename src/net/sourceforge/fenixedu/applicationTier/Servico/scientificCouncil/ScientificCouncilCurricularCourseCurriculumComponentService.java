/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Factory.ScientificCouncilCurricularCourseCurriculumComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 *  
 */
public class ScientificCouncilCurricularCourseCurriculumComponentService extends Service {

    public SiteView run(ISiteComponent bodyComponent, Integer curricularCourseId, Integer curriculumId)
            throws FenixServiceException{

        ScientificCouncilCurricularCourseCurriculumComponentBuilder componentBuilder = ScientificCouncilCurricularCourseCurriculumComponentBuilder
                .getInstance();
        bodyComponent = componentBuilder.getComponent(bodyComponent, curricularCourseId, curriculumId);
        SiteView siteView = new SiteView();
        siteView.setComponent(bodyComponent);

        return siteView;
    }

}