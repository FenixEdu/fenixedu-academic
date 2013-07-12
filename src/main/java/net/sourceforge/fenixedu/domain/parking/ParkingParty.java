package net.sourceforge.fenixedu.domain.parking;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.parking.ParkingPartyBean;
import net.sourceforge.fenixedu.dataTransferObject.parking.VehicleBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExternalTeacherAuthorization;
import net.sourceforge.fenixedu.domain.PartyClassification;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest.ParkingRequestFactoryCreator;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ParkingParty extends ParkingParty_Base {

    public static ParkingParty readByCardNumber(Long cardNumber) {
        for (ParkingParty parkingParty : RootDomainObject.getInstance().getParkingParties()) {
            if (parkingParty.getCardNumber() != null && parkingParty.getCardNumber().equals(cardNumber)) {
                return parkingParty;
            }
        }
        return null;
    }

    public ParkingParty(Party party) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setParty(party);
        setAuthorized(Boolean.FALSE);
        setAcceptedRegulation(Boolean.FALSE);
    }

    @Override
    public void setCardNumber(Long cardNumber) {
        if (getCardNumber() == null || getCardNumber() != cardNumber) {
            new ParkingPartyHistory(this, false);
            super.setCardNumber(cardNumber);
        }
    }

    public boolean getHasAllNecessaryPersonalInfo() {
        return ((getParty().getDefaultPhone() != null && !StringUtils.isEmpty(getParty().getDefaultPhone().getNumber())) || (getParty()
                .getDefaultMobilePhone() != null && !StringUtils.isEmpty(getParty().getDefaultMobilePhone().getNumber())))
                && (isEmployee() || (getParty().getDefaultEmailAddress() != null
                        && getParty().getDefaultEmailAddress().hasValue() && !StringUtils.isEmpty(getParty()
                        .getDefaultEmailAddress().getValue())));
    }

    private boolean isEmployee() {
        if (getParty().isPerson()) {
            Person person = (Person) getParty();
            Teacher teacher = person.getTeacher();
            if (teacher == null) {
                Employee employee = person.getEmployee();
                if (employee != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<ParkingRequest> getOrderedParkingRequests() {
        List<ParkingRequest> requests = new ArrayList<ParkingRequest>(getParkingRequests());
        Collections.sort(requests, new BeanComparator("creationDate"));
        return requests;
    }

    public ParkingRequest getFirstRequest() {
        List<ParkingRequest> requests = getOrderedParkingRequests();
        if (requests.size() != 0) {
            return requests.iterator().next();
        }
        return null;
    }

    public ParkingRequest getLastRequest() {
        List<ParkingRequest> requests = getOrderedParkingRequests();
        if (requests.size() != 0) {
            return requests.get(requests.size() - 1);
        }
        return null;
    }

    public ParkingRequestFactoryCreator getParkingRequestFactoryCreator() {
        return new ParkingRequestFactoryCreator(this);
    }

    public String getParkingAcceptedRegulationMessage() {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", Language.getLocale());
        String name = getParty().getName();
        String number = "";
        if (getParty().isPerson()) {
            Person person = (Person) getParty();
            Employee employee = person.getEmployee();
            if (employee == null) {
                Student student = person.getStudent();
                if (student != null) {
                    number = student.getNumber().toString();
                }
            } else {
                number = employee.getEmployeeNumber().toString();
            }
        }

        return MessageFormat.format(bundle.getString("message.acceptedRegulation"), new Object[] { name, number });
    }

    public boolean isStudent() {
        if (getParty().isPerson()) {
            Person person = (Person) getParty();
            Teacher teacher = person.getTeacher();
            if (teacher == null) {
                Employee employee = person.getEmployee();
                if (employee == null) {
                    Student student = person.getStudent();
                    if (student != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String getDriverLicenseFileNameToDisplay() {
        NewParkingDocument driverLicenseDocument = getDriverLicenseDocument();
        if (driverLicenseDocument != null) {
            return driverLicenseDocument.getParkingFile().getFilename();
        } else if (getDriverLicenseDeliveryType() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", Language.getLocale());
            return bundle.getString(getDriverLicenseDeliveryType().name());
        }
        return "";
    }

    public String getParkingGroupToDisplay() {
        if (getParkingGroup() != null) {
            return getParkingGroup().getGroupName();
        }
        return null;
    }

    public String getWorkPhone() {
        if (getParty().isPerson()) {
            return getParty().getDefaultPhone() != null ? getParty().getDefaultPhone().getNumber() : StringUtils.EMPTY;
        }
        return null;
    }

    public List<RoleType> getSubmitAsRoles() {
        List<RoleType> roles = new ArrayList<RoleType>();
        if (getParty().isPerson()) {
            Person person = (Person) getParty();
            Teacher teacher = person.getTeacher();
            if (teacher != null && person.getPersonRole(RoleType.TEACHER) != null
                    && !teacher.isMonitor(ExecutionSemester.readActualExecutionSemester())) {
                roles.add(RoleType.TEACHER);
            }
            Employee employee = person.getEmployee();
            if (employee != null && person.getPersonRole(RoleType.TEACHER) == null
                    && person.getPersonRole(RoleType.EMPLOYEE) != null
                    && employee.getCurrentContractByContractType(AccountabilityTypeEnum.WORKING_CONTRACT) != null) {
                roles.add(RoleType.EMPLOYEE);
            }
            Student student = person.getStudent();
            if (student != null && person.getPersonRole(RoleType.STUDENT) != null) {
                DegreeType degreeType = student.getMostSignificantDegreeType();
                Collection<Registration> registrations = student.getRegistrationsByDegreeType(degreeType);
                for (Registration registration : registrations) {
                    StudentCurricularPlan scp = registration.getActiveStudentCurricularPlan();
                    if (scp != null) {
                        roles.add(RoleType.STUDENT);
                        break;
                    }
                }
                if (!roles.contains(RoleType.STUDENT)) {
                    for (PhdIndividualProgramProcess phdIndividualProgramProcess : person.getPhdIndividualProgramProcesses()) {
                        if (phdIndividualProgramProcess.getActiveState().isPhdActive()) {
                            roles.add(RoleType.STUDENT);
                            break;
                        }
                    }
                }
            }
            if (person.getPersonRole(RoleType.GRANT_OWNER) != null && person.getEmployee() != null) {
                PersonContractSituation currentGrantOwnerContractSituation =
                        person.getPersonProfessionalData() != null ? person.getPersonProfessionalData()
                                .getCurrentPersonContractSituationByCategoryType(CategoryType.GRANT_OWNER) : null;
                if (currentGrantOwnerContractSituation != null) {
                    roles.add(RoleType.GRANT_OWNER);
                }
            }

        }
        if (roles.size() == 0) {
            roles.add(RoleType.PERSON);
        }
        return roles;
    }

    public List<String> getOccupations() {
        List<String> occupations = new ArrayList<String>();
        if (getParty().isPerson()) {
            Person person = (Person) getParty();
            Teacher teacher = person.getTeacher();
            if (teacher != null) {
                ExecutionSemester currentExecutionSemester = ExecutionSemester.readActualExecutionSemester();
                String currentWorkingDepartmentName =
                        teacher.getCurrentWorkingDepartment() != null ? teacher.getCurrentWorkingDepartment().getName() : null;
                PersonContractSituation currentOrLastTeacherContractSituation =
                        teacher.getCurrentOrLastTeacherContractSituation();
                if (currentOrLastTeacherContractSituation != null) {
                    String employeeType = RoleType.TEACHER.getLocalizedName();
                    if (teacher.isMonitor(currentExecutionSemester)) {
                        employeeType = "Monitor";
                    }
                    occupations.add(getOccupation(employeeType, teacher.getPerson().getEmployee().getEmployeeNumber().toString(),
                            currentWorkingDepartmentName, currentOrLastTeacherContractSituation.getBeginDate(),
                            currentOrLastTeacherContractSituation.getEndDate()));
                }

                String employeeType = "Docente Autorizado";
                ExternalTeacherAuthorization teacherAuthorization =
                        (ExternalTeacherAuthorization) teacher.getTeacherAuthorization(currentExecutionSemester);
                if (teacherAuthorization != null && teacherAuthorization.getCanPark()) {
                    occupations.add(getOccupation(employeeType, teacher.getTeacherId(), currentWorkingDepartmentName,
                            currentExecutionSemester.getBeginDateYearMonthDay().toLocalDate(), currentExecutionSemester
                                    .getEndDateYearMonthDay().toLocalDate()));
                }
            }
            PersonContractSituation currentEmployeeContractSituation =
                    person.hasEmployee() ? person.getEmployee().getCurrentEmployeeContractSituation() : null;
            if (currentEmployeeContractSituation != null) {
                Unit currentUnit = person.getEmployee().getCurrentWorkingPlace();
                String thisOccupation =
                        getOccupation(RoleType.EMPLOYEE.getLocalizedName(), person.getEmployee().getEmployeeNumber().toString(),
                                currentUnit == null ? null : currentUnit.getName(),
                                currentEmployeeContractSituation.getBeginDate(), currentEmployeeContractSituation.getEndDate());
                occupations.add(thisOccupation);
            }
            if (person.getPersonRole(RoleType.GRANT_OWNER) != null && person.getEmployee() != null) {
                PersonContractSituation currentGrantOwnerContractSituation =
                        person.getPersonProfessionalData() != null ? person.getPersonProfessionalData()
                                .getCurrentPersonContractSituationByCategoryType(CategoryType.GRANT_OWNER) : null;
                if (currentGrantOwnerContractSituation != null) {
                    Unit currentUnit = person.getEmployee().getCurrentWorkingPlace();
                    String thisOccupation =
                            getOccupation(RoleType.GRANT_OWNER.getLocalizedName(), person.getEmployee().getEmployeeNumber()
                                    .toString(), currentUnit == null ? null : currentUnit.getName(),
                                    currentGrantOwnerContractSituation.getBeginDate(),
                                    currentGrantOwnerContractSituation.getEndDate());
                    occupations.add(thisOccupation);
                }
            }

            if (person.hasResearcher()) {
                StringBuilder stringBuilder =
                        new StringBuilder(BundleUtil.getStringFromResourceBundle("resources.ParkingResources",
                                "message.person.identification", new String[] { RoleType.RESEARCHER.getLocalizedName(),
                                        person.getMostSignificantNumber().toString() }));

                String researchUnitNames = person.getWorkingResearchUnitNames();
                if (!StringUtils.isEmpty(researchUnitNames)) {
                    stringBuilder.append(researchUnitNames).append("<br/>");
                }

                PersonContractSituation currentResearcherContractSituation =
                        person.hasResearcher() ? person.getResearcher().getCurrentContractedResearcherContractSituation() : null;
                if (currentResearcherContractSituation != null) {
                    Unit currentUnit = person.hasEmployee() ? person.getEmployee().getCurrentWorkingPlace() : null;
                    if (currentUnit != null) {
                        stringBuilder.append(currentUnit.getName()).append("<br/>");
                    }
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/MM/dd");
                    stringBuilder.append("(Data inicio: ").append(fmt.print(currentResearcherContractSituation.getBeginDate()));
                    if (currentResearcherContractSituation.getEndDate() != null) {
                        stringBuilder.append(" - Data fim: ").append(fmt.print(currentResearcherContractSituation.getEndDate()));
                    }
                    occupations.add(stringBuilder.append(")<br/>").toString());
                }
            }
            Student student = person.getStudent();
            if (student != null && person.getPersonRole(RoleType.STUDENT) != null) {

                StringBuilder stringBuilder = null;
                for (Registration registration : student.getActiveRegistrations()) {
                    StudentCurricularPlan scp = registration.getLastStudentCurricularPlan();
                    if (scp != null) {
                        if (stringBuilder == null) {
                            stringBuilder =
                                    new StringBuilder(BundleUtil.getStringFromResourceBundle("resources.ParkingResources",
                                            "message.person.identification", new String[] { RoleType.STUDENT.getLocalizedName(),
                                                    student.getNumber().toString() }));

                        }
                        stringBuilder.append("\n").append(scp.getDegreeCurricularPlan().getName());
                        stringBuilder.append("\n (").append(registration.getCurricularYear()).append("� ano");
                        if (isFirstTimeEnrolledInCurrentYear(registration)) {
                            stringBuilder.append(" - 1� vez)");
                        } else {
                            stringBuilder.append(")");
                        }
                        stringBuilder.append("<br/>Media: ").append(registration.getAverage());
                        stringBuilder.append("<br/>");
                    }
                }

                if (stringBuilder != null) {
                    occupations.add(stringBuilder.toString());
                }

                for (PhdIndividualProgramProcess phdIndividualProgramProcess : person.getPhdIndividualProgramProcesses()) {
                    if (phdIndividualProgramProcess.getActiveState().isPhdActive()) {
                        String thisOccupation =
                                getOccupation(RoleType.STUDENT.getLocalizedName(), student.getNumber().toString(),
                                        "\nPrograma Doutoral: " + phdIndividualProgramProcess.getPhdProgram().getName());
                        occupations.add(thisOccupation);

                    }

                }

            }
            List<Invitation> invitations = person.getActiveInvitations();
            if (!invitations.isEmpty()) {
                for (Invitation invitation : invitations) {
                    String thisOccupation =
                            getOccupation("Convidado", "-", invitation.getUnit().getName(), invitation.getBeginDate()
                                    .toLocalDate(), invitation.getEndDate().toLocalDate());
                    occupations.add(thisOccupation);
                }
            }
        }
        return occupations;
    }

    private String getOccupation(String type, String identification, String workingPlace) {
        StringBuilder stringBuilder =
                new StringBuilder(BundleUtil.getStringFromResourceBundle("resources.ParkingResources",
                        "message.person.identification", new String[] { type, identification }));
        if (!StringUtils.isBlank(workingPlace)) {
            stringBuilder.append(workingPlace).append("<br/>");
        }
        return stringBuilder.toString();
    }

    private String getOccupation(String type, String identification, String workingPlace, LocalDate beginDate, LocalDate endDate) {
        StringBuilder stringBuilder = new StringBuilder(getOccupation(type, identification, workingPlace));
        if (beginDate != null) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/MM/dd");
            stringBuilder.append("(Data inicio: ").append(fmt.print(beginDate));
            if (endDate != null) {
                stringBuilder.append(" - Data fim: ").append(fmt.print(endDate));
            }
        } else {
            stringBuilder.append("(inactivo");
        }
        stringBuilder.append(")<br/>");
        return stringBuilder.toString();
    }

    public List<String> getDegreesInformation() {
        List<String> result = new ArrayList<String>();
        if (getParty().isPerson()) {
            Person person = (Person) getParty();
            Student student = person.getStudent();
            if (student != null && person.getPersonRole(RoleType.STUDENT) != null) {
                for (Registration registration : student.getActiveRegistrations()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    StudentCurricularPlan scp = registration.getLastStudentCurricularPlan();
                    if (scp != null) {
                        stringBuilder.append(scp.getDegreeCurricularPlan().getName());
                        stringBuilder.append(" ").append(registration.getCurricularYear()).append("� ano");
                        if (isFirstTimeEnrolledInCurrentYear(registration)) {
                            stringBuilder.append(" - 1� vez");
                        }
                        stringBuilder.append(" - ").append(registration.getAverage());
                        result.add(stringBuilder.toString());
                    }
                }
            }
        }
        return result;
    }

    public boolean hasVehicleContainingPlateNumber(String plateNumber) {
        String plateNumberLowerCase = plateNumber.toLowerCase();
        for (Vehicle vehicle : getVehicles()) {
            if (vehicle.getPlateNumber().toLowerCase().contains(plateNumberLowerCase)) {
                return true;
            }
        }
        return false;
    }

    public void delete() {
        if (canBeDeleted()) {
            setParty(null);
            setParkingGroup(null);
            deleteDriverLicenseDocument();
            for (; getVehicles().size() != 0; getVehicles().iterator().next().delete()) {
                ;
            }
            for (; getParkingRequests().size() != 0; getParkingRequests().iterator().next().delete()) {
                ;
            }
            setRootDomainObject(null);
            deleteDomainObject();
        }
    }

    private void deleteDriverLicenseDocument() {
        NewParkingDocument parkingDocument = getDriverLicenseDocument();
        if (parkingDocument != null) {
            parkingDocument.delete();
        }
    }

    private boolean canBeDeleted() {
        return getVehicles().isEmpty();
    }

    public boolean hasFirstTimeRequest() {
        return getFirstRequest() != null;
    }

    public String getAllNumbers() {
        List<String> result = new ArrayList<String>();
        if (getParty().isPerson()) {
            final Person person = (Person) getParty();
            final Employee employee = person.getEmployee();
            if (employee != null
                    && (person.hasRole(RoleType.TEACHER) || person.hasRole(RoleType.EMPLOYEE)
                            || person.hasRole(RoleType.RESEARCHER) || person.hasRole(RoleType.GRANT_OWNER))) {
                result.add(employee.getEmployeeNumber().toString());
            }
            final Student student = person.getStudent();
            if (person.hasRole(RoleType.STUDENT)) {
                result.add(student.getNumber().toString());
            }
        }
        return StringUtils.join(result, "\n");
    }

    public Integer getMostSignificantNumber() {
        if (getParty().isPerson()) {
            final Person person = (Person) getParty();
            final Teacher teacher = person.getTeacher();
            ExecutionSemester actualExecutionSemester = ExecutionSemester.readActualExecutionSemester();
            final boolean isTeacher = teacher != null && !teacher.isInactive(actualExecutionSemester);
            final boolean isMonitor = isTeacher && teacher.isMonitor(actualExecutionSemester);
            final Employee employee = person.getEmployee();

            if (employee != null && employee.getCurrentWorkingContract() != null) {
                if (person.getPersonRole(RoleType.TEACHER) == null || (teacher != null && isTeacher && !isMonitor)) {
                    return employee.getEmployeeNumber();
                }
            }
            if (employee != null && getPartyClassification().equals(PartyClassification.RESEARCHER)) {
                return employee.getEmployeeNumber();
            }

            final Student student = person.getStudent();

            if (student != null) {
                DegreeType degreeType = student.getMostSignificantDegreeType();
                Collection<Registration> registrations = student.getRegistrationsByDegreeType(degreeType);
                for (Registration registration : registrations) {
                    StudentCurricularPlan scp = registration.getActiveStudentCurricularPlan();
                    if (scp != null) {
                        return student.getNumber();
                    }
                }
                for (PhdIndividualProgramProcess phdIndividualProgramProcess : person.getPhdIndividualProgramProcesses()) {
                    if (phdIndividualProgramProcess.getActiveState().isPhdActive()) {
                        return student.getNumber();
                    }
                }
            }

            if (employee != null && isTeacher && isMonitor) {
                return employee.getEmployeeNumber();
            }
            if (getPhdNumber() != null) {
                return getPhdNumber();
            }
        }

        return 0;
    }

    public static List<ParkingParty> getAll() {
        return RootDomainObject.getInstance().getParkingParties();
    }

    public void edit(ParkingPartyBean parkingPartyBean) {
        if (!parkingPartyBean.getCardAlwaysValid()
                && parkingPartyBean.getCardStartDate().isAfter(parkingPartyBean.getCardEndDate())) {
            throw new DomainException("error.parkingParty.invalidPeriod");
        }
        if (getCardNumber() != null
                && (changedDates(getCardStartDate(), parkingPartyBean.getCardStartDate(), parkingPartyBean.getCardAlwaysValid())
                        || changedDates(getCardEndDate(), parkingPartyBean.getCardEndDate(),
                                parkingPartyBean.getCardAlwaysValid())
                        || changedObject(getCardNumber(), parkingPartyBean.getCardNumber())
                        || changedObject(getParkingGroup(), parkingPartyBean.getParkingGroup()) || changedObject(getPhdNumber(),
                            parkingPartyBean.getPhdNumber()))) {
            new ParkingPartyHistory(this, false);
        }
        setCardNumber(parkingPartyBean.getCardNumber());
        setCardStartDate(parkingPartyBean.getCardStartDate());
        setCardEndDate(parkingPartyBean.getCardEndDate());
        setPhdNumber(parkingPartyBean.getPhdNumber());
        setParkingGroup(parkingPartyBean.getParkingGroup());
        for (VehicleBean vehicleBean : parkingPartyBean.getVehicles()) {
            if (vehicleBean.getVehicle() != null) {
                if (vehicleBean.getDeleteVehicle()) {
                    vehicleBean.getVehicle().delete();
                } else {
                    vehicleBean.getVehicle().edit(vehicleBean);
                }
            } else {
                if (!vehicleBean.getDeleteVehicle()) {
                    new Vehicle(vehicleBean);
                }
            }
        }
        setNotes(parkingPartyBean.getNotes());
    }

    private boolean changedDates(DateTime oldDate, DateTime newDate, Boolean cardAlwaysValid) {
        return cardAlwaysValid ? (oldDate == null ? false : true) : ((oldDate == null || (!oldDate.equals(newDate))) ? true : oldDate
                .equals(newDate));
    }

    private boolean changedObject(Object oldObject, Object newObject) {
        return oldObject == null && newObject == null ? false : (oldObject != null && newObject != null ? (!oldObject
                .equals(newObject)) : true);
    }

    public void edit(ParkingRequest parkingRequest) {
        setDriverLicenseDeliveryType(parkingRequest.getDriverLicenseDeliveryType());
        setDriverLicenseDocument(parkingRequest.getDriverLicenseDocument());
        for (Vehicle vehicle : parkingRequest.getVehicles()) {
            Vehicle partyVehicle = geVehicleByPlateNumber(vehicle.getPlateNumber());
            if (partyVehicle != null) {
                vehicle.deleteUnnecessaryDocuments();
                partyVehicle.edit(vehicle);
            } else {
                vehicle.deleteUnnecessaryDocuments();
                addVehicles(new Vehicle(vehicle));
            }
        }
        setRequestedAs(parkingRequest.getRequestedAs());
    }

    private Vehicle geVehicleByPlateNumber(String plateNumber) {
        for (Vehicle vehicle : getVehicles()) {
            if (vehicle.getPlateNumber().equalsIgnoreCase(plateNumber)) {
                return vehicle;
            }
        }
        return null;
    }

    public boolean isActiveInHisGroup() {
        if (getParkingGroup() == null) {
            return Boolean.FALSE;
        }
        if (getParty().isPerson()) {
            Person person = (Person) getParty();
            if (getParkingGroup().getGroupName().equalsIgnoreCase("Docentes")) {
                return person.getTeacher() != null && person.getTeacher().getCurrentWorkingDepartment() != null;
            }
            if (getParkingGroup().getGroupName().equalsIgnoreCase("Não Docentes")) {
                return person.getEmployee() != null && person.getEmployee().getCurrentWorkingPlace() != null;
            }
            if (getParkingGroup().getGroupName().equalsIgnoreCase("Especiais")) {
                return person.getPartyClassification() != PartyClassification.PERSON;
            }
            if (getParkingGroup().getGroupName().equalsIgnoreCase("2º ciclo")) {
                if (person.hasStudent()) {
                    return canRequestUnlimitedCard(person.getStudent());
                } else {
                    return Boolean.FALSE;
                }
            }
            if (getParkingGroup().getGroupName().equalsIgnoreCase("Bolseiros")) {
                if (person.getPersonRole(RoleType.GRANT_OWNER) != null && person.getEmployee() != null) {
                    PersonContractSituation currentGrantOwnerContractSituation =
                            person.getPersonProfessionalData() != null ? person.getPersonProfessionalData()
                                    .getCurrentPersonContractSituationByCategoryType(CategoryType.GRANT_OWNER) : null;
                    return currentGrantOwnerContractSituation != null;
                }
                return false;
            }
            if (getParkingGroup().getGroupName().equalsIgnoreCase("3º ciclo")) {
                if (person.hasStudent()) {
                    Registration registration =
                            getRegistrationByDegreeType(person.getStudent(), DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA);
                    return registration != null && registration.isActive();
                } else {
                    return Boolean.FALSE;
                }
            }
            if (getParkingGroup().getGroupName().equalsIgnoreCase("Limitados")) {
                return person.getPartyClassification() != PartyClassification.PERSON
                        && person.getPartyClassification() != PartyClassification.RESEARCHER;
            }
        }
        return Boolean.FALSE;
    }

    public boolean getCanRequestUnlimitedCardAndIsInAnyRequestPeriod() {
        ParkingRequestPeriod current = ParkingRequestPeriod.getCurrentRequestPeriod();
        return current != null && canRequestUnlimitedCard(current);
    }

    public boolean canRequestUnlimitedCard() {
        return canRequestUnlimitedCard(ParkingRequestPeriod.getCurrentRequestPeriod());
    }

    public boolean canRequestUnlimitedCard(ParkingRequestPeriod parkingRequestPeriod) {
        if (!alreadyRequestParkingRequestTypeInPeriod(ParkingRequestType.RENEW, parkingRequestPeriod)) {
            return hasRolesToRequestUnlimitedCard();
        }
        return Boolean.FALSE;
    }

    public boolean hasRolesToRequestUnlimitedCard() {
        List<RoleType> roles = getSubmitAsRoles();
        if (roles.contains(RoleType.GRANT_OWNER)) {
            return Boolean.TRUE;
        } else if (roles.contains(RoleType.STUDENT) && canRequestUnlimitedCard(((Person) getParty()).getStudent())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public RoleType getRoleToRequestUnlimitedCard() {
        List<RoleType> roles = getSubmitAsRoles();
        if (roles.contains(RoleType.GRANT_OWNER)) {
            return RoleType.GRANT_OWNER;
        } else if (roles.contains(RoleType.STUDENT) && canRequestUnlimitedCard(((Person) getParty()).getStudent())) {
            return RoleType.STUDENT;
        }
        return null;
    }

    public boolean alreadyRequestParkingRequestTypeInPeriod(ParkingRequestType parkingRequestType,
            ParkingRequestPeriod parkingRequestPeriod) {
        List<ParkingRequest> requests = getOrderedParkingRequests();
        for (ParkingRequest parkingRequest : requests) {
            if (parkingRequestPeriod.getRequestPeriodInterval().contains(parkingRequest.getCreationDate())
                    && parkingRequest.getParkingRequestType().equals(parkingRequestType)) {
                return true;
            }
        }
        return false;
    }

    public boolean canRequestUnlimitedCard(Student student) {
        if (student != null && student.getPerson().getPersonRole(RoleType.STUDENT) != null) {
            for (PhdIndividualProgramProcess phdIndividualProgramProcess : student.getPerson().getPhdIndividualProgramProcesses()) {
                if (phdIndividualProgramProcess.getActiveState().isPhdActive()) {
                    return true;
                }
            }
        }
        // List<DegreeType> degreeTypes = new ArrayList<DegreeType>();
        // degreeTypes.add(DegreeType.DEGREE);
        // degreeTypes.add(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA);
        // degreeTypes.add(DegreeType.BOLONHA_MASTER_DEGREE);
        // degreeTypes.add(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
        //
        // for (DegreeType degreeType : degreeTypes) {
        // Registration registration = getRegistrationByDegreeType(student,
        // degreeType);
        // if (registration != null && registration.isInFinalDegreeYear()) {
        // return
        // degreeType.equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA) ?
        // Boolean.TRUE
        // : isFirstTimeEnrolledInCurrentYear(registration);
        // }
        // }
        return false;
        // DEGREE=Licenciatura (5 anos) - 5� ano
        // MASTER_DEGREE=Mestrado = 2ciclo - n�o tem
        // BOLONHA_DEGREE=Licenciatura Bolonha - n�o podem
        // BOLONHA_MASTER_DEGREE=Mestrado Bolonha - s� no 5+ ano 1� vez
        // BOLONHA_INTEGRATED_MASTER_DEGREE=Mestrado Integrado (ultimo ano 1�
        // vez)
        // BOLONHA_ADVANCED_FORMATION_DIPLOMA =Diploma Forma��o Avan�ada =
        // cota
        // pos grad (sempre)

        // BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA=Diploma de Estudos
        // Avan�ados
        // -n�o est�o todos no f�nix por isso t�m de se candidatar em
        // papel
        // BOLONHA_SPECIALIZATION_DEGREE=Curso de Especializa��o - n�o
        // est�o no
        // f�nix -este tipo n�o � usado

    }

    public boolean isFirstTimeEnrolledInCurrentYear(Registration registration) {
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        return registration.getCurricularYear(executionYear.getPreviousExecutionYear()) != registration
                .getCurricularYear(executionYear);
    }

    private Registration getRegistrationByDegreeType(Student student, DegreeType degreeType) {
        for (Registration registration : student.getRegistrationsByDegreeType(degreeType)) {
            if (registration.isActive()) {
                StudentCurricularPlan scp = registration.getActiveStudentCurricularPlan();
                if (scp != null) {
                    return registration;
                }
            }
        }
        return null;
    }

    public PartyClassification getPartyClassification() {
        return getParty().isPerson() ? ((Person) getParty()).getPartyClassification() : null;
    }

    public DateTime getCardEndDateToCompare() {
        if (getCardEndDate() == null) {
            return new DateTime(9999, 9, 9, 9, 9, 9, 9);
        } else {
            return getCardEndDate();
        }
    }

    public DateTime getCardStartDateToCompare() {
        if (getCardStartDate() == null) {
            return new DateTime(9999, 9, 9, 9, 9, 9, 9);
        } else {
            return getCardStartDate();
        }
    }

    public void renewParkingCard(DateTime newBeginDate, DateTime newEndDate, ParkingGroup newParkingGroup) {
        new ParkingPartyHistory(this, false);
        if (newBeginDate != null) {
            setCardStartDate(newBeginDate);
        }
        setCardEndDate(newEndDate);
        if (newParkingGroup != null) {
            setParkingGroup(newParkingGroup);
        }
    }

    public Vehicle getFirstVehicle() {
        List<Vehicle> vehicles = new ArrayList<Vehicle>(getVehicles());
        Collections.sort(vehicles, new BeanComparator("plateNumber"));
        return vehicles.size() > 0 ? vehicles.iterator().next() : null;
    }

    public Vehicle getSecondVehicle() {
        List<Vehicle> vehicles = new ArrayList<Vehicle>(getVehicles());
        Collections.sort(vehicles, new BeanComparator("plateNumber"));
        return vehicles.size() > 1 ? vehicles.get(1) : null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.parking.ParkingRequest> getParkingRequests() {
        return getParkingRequestsSet();
    }

    @Deprecated
    public boolean hasAnyParkingRequests() {
        return !getParkingRequestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.parking.Vehicle> getVehicles() {
        return getVehiclesSet();
    }

    @Deprecated
    public boolean hasAnyVehicles() {
        return !getVehiclesSet().isEmpty();
    }

    @Deprecated
    public boolean hasCardNumber() {
        return getCardNumber() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasFirstCarPropertyRegistryDocumentState() {
        return getFirstCarPropertyRegistryDocumentState() != null;
    }

    @Deprecated
    public boolean hasRequestedAs() {
        return getRequestedAs() != null;
    }

    @Deprecated
    public boolean hasAcceptedRegulation() {
        return getAcceptedRegulation() != null;
    }

    @Deprecated
    public boolean hasFirstCarOwnerIdDocumentState() {
        return getFirstCarOwnerIdDocumentState() != null;
    }

    @Deprecated
    public boolean hasPhdNumber() {
        return getPhdNumber() != null;
    }

    @Deprecated
    public boolean hasParty() {
        return getParty() != null;
    }

    @Deprecated
    public boolean hasCardStartDate() {
        return getCardStartDate() != null;
    }

    @Deprecated
    public boolean hasFirstCarInsuranceDocumentState() {
        return getFirstCarInsuranceDocumentState() != null;
    }

    @Deprecated
    public boolean hasUsedNumber() {
        return getUsedNumber() != null;
    }

    @Deprecated
    public boolean hasDriverLicenseDeliveryType() {
        return getDriverLicenseDeliveryType() != null;
    }

    @Deprecated
    public boolean hasDriverLicenseDocument() {
        return getDriverLicenseDocument() != null;
    }

    @Deprecated
    public boolean hasParkingGroup() {
        return getParkingGroup() != null;
    }

    @Deprecated
    public boolean hasSecondCarOwnerIdDocumentState() {
        return getSecondCarOwnerIdDocumentState() != null;
    }

    @Deprecated
    public boolean hasFirstCarDeclarationDocumentState() {
        return getFirstCarDeclarationDocumentState() != null;
    }

    @Deprecated
    public boolean hasNotes() {
        return getNotes() != null;
    }

    @Deprecated
    public boolean hasAuthorized() {
        return getAuthorized() != null;
    }

    @Deprecated
    public boolean hasSecondCarDeclarationDocumentState() {
        return getSecondCarDeclarationDocumentState() != null;
    }

    @Deprecated
    public boolean hasSecondCarPropertyRegistryDocumentState() {
        return getSecondCarPropertyRegistryDocumentState() != null;
    }

    @Deprecated
    public boolean hasSecondCarInsuranceDocumentState() {
        return getSecondCarInsuranceDocumentState() != null;
    }

    @Deprecated
    public boolean hasDriverLicenseDocumentState() {
        return getDriverLicenseDocumentState() != null;
    }

    @Deprecated
    public boolean hasCardEndDate() {
        return getCardEndDate() != null;
    }

}
