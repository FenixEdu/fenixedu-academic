/*
 * Created on 13/Mar/2003 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;

/**
 * @author PTRLV
 */
public class EditSite extends Service {

    public Boolean run(InfoSite infoSiteOld, InfoSite infoSiteNew) throws FenixServiceException,
            ExcepcaoPersistencia {
        IPersistentSite persistentSite = persistentSupport.getIPersistentSite();
        Site siteOld = persistentSite.readByExecutionCourse(infoSiteOld.getInfoExecutionCourse()
                .getIdInternal());

        siteOld.edit(infoSiteNew.getInitialStatement(), infoSiteNew.getIntroduction(), infoSiteNew
                .getMail(), infoSiteNew.getAlternativeSite());

        return true;
    }
}