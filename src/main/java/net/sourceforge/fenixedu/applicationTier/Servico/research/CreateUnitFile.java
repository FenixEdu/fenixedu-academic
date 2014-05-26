package net.sourceforge.fenixedu.applicationTier.Servico.research;

import java.io.File;
import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

import pt.ist.fenixframework.Atomic;

import com.google.common.io.Files;

public class CreateUnitFile {

    private static byte[] read(final File file) {
        try {
            return Files.toByteArray(file);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    @Atomic
    public static void run(java.io.File file, String originalFilename, String displayName, String description, String tags,
            Group permittedGroup, Unit unit, Person person) throws FenixServiceException {

        final byte[] content = read(file);
        new UnitFile(unit, person, description, tags, originalFilename, displayName, content,
                !isPublic(permittedGroup) ? permittedGroup.or(UserGroup.of(person.getUser())) : permittedGroup);
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