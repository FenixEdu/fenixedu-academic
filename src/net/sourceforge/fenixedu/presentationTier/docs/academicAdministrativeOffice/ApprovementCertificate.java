package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ApprovementCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.util.StringUtils;

public class ApprovementCertificate extends AdministrativeOfficeDocument {

    protected ApprovementCertificate(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	super.fillReport();
	addParameter("approvementsInfo", getApprovementsInfo());
    }

    final private String getApprovementsInfo() {
	final StringBuilder result = new StringBuilder();

	final ApprovementCertificateRequest approvementCertificateRequest = (ApprovementCertificateRequest) getDocumentRequest();

	final SortedSet<ICurriculumEntry> entries = new TreeSet<ICurriculumEntry>(
		ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
	entries.addAll(approvementCertificateRequest.getEntriesToReport());

	final Map<Unit, String> academicUnitIdentifiers = new HashMap<Unit, String>();
	reportEntries(result, entries, academicUnitIdentifiers);

	entries.clear();
	entries.addAll(approvementCertificateRequest.getExtraCurricularEntriesToReport());
	if (!entries.isEmpty()) {
	    reportRemainingEntries(result, entries, academicUnitIdentifiers, "Extra-Curriculares");
	}

	entries.clear();
	entries.addAll(approvementCertificateRequest.getPropaedeuticEntriesToReport());
	if (!entries.isEmpty()) {
	    reportRemainingEntries(result, entries, academicUnitIdentifiers, "Propedêuticas");
	}

	if (getDocumentRequest().isToShowCredits()) {
	    result.append(getRemainingCreditsInfo(approvementCertificateRequest.getCurriculum()));
	}

	result.append(generateEndLine());

	if (!academicUnitIdentifiers.isEmpty()) {
	    result.append("\n").append(
		    getAcademicUnitInfo(academicUnitIdentifiers, ((ApprovementCertificateRequest) getDocumentRequest())
			    .getMobilityProgram()));
	}

	return result.toString();
    }

    final private void reportEntries(final StringBuilder result, final SortedSet<ICurriculumEntry> entries,
	    final Map<Unit, String> academicUnitIdentifiers) {
	ExecutionYear lastReportedExecutionYear = null;
	for (final ICurriculumEntry entry : entries) {
	    final ExecutionYear executionYear = entry.getExecutionYear();
	    if (lastReportedExecutionYear == null) {
		lastReportedExecutionYear = executionYear;
	    }

	    if (executionYear != lastReportedExecutionYear) {
		lastReportedExecutionYear = executionYear;
		result.append("\n");
	    }

	    reportEntry(result, entry, academicUnitIdentifiers, executionYear);
	}
    }

    final private void reportRemainingEntries(final StringBuilder result, final Collection<ICurriculumEntry> entries,
	    final Map<Unit, String> academicUnitIdentifiers, final String title) {
	result.append(generateEndLine()).append("\n").append(title).append(":\n");

	for (final ICurriculumEntry entry : entries) {
	    reportEntry(result, entry, academicUnitIdentifiers, entry.getExecutionYear());
	}
    }

    final private void reportEntry(final StringBuilder result, final ICurriculumEntry entry,
	    final Map<Unit, String> academicUnitIdentifiers, final ExecutionYear executionYear) {
	result.append(
		StringUtils.multipleLineRightPadWithSuffix(getCurriculumEntryName(academicUnitIdentifiers, entry), LINE_LENGTH,
			'-', getCreditsAndGradeInfo(entry, executionYear))).append("\n");
    }

    final private String getCreditsAndGradeInfo(final ICurriculumEntry entry, final ExecutionYear executionYear) {
	final StringBuilder result = new StringBuilder();

	if (getDocumentRequest().isToShowCredits()) {
	    getCreditsInfo(result, entry);
	}
	result.append(entry.getGradeValue());
	result.append(StringUtils.rightPad("(" + enumerationBundle.getString(entry.getGradeValue()) + ")", SUFFIX_LENGTH, ' '));

	result.append(" ");
	final String in = resourceBundle.getString("label.in");
	if (executionYear == null) {
	    result.append(StringUtils.rightPad("", in.length(), ' '));
	    result.append(" ").append(StringUtils.rightPad("", 9, ' '));
	} else {
	    result.append(in);
	    result.append(" ").append(executionYear.getYear());
	}

	return result.toString();
    }

}
