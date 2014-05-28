/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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