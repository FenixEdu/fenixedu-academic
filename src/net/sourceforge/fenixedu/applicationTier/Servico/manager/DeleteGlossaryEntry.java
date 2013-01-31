/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz
 */
public class DeleteGlossaryEntry extends FenixService {

	@Checked("RolePredicates.MANAGER_PREDICATE")
	@Service
	public static void run(Integer entryId) {
		rootDomainObject.readGlossaryEntryByOID(entryId).delete();
	}

}