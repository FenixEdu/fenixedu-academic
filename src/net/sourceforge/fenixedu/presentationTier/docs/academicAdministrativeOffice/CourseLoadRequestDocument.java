package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CourseLoadRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

public class CourseLoadRequestDocument extends AdministrativeOfficeDocument {

    private static final long serialVersionUID = 10L;

    protected CourseLoadRequestDocument(final CourseLoadRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected CourseLoadRequest getDocumentRequest() {
	return (CourseLoadRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
	setPersonFields();
	addParametersInformation();
    }

    private void addParametersInformation() {
	addParameter("studentNumber", getStudentNumber());
	addParameter("degreeDescription", getDegreeDescription());

	final Employee employee = AccessControl.getPerson().getEmployee();

	Person activeUnitCoordinator = employee.getCurrentWorkingPlace().getActiveUnitCoordinator();
	addParameter("administrativeOfficeCoordinatorName", activeUnitCoordinator
		.getName());
	addParameter("administrativeOfficeName", employee.getCurrentWorkingPlace().getName());
	addParameter("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());
	addParameter("universityName", UniversityUnit.getInstitutionsUniversityUnit().getName());
	addParameter("day", new LocalDate().toString(DD_MMMM_YYYY, getLocale()));

	AdministrativeOffice administrativeOffice = employee.getCurrentWorkingPlace().getAdministrativeOffice();

	addParameter("coordinatorSignature", coordinatorSignature(administrativeOffice, activeUnitCoordinator));
	addParameter("adminOfficeIntroMessage", adminOfficeIntroMessage(administrativeOffice, activeUnitCoordinator));

	addParameter("coordinatorWithoutArticle", coordinatorSignatureWithoutArticle(administrativeOffice, activeUnitCoordinator));

	createCourseLoadsList();
    }

    private String coordinatorSignatureWithoutArticle(AdministrativeOffice administrativeOffice, Person activeUnitCoordinator) {
	String coordinatorSignature = coordinatorSignature(administrativeOffice, activeUnitCoordinator);
	String withoutArticle = coordinatorSignature.substring(2, coordinatorSignature.length());
	Integer index = withoutArticle.indexOf(" do ");

	return withoutArticle.substring(0, index) + withoutArticle.substring(index, withoutArticle.length()).toUpperCase();
    }

    private String adminOfficeIntroMessage(AdministrativeOffice administrativeOffice, Person activeUnitCoordinator) {
	String adminOfficeIntroMessage = "message.academicServiceRequest.course.load.admin.office.intro";

	if (administrativeOffice.isMasterDegree()) {
	    adminOfficeIntroMessage += ".master.degree";
	} else {
	    adminOfficeIntroMessage += ".degree";
	}

	if (activeUnitCoordinator.isMale()) {
	    adminOfficeIntroMessage += ".male";
	} else {
	    adminOfficeIntroMessage += ".female";
	}

	return ResourceBundle.getBundle("resources.AcademicAdminOffice", getLocale()).getString(adminOfficeIntroMessage);
    }

    private String coordinatorSignature(AdministrativeOffice administrativeOffice, Person activeUnitCoordinator) {
	String coordinatorSignatureMessage = "message.academicServiceRequest.course.load.coordinator.signature";

	if (administrativeOffice.isMasterDegree()) {
	    coordinatorSignatureMessage += ".master.degree";
	} else {
	    coordinatorSignatureMessage += ".degree";
	}

	if (activeUnitCoordinator.isMale()) {
	    coordinatorSignatureMessage += ".male";
	} else {
	    coordinatorSignatureMessage += ".female";
	}

	return ResourceBundle.getBundle("resources.AcademicAdminOffice", getLocale()).getString(coordinatorSignatureMessage);
    }

    @Override
    protected String getDegreeDescription() {
	final CycleType requestedCycle = getDocumentRequest().getRequestedCycle();
	if (requestedCycle == null) {
	    final Registration registration = getDocumentRequest().getRegistration();
	    final DegreeType degreeType = registration.getDegreeType();
	    final CycleType cycleType = degreeType.hasExactlyOneCycleType() ? degreeType.getCycleType() : registration
		    .getCycleType(getExecutionYear());
	    return registration.getDegreeDescription(getExecutionYear(), cycleType, getLocale());
	}
	return getDocumentRequest().getRegistration().getDegreeDescription(getExecutionYear(), requestedCycle, getLocale());
    }

    private String getStudentNumber() {
	final Registration registration = getDocumentRequest().getRegistration();
	if (CourseLoadRequest.FREE_PAYMENT_AGREEMENTS.contains(registration.getRegistrationAgreement())) {
	    final String agreementInformation = registration.getAgreementInformation();
	    if (!StringUtils.isEmpty(agreementInformation)) {
		return registration.getRegistrationAgreement().toString() + SINGLE_SPACE + agreementInformation;
	    }
	}
	return registration.getStudent().getNumber().toString();
    }

    private void createCourseLoadsList() {
	final List<CourseLoadEntry> bolonha = new ArrayList<CourseLoadEntry>();
	final List<CourseLoadEntry> preBolonha = new ArrayList<CourseLoadEntry>();
	final List<CourseLoadEntry> dissertations = new ArrayList<CourseLoadEntry>();

	addParameter("bolonhaList", bolonha);
	addParameter("preBolonhaList", preBolonha);
	addParameter("dissertationsList", dissertations);

	for (final Enrolment enrolment : getDocumentRequest().getEnrolmentsSet()) {
	    
	    if (enrolment.isBolonhaDegree()) {
		
		if (enrolment.isDissertation()) {
		    dissertations.add(new BolonhaCourseLoadEntry(enrolment));
		} else {
		    bolonha.add(new BolonhaCourseLoadEntry(enrolment));
		}
		
	    } else {
		preBolonha.add(new PreBolonhaCourseLoadEntry(enrolment));
	    }
	    
	}
	
	Collections.sort(bolonha);
	Collections.sort(preBolonha);
	Collections.sort(dissertations);
    }

    @Override
    protected boolean showPriceFields() {
	return false;
    }

    @Override
    protected void setPersonFields() {
	addParameter("name", getDocumentRequest().getPerson().getName());
    }

    static abstract public class CourseLoadEntry implements Comparable<CourseLoadEntry> {
	private String curricularCourseName;
	private String year;
	private Double total;

	protected CourseLoadEntry(final String name, final String year) {
	    this.curricularCourseName = name;
	    this.year = year;
	}

	public String getCurricularCourseName() {
	    return curricularCourseName;
	}

	public void setCurricularCourseName(String curricularCourseName) {
	    this.curricularCourseName = curricularCourseName;
	}

	public String getYear() {
	    return year;
	}

	public void setYear(String year) {
	    this.year = year;
	}

	public int compareTo(CourseLoadEntry other) {
	    return getCurricularCourseName().compareTo(other.getCurricularCourseName());
	}

	public Double getTotal() {
	    return total;
	}

	public void setTotal(Double total) {
	    this.total = total;
	}

	public Boolean getCourseLoadCorrect() {
	    return Boolean.valueOf(total.doubleValue() != 0d);
	}

	static CourseLoadEntry create(final Enrolment enrolment) {
	    if (enrolment.isBolonhaDegree()) {
		return new BolonhaCourseLoadEntry(enrolment);
	    } else {
		return new PreBolonhaCourseLoadEntry(enrolment);
	    }
	}
    }

    static public class BolonhaCourseLoadEntry extends CourseLoadEntry {
	private Double contactLoad;
	private Double autonomousWork;

	public BolonhaCourseLoadEntry(final Enrolment enrolment) {
	    super(enrolment.getCurricularCourse().getName(), enrolment.getExecutionYear().getYear());

	    final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
	    setContactLoad(curricularCourse.getContactLoad(enrolment.getExecutionPeriod()));
	    setAutonomousWork(curricularCourse.getAutonomousWorkHours(enrolment.getExecutionPeriod()));
	    setTotal(curricularCourse.getTotalLoad(enrolment.getExecutionPeriod()));
	}

	public Double getAutonomousWork() {
	    return autonomousWork;
	}

	public void setAutonomousWork(Double autonomousWork) {
	    this.autonomousWork = autonomousWork;
	}

	public Double getContactLoad() {
	    return contactLoad;
	}

	public void setContactLoad(Double contactLoad) {
	    this.contactLoad = contactLoad;
	}
    }

    static public class PreBolonhaCourseLoadEntry extends CourseLoadEntry {
	private Double theoreticalHours;
	private Double praticalHours;
	private Double labHours;
	private Double theoPratHours;

	public PreBolonhaCourseLoadEntry(final Enrolment enrolment) {
	    super(enrolment.getCurricularCourse().getName(), enrolment.getExecutionYear().getYear());
	    initInformation(enrolment.getCurricularCourse());
	}

	private void initInformation(final CurricularCourse curricularCourse) {
	    setTheoreticalHours(curricularCourse.getTheoreticalHours());
	    setPraticalHours(curricularCourse.getPraticalHours());
	    setLabHours(curricularCourse.getLabHours());
	    setTheoPratHours(curricularCourse.getTheoPratHours());
	    setTotal(calculateTotal(curricularCourse));
	}

	public Double getLabHours() {
	    return labHours;
	}

	public void setLabHours(Double labHours) {
	    this.labHours = labHours;
	}

	public Double getPraticalHours() {
	    return praticalHours;
	}

	public void setPraticalHours(Double praticalHours) {
	    this.praticalHours = praticalHours;
	}

	public Double getTheoPratHours() {
	    return theoPratHours;
	}

	public void setTheoPratHours(Double theoPratHours) {
	    this.theoPratHours = theoPratHours;
	}

	public Double getTheoreticalHours() {
	    return theoreticalHours;
	}

	public void setTheoreticalHours(Double theoreticalHours) {
	    this.theoreticalHours = theoreticalHours;
	}

	private Double calculateTotal(final CurricularCourse curricularCourse) {
	    double result = 0d;
	    result += curricularCourse.getTheoreticalHours();
	    result += curricularCourse.getPraticalHours();
	    result += curricularCourse.getLabHours();
	    result += curricularCourse.getTheoPratHours();
	    return Double.valueOf(result);
	}
    }

}
