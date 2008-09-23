package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.space.Space;

public class DeleteSpace extends FenixService {

    public void run(final Space space) {
	if (space != null) {
	    space.delete();
	}
    }
}
