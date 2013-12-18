package net.sourceforge.fenixedu.presentationTier.docs.phd.notification;

import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PhdCandidacyDeclarationDocument extends FenixReport {
    private PhdProgramCandidacyProcess candidacyProcess;

    private Language language;

    public PhdCandidacyDeclarationDocument(PhdProgramCandidacyProcess candidacyProcess, Language language) {
        setCandidacyProcess(candidacyProcess);
        setLanguage(language);

        fillReport();
    }

    @Override
    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public PhdProgramCandidacyProcess getCandidacyProcess() {
        return this.candidacyProcess;
    }

    public void setCandidacyProcess(PhdProgramCandidacyProcess arg) {
        this.candidacyProcess = arg;
    }

    @Override
    protected void fillReport() {
        addParameter("name", getCandidacyProcess().getPerson().getName());
        addParameter("programName",
                getCandidacyProcess().getIndividualProgramProcess().getPhdProgram().getName().getContent(getLanguage()));
        addParameter("candidacyDate", getCandidacyProcess().getCandidacyDate());
        addParameter("currentDate", new LocalDate());
        addParameter("documentIdNumber", getCandidacyProcess().getPerson().getDocumentIdNumber());
        addParameter("candidacyNumber", getCandidacyProcess().getProcessNumber());

        addParameter("administrativeOfficeCoordinator", getCandidacyProcess().getIndividualProgramProcess().getPhdProgram()
                .getAdministrativeOffice().getUnit().getActiveUnitCoordinator().getFirstAndLastName());

        addParameter("institutionName", Bennu.getInstance().getInstitutionUnit().getPartyName().getContent());
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
