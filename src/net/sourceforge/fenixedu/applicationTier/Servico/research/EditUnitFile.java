package net.sourceforge.fenixedu.applicationTier.Servico.research;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class EditUnitFile extends Service {
	
	public void run(UnitFile file, String name, String description, IGroup group) {
		file.setDisplayName(name);
		file.setDescription(description);
		file.setPermittedGroup((Group)group);
	}
}
