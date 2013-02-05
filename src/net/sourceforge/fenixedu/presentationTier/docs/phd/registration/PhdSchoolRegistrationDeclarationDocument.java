package net.sourceforge.fenixedu.presentationTier.docs.phd.registration;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PhdSchoolRegistrationDeclarationDocument extends FenixReport {

    static private final long serialVersionUID = 1L;

    private final PhdIndividualProgramProcess process;

    public PhdSchoolRegistrationDeclarationDocument(final PhdIndividualProgramProcess process) {
        this.process = process;

        setResourceBundle(ResourceBundle.getBundle("resources.PhdResources", Language.getLocale()));
        fillReport();
    }

    @Override
    protected void fillReport() {
        final Unit unit = process.getPhdProgram().getAdministrativeOffice().getUnit();

        addParameter("administrativeOfficeName", unit.getName());
        addParameter("administrativeOfficeCoordinator", unit.getActiveUnitCoordinator().getName());

        addParameter("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getPartyName().getContent());
        addParameter("universityName", UniversityUnit.getInstitutionsUniversityUnit().getPartyName().getContent());

        addParameter("studentNumber", getStudentNumber());
        addParameter("studentName", getPerson().getName());
        addParameter("documentId", getPerson().getDocumentIdNumber());
        addParameter("parishOfBirth", getPerson().getParishOfBirth());
        addParameter("nationality", getPerson().getCountry().getCountryNationality().getContent());

        addParameter("registrationState", getRegistrationStateLabel());
        addParameter("executionYear", process.getExecutionYear().getName());
        addParameter("phdProgramName", process.getPhdProgram().getName().getContent());

        addParameter("documentDate", new LocalDate().toString(DD_MMMM_YYYY, Language.getLocale()));
    }

    private String getStudentNumber() {
        return hasRegistration() ? getRegistration().getNumber().toString() : getStudent().getNumber().toString();
    }

    private String getRegistrationStateLabel() {
        final Gender gender = getPerson().getGender();
        return gender == Gender.MALE ? getMessage("label.phd.schoolRegistrationDeclaration.registered.male") : getMessage("label.phd.schoolRegistrationDeclaration.registered.female");
    }

    private Person getPerson() {
        return process.getPerson();
    }

    private Student getStudent() {
        return getPerson().getStudent();
    }

    private Registration getRegistration() {
        return process.getRegistration();
    }

    private boolean hasRegistration() {
        return process.hasRegistration();
    }

    private String getMessage(final String key) {
        return getResourceBundle().getString(key);
    }

    @Override
    public String getReportFileName() {
        return "SchoolRegistrationDeclaration-" + new DateTime().toString(YYYYMMDDHHMMSS);
    }

    @Override
    public String getReportTemplateKey() {
        return super.getReportTemplateKey() + ".pt";
    }
}
