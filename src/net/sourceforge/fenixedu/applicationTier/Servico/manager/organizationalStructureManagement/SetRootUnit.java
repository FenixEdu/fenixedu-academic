package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class SetRootUnit extends FenixService {

	@Checked("RolePredicates.MANAGER_PREDICATE")
	@Service
	public static void run(final Unit unit, final Boolean institutionUnit) {

		if (unit.isPlanetUnit()) {
			rootDomainObject.setEarthUnit(unit);

		} else if (institutionUnit) {
			rootDomainObject.setInstitutionUnit(unit);

		} else if (!institutionUnit) {
			rootDomainObject.setExternalInstitutionUnit(unit);
		}
	}
}