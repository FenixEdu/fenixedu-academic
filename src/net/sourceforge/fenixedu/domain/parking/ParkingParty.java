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
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest.ParkingRequestFactoryCreator;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.ContractType;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.file.FileManagerFactory;

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

    public boolean getHasAllNecessaryPersonalInfo() {
        Person person = (Person) getParty();
        return (!StringUtils.isEmpty(person.getWorkPhone()) || !StringUtils.isEmpty(person.getMobile()))
                && (isEmployee() || !StringUtils.isEmpty(person.getEmail()));
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

    public ParkingRequest getFirstRequest() {
        return (getParkingRequests().isEmpty() ? null : getParkingRequests().iterator().next());
    }

    public ParkingRequestFactoryCreator getParkingRequestFactoryCreator() {
        return new ParkingRequestFactoryCreator(this);
    }

    public String getParkingAcceptedRegulationMessage() {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                .getLocale());
        String name = getParty().getName();
        String number = "";
        if (getParty().isPerson()) {
            Person person = (Person) getParty();
            Teacher teacher = person.getTeacher();
            if (teacher == null) {
                Employee employee = person.getEmployee();
                if (employee == null) {
                    Student student = person.getStudent();
                    if (student != null) {
                        number = student.getNumber().toString();
                    }
                } else {
                    number = employee.getEmployeeNumber().toString();
                }

            } else {
                number = teacher.getTeacherNumber().toString();
            }
        }

        return MessageFormat.format(bundle.getString("message.acceptedRegulation"), new Object[] { name,
                number });
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
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getDriverLicenseDeliveryType().name());
        }
        return "";
    }

    public String getDeclarationDocumentLink() {
        NewParkingDocument parkingDocument = getDriverLicenseDocument();
        if (parkingDocument != null
                && parkingDocument.getParkingDocumentType() == NewParkingDocumentType.DRIVER_LICENSE) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            return FileManagerFactory.getFactoryInstance().getFileManager().formatDownloadUrl(
                    parkingFile.getExternalStorageIdentification(), parkingFile.getFilename());
        }
        return "";
    }

    public ParkingDocument getParkingDocument(ParkingDocumentType documentType) {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(documentType)) {
                return parkingDocument;
            }
        }
        return null;
    }
    
    public String getParkingGroupToDisplay() {
        if (getParkingGroup() != null) {
            return getParkingGroup().getGroupName();
        }
        return null;
    }

    public String getWorkPhone() {
        if (getParty().isPerson()) {
            return ((Person) getParty()).getWorkPhone();
        }
        return null;
    }

    public List<RoleType> getSubmitAsRoles() {
        List<RoleType> roles = new ArrayList<RoleType>();
        if (getParty().isPerson()) {
            Person person = (Person) getParty();
            Teacher teacher = person.getTeacher();
            if (teacher != null && person.getPersonRole(RoleType.TEACHER) != null
                    && !teacher.isMonitor(ExecutionPeriod.readActualExecutionPeriod())) {
                roles.add(RoleType.TEACHER);
            }
            Employee employee = person.getEmployee();
            if (employee != null && person.getPersonRole(RoleType.TEACHER) == null
                    && person.getPersonRole(RoleType.EMPLOYEE) != null
                    && employee.getCurrentContractByContractType(ContractType.WORKING) != null) {
                roles.add(RoleType.EMPLOYEE);
            }
            Student student = person.getStudent();
            if (student != null && person.getPersonRole(RoleType.STUDENT) != null) {
                DegreeType degreeType = student.getMostSignificantDegreeType();
                Collection<Registration> registrations = student
                        .getRegistrationsByDegreeType(degreeType);
                for (Registration registration : registrations) {
                    StudentCurricularPlan scp = registration.getActiveStudentCurricularPlan();
                    if (scp != null) {
                        roles.add(RoleType.STUDENT);
                        break;
                    }
                }
            }
            GrantOwner grantOwner = person.getGrantOwner();
            if (grantOwner != null && person.getPersonRole(RoleType.GRANT_OWNER) != null
                    && grantOwner.hasCurrentContract()) {
                roles.add(RoleType.GRANT_OWNER);
            }
        }
        return roles;
    }

    public List<String> getOccupations() {
        List<String> occupations = new ArrayList<String>();
        if (getParty().isPerson()) {
            Person person = (Person) getParty();
            Teacher teacher = person.getTeacher();
            if (teacher != null && person.getPersonRole(RoleType.TEACHER) != null) {
                String currenteDepartment = "";
                if (teacher.getCurrentWorkingDepartment() != null) {
                    currenteDepartment = teacher.getCurrentWorkingDepartment().getName();
                }
                if (teacher.isMonitor(ExecutionPeriod.readActualExecutionPeriod())) {
                    occupations.add("<strong>Monitor</strong><br/> Nº " + teacher.getTeacherNumber()
                            + "<br/>" + currenteDepartment);
                } else {
                    occupations.add("<strong>Docente</strong><br/> Nº " + teacher.getTeacherNumber()
                            + "<br/>" + currenteDepartment);
                }
            }
            Employee employee = person.getEmployee();
            if (employee != null && person.getPersonRole(RoleType.TEACHER) == null
                    && person.getPersonRole(RoleType.EMPLOYEE) != null
                    && employee.getCurrentContractByContractType(ContractType.WORKING) != null) {
                Unit currentUnit = employee.getCurrentWorkingPlace();
                if (currentUnit != null) {
                    occupations.add("<strong>Funcionário</strong><br/> Nº "
                            + employee.getEmployeeNumber() + "<br/>" + currentUnit.getName() + " - "
                            + currentUnit.getCostCenterCode());
                } else {
                    occupations.add("<strong>Funcionário</strong><br/> Nº "
                            + employee.getEmployeeNumber());
                }
            }
            Student student = person.getStudent();
            if (student != null && person.getPersonRole(RoleType.STUDENT) != null) {
                DegreeType degreeType = student.getMostSignificantDegreeType();
                Collection<Registration> registrations = student
                        .getRegistrationsByDegreeType(degreeType);
                StringBuilder stringBuilder = null;
                for (Registration registration : registrations) {
                    StudentCurricularPlan scp = registration.getActiveStudentCurricularPlan();
                    if (scp != null) {
                        if (stringBuilder == null) {
                            stringBuilder = new StringBuilder("<strong>Estudante</strong><br/> Nº ");
                            stringBuilder.append(student.getNumber()).append(" ");
                        }
                        stringBuilder.append("\n").append(scp.getDegreeCurricularPlan().getName());
                        stringBuilder.append("\t");
                    }
                }
                if (stringBuilder != null) {
                    occupations.add(stringBuilder.toString());
                }
            }
            GrantOwner grantOwner = person.getGrantOwner();
            if (grantOwner != null && person.getPersonRole(RoleType.GRANT_OWNER) != null
                    && grantOwner.hasCurrentContract()) {
                List<GrantContractRegime> contractRegimeList = new ArrayList<GrantContractRegime>();
                occupations.add("<strong>Bolseiro</strong><br/> Nº " + grantOwner.getNumber());
                for (GrantContract contract : grantOwner.getGrantContracts()) {
                    contractRegimeList.addAll(contract.getContractRegimes());
                }
                Collections
                        .sort(contractRegimeList, new BeanComparator("dateBeginContractYearMonthDay"));
                for (GrantContractRegime contractRegime : contractRegimeList) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("<strong>Início:</strong> "
                            + contractRegime.getDateBeginContractYearMonthDay().toString("dd/MM/yyyy"));
                    stringBuilder.append("&nbsp&nbsp&nbsp -&nbsp&nbsp&nbsp<strong>Fim:</strong> "
                            + contractRegime.getDateEndContractYearMonthDay().toString("dd/MM/yyyy"));
                    stringBuilder.append("&nbsp&nbsp&nbsp -&nbsp&nbsp&nbsp<strong>Activo:</strong> ");
                    if (contractRegime.isActive()) {
                        stringBuilder.append("Sim");
                    } else {
                        stringBuilder.append("Nï¿½o");
                    }
                    occupations.add(stringBuilder.toString());
                }
            }
        }
        return occupations;
    }

    public boolean hasVehicleContainingPlateNumber(String plateNumber) {
        String plateNumberLowerCase = plateNumber.toLowerCase();
        for (Vehicle vehicle : getVehicles()) {
            if(vehicle.getPlateNumber().toLowerCase().contains(plateNumberLowerCase)){
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
            for (; getVehicles().size() != 0; getVehicles().get(0).delete());
            for (; getParkingRequests().size() != 0; getParkingRequests().get(0).delete());
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
        return !getVehicles().isEmpty();
    }

    public boolean hasFirstTimeRequest() {
        for (ParkingRequest parkingRequest : getParkingRequests()) {
            if (parkingRequest.getFirstRequest()) {
                return true;
            }
        }
        return false;
    }

    public Integer getMostSignificantNumber() {
        if (getParty().isPerson()) {
            Person person = (Person) getParty();
            if (person.getTeacher() != null && person.getTeacher().getCurrentWorkingDepartment() != null
                    && !person.getTeacher().isMonitor(ExecutionPeriod.readActualExecutionPeriod())) {
                return person.getTeacher().getTeacherNumber();
            }
            if (person.getEmployee() != null && person.getEmployee().getCurrentWorkingContract() != null
                    && person.getPersonRole(RoleType.TEACHER) == null) {
                return person.getEmployee().getEmployeeNumber();
            }
            if (person.getStudent() != null) {
                DegreeType degreeType = person.getStudent().getMostSignificantDegreeType();
                Collection<Registration> registrations = person.getStudent()
                        .getRegistrationsByDegreeType(degreeType);
                for (Registration registration : registrations) {
                    StudentCurricularPlan scp = registration.getActiveStudentCurricularPlan();
                    if (scp != null) {
                        return person.getStudent().getNumber();
                    }
                }
            }
            if (person.getGrantOwner() != null && person.getGrantOwner().hasCurrentContract()) {
                return person.getGrantOwner().getNumber();
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
        if (getParty().isPerson()) {
            ((Person) getParty()).setEmail(parkingPartyBean.getEmail());
        }
    }

    public void edit(ParkingRequest parkingRequest) {
        setDriverLicenseDeliveryType(parkingRequest.getDriverLicenseDeliveryType());
        parkingRequest.deleteDriverLicenseDocument();

        for (Vehicle vehicle : parkingRequest.getVehicles()) {
            Vehicle partyVehicle = geVehicleByPlateNumber(vehicle.getPlateNumber());
            if (partyVehicle != null) {
                partyVehicle.edit(vehicle);
                vehicle.deleteDocuments();
            } else {
                addVehicles(new Vehicle(vehicle));
                vehicle.deleteDocuments();
            }
        }
    }

    private Vehicle geVehicleByPlateNumber(String plateNumber) {
        for (Vehicle vehicle : getVehicles()) {
            if (vehicle.getPlateNumber().equalsIgnoreCase(plateNumber)) {
                return vehicle;
            }
        }
        return null;
    }
}
