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
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadSectionsBySiteAndSuperiorSection implements IService {

    /**
     * Executes the service. Returns the current collection of all infosections
     * that belong to a site.
     * @throws ExcepcaoPersistencia
     */
    public List run(InfoSite infoSite, InfoSection infoSuperiorSection) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ISite site = (ISite) sp.getIPersistentSite().readByOID(Site.class, infoSite.getIdInternal());
        List allSections = null;

        ISection superiorSection = null;
        if (infoSuperiorSection != null) {
            superiorSection = (ISection) sp.getIPersistentSection().readByOID(Section.class, infoSuperiorSection.getIdInternal());
            superiorSection.setSite(site);
        }

        try {
            if(superiorSection != null){
	            allSections = sp.getIPersistentSection().readBySiteAndSection(site.getExecutionCourse().getSigla(),
	                    site.getExecutionCourse().getExecutionPeriod().getName(),
	                    site.getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear(),
	                    superiorSection.getIdInternal());
            }
            else{
                allSections = sp.getIPersistentSection().readBySiteAndSection(site.getExecutionCourse().getSigla(),
	                    site.getExecutionCourse().getExecutionPeriod().getName(),
	                    site.getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear(),
	                    null);
            }
                

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