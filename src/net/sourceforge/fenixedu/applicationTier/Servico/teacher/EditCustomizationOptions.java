package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

public class EditCustomizationOptions implements IService {

    public boolean run(Integer infoExecutionCourseCode, InfoSite infoSiteNew)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        Site siteOld = sp.getIPersistentSite().readByExecutionCourse(infoExecutionCourseCode);

        siteOld.setAlternativeSite(infoSiteNew.getAlternativeSite());
        siteOld.setMail(infoSiteNew.getMail());
        siteOld.setInitialStatement(infoSiteNew.getInitialStatement());
        siteOld.setIntroduction(infoSiteNew.getIntroduction());

        return true;
    }

}
