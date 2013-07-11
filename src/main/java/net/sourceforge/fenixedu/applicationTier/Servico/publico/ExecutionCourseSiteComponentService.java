/*
 * Created on 6/Mai/2003
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.Factory.ExecutionCourseSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Filtro.PublishedExamsMapAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingAssociatedCurricularCoursesServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Jo�o Mota
 * 
 * 
 */
public class ExecutionCourseSiteComponentService {

    protected ExecutionCourseSiteView run(ISiteComponent commonComponent, ISiteComponent bodyComponent, String infoSiteCode,
            String infoExecutionCourseCode, Integer sectionIndex, Integer curricularCourseId) throws FenixServiceException,
            NonExistingAssociatedCurricularCoursesServiceException {
        final ExecutionCourseSite site;
        if (infoSiteCode != null) {
            site = ExecutionCourseSite.readExecutionCourseSiteByOID(infoSiteCode);
        } else {
            final ExecutionCourse executionCourse = FenixFramework.getDomainObject(infoExecutionCourseCode);
            site = executionCourse.getSite();
        }

        if (site == null) {
            throw new NonExistingServiceException();
        }

        ExecutionCourseSiteComponentBuilder componentBuilder = ExecutionCourseSiteComponentBuilder.getInstance();
        commonComponent = componentBuilder.getComponent(commonComponent, site, null, null, null);
        bodyComponent = componentBuilder.getComponent(bodyComponent, site, commonComponent, sectionIndex, curricularCourseId);
        final ExecutionCourseSiteView executionCourseSiteView = new ExecutionCourseSiteView(commonComponent, bodyComponent);
        executionCourseSiteView.setExecutionCourse(site.getExecutionCourse());

        if (!AccessControl.getUserView().hasRoleType(RoleType.RESOURCE_ALLOCATION_MANAGER)) {
            PublishedExamsMapAuthorizationFilter.execute(executionCourseSiteView);
        }

        return executionCourseSiteView;
    }

    // Service Invokers migrated from Berserk

    private static final ExecutionCourseSiteComponentService serviceInstance = new ExecutionCourseSiteComponentService();

    @Service
    public static ExecutionCourseSiteView runExecutionCourseSiteComponentService(ISiteComponent commonComponent,
            ISiteComponent bodyComponent, String infoSiteCode, String infoExecutionCourseCode, Integer sectionIndex,
            Integer curricularCourseId) throws FenixServiceException, NonExistingAssociatedCurricularCoursesServiceException {
        return serviceInstance.run(commonComponent, bodyComponent, infoSiteCode, infoExecutionCourseCode, sectionIndex,
                curricularCourseId);
    }

}