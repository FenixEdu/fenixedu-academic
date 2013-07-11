package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;


import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadSite {

    @Atomic
    public static InfoSite run(InfoExecutionCourse infoExecutionCourse) {
        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(infoExecutionCourse.getExternalId());
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