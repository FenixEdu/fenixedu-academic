package net.sourceforge.fenixedu.presentationTier.docs.phd.notification;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PhdCandidacyDeclarationDocument extends FenixReport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DomainReference<PhdProgramCandidacyProcess> candidacyProcess;

    private Language language;

    public PhdCandidacyDeclarationDocument(PhdProgramCandidacyProcess candidacyProcess, Language language) {
	setCandidacyProcess(candidacyProcess);
	setLanguage(language);

	fillReport();
    }

    public Language getLanguage() {
	return language;
    }

    public void setLanguage(Language language) {
	this.language = language;
    }

    public PhdProgramCandidacyProcess getCandidacyProcess() {
	return (this.candidacyProcess != null) ? this.candidacyProcess.getObject() : null;
    }

    public void setCandidacyProcess(PhdProgramCandidacyProcess arg) {
	this.candidacyProcess = (arg != null) ? new DomainReference<PhdProgramCandidacyProcess>(arg) : null;
    }

    @Override
    protected void fillReport() {
	addParameter("name", getCandidacyProcess().getPerson().getName());
	addParameter("programName", getCandidacyProcess().getIndividualProgramProcess().getPhdProgram().getName().getContent(
		getLanguage()));
	addParameter("candidacyDate", getCandidacyProcess().getCandidacyDate());
	addParameter("currentDate", new LocalDate());
	addParameter("documentIdNumber", getCandidacyProcess().getPerson().getDocumentIdNumber());
	addParameter("candidacyNumber", getCandidacyProcess().getProcessNumber());

	addParameter("administrativeOfficeCoordinator", AdministrativeOffice.readByAdministrativeOfficeType(
		AdministrativeOfficeType.MASTER_DEGREE).getUnit().getActiveUnitCoordinator().getFirstAndLastName());

	addParameter("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getPartyName().getContent());
	addParameter("universityName", UniversityUnit.getInstitutionsUniversityUnit().getPartyName().getContent());
    }

    @Override
    public String getReportFileName() {
	return "CandidacyDeclaration-" + new DateTime().toString(YYYYMMDDHHMMSS);
    }

    @Override
    public String getReportTemplateKey() {
	return getClass().getName() + "." + getLanguage().name();
    }

}
