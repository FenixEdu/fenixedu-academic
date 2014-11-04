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
package org.fenixedu.academic.service.services.thesis;

import org.fenixedu.academic.domain.accessControl.ScientificCommissionGroup;
import org.fenixedu.academic.domain.accessControl.ThesisReadersGroup;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisFile;
import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;

public class MakeThesisDocumentsAvailable {

    @Atomic
    public static void run(Thesis thesis) {
        final ThesisFile thesisFile = thesis.getDissertation();

        Group scientificCouncil = RoleType.SCIENTIFIC_COUNCIL.actualGroup();
        Group commissionMembers = ScientificCommissionGroup.get(thesis.getDegree());
        Group student = thesis.getStudent().getPerson().getPersonGroup();
        Group thesisGroup = ThesisReadersGroup.get(thesis);

        thesisFile.setPermittedGroup(scientificCouncil.or(commissionMembers).or(student).or(thesisGroup));
    }
}