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
package org.fenixedu.academic.ui.struts.action.library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.service.services.person.SearchPerson;
import org.fenixedu.academic.service.services.person.SearchPerson.SearchParameters;
import org.fenixedu.academic.service.services.person.SearchPerson.SearchPersonPredicate;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Accountability;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Invitation;
import org.fenixedu.academic.domain.organizationalStructure.ResearchUnit;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.personnelSection.contracts.GiafProfessionalData;
import org.fenixedu.academic.domain.personnelSection.contracts.PersonContractSituation;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.space.SpaceAttendances;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.teacher.CategoryType;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.renderers.providers.AbstractDomainObjectProvider;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

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
                if (currentAttendaceCount(space) < space.getAllocatableCapacity()) {
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
        setPersonId(attendance.getPersonUsername());
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
                externalTeacherUnit = person.getEmployee() != null ? person.getEmployee().getCurrentWorkingPlace() : null;
            }
            if (!ResearchUnit.getWorkingResearchUnits(person).isEmpty()) {
                researcherUnit = getWorkingPlaceUnitForAnyRoleType(person);
            }

            if (person.hasRole(RoleType.GRANT_OWNER) && person.getEmployee() != null) {
                PersonContractSituation currentGrantOwnerContractSituation =
                        person.getPersonProfessionalData() != null ? person.getPersonProfessionalData()
                                .getCurrentPersonContractSituationByCategoryType(CategoryType.GRANT_OWNER) : null;
                if (currentGrantOwnerContractSituation != null) {
                    grantOwnerUnit = getWorkingPlaceUnitForAnyRoleType(person);
                    grantOwnerEnd = currentGrantOwnerContractSituation.getEndDate();
                }
            }
            if (person.getStudent() != null) {
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

            for (Invitation otherInvitation : Invitation.getActiveInvitations(person)) {
                if (invitation == null || invitation.getEndDate().isBefore(otherInvitation.getEndDate())) {
                    invitation = otherInvitation;
                }
            }
        }
    }

    public static Unit getWorkingPlaceUnitForAnyRoleType(Person person) {
        if (person.hasRole(RoleType.TEACHER) || person.hasRole(RoleType.EMPLOYEE) || person.hasRole(RoleType.GRANT_OWNER)) {
            return person.getEmployee() != null ? person.getEmployee().getCurrentWorkingPlace() : null;
        }
        if (person.hasRole(RoleType.RESEARCHER)) {
            if (person.getEmployee() != null && person.getResearcher() != null
                    && person.getResearcher().isActiveContractedResearcher()) {
                final Unit currentWorkingPlace = person.getEmployee().getCurrentWorkingPlace();
                if (currentWorkingPlace != null) {
                    return currentWorkingPlace;
                }
            }
            final Collection<? extends Accountability> accountabilities =
                    person.getParentAccountabilities(AccountabilityTypeEnum.RESEARCH_CONTRACT);
            final YearMonthDay currentDate = new YearMonthDay();
            for (final Accountability accountability : accountabilities) {
                if (accountability.isActive(currentDate)) {
                    return (Unit) accountability.getParentParty();
                }
            }
        }
        return null;
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
                .getPersonProfessionalData().getGiafProfessionalDatasSet();
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
        return currentAttendaceCount(getLibrary()) >= getLibrary().getAllocatableCapacity();
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
                        Boolean.TRUE, null, (String) null);

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
        setPersonAttendance(addAttendance(space, getPerson(), AccessControl.getPerson().getUsername()));
    }

    @Atomic
    public void exitSpace() {
        getPersonAttendance().exit(AccessControl.getPerson().getUsername());
        if (getPerson() != null && getPerson().equals(getPersonAttendance().getPerson())) {
            setPersonAttendance(null);
            setSelectedSpace(null);
        }
    }

    public static int currentAttendaceCount(Space space) {
        return space.getCurrentAttendanceSet().size()
                + space.getChildren().stream().mapToInt(LibraryAttendance::currentAttendaceCount).sum();
    }

    private static SpaceAttendances addAttendance(Space space, Person person, String responsibleUsername) {
        if (person == null) {
            return null;
        }
        if (!(currentAttendaceCount(space) < space.getAllocatableCapacity())) {
            throw new DomainException("error.space.maximumAttendanceExceeded");
        }
        SpaceAttendances attendance = new SpaceAttendances(person.getUsername(), responsibleUsername, new DateTime());
        space.addCurrentAttendance(attendance);
        space.addPastAttendances(attendance);
        return attendance;
    }
}