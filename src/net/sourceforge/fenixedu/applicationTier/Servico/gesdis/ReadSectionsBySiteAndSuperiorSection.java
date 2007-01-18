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

public class ReadSectionsBySiteAndSuperiorSection extends Service {

	public List<InfoSection> run(InfoSite infoSite, InfoSection infoSuperiorSection) throws FenixServiceException, ExcepcaoPersistencia {
		final Site site = ExecutionCourseSite.readExecutionCourseSiteByOID(infoSite.getIdInternal());
		if (site == null) {
            		throw new FenixServiceException("error.noSite");     
        	}
        
        final Section parentSection = (infoSuperiorSection == null) ? null :
            rootDomainObject.readSectionByOID(infoSuperiorSection.getIdInternal());
        
        final List<InfoSection> result = new ArrayList<InfoSection>();
        for (final Section section : site.getAssociatedSections(parentSection)) {
            result.add(InfoSectionWithAll.newInfoFromDomain(section));
        }

		return result;
	}

}
