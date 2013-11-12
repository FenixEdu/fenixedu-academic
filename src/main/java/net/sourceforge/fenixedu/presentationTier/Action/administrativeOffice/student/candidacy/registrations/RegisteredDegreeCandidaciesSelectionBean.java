package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.candidacy.registrations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class RegisteredDegreeCandidaciesSelectionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    final static Comparator<StudentCandidacy> DEGREE_CANDIDACIES_COMPARATOR = new Comparator<StudentCandidacy>() {
        @Override
        public int compare(StudentCandidacy o1, StudentCandidacy o2) {
            int result = o1.getEntryPhase().compareTo(o2.getEntryPhase());
            if (result == 0) {
                result =
                        o1.getActiveCandidacySituation().getSituationDate()
                                .compareTo(o2.getActiveCandidacySituation().getSituationDate());
            }
            if (result == 0) {
                final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
                result =
                        o1.getExecutionDegree().getDegree().getNameFor(executionYear)
                                .compareTo(o2.getExecutionDegree().getDegree().getNameFor(executionYear));
            }
            if (result == 0) {
                result = o1.getRegistration().getNumber().compareTo(o2.getRegistration().getNumber());
            }
            if (result == 0) {
                result = o1.getPerson().getName().compareTo(o2.getPerson().getName());
            }
            if (result == 0) {
                result = o1.getPerson().getDocumentIdNumber().compareTo(o2.getPerson().getDocumentIdNumber());
            }
            return (result != 0) ? result : o1.getExternalId().compareTo(o2.getExternalId());
        }
    };

    private Campus campus;
    private ExecutionYear executionYear;
    private EntryPhase entryPhase;
    private DateTime beginDate;
    private DateTime endDate;

    public RegisteredDegreeCandidaciesSelectionBean() {
        this.executionYear = ExecutionYear.readCurrentExecutionYear();
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public EntryPhase getEntryPhase() {
        return entryPhase;
    }

    public void setEntryPhase(EntryPhase entryPhase) {
        this.entryPhase = entryPhase;
    }

    public DateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(DateTime beginDate) {
        this.beginDate = beginDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public List<StudentCandidacy> search(final Set<Degree> allowedPrograms) {
        final List<StudentCandidacy> degreeCandidacies = new ArrayList<StudentCandidacy>();
        for (final ExecutionDegree executionDegree : getExecutionYear().getExecutionDegreesSet()) {
            if (executionDegree.getCampus() == getCampus()) {
                for (final StudentCandidacy candidacy : executionDegree
                        .getFirstCycleCandidaciesBy(CandidacySituationType.REGISTERED)) {
                    DateTime situationDate = candidacy.getActiveCandidacySituation().getSituationDate();

                    if (getBeginDate() != null && situationDate.isBefore(getBeginDate())) {
                        continue;
                    }

                    if (getEndDate() != null && situationDate.isAfter(getEndDate())) {
                        continue;
                    }

                    if (candidacy.hasEntryPhase() && candidacy.getEntryPhase().equals(getEntryPhase())) {
                        if (candidacy.getRegistration() != null && candidacy.getRegistration().isCanceled()) {
                            continue;
                        }
                        if (allowedPrograms.contains(candidacy.getExecutionDegree().getDegree())) {
                            degreeCandidacies.add(candidacy);
                        }
                    }
                }
            }
        }

        Collections.sort(degreeCandidacies, DEGREE_CANDIDACIES_COMPARATOR);
        return degreeCandidacies;
    }

    public Spreadsheet export(final Set<Degree> allowedPrograms) {
        final Spreadsheet spreadsheet = new Spreadsheet(campus.getName());
        addHeaders(spreadsheet);

        List<StudentCandidacy> result = search(allowedPrograms);
        Collections.sort(result, DEGREE_CANDIDACIES_COMPARATOR);

        for (final StudentCandidacy candidacy : result) {
            addRow(spreadsheet, candidacy);
        }

        return spreadsheet;
    }

    public String getFilename() {
        return new LocalDate().toString("ddMMyyyy") + "-Candidatos-" + campus.getName() + "-Fase"
                + this.entryPhase.getPhaseNumber() + ".xls";
    }

    private void addHeaders(Spreadsheet spreadsheet) {
        spreadsheet.setHeader("Data de Matricula");
        spreadsheet.setHeader("Tipo Ingresso");
        spreadsheet.setHeader("Curso");
        spreadsheet.setHeader("Nº de Aluno");
        spreadsheet.setHeader("Nome");
        spreadsheet.setHeader("B.I");

        spreadsheet.setHeader("Aluno deslocado");
        spreadsheet.setHeader("Localidade");
        spreadsheet.setHeader("Email " + Unit.getInstitutionAcronym());
        spreadsheet.setHeader("Email pessoal");
        spreadsheet.setHeader("Telefone");
        spreadsheet.setHeader("Telemovel");
        spreadsheet.setHeader("Deslocado da residencia");
        spreadsheet.setHeader("Turnos");
    }

    private void addRow(final Spreadsheet spreadsheet, final StudentCandidacy candidacy) {
        final Row row = spreadsheet.addRow();
        final Person person = candidacy.getPerson();
        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();

        row.setCell(candidacy.getActiveCandidacySituation().getSituationDate().toString("dd/MM/yyyy HH:mm"));
        row.setCell(candidacy.getRegistration().getIngression().getName());
        row.setCell(candidacy.getExecutionDegree().getDegree().getNameFor(executionYear).getContent());
        row.setCell(candidacy.getRegistration().getNumber().toString());
        row.setCell(person.getName());
        row.setCell(person.getDocumentIdNumber());

        row.setCell(candidacy.getDislocatedFromPermanentResidence() != null
                && candidacy.getDislocatedFromPermanentResidence().booleanValue() ? "Sim" : "Nao");
        row.setCell(person.getArea());
        row.setCell(person.getInstitutionalEmailAddressValue());
        row.setCell(getPersonalEmailAddress(person));
        row.setCell(getPhone(person));
        row.setCell(getMobilePhone(person));
        row.setCell(candidacy.getDislocatedFromPermanentResidence() != null
                && candidacy.getDislocatedFromPermanentResidence().booleanValue() ? "Sim" : "Nao");
        row.setCell(getShiftNames(candidacy));
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

    private String getShiftNames(final StudentCandidacy candidacy) {
        if (!candidacy.hasRegistration()) {
            return " ";
        }

        final Registration registration = candidacy.getRegistration();
        final StringBuilder builder = new StringBuilder();

        for (final Shift shift : registration.getShiftsFor(candidacy.getExecutionYear().getFirstExecutionPeriod())) {
            builder.append(shift.getNome()).append(",");
        }

        if (builder.length() > 1) {
            builder.deleteCharAt(builder.length() - 1);
        }

        return builder.toString();
    }

}
