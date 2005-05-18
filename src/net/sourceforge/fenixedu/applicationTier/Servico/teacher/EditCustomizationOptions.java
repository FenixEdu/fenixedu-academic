package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 */
public class EditCustomizationOptions implements IService {

    public boolean run(Integer infoExecutionCourseCode, InfoSite infoSiteNew)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentSite persistentSite = sp.getIPersistentSite();

        ISite siteOld = persistentSite.readByExecutionCourse(infoExecutionCourseCode);
        persistentSite.simpleLockWrite(siteOld);

        siteOld.setAlternativeSite(infoSiteNew.getAlternativeSite());
        siteOld.setMail(infoSiteNew.getMail());
        siteOld.setInitialStatement(infoSiteNew.getInitialStatement());
        siteOld.setIntroduction(infoSiteNew.getIntroduction());

        return true;
    }
}