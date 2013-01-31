package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.domain.space.MaterialSpaceOccupation;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteMaterialSpaceOccupation {

	@Checked("RolePredicates.SPACE_MANAGER_PREDICATE")
	@Service
	public static void run(MaterialSpaceOccupation materialSpaceOccupation) {
		if (materialSpaceOccupation != null) {
			materialSpaceOccupation.delete();
		}
	}
}