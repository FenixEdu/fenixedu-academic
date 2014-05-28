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
package net.sourceforge.fenixedu.presentationTier.Action.library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExternalTeacherAuthorization;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.TeacherAuthorization;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.space.SpaceAttendances;
import net.sourceforge.fenixedu.domain.space.SpaceUtils;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.CollectionPager;

public class LibraryAttendance implements Serializable {
    public static class PlaceProvider extends AbstractDomainObjectProvider {
        @Override
        public Object provide(Object source, Object currentValue) {
            LibraryAttendance attendance = (LibraryAttendance) source;
            Set<Space> availableSpaces = new HashSet<Space>();
            for (Space space : attendance.getLibrary().getChildren()) {
                if (SpaceUtils.currentAttendaceCount(space) < space.getAllocatableCapacity()) {
                    availableSpaces.add(space);
                }
            }
            return availableSpaces;
        }
    }

    public static class RoleTypeProvider implements DataProvider {
        @Override
        public Object provide(Object source, Object currentValue) {
            List<RoleType> roles = new ArrayList<RoleType>();
            roles.add(RoleType.STUDENT);
            roles.add(RoleType.TEACHER);
            roles.add(RoleType.EMPLOYEE);
            roles.add(RoleType.GRANT_OWNER);
            roles.add(RoleType.ALUMNI);
            return roles;
        }

        @Override
        public Converter getConverter() {
            return new EnumConverter(RoleType.class);
        }
    }

    private Space library;

    private String personId;

    private RoleType personType;

    private String personName;

    private Person person;

    private Collection<Person> matches;

    private int numberOfPages;

    private Space selectedSpace;

    private SpaceAttendances personAttendance;

    private Unit externalTeacherUnit;

    private Unit researcherUnit;

    private Unit grantOwnerUnit;

    private LocalDate grantOwnerEnd;

    private Invitation invitation;

    private Registration studentRegistration;

    private Registration alumniRegistration;

    private PhdIndividualProgramProcess phdProcess;

    public LibraryAttendance() {
    }

    public LibraryAttendance(String personId, Space library) {
        setLibrary(library);
        setPersonId(personId);
    }

    public LibraryAttendance(SpaceAttendances attendance, Space library) {
        setLibrary(library);
        setPersonId(attendance.getPersonIstUsername());
        if (attendance.getPerson() != null) {
            setPerson(attendance.getPerson());
        }
    }

    public LibraryAttendance(String personType, String personName, Space library) {
        setPersonType(personType != null ? RoleType.valueOf(personType) : null);
        setPersonName(personName);
        setLibrary(library);
    }

    public Space getLibrary() {
        return library;
    }

