package ServidorAplicacao.Servico.gesdis;

/**
 * Created on 19/03/2003
 * 
 * @author lmac1
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoSection;
import DataBeans.InfoSite;
import DataBeans.util.Cloner;
import Dominio.ISection;
import Dominio.ISite;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadSectionsBySiteAndSuperiorSection implements IService {

    /**
     * Executes the service. Returns the current collection of all infosections
     * that belong to a site.
     */
    public List run(InfoSite infoSite, InfoSection infoSuperiorSection) throws FenixServiceException {

        ISite site = Cloner.copyInfoSite2ISite(infoSite);
        ISuportePersistente sp;
        List allSections = null;

        ISection superiorSection = null;
        if (infoSuperiorSection != null) {
            superiorSection = Cloner.copyInfoSection2ISection(infoSuperiorSection);
            superiorSection.setSite(site);
        }

        try {
            sp = SuportePersistenteOJB.getInstance();
            allSections = sp.getIPersistentSection().readBySiteAndSection(site, superiorSection);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        List result = new ArrayList();
        if (allSections != null) {
            // build the result of this service
            Iterator iterator = allSections.iterator();

            while (iterator.hasNext())
                result.add(Cloner.copyISection2InfoSection((ISection) iterator.next()));
        }

        return result;
    }
}