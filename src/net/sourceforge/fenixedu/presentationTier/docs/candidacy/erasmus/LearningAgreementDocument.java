package net.sourceforge.fenixedu.presentationTier.docs.candidacy.erasmus;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcess;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;
import net.sourceforge.fenixedu.util.StringUtils;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class LearningAgreementDocument extends FenixReport {

    ErasmusIndividualCandidacyProcess process;

    static final protected char END_CHAR = ' ';
    static final protected int LINE_LENGTH = 70;
    static final protected String LINE_BREAK = "\n";

    public LearningAgreementDocument(ErasmusIndividualCandidacyProcess process) {
	this.process = process;
	fillReport();
    }

    public LearningAgreementDocument(ErasmusIndividualCandidacyProcess process, Locale locale) {
	super(locale);
	this.process = process;
	fillReport();
    }

    @Override
    protected void fillReport() {
	addParameter("academicYear", process.getCandidacyExecutionInterval().getName());
	addParameter("studentName", process.getPersonalDetails().getName());
	addParameter("sendingInstitution", process.getCandidacy().getErasmusStudentData().getSelectedVacancy()
		.getUniversityUnit().getNameI18n().getContent());

	addParameter("desiredEnrollments", getChosenSubjectsInformation());
    }

    private String getChosenSubjectsInformation() {
	StringBuilder result = new StringBuilder();

	for (CurricularCourse course : process.getCandidacy().getCurricularCourses()) {
	    result.append(
		    StringUtils.multipleLineRightPadWithSuffix(course.getNameI18N().getContent(Language.en), LINE_LENGTH,
			    END_CHAR, course.getEctsCredits().toString())).append(LINE_BREAK);
	}
	
	return result.toString();
    }

    @Override
    public String getReportFileName() {
	return "learning_agreement_" + process.getCandidacy().getPersonalDetails().getDocumentIdNumber();
    }

}
