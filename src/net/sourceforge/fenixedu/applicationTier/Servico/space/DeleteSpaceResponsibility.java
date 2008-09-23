package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.space.SpaceResponsibility;

public class DeleteSpaceResponsibility extends FenixService {

    public boolean run(SpaceResponsibility spaceResponsibility) {
	if (spaceResponsibility != null) {
	    spaceResponsibility.delete();
	}
	return true;
    }
}
