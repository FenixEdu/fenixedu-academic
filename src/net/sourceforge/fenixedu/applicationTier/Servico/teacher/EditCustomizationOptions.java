package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditCustomizationOptions extends Service {

    public boolean run(Integer infoExecutionCourseCode, InfoSite infoSiteNew)
            throws FenixServiceException, ExcepcaoPersistencia {
    	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID( infoExecutionCourseCode);
        final Site site = executionCourse.getSite();

        site.setAlternativeSite(infoSiteNew.getAlternativeSite());
        site.setMail(infoSiteNew.getMail());
        site.setInitialStatement(infoSiteNew.getInitialStatement());
        site.setIntroduction(infoSiteNew.getIntroduction());
        site.setDynamicMailDistribution(infoSiteNew.getDynamicMailDistribution());

        return true;
    }

}
