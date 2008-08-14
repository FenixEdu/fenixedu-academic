package net.sourceforge.fenixedu.applicationTier.Servico.library;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;

public class MarkExportedTheses extends Service {

    public void run(Collection<Thesis> theses, Boolean value) {
	for (Thesis thesis : theses) {
	    thesis.setLibraryExported(value);
	}
    }

}
