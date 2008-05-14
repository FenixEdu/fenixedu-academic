package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentPurposeType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.student.MobilityProgram;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.YearMonthDay;

public class DocumentRequestCreateBean extends RegistrationSelectExecutionYearBean {

    private DocumentRequestType chosenDocumentRequestType;

    private DocumentPurposeType chosenDocumentPurposeType;

    private String otherPurpose;

    private Boolean urgentRequest = Boolean.FALSE;

    private Boolean freeProcessed = Boolean.FALSE;

    private Boolean average = Boolean.FALSE;

    private Boolean detailed = Boolean.FALSE;

    private Boolean internshipAbolished = Boolean.FALSE;

    private Boolean internshipApproved = Boolean.FALSE;

    private Boolean studyPlan = Boolean.FALSE;

    private YearMonthDay exceptionalConclusionDate;

    private Boolean toBeCreated;

    private String schema;

    private Collection<String> warningsToReport;

    private Integer year;

    private CycleType requestedCycle;

    private MobilityProgram mobilityProgram;

    private boolean toUseAll = false;

    private List<DomainReference<Enrolment>> enrolments;

    private DomainReference<ExecutionSemester> executionSemester;

    private List<DomainReference<Exam>> exams;

    public DocumentRequestCreateBean(Registration registration) {
	super(registration);
	this.enrolments = new ArrayList<DomainReference<Enrolment>>();
	this.exams = new ArrayList<DomainReference<Exam>>();
    }

    public DocumentRequestType getChosenDocumentRequestType() {
	return chosenDocumentRequestType;
    }

    public void setChosenDocumentRequestType(DocumentRequestType chosenDocumentRequestType) {
	this.chosenDocumentRequestType = chosenDocumentRequestType;
    }

    public DocumentPurposeType getChosenDocumentPurposeType() {
	return chosenDocumentPurposeType;
    }

    public void setChosenDocumentPurposeType(DocumentPurposeType chosenDocumentPurposeType) {
	this.chosenDocumentPurposeType = chosenDocumentPurposeType;
    }

    public String getOtherPurpose() {
	return otherPurpose;
    }

    public void setOtherPurpose(String otherPurpose) {
	this.otherPurpose = otherPurpose;
    }

    public Boolean getUrgentRequest() {
	return urgentRequest;
    }

    public void setUrgentRequest(Boolean urgentRequest) {
	this.urgentRequest = urgentRequest;
    }

    public Boolean getFreeProcessed() {
	return freeProcessed;
    }

    public void setFreeProcessed(Boolean freeProcessed) {
	this.freeProcessed = freeProcessed;
    }

    public Boolean getAverage() {
	return average;
    }

    public void setAverage(Boolean average) {
	this.average = average;
    }

    public Boolean getDetailed() {
	return detailed;
    }

    public void setDetailed(Boolean detailed) {
	this.detailed = detailed;
    }

    public Boolean getInternshipAbolished() {
	return internshipAbolished;
    }

    public void setInternshipAbolished(Boolean internshipAbolished) {
	this.internshipAbolished = internshipAbolished;
    }

    public Boolean getInternshipApproved() {
	return internshipApproved;
    }

    public void setInternshipApproved(Boolean internshipApproved) {
	this.internshipApproved = internshipApproved;
    }

    public Boolean getStudyPlan() {
	return studyPlan;
    }

    public void setStudyPlan(Boolean studyPlan) {
	this.studyPlan = studyPlan;
    }

    public YearMonthDay getExceptionalConclusionDate() {
	return exceptionalConclusionDate;
    }

    public void setExceptionalConclusionDate(YearMonthDay exceptionalConclusionDate) {
	this.exceptionalConclusionDate = exceptionalConclusionDate;
    }

    public Integer getYear() {
	return this.year;
    }

    public void setYear(Integer year) {
	this.year = year;
    }

    public Boolean getToBeCreated() {
	return toBeCreated;
    }

    public void setToBeCreated(Boolean toBeCreated) {
	this.toBeCreated = toBeCreated;
    }

