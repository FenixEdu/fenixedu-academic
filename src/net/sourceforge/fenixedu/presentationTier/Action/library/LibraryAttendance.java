package net.sourceforge.fenixedu.presentationTier.Action.library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExternalTeacherAuthorization;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.TeacherAuthorization;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.SpaceAttendances;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.CollectionPager;

public class LibraryAttendance implements Serializable {
    public static class PlaceProvider extends AbstractDomainObjectProvider {
        @Override
        public Object provide(Object source, Object currentValue) {
            LibraryAttendance attendance = (LibraryAttendance) source;
            Set<Space> availableSpaces = new HashSet<Space>();
            for (Space space : attendance.getLibrary().getActiveContainedSpaces()) {
                if (space.canAddAttendance()) {
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

    private String personLibraryCardNumber;

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
        setPersonLibraryCardNumber(null);
        setSelectedSpace(null);
        setPersonAttendance(null);
        if (person != null) {
            setPersonLibraryCardNumber(person.getLibraryCardNumber());
            Set<Space> spaces = new HashSet<Space>();
            spaces.add(library);
            spaces.addAll(library.getActiveContainedSpaces());
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

            if (person.hasGrantOwner() && person.getGrantOwner().isActive()) {
                GrantContract contract = person.getGrantOwner().getCurrentContract();
                GrantContractRegime regime = contract.getActiveRegime();
                if (regime != null && contract.getGrantCostCenter() != null) {
                    grantOwnerUnit = contract.getGrantCostCenter().getUnit();
                    grantOwnerEnd = regime.getEndDateOrRescissionDate();
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

    public List<GiafProfessionalData> getGiafProfessionalDataSet() {
        return getPerson().getPersonProfessionalData() == null ? new ArrayList<GiafProfessionalData>() : getPerson()
                .getPersonProfessionalData().getGiafProfessionalDatas();
    }

    public GiafProfessionalData getGiafProfessionalDatas(int index) {
        return getPerson().getPersonProfessionalData() == null ? null : getPerson().getPersonProfessionalData()
                .getGiafProfessionalDatas().get(index);
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

    public String getPersonLibraryCardNumber() {
        return personLibraryCardNumber;
    }

    public void setPersonLibraryCardNumber(String personLibraryCardNumber) {
        this.personLibraryCardNumber = personLibraryCardNumber;
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
        attendances.addAll(library.getCurrentAttendance());
        for (Space space : library.getActiveContainedSpaces()) {
            attendances.addAll(space.getCurrentAttendance());
        }
        return attendances;
    }

    public boolean isFull() {
        return !getLibrary().canAddAttendance();
    }

    public void search() {
        this.matches = null;
        if (!StringUtils.isEmpty(getPersonId())) {
            if (getPersonId().startsWith("ist")) {
                setPerson(Person.readPersonByIstUsername(getPersonId()));
            } else {
                setPerson(Person.readPersonByLibraryCardNumber(getPersonId()));
            }
        } else {
            setPerson(null);
        }
    }

    public void advancedSearch(int pageNumber) {
        SearchParameters searchParameters =
                new SearchPerson.SearchParameters(getPersonName(), null, null, null, null, getPersonTypeName(), null, null, null,
                        Boolean.TRUE, null, Boolean.FALSE, (String) null);

        SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);

        Object[] args = { searchParameters, predicate };

        CollectionPager<Person> result = null;
        try {
            result = (CollectionPager<Person>) ServiceManagerServiceFactory.executeService("SearchPerson", args);
            Collection<Person> matches = result.getCollection();
            numberOfPages = result.getNumberOfPages();
            if (matches.size() == 1) {
                setPerson(matches.iterator().next());
                this.matches = null;
            } else {
                setPerson(null);
                this.matches = result.getPage(pageNumber);
            }
            return;
        } catch (FenixServiceException e) {
        } catch (FenixFilterException e) {
        }
        this.matches = null;
        setPerson(null);
    }

    @Service
    public void generateCardNumber() {
        getPerson().setLibraryCardNumber(RootDomainObject.getInstance().getLibraryCardSystem().generateNewMilleniumCode());
        setPersonLibraryCardNumber(getPerson().getLibraryCardNumber());
    }

    @Service
    public void enterSpace() {
        Space space = getSelectedSpace() != null ? getSelectedSpace() : library;
        setPersonAttendance(space.addAttendance(getPerson(), AccessControl.getPerson().getIstUsername()));
    }

    @Service
    public void exitSpace() {
        getPersonAttendance().exit(AccessControl.getPerson().getIstUsername());
        if (getPerson() != null && getPerson().equals(getPersonAttendance().getPerson())) {
            setPersonAttendance(null);
            setSelectedSpace(null);
        }
    }

}