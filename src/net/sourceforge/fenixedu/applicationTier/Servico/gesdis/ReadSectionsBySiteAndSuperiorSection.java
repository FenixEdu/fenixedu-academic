package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

/**
 * Created on 19/03/2003
 * 
 * @author lmac1
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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