package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Locale;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeOfficialPublication;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsGraduationGradeConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsTableIndex;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.IDiplomaSupplementRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.CycleConclusionProcess;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class DiplomaSupplementRequest extends DiplomaSupplementRequest_Base implements IDiplomaSupplementRequest,
        IRectorateSubmissionBatchDocumentEntry {

    public DiplomaSupplementRequest() {
        super();
    }

    public DiplomaSupplementRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);
        checkParameters(bean);
        setLanguage(Language.pt);
    }

    @Override
    protected void checkParameters(DocumentRequestCreateBean bean) {
        if (bean.getHasCycleTypeDependency()) {
            if (bean.getRequestedCycle() == null) {
                throw new DomainException("error.diplomaSupplementRequest.requestedCycleMustBeGiven");
            } else if (!getDegreeType().getCycleTypes().contains(bean.getRequestedCycle())) {
                throw new DomainException(
                        "error.diplomaSupplementRequest.requestedDegreeTypeIsNotAllowedForGivenStudentCurricularPlan");
            }
            super.setRequestedCycle(bean.getRequestedCycle());
        } else {
            if (bean.getRegistration().getDegreeType().hasExactlyOneCycleType()) {
                super.setRequestedCycle(bean.getRegistration().getDegreeType().getCycleType());
            }
        }

        final String fullName = getRegistration().getStudent().getPerson().getName();
        final String familyName = bean.getFamilyNames();
        final String composedName =
                familyName == null || familyName.isEmpty() ? bean.getGivenNames() : bean.getGivenNames() + " " + familyName;

        if (!fullName.equals(composedName)) {
            throw new DomainException("error.diplomaSupplementRequest.splittedNamesDoNotMatch");
        }
        getRegistration().getPerson().setGivenNames(bean.getGivenNames());
        getRegistration().getPerson().setFamilyNames(bean.getFamilyNames());
        RegistryDiplomaRequest registry = getRegistration().getRegistryDiplomaRequest(bean.getRequestedCycle());
        DiplomaRequest diploma = getRegistration().getDiplomaRequest(bean.getRequestedCycle());
        if (registry == null && diploma == null) {
            throw new DomainException(
                    "error.diplomaSupplementRequest.cannotAskForSupplementWithoutEitherRegistryDiplomaOrDiplomaRequest");
        }
        final DiplomaSupplementRequest supplement = getRegistration().getDiplomaSupplementRequest(bean.getRequestedCycle());
        if (supplement != null && supplement != this) {
            throw new DomainException("error.diplomaSupplementRequest.alreadyRequested");
        }
    }

    @Override
    public void setRequestedCycle(CycleType requestedCycle) {
        throw new DomainException("error.diplomaSupplementRequest.cannotModifyRequestedCycle");
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.DIPLOMA_SUPPLEMENT_REQUEST;
    }

    @Override
    final public String getDescription() {
        final DegreeType degreeType = getDegreeType();
        final CycleType requestedCycle = getRequestedCycle();

        return getDescription(getAcademicServiceRequestType(),
                getDocumentRequestType().getQualifiedName() + "." + degreeType.name()
                        + (degreeType.isComposite() ? "." + requestedCycle.name() : ""));
    }

    @Override
    public String getDocumentTemplateKey() {
        String result = getClass().getName() + "." + getDegreeType().getName();
        if (getRequestedCycle() != null) {
            result += "." + getRequestedCycle().name();
        }
        return result;
    }

    public String getGivenNames() {
        return getRegistration().getPerson().getGivenNames();
    }

    public String getFamilyNames() {
        return getRegistration().getPerson().getFamilyNames();
    }

    @Override
    public boolean isPagedDocument() {
        return false;
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
        return false;
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
    public boolean isPayedUponCreation() {
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
    public boolean isToPrint() {
        return false;
    }

    @Override
    public boolean isPiggyBackedOnRegistry() {
        return hasRegistryDiplomaRequest();
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.DIPLOMA_SUPPLEMENT_REQUEST;
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);
        if (academicServiceRequestBean.isToProcess()) {
            if (!getRegistration().isRegistrationConclusionProcessed(getRequestedCycle())) {
                throw new DomainException("error.diplomaSupplement.registration.not.submited.to.conclusion.process");
            }
            if (getRegistryCode() == null) {
                RegistryDiplomaRequest registryRequest = getRegistration().getRegistryDiplomaRequest(getRequestedCycle());
                DiplomaRequest diploma = getRegistration().getDiplomaRequest(getRequestedCycle());
                if (registryRequest != null) {
                    registryRequest.getRegistryCode().addDocumentRequest(this);
                } else if (diploma != null && diploma.hasRegistryCode()) {
                    diploma.getRegistryCode().addDocumentRequest(this);
                } else {
                    getRootDomainObject().getInstitutionUnit().getRegistryCodeGenerator().createRegistryFor(this);
                    getAdministrativeOffice().getCurrentRectorateSubmissionBatch().addDocumentRequest(this);
                }
                getAdministrativeOffice().getCurrentRectorateSubmissionBatch().addDocumentRequest(this);
            }
            if (getLastGeneratedDocument() == null) {
                generateDocument();
            }
        }
    }

    @Override
    public String getGraduateTitle(final Locale locale) {
        return getRegistration().getGraduateTitle(getRequestedCycle(), locale);
    }

    @Override
    public Integer getRegistrationNumber() {
        return getRegistration().getNumber();
    }

    @Override
    public String getPrevailingScientificArea(final Locale locale) {
        Degree degree = getDegree();
        ExecutionYear conclusion =
                getRegistration().getLastStudentCurricularPlan().getCycle(getRequestedCycle()).getConclusionProcess()
                        .getConclusionYear();
        return degree.getFilteredName(conclusion, locale);
    }

    @Override
    public long getEctsCredits() {
        ExecutionYear conclusion = getConclusionYear();

        return Math.round(getRegistration().getLastStudentCurricularPlan().getCycle(getRequestedCycle())
                .getDefaultEcts(conclusion));
    }

    @Override
    public DegreeOfficialPublication getDegreeOfficialPublication() {
        CycleConclusionProcess conclusionProcess =
                getRegistration().getLastStudentCurricularPlan().getCycle(getRequestedCycle()).getConclusionProcess();

        DegreeOfficialPublication dr =
                getRegistration().getDegree().getOfficialPublication(
                        conclusionProcess.getConclusionDate().toDateTimeAtStartOfDay());

        return dr;
    }

    @Override
    public Integer getFinalAverage() {
        return getRegistration().getFinalAverage(getRequestedCycle());
    }

    @Override
    public String getFinalAverageQualified(final Locale locale) {
        Integer finalAverage = getRegistration().getFinalAverage(getRequestedCycle());
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
    public ExecutionYear getConclusionYear() {
        return getRegistration().getLastStudentCurricularPlan().getCycle(getRequestedCycle()).getConclusionProcess()
                .getConclusionYear();
    }

    @Override
    public EctsGraduationGradeConversionTable getGraduationConversionTable() {
        return EctsTableIndex.getGraduationGradeConversionTable(getRegistration().getDegree(), getRequestedCycle(),
                getConclusionYear().getAcademicInterval(), getProcessingDate() != null ? getProcessingDate() : new DateTime());
    }

    @Override
    public Integer getNumberOfCurricularYears() {
        Registration registration = getRegistration();
        DegreeType degreeType = registration.getDegree().getDegreeType();
        return degreeType.getYears(getRequestedCycle());
    }

    @Override
    public Integer getNumberOfCurricularSemesters() {
        Registration registration = getRegistration();
        DegreeType degreeType = registration.getDegree().getDegreeType();
        return degreeType.getSemesters(getRequestedCycle());
    }

    @Override
    public Boolean isExemptedFromStudy() {
        return false;
    }

    @Override
    public String getProgrammeTypeDescription() {
        return getDegreeType().getLocalizedName();
    }

    @Override
    public String getViewStudentProgrammeLink() {
        return "/student.do?method=visualizeRegistration&amp;registrationID=" + getRegistration().getExternalId();
    }

    @Override
    public String getReceivedActionLink() {
        return "/academicServiceRequestsManagement.do?method=prepareReceiveAcademicServiceRequest&amp;academicServiceRequestId="
                + getExternalId();
    }

    @Override
    public boolean isProgrammeLinkVisible() {
        return getRegistration().isAllowedToManageRegistration();
    }
}