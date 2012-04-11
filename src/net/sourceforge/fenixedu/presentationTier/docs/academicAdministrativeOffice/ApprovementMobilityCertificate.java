package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.documents.ApprovementMobilityCertificateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests.CertificateRequestPR;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ApprovementMobilityCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.DateTime;

public class ApprovementMobilityCertificate extends AdministrativeOfficeDocument {

    private static final long serialVersionUID = 1L;

    protected ApprovementMobilityCertificate(final IDocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	super.fillReport();
	// addParameter("approvementsInfo", getApprovementsInfo());
	addParameter("mobilityProgram", getMobilityProgramDescription());
	addDataSourceElements(createApprovementsBean());
	addParameter("printPriceFieldsCert", !isMobility());
    }

    @Override
    protected DocumentRequest getDocumentRequest() {
	// TODO Auto-generated method stub
	return (DocumentRequest) super.getDocumentRequest();
    }

    @Override
    protected String getDegreeDescription() {
	return getDocumentRequest().getRegistration().getDegreeDescription(getDocumentRequest().getExecutionYear(), null,
		getLocale());
    }

    /* ###################### */

    private boolean isMobility() {
	RegistrationAgreement agreement = getDocumentRequest().getRegistration().getRegistrationAgreement();

	if (RegistrationAgreement.MOBILITY_AGREEMENTS.contains(agreement)) {
	    return true;
	}
	return false;
    }

    private String getMobilityProgramDescription() {

	if (isMobility()) {
	    return getDocumentRequest().getRegistration().getRegistrationAgreement().getDescription();
	}

	return "";
    }

    final private void mapCycles(final SortedSet<ICurriculumEntry> entries) {
	final Collection<CycleCurriculumGroup> cycles = new TreeSet<CycleCurriculumGroup>(
		CycleCurriculumGroup.COMPARATOR_BY_CYCLE_TYPE_AND_ID);
	cycles.addAll(getDocumentRequest().getRegistration().getLastStudentCurricularPlan().getInternalCycleCurriculumGrops());

	for (final CycleCurriculumGroup cycle : cycles) {
	    if (!cycle.isConclusionProcessed() || isDEARegistration()) {
		final ApprovementMobilityCertificateRequest request = ((ApprovementMobilityCertificateRequest) getDocumentRequest());
		final Curriculum curriculum = cycle.getCurriculum(request.getFilteringDate());
		ApprovementMobilityCertificateRequest.filterEntries(entries, request, curriculum);
	    }
	}
    }

    final private SortedSet<ICurriculumEntry> mapEntries() {
	final ApprovementMobilityCertificateRequest request = (ApprovementMobilityCertificateRequest) getDocumentRequest();

	final SortedSet<ICurriculumEntry> entries = new TreeSet<ICurriculumEntry>(
		ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);

	final Registration registration = getDocumentRequest().getRegistration();
	if (registration.isBolonha()) {
	    mapCycles(entries);
	} else {
	    final ICurriculum curriculum = registration.getCurriculum(request.getFilteringDate());
	    ApprovementMobilityCertificateRequest.filterEntries(entries, request, curriculum);
	}

	entries.addAll(request.getExtraCurricularEntriesToReport());
	entries.addAll(request.getPropaedeuticEntriesToReport());
	return entries;
    }

    final private List<ApprovementMobilityCertificateBean> createApprovementsBean() {
	SortedSet<ICurriculumEntry> entries = mapEntries();
	List<ApprovementMobilityCertificateBean> beans = new ArrayList<ApprovementMobilityCertificateBean>();
	final Map<Unit, String> ids = new HashMap<Unit, String>();
	for (final ICurriculumEntry entry : entries) {
	    final ExecutionYear executionYear = entry.getExecutionYear();
	    beans.add(new ApprovementMobilityCertificateBean(getCurriculumEntryName(ids, entry), entry
		    .getEctsCreditsForCurriculum().toString(), entry.getGradeValue(), getEctsGrade(entry), executionYear
		    .getYear()));
	}
	return beans;
    }

    /* ###################### */

    // TODO: remove this after DEA diplomas and certificates
    private boolean isDEARegistration() {
	return getDocumentRequest().getRegistration().getDegreeType() == DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA;
    }

    private String getEctsGradeDescription() {
	return getResourceBundle().getString("label.ects.grade").concat(":");
    }

    private DateTime computeProcessingDateToLockECTSTableUse() {
	DateTime date = documentRequestDomainReference.getProcessingDate();
	return date != null ? date : new DateTime();
    }

    private String getEctsGrade(final ICurriculumEntry entry) {

	if (entry instanceof IEnrolment) {
	    IEnrolment enrolment = (IEnrolment) entry;
	    DateTime processingDate = computeProcessingDateToLockECTSTableUse();
	    Grade grade = enrolment.getEctsGrade(getDocumentRequest().getRegistration().getLastStudentCurricularPlan(),
		    processingDate);

	    return grade.getValue();
	} else {
	    return null;
	}
    }

    @Override
    protected String getCreditsAndGradeInfo(final ICurriculumEntry entry, final ExecutionYear executionYear) {
	final StringBuilder result = new StringBuilder();

	if (getDocumentRequest().isToShowCredits()) {
	    getCreditsInfo(result, entry);
	}
	result.append(entry.getGradeValue());
	result.append(StringUtils.rightPad("(" + getEnumerationBundle().getString(entry.getGradeValue()) + ")", SUFFIX_LENGTH,
		' '));

	result.append(", ");

	result.append(getEctsGradeDescription());
	result.append(SINGLE_SPACE);
	result.append(getEctsGrade(entry));
	result.append(SINGLE_SPACE);
	result.append(", ");

	final String in = getResourceBundle().getString("label.in");
	if (executionYear == null) {
	    result.append(StringUtils.rightPad(EMPTY_STR, in.length(), ' '));
	    result.append(SINGLE_SPACE).append(StringUtils.rightPad(EMPTY_STR, 9, ' '));
	} else {
	    result.append(in);
	    result.append(SINGLE_SPACE).append(executionYear.getYear());
	}

	return result.toString();
    }

    @Override
    protected void addPriceFields() {
	final CertificateRequest certificateRequest = (CertificateRequest) getDocumentRequest();
	final CertificateRequestPR certificateRequestPR = (CertificateRequestPR) getPostingRule();

	final Money amountPerPage = certificateRequestPR.getAmountPerPage();
	final Money baseAmountPlusAmountForUnits = certificateRequestPR.getBaseAmount().add(
		certificateRequestPR.getAmountForUnits(certificateRequest.getNumberOfUnits()));
	final Money urgencyAmount = certificateRequest.getUrgentRequest() ? certificateRequestPR.getBaseAmount() : Money.ZERO;

	addParameter("amountPerPage", amountPerPage);
	addParameter("baseAmountPlusAmountForUnits", baseAmountPlusAmountForUnits);
	addParameter("urgencyAmount", urgencyAmount);
	addParameter("printPriceFields", printPriceParameters(certificateRequest));
    }

}
