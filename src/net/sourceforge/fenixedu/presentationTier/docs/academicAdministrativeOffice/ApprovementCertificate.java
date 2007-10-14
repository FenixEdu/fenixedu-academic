package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ApprovementCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;
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

	final Curriculum curriculum = getDocumentRequest().getRegistration().getCurriculum();
	
	final SortedSet<ICurriculumEntry> entries = new TreeSet<ICurriculumEntry>(ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME);
	entries.addAll(curriculum.getEntries());
	entries.addAll(curriculum.getDismissalRelatedEntries());

	final List<Enrolment> extraCurricularEnrolments = new ArrayList<Enrolment>();
	final List<Enrolment> propaedeuticEnrolments = new ArrayList<Enrolment>();
	final Map<Unit,String> academicUnitIdentifiers = new HashMap<Unit,String>();

	reportEntries(result, entries, extraCurricularEnrolments, propaedeuticEnrolments, academicUnitIdentifiers);
	
	if (!extraCurricularEnrolments.isEmpty()) {
	    reportRemainingEnrolments(result, extraCurricularEnrolments, "Extra-Curriculares");
	}
	    
	if (!propaedeuticEnrolments.isEmpty()) {
	    reportRemainingEnrolments(result, propaedeuticEnrolments, "Propedêuticas");
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

    final private void reportEntries(final StringBuilder result, final SortedSet<ICurriculumEntry> entries, final List<Enrolment> extraCurricularEnrolments, final List<Enrolment> propaedeuticEnrolments, final Map<Unit, String> academicUnitIdentifiers) {
	ExecutionYear lastReportedExecutionYear = entries.first().getExecutionYear(); 
	for (final ICurriculumEntry entry : entries) {
	    if (entry instanceof Enrolment) {
		final Enrolment enrolment = (Enrolment) entry;

		if (enrolment.isExtraCurricular()) {
		    extraCurricularEnrolments.add(enrolment);
		    continue;
		} else if (enrolment.isPropaedeutic()) {
		    propaedeuticEnrolments.add(enrolment);
		    continue;
		}
	    }
	
	    final ExecutionYear executionYear = entry.getExecutionYear();
	    if (executionYear != lastReportedExecutionYear) {
		lastReportedExecutionYear = executionYear;
		result.append("\n");
	    }
	    
	    reportEntry(result, entry, academicUnitIdentifiers, executionYear);
	}
    }

    final private void reportRemainingEnrolments(final StringBuilder result, final List<Enrolment> enrolments, final String title) {
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
