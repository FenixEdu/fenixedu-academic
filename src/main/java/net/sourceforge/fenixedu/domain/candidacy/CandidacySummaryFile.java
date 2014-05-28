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
package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

public class CandidacySummaryFile extends CandidacySummaryFile_Base {

    public CandidacySummaryFile(String fileName, byte[] fileByteArray, StudentCandidacy studentCandidacy) {
        super();
        init(fileName, fileName, fileByteArray, createPermittedGroup(studentCandidacy.getPerson()));
        setStudentCandidacy(studentCandidacy);
    }

    private Group createPermittedGroup(Person candidacyStudent) {
        return UserGroup.of(candidacyStudent.getUser()).or(RoleGroup.get(RoleType.ADMINISTRATOR));
    }

    @Deprecated
    public boolean hasStudentCandidacy() {
        return getStudentCandidacy() != null;
    }

}
