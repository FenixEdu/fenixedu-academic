package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditCustomizationOptions extends Service {

    public boolean run(Integer infoExecutionCourseCode, InfoSite infoSiteNew)
            throws FenixServiceException, ExcepcaoPersistencia {
        Site siteOld = persistentSupport.getIPersistentSite().readByExecutionCourse(infoExecutionCourseCode);

        siteOld.setAlternativeSite(infoSiteNew.getAlternativeSite());
        siteOld.setMail(infoSiteNew.getMail());
        siteOld.setInitialStatement(infoSiteNew.getInitialStatement());
        siteOld.setIntroduction(infoSiteNew.getIntroduction());

        return true;
    }

}
