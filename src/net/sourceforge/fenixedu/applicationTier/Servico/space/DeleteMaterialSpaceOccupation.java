package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.space.MaterialSpaceOccupation;

public class DeleteMaterialSpaceOccupation<T extends MaterialSpaceOccupation> extends FenixService {

    public void run(T t) {
	if (t != null) {
	    t.delete();
	}
    }
}
