/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.candidacyProcess;

import java.util.stream.Collectors;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.util.FileUtils;
import org.fenixedu.bennu.core.domain.User;

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
        init(filename, filename, contents);
    }

    @Override
    public void setFilename(String filename) {
        super.setFilename(FileUtils.cleanupUserInputFilename(filename));
    }

    @Override
    public void setDisplayName(String displayName) {
        super.setDisplayName(FileUtils.cleanupUserInputFileDisplayName(displayName));
    }

    @Atomic
    public static IndividualCandidacyDocumentFile createCandidacyDocument(byte[] contents, String filename,
            IndividualCandidacyDocumentFileType type, String processName, String documentIdNumber) {
        return new IndividualCandidacyDocumentFile(type, contents, filename);
    }

    @Override
    public boolean isAccessible(User user) {
        if (user == null || user.getPerson() == null) {
            return false;
        }

        Person person = user.getPerson();

        // Academic Administration Permissions
        for (AcademicProgram program : AcademicAccessRule.getProgramsAccessibleToFunction(
                AcademicOperationType.MANAGE_CANDIDACY_PROCESSES, person.getUser()).collect(Collectors.toSet())) {
            for (IndividualCandidacy individualCandidacy : getIndividualCandidacySet()) {
                if (individualCandidacy.getAllDegrees().contains(program)) {
                    return true;
                }
            }
        }

        // International Relation Office permissions
        if (RoleType.INTERNATIONAL_RELATION_OFFICE.isMember(person.getUser())) {
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

}
