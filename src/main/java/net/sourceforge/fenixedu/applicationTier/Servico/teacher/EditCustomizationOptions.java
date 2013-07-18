package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ContentManagementLog;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class EditCustomizationOptions {

    protected Boolean run(Integer infoExecutionCourseCode, final String alternativeSite, final String mail,
            final Boolean dynamicMailDistribution, final String initialStatement, final String introduction)
            throws FenixServiceException {
        final ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(infoExecutionCourseCode);
        final ExecutionCourseSite site = executionCourse.getSite();

        site.setAlternativeSite(alternativeSite);
        site.setMail(mail);
        site.setInitialStatement(initialStatement);
        site.setIntroduction(introduction);
        site.setDynamicMailDistribution(dynamicMailDistribution);

        ContentManagementLog.createLog(executionCourse, "resources.MessagingResources",
                "log.executionCourse.content.customization.edited", executionCourse.getNome(),
                executionCourse.getDegreePresentationString());
        return true;
    }

    // Service Invokers migrated from Berserk

    private static final EditCustomizationOptions serviceInstance = new EditCustomizationOptions();

    @Service
    public static Boolean runEditCustomizationOptions(Integer infoExecutionCourseCode, String alternativeSite, String mail,
            Boolean dynamicMailDistribution, String initialStatement, String introduction) throws FenixServiceException,
            NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(infoExecutionCourseCode);
        return serviceInstance.run(infoExecutionCourseCode, alternativeSite, mail, dynamicMailDistribution, initialStatement,
                introduction);
    }

}