/*
 * Created on 13/Mar/2003 To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoSite;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.ISite;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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