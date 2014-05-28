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
package net.sourceforge.fenixedu.domain.credits;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UnionGroup;
import org.fenixedu.bennu.core.groups.UserGroup;

public class AnnualTeachingCreditsDocument extends AnnualTeachingCreditsDocument_Base {

    public AnnualTeachingCreditsDocument(AnnualTeachingCredits annualTeachingCredits, byte[] content,
            boolean hasConfidencialInformation) {
        super();
        String filename = getFilename(annualTeachingCredits);

        final Set<Group> groups = new HashSet<>();
        final Teacher teacher = annualTeachingCredits.getTeacher();
        if (teacher != null) {
            final Person person = teacher.getPerson();
            if (person != null) {
                groups.add(UserGroup.of(person.getUser()));
            }
        }
        groups.add(RoleGroup.get(RoleType.SCIENTIFIC_COUNCIL));

        init(filename, filename, content, UnionGroup.of(groups));
        setAnnualTeachingCredits(annualTeachingCredits);
        setHasConfidencialInformation(hasConfidencialInformation);
    }

    private String getFilename(AnnualTeachingCredits annualTeachingCredits) {
        return (annualTeachingCredits.getTeacher().getPerson().getIstUsername() + "_"
                + annualTeachingCredits.getAnnualCreditsState().getExecutionYear().getYear() + ".pdf").replaceAll(" ", "_")
                .replaceAll("/", "_");
    }

    @Deprecated
    public boolean hasHasConfidencialInformation() {
        return getHasConfidencialInformation() != null;
    }

    @Deprecated
    public boolean hasAnnualTeachingCredits() {
        return getAnnualTeachingCredits() != null;
    }

}
