/*
 * Created on 6/Mai/2003
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Factory.ExecutionCourseSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingAssociatedCurricularCoursesServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Jo�o Mota
 * 
 *  
 */
public class ExecutionCourseSiteComponentService extends Service {

    public Object run(ISiteComponent commonComponent, ISiteComponent bodyComponent,
            Integer infoSiteCode, Integer infoExecutionCourseCode, Integer sectionIndex,
            Integer curricularCourseId) throws FenixServiceException,
            NonExistingAssociatedCurricularCoursesServiceException, ExcepcaoPersistencia {
        final ExecutionCourseSite site;
        if (infoSiteCode != null)
            site = ExecutionCourseSite.readExecutionCourseSiteByOID(infoSiteCode);
        else {
        	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID( infoExecutionCourseCode);
            site = executionCourse.getSite();
        }

        if (site == null) {
            throw new NonExistingServiceException();
        }

        ExecutionCourseSiteComponentBuilder componentBuilder = ExecutionCourseSiteComponentBuilder
                .getInstance();
        commonComponent = componentBuilder.getComponent(commonComponent, site, null, null, null);
        bodyComponent = componentBuilder.getComponent(bodyComponent, site, commonComponent,
                sectionIndex, curricularCourseId);
        final ExecutionCourseSiteView executionCourseSiteView = new ExecutionCourseSiteView(commonComponent, bodyComponent);
        executionCourseSiteView.setExecutionCourse(site.getExecutionCourse());
        return executionCourseSiteView;
    }
}