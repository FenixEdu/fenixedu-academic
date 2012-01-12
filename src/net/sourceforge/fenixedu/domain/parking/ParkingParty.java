package net.sourceforge.fenixedu.domain.parking;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.parking.ParkingPartyBean;
import net.sourceforge.fenixedu.dataTransferObject.parking.VehicleBean;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PartyClassification;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
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
	    super.setCardNumber(cardNumber);
	    new ParkingPartyHistory(this, false);
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
	    return requests.get(0);
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
	    GrantOwner grantOwner = person.getGrantOwner();
	    if (grantOwner != null && person.getPersonRole(RoleType.GRANT_OWNER) != null && grantOwner.hasCurrentContract()) {
		roles.add(RoleType.GRANT_OWNER);
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
		String employeeType = RoleType.TEACHER.getLocalizedName();
		if (teacher.isMonitor(ExecutionSemester.readActualExecutionSemester())) {
		    employeeType = "Monitor";
		}
		StringBuilder stringBuilder = new StringBuilder(BundleUtil.getStringFromResourceBundle(
			"resources.ParkingResources", "message.person.identification", new String[] { employeeType,
				teacher.getPerson().getIstUsername() }));
		Department currentWorkingDepartment = teacher.getCurrentWorkingDepartment();
		if (currentWorkingDepartment != null) {
		    stringBuilder.append(currentWorkingDepartment.getName()).append("<br/>");
		}
		PersonContractSituation currentOrLastTeacherContractSituation = teacher
			.getCurrentOrLastTeacherContractSituation();
		if (currentOrLastTeacherContractSituation != null) {
		    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/MM/dd");
		    stringBuilder.append("\n (Data inicio: ").append(
			    fmt.print(currentOrLastTeacherContractSituation.getBeginDate()));
		    if (currentOrLastTeacherContractSituation.getEndDate() != null) {
			stringBuilder.append(" - Data fim: ").append(
				fmt.print(currentOrLastTeacherContractSituation.getEndDate()));
		    }
		    stringBuilder.append(")<br/>");
		} else {
		    stringBuilder.append("(inactivo)<br/>");
		}
		occupations.add(stringBuilder.toString());
	    }
	    Employee employee = person.getEmployee();
	    if (employee != null && person.getPersonRole(RoleType.TEACHER) == null
		    && person.getPersonRole(RoleType.EMPLOYEE) != null
		    && employee.getCurrentContractByContractType(AccountabilityTypeEnum.WORKING_CONTRACT) != null
		    && !person.isPersonResearcher()) {
		StringBuilder stringBuilder = new StringBuilder(BundleUtil.getStringFromResourceBundle(
			"resources.ParkingResources", "message.person.identification",
			new String[] { RoleType.EMPLOYEE.getLocalizedName(), employee.getEmployeeNumber().toString() }));

		Unit currentUnit = employee.getCurrentWorkingPlace();
		if (currentUnit != null) {
		    stringBuilder.append(currentUnit.getName()).append("<br/>");
		}
		if (employee.getAssiduousness() != null) {
		    AssiduousnessStatusHistory assiduousnessStatusHistory = employee.getAssiduousness()
			    .getCurrentOrLastAssiduousnessStatusHistory();
		    if (assiduousnessStatusHistory != null) {
			DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/MM/dd");
			stringBuilder.append("<br/> (Data inicio: ").append(fmt.print(assiduousnessStatusHistory.getBeginDate()));
			if (assiduousnessStatusHistory.getEndDate() != null) {
			    stringBuilder.append(" - Data fim: ").append(fmt.print(assiduousnessStatusHistory.getEndDate()));
			}
			stringBuilder.append(")<br/>");
		    }
		}
		occupations.add(stringBuilder.toString());
	    }
	    GrantOwner grantOwner = person.getGrantOwner();
	    if (grantOwner != null && person.getPersonRole(RoleType.GRANT_OWNER) != null && grantOwner.hasCurrentContract()) {
		List<GrantContractRegime> contractRegimeList = new ArrayList<GrantContractRegime>();
		StringBuilder stringBuilder = new StringBuilder(BundleUtil.getStringFromResourceBundle(
			"resources.ParkingResources", "message.person.identification",
			new String[] { RoleType.GRANT_OWNER.getLocalizedName(), grantOwner.getNumber().toString() }));

		for (GrantContract contract : grantOwner.getGrantContracts()) {
		    contractRegimeList.addAll(contract.getContractRegimes());
		}
		Collections.sort(contractRegimeList, new BeanComparator("dateBeginContractYearMonthDay"));
		for (GrantContractRegime contractRegime : contractRegimeList) {

		    stringBuilder.append("<br/><strong>Inicio:</strong> "
			    + contractRegime.getDateBeginContractYearMonthDay().toString("dd/MM/yyyy"));
		    LocalDate endDate = contractRegime.getDateEndContractYearMonthDay().toLocalDate();
		    boolean rescinded = false;
		    if (contractRegime.getGrantContract().getRescissionDate() != null
			    && endDate.isAfter(contractRegime.getGrantContract().getRescissionDate())) {
			endDate = contractRegime.getGrantContract().getRescissionDate();
			if (endDate.isBefore(new LocalDate())) {
			    rescinded = true;
			}
		    }
		    stringBuilder.append("&nbsp&nbsp&nbsp -&nbsp&nbsp&nbsp<strong>Fim:</strong> "
			    + endDate.toString("dd/MM/yyyy"));
		    stringBuilder.append("&nbsp&nbsp&nbsp -&nbsp&nbsp&nbsp<strong>Activo:</strong> ");
		    if (contractRegime.isActive() && !rescinded) {
			stringBuilder.append("Sim");
		    } else {
			stringBuilder.append("Nao");
		    }
		}
		occupations.add(stringBuilder.toString());
	    }
	    if (person.isPersonResearcher()) {
		String researchUnitNames = person.getWorkingResearchUnitNames();
		if (!StringUtils.isEmpty(researchUnitNames)
			|| !person.getPartyClassification().equals(PartyClassification.TEACHER)) {
		    occupations.add(BundleUtil.getStringFromResourceBundle("resources.ParkingResources",
			    "message.person.identification", new String[] { RoleType.RESEARCHER.getLocalizedName(),
				    person.getMostSignificantNumber().toString() }));
		    if (!StringUtils.isEmpty(researchUnitNames)) {
			occupations.add("<br/>" + researchUnitNames);
		    }
		}
	    }
	    Student student = person.getStudent();
	    if (student != null && person.getPersonRole(RoleType.STUDENT) != null) {

		StringBuilder stringBuilder = null;
		for (Registration registration : student.getActiveRegistrations()) {
		    StudentCurricularPlan scp = registration.getLastStudentCurricularPlan();
		    if (scp != null) {
			if (stringBuilder == null) {
			    stringBuilder = new StringBuilder(BundleUtil.getStringFromResourceBundle(
				    "resources.ParkingResources", "message.person.identification", new String[] {
					    RoleType.STUDENT.getLocalizedName(), student.getNumber().toString() }));

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

		for (PhdIndividualProgramProcess phdIndividualProgramProcess : person.getPhdIndividualProgramProcesses()) {
		    if (phdIndividualProgramProcess.getActiveState().isPhdActive()) {
			if (stringBuilder == null) {
			    stringBuilder = new StringBuilder(BundleUtil.getStringFromResourceBundle(
				    "resources.ParkingResources", "message.person.identification", new String[] {
					    RoleType.STUDENT.getLocalizedName(), student.getNumber().toString() }));
			}
			stringBuilder.append("\nPrograma Doutoral: ").append(
				phdIndividualProgramProcess.getPhdProgram().getName());
		    }

		}

		if (stringBuilder != null) {
		    occupations.add(stringBuilder.toString());
		}
	    }
	    List<Invitation> invitations = person.getActiveInvitations();
	    if (!invitations.isEmpty()) {
		StringBuilder stringBuilder = new StringBuilder("<strong>Convidado</strong><br/>");
		for (Invitation invitation : invitations) {
		    stringBuilder.append("<strong>Por:</strong> ").append(invitation.getUnit().getName()).append("<br/>");
		    stringBuilder.append("<strong>Inicio:</strong> " + invitation.getBeginDate().toString("dd/MM/yyyy"));
		    stringBuilder.append("&nbsp&nbsp&nbsp -&nbsp&nbsp&nbsp<strong>Fim:</strong> "
			    + invitation.getEndDate().toString("dd/MM/yyyy"));
		    occupations.add(stringBuilder.toString());
		}
	    }
	}
	return occupations;
    }

    public List<String> getPastOccupations() {
	List<String> occupations = new ArrayList<String>();
	if (getParty().isPerson()) {
	    Person person = (Person) getParty();
	    Teacher teacher = person.getTeacher();
	    if (teacher != null) {
		StringBuilder stringBuilder = new StringBuilder();
		String currentDepartment = "";
		if (teacher.getCurrentWorkingDepartment() != null) {
		    currentDepartment = teacher.getCurrentWorkingDepartment().getName();
		}
		if (teacher.isMonitor(ExecutionSemester.readActualExecutionSemester())) {
		    stringBuilder.append("<strong>Monitor</strong><br/> N� " + teacher.getPerson().getIstUsername() + "<br/>"
			    + currentDepartment);
		} else {
		    stringBuilder.append("<strong>Docente</strong><br/> N� " + teacher.getPerson().getIstUsername() + "<br/>"
			    + currentDepartment);
		}

		PersonContractSituation currentOrLastTeacherContractSituation = teacher
			.getCurrentOrLastTeacherContractSituation();
		if (currentOrLastTeacherContractSituation != null) {
		    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/MM/dd");
		    stringBuilder.append("\n (Data inicio: ").append(
			    fmt.print(currentOrLastTeacherContractSituation.getBeginDate()));
		    if (currentOrLastTeacherContractSituation.getEndDate() != null) {
			stringBuilder.append(" - Data fim: ").append(
				fmt.print(currentOrLastTeacherContractSituation.getEndDate()));
		    }
		    stringBuilder.append(")<br/>");
		} else {
		    stringBuilder.append("(inactivo)<br/>");
		}
		occupations.add(stringBuilder.toString());
	    }
	    Employee employee = person.getEmployee();
	    if (employee != null && person.getPersonRole(RoleType.TEACHER) == null
		    && employee.getCurrentContractByContractType(AccountabilityTypeEnum.WORKING_CONTRACT) != null
		    && !person.isPersonResearcher()) {
		StringBuilder stringBuilder = new StringBuilder();
		AssiduousnessStatusHistory assiduousnessStatusHistory = employee.getAssiduousness()
			.getCurrentOrLastAssiduousnessStatusHistory();
		if (assiduousnessStatusHistory != null) {
		    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/MM/dd");
		    stringBuilder.append("<br/> (Data inicio: ").append(fmt.print(assiduousnessStatusHistory.getBeginDate()));
		    if (assiduousnessStatusHistory.getEndDate() != null) {
			stringBuilder.append(" - Data fim: ").append(fmt.print(assiduousnessStatusHistory.getEndDate()));
		    }
		    stringBuilder.append(")<br/>");
		}
		occupations.add(stringBuilder.toString());
	    }
	    Student student = person.getStudent();
	    if (student != null) {

		StringBuilder stringBuilder = null;
		for (Registration registration : student.getRegistrations()) {
		    StudentCurricularPlan scp = registration.getLastStudentCurricularPlan();
		    if (scp != null) {
			if (stringBuilder == null) {
			    stringBuilder = new StringBuilder("<strong>Estudante</strong><br/> N� ");
			    stringBuilder.append(student.getNumber()).append("<br/>");
			}
			stringBuilder.append("\n").append(scp.getDegreeCurricularPlan().getName());
			stringBuilder.append("\n (").append(registration.getCurricularYear()).append("� ano");
			if (isFirstTimeEnrolledInCurrentYear(registration)) {
			    stringBuilder.append(" - 1� vez)");
			} else {
			    stringBuilder.append(")");
			}
			stringBuilder.append("<br/>M�dia: ").append(registration.getAverage());
			stringBuilder.append("<br/>");
		    }
		}
		if (stringBuilder != null) {
		    occupations.add(stringBuilder.toString());
		}
	    }
	    if (person.isPersonResearcher()) {
		String researchUnitNames = person.getWorkingResearchUnitNames();
		if (!StringUtils.isEmpty(researchUnitNames)
			|| !person.getPartyClassification().equals(PartyClassification.TEACHER)) {
		    occupations.add("<strong>Investigador</strong><br/> N� " + person.getMostSignificantNumber());
		    if (!StringUtils.isEmpty(researchUnitNames)) {
			occupations.add("<br/>" + researchUnitNames);
		    }
		}
	    }
	    GrantOwner grantOwner = person.getGrantOwner();
	    if (grantOwner != null) {
		List<GrantContractRegime> contractRegimeList = new ArrayList<GrantContractRegime>();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<strong>Bolseiro</strong><br/> N� " + grantOwner.getNumber());
		for (GrantContract contract : grantOwner.getGrantContracts()) {
		    contractRegimeList.addAll(contract.getContractRegimes());
		}
		Collections.sort(contractRegimeList, new BeanComparator("dateBeginContractYearMonthDay"));
		for (GrantContractRegime contractRegime : contractRegimeList) {
		    stringBuilder.append("<br/><strong>In�cio:</strong> "
			    + contractRegime.getDateBeginContractYearMonthDay().toString("dd/MM/yyyy"));
		    stringBuilder.append("&nbsp&nbsp&nbsp -&nbsp&nbsp&nbsp<strong>Fim:</strong> "
			    + contractRegime.getDateEndContractYearMonthDay().toString("dd/MM/yyyy"));
		    stringBuilder.append("&nbsp&nbsp&nbsp -&nbsp&nbsp&nbsp<strong>Activo:</strong> ");
		    if (contractRegime.isActive()) {
			stringBuilder.append("Sim");
		    } else {
			stringBuilder.append("N�o");
		    }
		}
		occupations.add(stringBuilder.toString());
	    }
	    List<Invitation> invitations = person.getActiveInvitations();
	    if (!invitations.isEmpty()) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<strong>Convidado</strong><br/>");
		for (Invitation invitation : invitations) {
		    stringBuilder.append("<strong>Por:</strong> ").append(invitation.getUnit().getName()).append("<br/>");
		    stringBuilder.append("<strong>In�cio:</strong> " + invitation.getBeginDate().toString("dd/MM/yyyy"));
		    stringBuilder.append("&nbsp&nbsp&nbsp -&nbsp&nbsp&nbsp<strong>Fim:</strong> "
			    + invitation.getEndDate().toString("dd/MM/yyyy"));
		    occupations.add(stringBuilder.toString());
		}
	    }
	}
	return occupations;
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
	    removeParty();
	    removeParkingGroup();
	    deleteDriverLicenseDocument();
	    for (; getVehicles().size() != 0; getVehicles().get(0).delete())
		;
	    for (; getParkingRequests().size() != 0; getParkingRequests().get(0).delete())
		;
	    removeRootDomainObject();
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

    public Integer getMostSignificantNumber() {
	if (getParty().isPerson()) {
	    if (getPhdNumber() != null) {
		return getPhdNumber();
	    }
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

	    final GrantOwner grantOwner = person.getGrantOwner();

	    if (grantOwner != null && grantOwner.hasCurrentContract()) {
		return grantOwner.getNumber();
	    }
	    if (employee != null && isTeacher && isMonitor) {
		return employee.getEmployeeNumber();
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
	return cardAlwaysValid ? (oldDate == null ? false : true) : ((oldDate == null || (!oldDate.equals(newDate))) ? true
		: oldDate.equals(newDate));
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
	    if (getParkingGroup().getGroupName().equalsIgnoreCase("N�o Docentes")) {
		return person.getEmployee() != null && person.getEmployee().getCurrentWorkingPlace() != null;
	    }
	    if (getParkingGroup().getGroupName().equalsIgnoreCase("Especiais")) {
		return person.getPartyClassification() != PartyClassification.PERSON;
	    }
	    if (getParkingGroup().getGroupName().equalsIgnoreCase("2� ciclo")) {
		if (person.hasStudent()) {
		    return canRequestUnlimitedCard(person.getStudent());
		} else {
		    return Boolean.FALSE;
		}
	    }
	    if (getParkingGroup().getGroupName().equalsIgnoreCase("Bolseiros")) {
		return person.hasGrantOwner() && person.getGrantOwner().hasCurrentContract();
	    }
	    if (getParkingGroup().getGroupName().equalsIgnoreCase("3� ciclo")) {
		if (person.hasStudent()) {
		    Registration registration = getRegistrationByDegreeType(person.getStudent(),
			    DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA);
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
	return vehicles.size() > 0 ? vehicles.get(0) : null;
    }

    public Vehicle getSecondVehicle() {
	List<Vehicle> vehicles = new ArrayList<Vehicle>(getVehicles());
	Collections.sort(vehicles, new BeanComparator("plateNumber"));
	return vehicles.size() > 1 ? vehicles.get(1) : null;
    }

}
