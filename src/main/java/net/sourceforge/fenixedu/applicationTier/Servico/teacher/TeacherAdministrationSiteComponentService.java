package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Fernanda Quitério
 * 
 * 
 */
public class TeacherAdministrationSiteComponentService {

    protected TeacherAdministrationSiteView run(String infoExecutionCourseCode, ISiteComponent commonComponent,
            ISiteComponent bodyComponent, Object obj1, Object obj2) throws FenixServiceException {

        final ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(infoExecutionCourseCode);
        final ExecutionCourseSite site = executionCourse.getSite();

        final TeacherAdministrationSiteComponentBuilder componentBuilder =
                TeacherAdministrationSiteComponentBuilder.getInstance();
        commonComponent = componentBuilder.getComponent(commonComponent, site, null, null, null);
        bodyComponent = componentBuilder.getComponent(bodyComponent, site, commonComponent, obj1, obj2);

        return new TeacherAdministrationSiteView(commonComponent, bodyComponent);
    }

    // Service Invokers migrated from Berserk

    private static final TeacherAdministrationSiteComponentService serviceInstance =
            new TeacherAdministrationSiteComponentService();

    @Service
    public static TeacherAdministrationSiteView runTeacherAdministrationSiteComponentService(String infoExecutionCourseCode,
            ISiteComponent commonComponent, ISiteComponent bodyComponent, Object obj1, Object obj2) throws FenixServiceException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute();
        return serviceInstance.run(infoExecutionCourseCode, commonComponent, bodyComponent, obj1, obj2);
    }

}