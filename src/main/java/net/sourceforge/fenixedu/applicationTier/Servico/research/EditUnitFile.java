package net.sourceforge.fenixedu.applicationTier.Servico.research;

import net.sourceforge.fenixedu.domain.UnitFile;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;

public class EditUnitFile {
    @Atomic
    public static void run(UnitFile file, String name, String description, String tags, Group group) {
        file.setDisplayName(name);
        file.setDescription(description);
        file.setPermittedGroup(group.grant(file.getUploader().getUser()));
        file.setUnitFileTags(tags);
    }
}