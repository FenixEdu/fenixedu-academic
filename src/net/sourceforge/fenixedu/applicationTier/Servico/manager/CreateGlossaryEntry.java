/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.support.InfoGlossaryEntry;
import net.sourceforge.fenixedu.domain.support.GlossaryEntry;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz
 */
public class CreateGlossaryEntry extends FenixService {

	@Checked("RolePredicates.MANAGER_PREDICATE")
	@Service
	public static void run(InfoGlossaryEntry infoGlossaryEntry) {
		GlossaryEntry glossaryEntry = new GlossaryEntry();
		glossaryEntry.setTerm(infoGlossaryEntry.getTerm());
		glossaryEntry.setDefinition(infoGlossaryEntry.getDefinition());
	}

}