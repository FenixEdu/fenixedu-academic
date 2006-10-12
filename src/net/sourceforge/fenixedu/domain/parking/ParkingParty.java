package net.sourceforge.fenixedu.domain.parking;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest.ParkingRequestFactoryCreator;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.ContractType;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.file.FileManagerFactory;

public class ParkingParty extends ParkingParty_Base {

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

    public ParkingDocument getParkingDocument(ParkingDocumentType documentType) {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(documentType)) {
                return parkingDocument;
            }
        }
        return null;
    }

    public String getDriverLicenseFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.DRIVER_LICENSE)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }

    public String getDriverLicenseFileNameToDisplay() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.DRIVER_LICENSE)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getDriverLicenseDocumentState() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getDriverLicenseDocumentState().name());
        }
        return null;
    }

    public String getFirstCarPropertyRegistryFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    ParkingDocumentType.FIRST_CAR_PROPERTY_REGISTER)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }

    public String getFirstCarPropertyRegistryFileNameToDisplay() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    ParkingDocumentType.FIRST_CAR_PROPERTY_REGISTER)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getFirstCarPropertyRegistryDocumentState() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getFirstCarPropertyRegistryDocumentState().name());
        }
        return null;
    }

    public String getFirstCarInsuranceFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.FIRST_CAR_INSURANCE)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }

    public String getFirstCarInsuranceFileNameToDisplay() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.FIRST_CAR_INSURANCE)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getFirstCarInsuranceDocumentState() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getFirstCarInsuranceDocumentState().name());
        }
        return null;
    }

    public String getFirstCarOwnerIdFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.FIRST_CAR_OWNER_ID)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }

    public String getFirstCarOwnerIdFileNameToDisplay() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.FIRST_CAR_OWNER_ID)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getFirstCarOwnerIdDocumentState() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getFirstCarOwnerIdDocumentState().name());
        }
        return null;
    }

    public String getFirstDeclarationAuthorizationFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    ParkingDocumentType.FIRST_DECLARATION_OF_AUTHORIZATION)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }

    public String getFirstDeclarationAuthorizationFileNameToDisplay() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    ParkingDocumentType.FIRST_DECLARATION_OF_AUTHORIZATION)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getFirstCarDeclarationDocumentState() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getFirstCarDeclarationDocumentState().name());
        }
        return null;
    }

    public String getSecondCarPropertyRegistryFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    ParkingDocumentType.SECOND_CAR_PROPERTY_REGISTER)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }

    public String getSecondCarPropertyRegistryFileNameToDisplay() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    ParkingDocumentType.SECOND_CAR_PROPERTY_REGISTER)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getSecondCarPropertyRegistryDocumentState() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getSecondCarPropertyRegistryDocumentState().name());
        }
        return null;
    }

    public String getSecondCarInsuranceFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType()
                    .equals(ParkingDocumentType.SECOND_CAR_INSURANCE)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }

    public String getSecondCarInsuranceFileNameToDisplay() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType()
                    .equals(ParkingDocumentType.SECOND_CAR_INSURANCE)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getSecondCarInsuranceDocumentState() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getSecondCarInsuranceDocumentState().name());
        }
        return null;
    }

    public String getSecondCarOwnerIdFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.SECOND_CAR_OWNER_ID)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }

    public String getSecondCarOwnerIdFileNameToDisplay() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.SECOND_CAR_OWNER_ID)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getSecondCarOwnerIdDocumentState() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getSecondCarOwnerIdDocumentState().name());
        }
        return null;
    }

    public String getSecondDeclarationAuthorizationFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    ParkingDocumentType.SECOND_DECLARATION_OF_AUTHORIZATION)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }

    public String getSecondDeclarationAuthorizationFileNameToDisplay() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    ParkingDocumentType.SECOND_DECLARATION_OF_AUTHORIZATION)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getSecondCarDeclarationDocumentState() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getSecondCarDeclarationDocumentState().name());
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

    public boolean getHasFirstCar() {
        return !StringUtils.isEmpty(getFirstCarMake());
    }

    public boolean getHasSecondCar() {
        return !StringUtils.isEmpty(getSecondCarMake());
    }

    public boolean getHasDriverLicense() {
        return (getDriverLicenseFileName() != null && getDriverLicenseFileName().length() > 0)
                || getDriverLicenseDocumentState() != null;
    }
    
    public List<RoleType> getSubmitAsRoles(){
        List<RoleType> roles = new ArrayList<RoleType>();
        if (getParty().isPerson()) {
            Person person = (Person) getParty();
            Teacher teacher = person.getTeacher();
            if (teacher != null && person.getPersonRole(RoleType.TEACHER) != null) {
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
                Collection<Registration> registrations = student.getRegistrationsByDegreeType(degreeType);
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
                occupations.add("<strong>Docente</strong><br/> Nº " + teacher.getTeacherNumber()
                        + "<br/>" + currenteDepartment);
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
                occupations.add("<strong>Bolseiro</strong><br/> Nº " + grantOwner.getNumber());
            }
        }
        return occupations;
    }

    public boolean hasCarContainingPlateNumber(String plateNumber) {
        if (getFirstCarPlateNumber() != null
                && (getFirstCarPlateNumber().equalsIgnoreCase(plateNumber) || getFirstCarPlateNumber()
                        .toLowerCase().contains(plateNumber.toLowerCase()))) {
            return true;
        }
        if (getSecondCarPlateNumber() != null
                && (getSecondCarPlateNumber().equalsIgnoreCase(plateNumber) || getSecondCarPlateNumber()
                        .toLowerCase().contains(plateNumber.toLowerCase()))) {
            return true;
        }
        return false;
    }

    public void deleteSecondCar() {
        setSecondCarMake(null);
        setSecondCarPlateNumber(null);
        setSecondCarDeclarationDocumentState(null);
        setSecondCarInsuranceDocumentState(null);
        setSecondCarOwnerIdDocumentState(null);
        setSecondCarPropertyRegistryDocumentState(null);

        deleteFile(ParkingDocumentType.SECOND_CAR_INSURANCE);
        deleteFile(ParkingDocumentType.SECOND_CAR_OWNER_ID);
        deleteFile(ParkingDocumentType.SECOND_CAR_PROPERTY_REGISTER);
        deleteFile(ParkingDocumentType.SECOND_DECLARATION_OF_AUTHORIZATION);
    }

    private void deleteFirstCar() {
        setFirstCarMake(null);
        setFirstCarPlateNumber(null);
        setFirstCarDeclarationDocumentState(null);
        setFirstCarInsuranceDocumentState(null);
        setFirstCarOwnerIdDocumentState(null);
        setFirstCarPropertyRegistryDocumentState(null);

        deleteFile(ParkingDocumentType.FIRST_CAR_INSURANCE);
        deleteFile(ParkingDocumentType.FIRST_CAR_OWNER_ID);
        deleteFile(ParkingDocumentType.FIRST_CAR_PROPERTY_REGISTER);
        deleteFile(ParkingDocumentType.FIRST_DECLARATION_OF_AUTHORIZATION);
    }

    private void deleteFile(ParkingDocumentType documentType) {
        ParkingDocument parkingDocument = getParkingDocument(documentType);
        if (parkingDocument != null) {
            String externalIdentifier = parkingDocument.getParkingFile()
                    .getExternalStorageIdentification();
            parkingDocument.delete();
            if (externalIdentifier != null) {
                FileManagerFactory.getFileManager().deleteFile(externalIdentifier);
            }
        }
    }

    public boolean hasCar() {
        return !StringUtils.isEmpty(getFirstCarMake());
    }

    public void delete() {
        if (canBeDeleted()) {
            setParty(null);
            deleteFile(ParkingDocumentType.DRIVER_LICENSE);
            deleteFirstCar();
            deleteSecondCar();
            List<ParkingRequest> parkingRequests = new ArrayList<ParkingRequest>(getParkingRequests());
            for (ParkingRequest parkingRequest : parkingRequests) {
                parkingRequest.delete();
            }
            deleteDomainObject();
        }
    }

    private boolean canBeDeleted() {
        return !hasCar();
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
            if (person.getTeacher() != null) {
                return person.getTeacher().getTeacherNumber();
            }
            if (person.getEmployee() != null && person.getEmployee().getCurrentWorkingContract() != null) {
                return person.getEmployee().getEmployeeNumber();
            }
            if (person.getStudent() != null) {
                DegreeType degreeType = person.getStudent().getMostSignificantDegreeType();
                Collection<Registration> registrations = person.getStudent().getRegistrationsByDegreeType(
                        degreeType);
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
}
