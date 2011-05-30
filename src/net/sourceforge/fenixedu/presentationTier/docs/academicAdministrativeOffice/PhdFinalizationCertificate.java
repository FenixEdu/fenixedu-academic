package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestEvent;
import net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests.phd.PhdFinalizationCertificateRequestPR;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.certificates.PhdFinalizationCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.StringUtils;

public class PhdFinalizationCertificate extends AdministrativeOfficeDocument {

    PhdFinalizationCertificate(IDocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected PhdFinalizationCertificateRequest getDocumentRequest() {
	return (PhdFinalizationCertificateRequest) super.getDocumentRequest();
    }

    @Override
    protected String getDegreeDescription() {
	PhdIndividualProgramProcess phdIndividualProgramProcess = getDocumentRequest().getPhdIndividualProgramProcess();
	return phdIndividualProgramProcess.getPhdProgram().getName().getContent(getLanguage());
    }

    @Override
    protected void addPriceFields() {
	AcademicServiceRequestEvent event = getDocumentRequest().getEvent();
	PhdFinalizationCertificateRequestPR postingRule = (PhdFinalizationCertificateRequestPR) event
		.getPostingRule();
	addParameter("originalAmount", postingRule.getFixedAmount().toString());
	addParameter("urgentAmount", getDocumentRequest().isUrgentRequest() ? postingRule.getFixedAmount().toString() : Money.ZERO.toString());
	addParameter("totalAmount", event.getOriginalAmountToPay().toString());
    }

    @Override
    protected void fillReport() {
	super.fillReport();

	addPersonalInfo();

	addProgrammeInfo();
    }

    private void addProgrammeInfo() {
	PhdIndividualProgramProcess phdIndividualProgramProcess = getDocumentRequest().getPhdIndividualProgramProcess();
	addParameter("conclusionDate", phdIndividualProgramProcess.getConclusionDate().toString("dd/MM/yyyy"));
	String thesisFinalGrade = phdIndividualProgramProcess.getFinalGrade().getLocalizedName(getLocale());
	addParameter("thesisFinalGrade", thesisFinalGrade);
	addParameter("thesisTitle", phdIndividualProgramProcess.getThesisTitle());
	addParameter("studentNumber", phdIndividualProgramProcess.getStudent().getNumber());

	StringBuilder builder = new StringBuilder();
	builder.append(
		getResourceBundle().getString("message.phd.finalization.certificate.made.thesis.presentation.on.doctoral.grade"))
		.append(":").append(SINGLE_SPACE);

	builder.append(phdIndividualProgramProcess.getPhdProgram().getName().getContent(getLanguage()).toUpperCase());
	addParameter("phdProgram", StringUtils.multipleLineRightPad(builder.toString(), LINE_LENGTH, END_CHAR));
	addParameter("finalizationInfo", buildFinalizationInfo());
    }

    private String buildFinalizationInfo() {
	PhdIndividualProgramProcess phdIndividualProgramProcess = getDocumentRequest().getPhdIndividualProgramProcess();
	String thesisFinalGrade = phdIndividualProgramProcess.getFinalGrade().getLocalizedName(getLocale());

	return String.format(getResourceBundle().getString("message.phd.finalization.info.thesis.grade.approved.by.jury"),
		thesisFinalGrade);
    }

    private void addPersonalInfo() {
	Person person = getDocumentRequest().getPerson();

	String fatherPrefixKey = "label.phd.finalization.certificate.father.prefix";
	String motherPrefixKey = "label.phd.finalization.certificate.mother.prefix";

	if (Gender.MALE.equals(person.getGender())) {
	    fatherPrefixKey += ".for.male";
	    motherPrefixKey += ".for.male";
	} else {
	    fatherPrefixKey += ".for.female";
	    motherPrefixKey += ".for.female";
	}

	StringBuilder builder = new StringBuilder();
	builder.append(getResourceBundle().getString(fatherPrefixKey)).append(SINGLE_SPACE);
	builder.append(person.getNameOfFather().toUpperCase());

	addParameter("fatherName", StringUtils.multipleLineRightPad(builder.toString(), LINE_LENGTH, END_CHAR));

	builder = new StringBuilder();
	builder.append(getResourceBundle().getString(motherPrefixKey)).append(SINGLE_SPACE);
	builder.append(person.getNameOfMother().toUpperCase());
	addParameter("motherName", StringUtils.multipleLineRightPad(builder.toString(), LINE_LENGTH, END_CHAR));

    }

}
