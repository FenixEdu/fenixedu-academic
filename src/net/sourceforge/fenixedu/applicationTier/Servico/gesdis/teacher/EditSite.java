/*
 * Created on 13/Mar/2003 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author PTRLV
 */
public class EditSite implements IService {

    public Boolean run(InfoSite infoSiteOld, InfoSite infoSiteNew) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentSite persistentSite = sp.getIPersistentSite();
        ISite siteOld = persistentSite.readByExecutionCourse(infoSiteOld.getInfoExecutionCourse()
                .getIdInternal());

        persistentSite.simpleLockWrite(siteOld);
        siteOld.setAlternativeSite(infoSiteNew.getAlternativeSite());
        siteOld.setMail(infoSiteNew.getMail());
        siteOld.setInitialStatement(infoSiteNew.getInitialStatement());
        siteOld.setIntroduction(infoSiteNew.getIntroduction());

        return new Boolean(true);
    }
}