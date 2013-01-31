package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class AddParentInherentFunction extends FenixService {

	@Checked("RolePredicates.MANAGER_PREDICATE")
	@Service
	public static void run(Integer functionID, Integer parentInherentFunctionID) throws FenixServiceException, DomainException {

		Function parentInherentFunction = (Function) rootDomainObject.readAccountabilityTypeByOID(parentInherentFunctionID);

		if (parentInherentFunction == null) {
			throw new FenixServiceException("error.no.parentInherentFunction");
		}

		Function function = (Function) rootDomainObject.readAccountabilityTypeByOID(functionID);
		if (function == null) {
			throw new FenixServiceException("error.noFunction");
		}

		function.addParentInherentFunction(parentInherentFunction);
	}
}