    /**
     * This method is only needed to report warnings to the user. While we don't
     * have enough info in our database to decide on what cases the document
     * request should abort (acording to the Administrative Office rules shown
     * in the use cases), warnings must be shown to the user.
     * 
     * @return
     */
    public Collection<String> getWarningsToReport() {
	if (warningsToReport == null) {
	    warningsToReport = new HashSet<String>();

	    if (chosenDocumentRequestType == DocumentRequestType.APPROVEMENT_CERTIFICATE) {
		if (chosenDocumentPurposeType == DocumentPurposeType.PROFESSIONAL) {
		    warningsToReport.add("aprovementType.professionalPurpose.thirdGrade");
		}

		warningsToReport.add("aprovementType.finished.degree");
	    }

	    if (chosenDocumentRequestType == DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE) {
		warningsToReport.add("degreeFinalizationType.withoutDegreeCertificate");
	    }
	}

	return warningsToReport;
    }

    public boolean hasWarningsToReport() {
	if (warningsToReport == null) {
	    getWarningsToReport();
	}
	return !warningsToReport.isEmpty();
    }

    public void setPurpose(DocumentPurposeType chosenDocumentPurposeType, String otherPurpose) {

	if (chosenDocumentPurposeType != null && chosenDocumentPurposeType.equals(DocumentPurposeType.OTHER)
		&& (otherPurpose == null || otherPurpose.length() == 0)) {
	    throw new DomainException("DocumentRequestCreateBean.error.other.purpose.required");
	}

	this.chosenDocumentPurposeType = chosenDocumentPurposeType;
	this.otherPurpose = otherPurpose;
    }

    public String getSchema() {
	return schema;
    }

    public void setSchema(String schema) {
	this.schema = schema;
    }

    public CycleType getRequestedCycle() {
	return requestedCycle;
    }

    public void setRequestedCycle(final CycleType cycleType) {
	this.requestedCycle = cycleType;
    }

    final public boolean getHasAdditionalInformation() {
	return getChosenDocumentRequestType().getHasAdditionalInformation();
    }

    final public boolean getHasCycleTypeDependency() {
	return getChosenDocumentRequestType().getHasCycleTypeDependency(getRegistration().getDegreeType());
    }

    final public boolean getHasMobilityProgramDependency() {
	return getRegistration().getCurriculum().hasAnyExternalApprovedEnrolment()
		&& (chosenDocumentRequestType == DocumentRequestType.APPROVEMENT_CERTIFICATE || chosenDocumentRequestType == DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE);
    }

    final public MobilityProgram getMobilityProgram() {
	return mobilityProgram;
    }

    final public void setMobilityProgram(final MobilityProgram mobilityProgram) {
	this.mobilityProgram = mobilityProgram;
    }

    public boolean isToUseAll() {
	return toUseAll;
    }

    public void setToUseAll(boolean toUseAll) {
	this.toUseAll = toUseAll;
    }

    public List<Enrolment> getEnrolments() {
	final List<Enrolment> result = new ArrayList<Enrolment>();
	for (final DomainReference<Enrolment> each : this.enrolments) {
	    result.add(each.getObject());
	}

	return result;
    }

    public void setEnrolments(List<Enrolment> enrolments) {
	final List<DomainReference<Enrolment>> enrolmentsToSet = new ArrayList<DomainReference<Enrolment>>();
	for (final Enrolment enrolment : enrolments) {
	    enrolmentsToSet.add(new DomainReference<Enrolment>(enrolment));
	}

	this.enrolments = enrolmentsToSet;
    }

    public List<Exam> getExams() {
	final List<Exam> result = new ArrayList<Exam>();
	for (final DomainReference<Exam> each : this.exams) {
	    result.add(each.getObject());
	}

	return result;
    }

    public void setExams(List<Exam> exams) {
	final List<DomainReference<Exam>> result = new ArrayList<DomainReference<Exam>>();
	for (final Exam each : exams) {
	    result.add(new DomainReference<Exam>(each));
	}

	this.exams = result;
    }

    public ExecutionSemester getExecutionPeriod() {
	return (this.executionSemester != null) ? this.executionSemester.getObject() : null;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
	this.executionSemester = (executionSemester != null) ? new DomainReference<ExecutionSemester>(executionSemester) : null;
    }

}