    public void setLibrary(Space library) {
        this.library = library;
        setPerson(null);
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public RoleType getPersonType() {
        return personType;
    }

    public void setPersonType(RoleType personType) {
        this.personType = personType;
    }

    public String getPersonTypeName() {
        return personType != null ? personType.name() : null;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
        externalTeacherUnit = null;
        researcherUnit = null;
        grantOwnerUnit = null;
        grantOwnerEnd = null;
        studentRegistration = null;
        alumniRegistration = null;
        phdProcess = null;
        invitation = null;
        setSelectedSpace(null);
        setPersonAttendance(null);
        if (person != null) {
            Set<Space> spaces = new HashSet<Space>();
            spaces.add(library);
            for (Space newSpace : library.getChildren()) {
                spaces.add(newSpace);
            }
            for (Space space : spaces) {
                for (SpaceAttendances attendance : space.getCurrentAttendanceSet()) {
                    if (person.equals(attendance.getPerson())) {
                        setPersonAttendance(attendance);
                        setSelectedSpace(space);
                    }
                }
            }
            if (person.getTeacher() != null) {
                TeacherAuthorization teacherAuthorization =
                        person.getTeacher().getTeacherAuthorization(ExecutionSemester.readActualExecutionSemester());
                if (teacherAuthorization != null && teacherAuthorization instanceof ExternalTeacherAuthorization) {
                    externalTeacherUnit =
                            ((ExternalTeacherAuthorization) teacherAuthorization).getDepartment().getDepartmentUnit();
                }
            }
            if (!person.getWorkingResearchUnits().isEmpty()) {
                researcherUnit = person.getWorkingPlaceUnitForAnyRoleType();
            }

            if (person.getPersonRole(RoleType.GRANT_OWNER) != null && person.getEmployee() != null) {
                PersonContractSituation currentGrantOwnerContractSituation =
                        person.getPersonProfessionalData() != null ? person.getPersonProfessionalData()
                                .getCurrentPersonContractSituationByCategoryType(CategoryType.GRANT_OWNER) : null;
                if (currentGrantOwnerContractSituation != null) {
                    grantOwnerUnit = person.getWorkingPlaceUnitForAnyRoleType();
                    grantOwnerEnd = currentGrantOwnerContractSituation.getEndDate();
                }
            }
            if (person.hasStudent()) {
                studentRegistration = person.getStudent().getLastActiveRegistration();
                if (studentRegistration == null) {
                    alumniRegistration = person.getStudent().getLastConcludedRegistration();
                }
                for (PhdIndividualProgramProcess process : person.getPhdIndividualProgramProcessesSet()) {
                    if (process.getActiveState().isPhdActive()) {
                        phdProcess = process;
                    }
                }
            }

            for (Invitation otherInvitation : person.getActiveInvitations()) {
                if (invitation == null || invitation.getEndDate().isBefore(otherInvitation.getEndDate())) {
                    invitation = otherInvitation;
                }
            }
        }
    }

    public Collection<Person> getMatches() {
        return matches;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public Unit getExternalTeacherUnit() {
        return externalTeacherUnit;
    }

    public Unit getResearcherUnit() {
        return researcherUnit;
    }

    public Unit getGrantOwnerUnit() {
        return grantOwnerUnit;
    }

    public LocalDate getGrantOwnerEnd() {
        return grantOwnerEnd;
    }

    public Boolean getHasGrantOwnerEnd() {
        return grantOwnerEnd != null;
    }

    public Invitation getInvitation() {
        return invitation;
    }

    public Collection<GiafProfessionalData> getGiafProfessionalDataSet() {
        return getPerson().getPersonProfessionalData() == null ? new ArrayList<GiafProfessionalData>() : getPerson()
                .getPersonProfessionalData().getGiafProfessionalDatas();
    }

    public Registration getStudentRegistration() {
        return studentRegistration;
    }

    public PhdIndividualProgramProcess getPhdProcess() {
        return phdProcess;
    }

    public Registration getAlumniRegistration() {
        return alumniRegistration;
    }

    public Space getSelectedSpace() {
        return selectedSpace;
    }

    public void setSelectedSpace(Space selectedSpace) {
        this.selectedSpace = selectedSpace;
    }

    public SpaceAttendances getPersonAttendance() {
        return personAttendance;
    }

    public void setPersonAttendance(SpaceAttendances personAttendance) {
        this.personAttendance = personAttendance;
    }

    public Set<SpaceAttendances> getLibraryAttendances() {
        Set<SpaceAttendances> attendances = new HashSet<SpaceAttendances>();
        attendances.addAll(library.getCurrentAttendanceSet());
        for (org.fenixedu.spaces.domain.Space space : library.getChildren()) {
            Space newSpace = space;
            attendances.addAll(newSpace.getCurrentAttendanceSet());
        }
        return attendances;
    }

    public boolean isFull() {
        return SpaceUtils.currentAttendaceCount(getLibrary()) >= getLibrary().getAllocatableCapacity();
    }

    public void search() {
        this.matches = null;
        if (!StringUtils.isEmpty(getPersonId())) {
            setPerson(Person.readPersonByUsername(getPersonId()));
        } else {
            setPerson(null);
        }
    }

    public void advancedSearch(int pageNumber) {
        SearchParameters searchParameters =
                new SearchPerson.SearchParameters(getPersonName(), null, null, null, null, getPersonTypeName(), null, null, null,
                        Boolean.TRUE, null, Boolean.FALSE, (String) null);

        SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);

        CollectionPager<Person> result = SearchPerson.runSearchPerson(searchParameters, predicate);

        Collection<Person> matches = result.getCollection();
        numberOfPages = result.getNumberOfPages();
        if (matches.size() == 1) {
            setPerson(matches.iterator().next());
            this.matches = null;
        } else {
            setPerson(null);
            this.matches = result.getPage(pageNumber);
        }

    }

    @Atomic
    public void enterSpace() {
        Space space = getSelectedSpace() != null ? getSelectedSpace() : library;
        setPersonAttendance(addAttendance(space, getPerson(), AccessControl.getPerson().getIstUsername()));
    }

    @Atomic
    public void exitSpace() {
        getPersonAttendance().exit(AccessControl.getPerson().getIstUsername());
        if (getPerson() != null && getPerson().equals(getPersonAttendance().getPerson())) {
            setPersonAttendance(null);
            setSelectedSpace(null);
        }
    }

    private static SpaceAttendances addAttendance(Space space, Person person, String responsibleUsername) {
        if (person == null) {
            return null;
        }
        if (!(SpaceUtils.currentAttendaceCount(space) < space.getAllocatableCapacity())) {
            throw new DomainException("error.space.maximumAttendanceExceeded");
        }
        SpaceAttendances attendance = new SpaceAttendances(person.getIstUsername(), responsibleUsername, new DateTime());
        space.addCurrentAttendance(attendance);
        space.addPastAttendances(attendance);
        return attendance;
    }
}