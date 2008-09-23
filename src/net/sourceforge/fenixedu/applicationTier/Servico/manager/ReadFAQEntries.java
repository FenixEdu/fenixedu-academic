package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQEntry;
import net.sourceforge.fenixedu.domain.support.FAQEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadFAQEntries extends FenixService {

    public Collection run() {
	List<InfoFAQEntry> result = new ArrayList<InfoFAQEntry>();

	for (FAQEntry faqEntry : rootDomainObject.getFAQEntrys()) {
	    result.add(InfoFAQEntry.newInfoFromDomain(faqEntry));
	}

	return result;
    }

}
