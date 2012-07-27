package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class DegreeProjectTutorialService extends DegreeProjectTutorialService_Base {

    public DegreeProjectTutorialService(Professorship professorship) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setProfessorship(professorship);
    }

}
