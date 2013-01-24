package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.candidacy.registrations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class RegisteredDegreeCandidaciesWithApplyForResidence {

    private final RegisteredDegreeCandidaciesSelectionBean bean;

    public RegisteredDegreeCandidaciesWithApplyForResidence(
	    RegisteredDegreeCandidaciesSelectionBean registeredDegreeCandidaciesSelectionBean) {
	this.bean = registeredDegreeCandidaciesSelectionBean;
    }

    public List<StudentCandidacy> search(Set<Degree> allowedDegrees) {
	final List<StudentCandidacy> degreeCandidacies = this.bean.search(allowedDegrees);
	final List<StudentCandidacy> result = new ArrayList<StudentCandidacy>();

	for (StudentCandidacy studentCandidacy : degreeCandidacies) {
	    if (studentCandidacy.getApplyForResidence() != null && studentCandidacy.getApplyForResidence()) {
		result.add(studentCandidacy);
	    }
	}

	Collections.sort(result, RegisteredDegreeCandidaciesSelectionBean.DEGREE_CANDIDACIES_COMPARATOR);
	return result;
    }

    public Spreadsheet export(Set<Degree> allowedDegrees) {
	final Spreadsheet spreadsheet = new Spreadsheet("Candidatura a Residencia");
	addHeaders(spreadsheet);

	for (final StudentCandidacy candidacy : search(allowedDegrees)) {
	    addRow(spreadsheet, candidacy);
	}

	return spreadsheet;
    }

    public String getFilename() {
	return new LocalDate().toString("dd-MM-yyyy") + ".Candidatos.Residencia.xls";
    }

    private void addHeaders(Spreadsheet spreadsheet) {
	spreadsheet.setHeader("Data de Matricula");
	spreadsheet.setHeader("Curso");
	spreadsheet.setHeader("Nº de Aluno");
	spreadsheet.setHeader("Nome");
	spreadsheet.setHeader("Nota de Seriacao");
	spreadsheet.setHeader("Localidade");
	spreadsheet.setHeader("Concelho");
	spreadsheet.setHeader("Distrito");
	spreadsheet.setHeader("Email IST");
	spreadsheet.setHeader("Email pessoal");
	spreadsheet.setHeader("Telefone");
	spreadsheet.setHeader("Telemovel");
	spreadsheet.setHeader("Nota sobre Candidatura");
    }

    private void addRow(Spreadsheet spreadsheet, StudentCandidacy candidacy) {

	final Person person = candidacy.getPerson();
	final Row row = spreadsheet.addRow();

	row.setCell(candidacy.getActiveCandidacySituation().getSituationDate().toString("dd/MM/yyyy HH:mm"));
	row.setCell(candidacy.getExecutionDegree().getDegree().getNameFor(ExecutionYear.readCurrentExecutionYear()).getContent());
	row.setCell(candidacy.getRegistration().getStudent().getNumber());
	row.setCell(person.getName());
	row.setCell(getEntryGrade(candidacy));
	row.setCell(person.getArea());
	row.setCell(person.getDistrictSubdivisionOfResidence());
	row.setCell(person.getDistrictOfResidence());
	row.setCell(person.getInstitutionalEmailAddressValue());
	row.setCell(getPersonalEmailAddress(person));
	row.setCell(getPhone(person));
	row.setCell(getMobilePhone(person));
	row.setCell(candidacy.getNotesAboutResidenceAppliance().replaceAll("\n", " ").replaceAll("\r", " "));
    }

    private BigDecimal getEntryGrade(final StudentCandidacy candidacy) {
	return new BigDecimal(candidacy.getEntryGrade().doubleValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    private String getPersonalEmailAddress(final Person person) {
	for (final EmailAddress email : person.getEmailAddresses()) {
	    if (email.isPersonalType() && email.hasValue()) {
		return email.getValue();
	    }
	}
	return "";
    }

    private String getPhone(final Person person) {
	return person.hasDefaultPhone() && person.getDefaultPhone().hasNumber() ? person.getDefaultPhone().getNumber() : "";
    }

    private String getMobilePhone(final Person person) {
	return person.hasDefaultMobilePhone() && person.getDefaultMobilePhone().hasNumber() ? person.getDefaultMobilePhone()
		.getNumber() : "";
    }
}
