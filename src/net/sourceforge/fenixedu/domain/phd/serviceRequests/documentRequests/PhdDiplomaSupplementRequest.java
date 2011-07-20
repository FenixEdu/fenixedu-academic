package net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.DegreeOfficialPublication;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsGraduationGradeConversionTable;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramInformation;
import net.sourceforge.fenixedu.domain.phd.conclusion.PhdConclusionProcess;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdDocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisFinalGrade;
import net.sourceforge.fenixedu.domain.serviceRequests.IDiplomaSupplementRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IRectorateSubmissionBatchDocumentEntry;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice.DiplomaSupplement;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PhdDiplomaSupplementRequest extends PhdDiplomaSupplementRequest_Base implements IDiplomaSupplementRequest,
	IRectorateSubmissionBatchDocumentEntry {

    protected PhdDiplomaSupplementRequest() {
	super();
    }

    protected PhdDiplomaSupplementRequest(final PhdDocumentRequestCreateBean bean) {
	this();
	init(bean);
    }

    @Override
    protected void init(AcademicServiceRequestCreateBean bean) {
	throw new DomainException("invoke init(PhdDocumentRequestCreateBean)");
    }

    @Override
    protected void init(PhdAcademicServiceRequestCreateBean bean) {
	throw new DomainException("invoke init(PhdDocumentRequestCreateBean)");
    }

    protected void init(final PhdDocumentRequestCreateBean bean) {
	super.init(bean);
	checkParameters(bean);

	getPhdIndividualProgramProcess().getPerson().setGivenNames(bean.getGivenNames());
	getPhdIndividualProgramProcess().getPerson().setFamilyNames(bean.getFamilyNames());
    }

    private void checkParameters(final PhdDocumentRequestCreateBean bean) {
	if (!getPhdIndividualProgramProcess().getStudent().getPerson().getName()
		.equals(bean.getGivenNames() + " " + bean.getFamilyNames())) {
	    throw new DomainException("error.diplomaSupplementRequest.splittedNamesDoNotMatch");
	}

	PhdIndividualProgramProcess process = getPhdIndividualProgramProcess();
	if (!process.hasRegistryDiplomaRequest() && process.hasDiplomaRequest()) {
	    throw new DomainException(
		    "error.diplomaSupplementRequest.cannotAskForSupplementWithoutEitherRegistryDiplomaOrDiplomaRequest");
	}

	final PhdDiplomaSupplementRequest supplement = process.getDiplomaSupplementRequest();
	if (supplement != null && supplement != this) {
	    throw new DomainException("error.diplomaSupplementRequest.alreadyRequested");
	}
    }

    @Override
    public boolean isPayedUponCreation() {
	return true;
    }

    @Override
    public boolean isToPrint() {
	return false;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
	return true;
    }

    @Override
    public boolean isManagedWithRectorateSubmissionBatch() {
	return true;
    }

    @Override
    public EventType getEventType() {
	return null;
    }

    @Override
    public boolean hasPersonalInfo() {
	return false;
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.DIPLOMA_SUPPLEMENT_REQUEST;
    }

    @Override
    public String getDocumentTemplateKey() {
	return DiplomaSupplement.class.getName();
    }

    @Override
    public CycleType getRequestedCycle() {
	return CycleType.THIRD_CYCLE;
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	try {
	    verifyIsToProcessAndHasPersonalInfo(academicServiceRequestBean);
	    verifyIsToDeliveredAndIsPayed(academicServiceRequestBean);
	} catch (DomainException e) {
	    throw new PhdDomainOperationException(e.getKey(), e, e.getArgs());
	}

	super.internalChangeState(academicServiceRequestBean);
	if (academicServiceRequestBean.isToProcess()) {
	    if (!getPhdIndividualProgramProcess().isConcluded()) {
		throw new PhdDomainOperationException(
			"error.phdDiplomaSupplement.registration.not.submited.to.conclusion.process");
	    }

	    if (getRegistryCode() == null) {
		getRegistryDiplomaRequest().getRegistryCode().addDocumentRequest(this);
		getAdministrativeOffice().getCurrentRectorateSubmissionBatch().addDocumentRequest(this);
	    }

	    if (getLastGeneratedDocument() == null) {
		generateDocument();
	    }
	}
    }

    public static PhdDiplomaSupplementRequest create(PhdDocumentRequestCreateBean bean) {
	return new PhdDiplomaSupplementRequest(bean);
    }

    @Override
    public String getDescription() {
	return getDescription(getAcademicServiceRequestType(), getDocumentRequestType().getQualifiedName() + "."
		+ DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA.name());
    }

    @Override
    public String getGraduateTitle(Locale locale) {
	return getPhdIndividualProgramProcess().getGraduateTitle(locale);
    }

    @Override
    public Integer getRegistrationNumber() {
	return getStudent().getNumber();
    }

    @Override
    public String getPrevailingScientificArea(final Locale locale) {
	return getPhdIndividualProgramProcess().getPhdProgram().getName().getContent(Language.valueOf(locale.getLanguage()));
    }

    @Override
    public long getEctsCredits() {
	PhdProgramInformation information = getPhdInformationForConclusionDate();

	if (information == null) {
	    return 0;
	}

	return information.getMaxStudyPlanEctsCredits().add(information.getMaxThesisEctsCredits()).longValue();
    }

    @Override
    public DegreeOfficialPublication getDegreeOfficialPublication() {
	PhdConclusionProcess lastConclusionProcess = getPhdIndividualProgramProcess().getLastConclusionProcess();

	DateTime conclusionDate = null;
	if (!getPhdIndividualProgramProcess().getStudyPlan().isExempted()) {
	    Registration registration = getPhdIndividualProgramProcess().getRegistration();
	    conclusionDate = registration.getConclusionDateForBolonha().toDateMidnight().toDateTime();
	} else {
	    conclusionDate = lastConclusionProcess.getConclusionDate().toDateMidnight().toDateTime();
	}

	return getPhdIndividualProgramProcess().getPhdProgram().getDegree().getOfficialPublication(conclusionDate);
    }

    @Override
    public Integer getFinalAverage() {
	if (getPhdIndividualProgramProcess().getStudyPlan().isExempted()) {
	    return null;
	}

	return getPhdIndividualProgramProcess().getRegistration().getFinalAverage(getRequestedCycle());
    }

    @Override
    public String getFinalAverageQualified(final Locale locale) {
	if (getPhdIndividualProgramProcess().getStudyPlan().isExempted()) {
	    return null;
	}

	Integer finalAverage = getPhdIndividualProgramProcess().getRegistration().getFinalAverage(getRequestedCycle());
	String qualifiedAverageGrade;
	if (finalAverage <= 13) {
	    qualifiedAverageGrade = "sufficient";
	} else if (finalAverage <= 15) {
	    qualifiedAverageGrade = "good";
	} else if (finalAverage <= 17) {
	    qualifiedAverageGrade = "verygood";
	} else {
	    qualifiedAverageGrade = "excelent";
	}

	return "diploma.supplement.qualifiedgrade." + qualifiedAverageGrade;
    }

    @Override
    public boolean isPiggyBackedOnRegistry() {
	return hasRegistryDiplomaRequest();
    }

    public String getThesisFinalGrade(final Locale locale) {
	PhdThesisFinalGrade finalGrade = getPhdIndividualProgramProcess().getFinalGrade();

	return finalGrade.getLocalizedName(locale);
    }

    @Override
    public ExecutionYear getConclusionYear() {
	return ExecutionYear.readByDateTime(getPhdIndividualProgramProcess().getConclusionDate());
    }

    @Override
    public EctsGraduationGradeConversionTable getGraduationConversionTable() {
	return getPhdIndividualProgramProcess().getPhdProgram().getDegree()
		.getGraduationConversionTable(getConclusionYear().getAcademicInterval(), getRequestedCycle());
    }

    @Override
    public Integer getNumberOfCurricularYears() {
	PhdProgramInformation information = getPhdInformationForConclusionDate();

	if (information == null) {
	    return null;
	}

	return information.getNumberOfYears();
    }

    private PhdProgramInformation getPhdInformationForConclusionDate() {
	LocalDate conclusionDate = null;

	if (!getPhdIndividualProgramProcess().getStudyPlan().isExempted()) {
	    Registration registration = getPhdIndividualProgramProcess().getRegistration();
	    conclusionDate = registration.getConclusionDateForBolonha().toDateMidnight().toLocalDate();
	} else {
	    PhdConclusionProcess conclusionProcess = getPhdIndividualProgramProcess().getLastConclusionProcess();
	    conclusionDate = conclusionProcess.getConclusionDate();
	}

	PhdProgram phdProgram = getPhdIndividualProgramProcess().getPhdProgram();
	PhdProgramInformation information = phdProgram.getPhdProgramInformationByDate(conclusionDate);
	return information;
    }

    @Override
    public Integer getNumberOfCurricularSemesters() {
	PhdProgramInformation information = getPhdInformationForConclusionDate();

	if (information == null) {
	    return null;
	}

	return information.getNumberOfSemesters();
    }

    @Override
    public Boolean isExemptedFromStudy() {
	return getPhdIndividualProgramProcess().getStudyPlan().isExempted();
    }

    @Override
    public Registration getRegistration() {
	if (isExemptedFromStudy()) {
	    return null;
	}

	return getPhdIndividualProgramProcess().getRegistration();
    }

    @Override
    public boolean hasRegistration() {
	return getRegistration() != null;
    }

    @Override
    public String getProgrammeTypeDescription() {
	return ResourceBundle.getBundle("resources.PhdResources", Locale.getDefault()).getString("label.php.program");
    }

    @Override
    public String getViewStudentProgrammeLink() {
	return "/phdIndividualProgramProcess.do?method=viewProcess&amp;processId="
		+ getPhdIndividualProgramProcess().getExternalId();
    }

    @Override
    public String getReceivedActionLink() {
	return String
		.format("/phdAcademicServiceRequestManagement.do?method=prepareReceiveOnRectorate&amp;phdAcademicServiceRequestId=%s&amp;batchOid=%s",
			getExternalId(), getRectorateSubmissionBatch().getExternalId());
    }
}
