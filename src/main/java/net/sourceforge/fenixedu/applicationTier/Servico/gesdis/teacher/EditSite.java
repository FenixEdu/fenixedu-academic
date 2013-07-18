/*
 * Created on 13/Mar/2003 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;


import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author PTRLV
 */
public class EditSite {

    protected Boolean run(InfoSite infoSiteOld, final String alternativeSite, final String mail, final String initialStatement,
            final String introduction) throws FenixServiceException {
        final ExecutionCourse executionCourse =
                RootDomainObject.getInstance().readExecutionCourseByOID(infoSiteOld.getInfoExecutionCourse().getIdInternal());
        final ExecutionCourseSite site = executionCourse.getSite();

        site.edit(initialStatement, introduction, mail, alternativeSite);

        return true;
    }

    // Service Invokers migrated from Berserk

    private static final EditSite serviceInstance = new EditSite();

    @Service
    public static Boolean runEditSite(InfoSite infoSiteOld, String alternativeSite, String mail, String initialStatement,
            String introduction) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(infoSiteOld.getInfoExecutionCourse().getIdInternal());
        return serviceInstance.run(infoSiteOld, alternativeSite, mail, initialStatement, introduction);
    }

}