/*
 * Created on 13/Mar/2003 To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author PTRLV
 */
public class EditSite implements IService {

    public EditSite() {
    }

    public Boolean run(InfoSite infoSiteOld, InfoSite infoSiteNew) throws FenixServiceException {
        IPersistentSite persistentSite = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            persistentSite = sp.getIPersistentSite();
            ISite siteOld = Cloner.copyInfoSite2ISite(infoSiteOld);
            IExecutionCourse executionCourse = siteOld.getExecutionCourse();
            siteOld = persistentSite.readByExecutionCourse(executionCourse);

            persistentSite.simpleLockWrite(siteOld);
            siteOld.setAlternativeSite(infoSiteNew.getAlternativeSite());
            siteOld.setMail(infoSiteNew.getMail());
            siteOld.setInitialStatement(infoSiteNew.getInitialStatement());
            siteOld.setIntroduction(infoSiteNew.getIntroduction());

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return new Boolean(true);
    }
}