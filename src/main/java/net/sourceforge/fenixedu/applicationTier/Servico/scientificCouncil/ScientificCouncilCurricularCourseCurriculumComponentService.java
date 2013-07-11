/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import net.sourceforge.fenixedu.applicationTier.Factory.ScientificCouncilCurricularCourseCurriculumComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

/**
 * @author João Mota
 * 
 *         23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 * 
 */
public class ScientificCouncilCurricularCourseCurriculumComponentService {

    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    @Atomic
    public static SiteView run(ISiteComponent bodyComponent, String curricularCourseId, Integer curriculumId)
            throws FenixServiceException {

        ScientificCouncilCurricularCourseCurriculumComponentBuilder componentBuilder =
                ScientificCouncilCurricularCourseCurriculumComponentBuilder.getInstance();
        bodyComponent = componentBuilder.getComponent(bodyComponent, curricularCourseId, curriculumId);
        SiteView siteView = new SiteView();
        siteView.setComponent(bodyComponent);

        return siteView;
    }

}