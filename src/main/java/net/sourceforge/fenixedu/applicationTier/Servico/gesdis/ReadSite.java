package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;


import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class ReadSite {

    @Service
    public static InfoSite run(InfoExecutionCourse infoExecutionCourse) {
        final ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(infoExecutionCourse.getIdInternal());
        final ExecutionCourseSite site = executionCourse.getSite();
        if (site != null) {
            final InfoSite infoSite = InfoSite.newInfoFromDomain(site);
            infoSite.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
            return infoSite;
        } else {
            return null;
        }
    }

}