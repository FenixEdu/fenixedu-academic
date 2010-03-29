package net.sourceforge.fenixedu.presentationTier.docs.candidacy.erasmus;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcess;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;

public class LearningAgreementDocument extends FenixReport {

    ErasmusIndividualCandidacyProcess process;

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
	addParameter("academicYear", "2010/2011");
    }

    @Override
    public String getReportFileName() {
	return "learning_agreement_" + process.getCandidacy().getPersonalDetails().getDocumentIdNumber();
    }

}
