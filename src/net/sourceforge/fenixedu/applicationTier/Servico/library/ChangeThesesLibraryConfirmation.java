package net.sourceforge.fenixedu.applicationTier.Servico.library;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

public class ChangeThesesLibraryConfirmation extends Service {

	public void run(List<Thesis> theses, Boolean value) {
		for (Thesis thesis : theses) {
			thesis.setLibraryConfirmation(value);	
		}
	}
	
}
