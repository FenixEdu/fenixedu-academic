package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.support.InfoGlossaryEntry;
import net.sourceforge.fenixedu.domain.support.GlossaryEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadGlossaryEntries extends Service {

    public List<InfoGlossaryEntry> run() {
	List<InfoGlossaryEntry> result = new ArrayList<InfoGlossaryEntry>();

	for (GlossaryEntry glossaryEntry : rootDomainObject.getGlossaryEntrys()) {
	    result.add(InfoGlossaryEntry.newInfoFromDomain(glossaryEntry));
	}

	return result;
    }

}
