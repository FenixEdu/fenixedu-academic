package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSectionWithAll;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadSections extends Service {

	public List<InfoSection> run(InfoSite infoSite) throws FenixServiceException, ExcepcaoPersistencia {
        
		final Site site = ExecutionCourseSite.readExecutionCourseSiteByOID(infoSite.getIdInternal());
		
        final List<InfoSection> infoSections = new ArrayList<InfoSection>(site.getAssociatedSectionsCount());
        for (final Section section : site.getAssociatedSectionsSet()) {
            infoSections.add(InfoSectionWithAll.newInfoFromDomain(section));
        }
        
		return infoSections;
	}
}