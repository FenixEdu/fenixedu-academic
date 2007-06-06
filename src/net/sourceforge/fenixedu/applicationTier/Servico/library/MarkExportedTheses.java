package net.sourceforge.fenixedu.applicationTier.Servico.library;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

public class MarkExportedTheses extends Service {

	public void run(Collection<Thesis> thesis, Boolean value) {
		for (Thesis t : thesis) {
			t.setLibraryExported(value);
		}
	}
	
}
