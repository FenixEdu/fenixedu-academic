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
import net.sourceforge.fenixedu.dataTransferObject.InfoSectionWithAll;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
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
	 * 
	 * @throws ExcepcaoPersistencia
	 */
	public List run(InfoSite infoSite, InfoSection infoSuperiorSection) throws FenixServiceException,
			ExcepcaoPersistencia {

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		Site site = (Site) sp.getIPersistentSite().readByOID(Site.class, infoSite.getIdInternal());
		List allSections = null;

		Section superiorSection = null;
		if (infoSuperiorSection != null) {
			superiorSection = (Section) sp.getIPersistentSection().readByOID(Section.class,
					infoSuperiorSection.getIdInternal());
			superiorSection.setSite(site);
		}

		if (superiorSection != null) {
			allSections = sp.getIPersistentSection().readBySiteAndSection(
					site.getExecutionCourse().getSigla(),
					site.getExecutionCourse().getExecutionPeriod().getName(),
					site.getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear(),
					superiorSection.getIdInternal());
		} else {
			allSections = sp.getIPersistentSection().readBySiteAndSection(
					site.getExecutionCourse().getSigla(),
					site.getExecutionCourse().getExecutionPeriod().getName(),
					site.getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear(), null);
		}

		List result = new ArrayList();
		if (allSections != null) {
			// build the result of this service
			Iterator iterator = allSections.iterator();

			while (iterator.hasNext())
				result.add(InfoSectionWithAll.newInfoFromDomain((Section) iterator.next()));
		}

		return result;
	}
}