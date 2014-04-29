package net.sourceforge.fenixedu.applicationTier.Servico.research;

import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import pt.ist.fenixframework.Atomic;

public class EditUnitFile {

    @Atomic
    public static void run(UnitFile file, String name, String description, String tags, IGroup group) {
        file.setDisplayName(name);
        file.setDescription(description);
        file.setPermittedGroup(!isPublic((Group) group) ? new GroupUnion(group, new PersonGroup(file.getUploader())) : (Group) group);
        file.setUnitFileTags(tags);
    }

    private static boolean isPublic(Group permittedGroup) {
        if (permittedGroup == null) {
            return true;
        }

        if (permittedGroup.isMember(null)) {
            return true;
        }

        return false;
    }
}