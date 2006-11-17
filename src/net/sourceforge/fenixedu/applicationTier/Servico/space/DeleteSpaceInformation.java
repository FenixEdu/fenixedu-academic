package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;

public class DeleteSpaceInformation extends Service {

    public void run(final SpaceInformation spaceInformation) {
	if(spaceInformation != null) {
	    spaceInformation.delete();
	}
    }

}
