package net.sourceforge.fenixedu.applicationTier.Servico.research;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class EditUnitFile extends Service {

    public void run(UnitFile file, String name, String description, String tags, IGroup group) {
	file.setDisplayName(name);
	file.setDescription(description);
	file.setPermittedGroup(!isPublic((Group) group) ? new GroupUnion(group, new PersonGroup(file.getUploader()))
		: (Group) group);
	file.setUnitFileTags(tags);
    }

    private boolean isPublic(Group permittedGroup) {
	if (permittedGroup == null) {
	    return true;
	}

	if (permittedGroup instanceof EveryoneGroup) {
	    return true;
	}

	return false;
    }
}
