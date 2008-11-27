package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.space.MaterialSpaceOccupation;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteMaterialSpaceOccupation<T extends MaterialSpaceOccupation> extends FenixService {

    @Checked("RolePredicates.SPACE_MANAGER_PREDICATE")
    @Service
    public void run(T t) {
	if (t != null) {
	    t.delete();
	}
    }
}