package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ApprovementCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.util.StringUtils;

public class ApprovementCertificate extends AdministrativeOfficeDocument {
    
    protected ApprovementCertificate(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	super.fillReport();
	parameters.put("approvementsInfo", getApprovementsInfo());
    }

    final private String getApprovementsInfo() {
	final StringBuilder result = new StringBuilder();

	Registration registration = getDocumentRequest().getRegistration();
	final ICurriculum curriculum = registration.getCurriculum();
	
	final SortedSet<ICurriculumEntry> entries = new TreeSet<ICurriculumEntry>(ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
	entries.addAll(curriculum.getCurriculumEntries());

	final Map<Unit,String> academicUnitIdentifiers = new HashMap<Unit,String>();
	reportEntries(result, entries, academicUnitIdentifiers);
	
	final SortedSet<Enrolment> remainingEntries = new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
	remainingEntries.addAll(registration.getExtraCurricularEnrolments());
	if (!remainingEntries.isEmpty()) {
	    reportRemainingEnrolments(result, remainingEntries, "Extra-Curriculares");
	}
	    
	remainingEntries.clear();
	remainingEntries.addAll(registration.getPropaedeuticEnrolments());
	if (!remainingEntries.isEmpty()) {
	    reportRemainingEnrolments(result, remainingEntries, "Propedêuticas");
	}
	
    	if (getDocumentRequest().isToShowCredits()) {
    	    result.append(getCreditsDismissalsEctsCreditsInfo(curriculum));
    	}
	
    	result.append(generateEndLine());
	
    	if (!academicUnitIdentifiers.isEmpty()) {
	    result.append("\n").append(getAcademicUnitInfo(academicUnitIdentifiers, ((ApprovementCertificateRequest) getDocumentRequest()).getMobilityProgram()));
	}
	
	return result.toString();
    }

    final private void reportEntries(final StringBuilder result, final SortedSet<ICurriculumEntry> entries, final Map<Unit, String> academicUnitIdentifiers) {
	ExecutionYear lastReportedExecutionYear = entries.first().getExecutionYear(); 
	for (final ICurriculumEntry entry : entries) {
	    final ExecutionYear executionYear = entry.getExecutionYear();
	    if (executionYear != lastReportedExecutionYear) {
		lastReportedExecutionYear = executionYear;
		result.append("\n");
	    }
	    
	    reportEntry(result, entry, academicUnitIdentifiers, executionYear);
	}
    }

    final private void reportRemainingEnrolments(final StringBuilder result, final Collection<Enrolment> enrolments, final String title) {
	result.append(generateEndLine()).append("\n").append(title).append(":\n");
	
	for (final Enrolment enrolment : enrolments) {
	    reportEntry(result, enrolment, null, enrolment.getExecutionYear());
	}
    }

    final private void reportEntry(final StringBuilder result, final ICurriculumEntry entry, final Map<Unit, String> academicUnitIdentifiers, final ExecutionYear executionYear) {
	result.append(
	    StringUtils.multipleLineRightPadWithSuffix(
		    getCurriculumEntryName(academicUnitIdentifiers, entry),
		    LINE_LENGTH, 
		    '-', 
		    getCreditsAndGradeInfo(entry,executionYear))).append("\n");
    }

    final private String getCreditsAndGradeInfo(final ICurriculumEntry entry, final ExecutionYear executionYear) {
	final StringBuilder result = new StringBuilder();
	
	if (getDocumentRequest().isToShowCredits()) {
	    getCreditsInfo(result, entry);
	}
	result.append(entry.getGradeValue());
	result.append(
	    StringUtils.rightPad(
		    "(" 
		    + enumerationBundle.getString(entry.getGradeValue()) 
		    + ")",
		    SUFFIX_LENGTH,
		    ' '));
	result.append(" ").append(resourceBundle.getString("label.in"));
	result.append(" ").append(executionYear.getYear());
	
	return result.toString();
    }

}
