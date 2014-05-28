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
package net.sourceforge.fenixedu.domain.candidacyProcess;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.NobodyGroup;

import pt.ist.fenixframework.Atomic;

public class IndividualCandidacyDocumentFile extends IndividualCandidacyDocumentFile_Base {

    protected IndividualCandidacyDocumentFile() {
        super();
        this.setCandidacyFileActive(Boolean.TRUE);
    }

    protected IndividualCandidacyDocumentFile(IndividualCandidacyDocumentFileType type, byte[] contents, String filename) {
        this();
        this.setCandidacyFileActive(Boolean.TRUE);
        setCandidacyFileType(type);
        init(filename, filename, contents, NobodyGroup.get());
    }

    @Atomic
    public static IndividualCandidacyDocumentFile createCandidacyDocument(byte[] contents, String filename,
            IndividualCandidacyDocumentFileType type, String processName, String documentIdNumber) {
        return new IndividualCandidacyDocumentFile(type, contents, filename);
    }

    @Override
    public boolean isPersonAllowedToAccess(Person person) {
        if (person == null) {
            return false;
        }

        // Academic Administration Permissions
        for (AcademicProgram program : AcademicAuthorizationGroup.getProgramsForOperation(person,
                AcademicOperationType.MANAGE_CANDIDACY_PROCESSES)) {
            for (IndividualCandidacy individualCandidacy : getIndividualCandidacySet()) {
                if (individualCandidacy.getAllDegrees().contains(program)) {
                    return true;
                }
            }
        }

        // International Relation Office permissions
        if (person.hasRole(RoleType.INTERNATIONAL_RELATION_OFFICE)) {
            return true;
        }

        // Coordinators
        for (IndividualCandidacy individualCandidacy : getIndividualCandidacySet()) {
            for (Degree degree : individualCandidacy.getAllDegrees()) {
                if (degree.isCurrentCoordinator(person)) {
                    return true;
                }
            }
        }

        // Mobility Coordinators
        if (person.getTeacher() != null && !person.getTeacher().getMobilityCoordinationsSet().isEmpty()) {
            return true;
        }

        // Candidates
        for (IndividualCandidacy individualCandidacy : getIndividualCandidacySet()) {
            IndividualCandidacyPersonalDetails personalDetails = individualCandidacy.getPersonalDetails();
            if (personalDetails != null) {
                if (person.equals(personalDetails.getPerson())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy> getIndividualCandidacy() {
        return getIndividualCandidacySet();
    }

    @Deprecated
    public boolean hasAnyIndividualCandidacy() {
        return !getIndividualCandidacySet().isEmpty();
    }

    @Deprecated
    public boolean hasCandidacyFileType() {
        return getCandidacyFileType() != null;
    }

    @Deprecated
    public boolean hasCandidacyFileActive() {
        return getCandidacyFileActive() != null;
    }

}
