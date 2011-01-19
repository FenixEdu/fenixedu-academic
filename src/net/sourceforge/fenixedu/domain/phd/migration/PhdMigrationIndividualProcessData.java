package net.sourceforge.fenixedu.domain.phd.migration;

import java.util.NoSuchElementException;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.CreateNewProcess;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramCollaborationType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.phd.migration.common.ConversionUtilities;
import net.sourceforge.fenixedu.domain.phd.migration.common.PhdProgramTranslator;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.IncompleteFieldsException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.PersonNotFoundException;

import org.joda.time.LocalDate;

public class PhdMigrationIndividualProcessData extends PhdMigrationIndividualProcessData_Base {

    private transient Integer processNumber;
    private transient PhdProgram phdProgram;
    private transient String title;
    private transient String guiderNumber;
    private transient String assistantGuiderNumber;
    private transient LocalDate startProcessDate;
    private transient LocalDate startDevelopmentDate;
    private transient LocalDate requirementDate;
    private transient LocalDate meetingDate;
    private transient LocalDate firstDiscussionDate;
    private transient LocalDate secondDiscussionDate;
    private transient LocalDate edictDate;

    private transient String classification;
    private transient LocalDate probateDate;
    private transient LocalDate annulmentDate;
    private transient LocalDate limitToFinishDate;

    private PhdMigrationIndividualProcessData() {
	super();
    }

    protected PhdMigrationIndividualProcessData(String data) {
	setData(data);
	setMigrationStatus(PhdMigrationProcessStateType.NOT_MIGRATED);
    }

    public void parseAndSetNumber() {
	parse();

	setNumber(processNumber);
    }

    public void parse() {
	try {
	    String[] fields = getData().split("\t");

	    try {
		processNumber = Integer.valueOf(fields[0].trim());
	    } catch (NumberFormatException e) {
		throw new IncompleteFieldsException("processNumber");
	    }

	    try {
		phdProgram = PhdProgramTranslator.translate(fields[1].trim());
	    } catch (NumberFormatException e) {
		throw new IncompleteFieldsException("phdProgram");
	    }
	    title = fields[2].trim();
	    guiderNumber = fields[3].trim();
	    assistantGuiderNumber = fields[4].trim();
	    startProcessDate = ConversionUtilities.parseDate(fields[5].trim());
	    startDevelopmentDate = ConversionUtilities.parseDate(fields[6].trim());
	    requirementDate = ConversionUtilities.parseDate(fields[7].trim());
	    meetingDate = ConversionUtilities.parseDate(fields[8].trim());
	    firstDiscussionDate = ConversionUtilities.parseDate(fields[9].trim());
	    secondDiscussionDate = ConversionUtilities.parseDate(fields[10].trim());
	    edictDate = ConversionUtilities.parseDate(fields[11].trim());

	    classification = fields[13].trim();
	    probateDate = ConversionUtilities.parseDate(fields[14].trim());
	    annulmentDate = ConversionUtilities.parseDate(fields[15].trim());
	    limitToFinishDate = ConversionUtilities.parseDate(fields[16].trim());

	} catch (NoSuchElementException e) {
	    throw new IncompleteFieldsException("Not enough fields");
	}
    }

    public Person getGuidingPerson() {
	if (guiderNumber.contains("E")) {
	    throw new PersonNotFoundException();
	}

	return getPerson(guiderNumber);
    }

    public Person getAssistantGuidingPerson() {
	if (assistantGuiderNumber.contains("E")) {
	    throw new PersonNotFoundException();
	}

	return getPerson(assistantGuiderNumber);
    }

    public Person getPerson(String identification) {
	Teacher teacher = Teacher.readByNumber(Integer.valueOf(identification));

	if (teacher == null) {
	    throw new PersonNotFoundException();
	}

	return teacher.getPerson();
    }

    public boolean hasExistingIndividualProgramProcess() {
	return getPhdIndividualProgramProcess() != null;
    }

    public PhdIndividualProgramProcess getPhdIndividualProgramProcess() {
	final PhdMigrationIndividualPersonalData personalData = getPhdMigrationIndividualPersonalData();

	if (personalData == null) {
	    return null;
	}

	if (!personalData.isPersonRegisteredOnFenix()) {
	    return null;
	}

	final Person student = personalData.getPerson();

	if (student.hasAnyPhdIndividualProgramProcesses()) {
	    return student.getPhdIndividualProgramProcesses().get(0);
	}

	return null;
    }

    private LocalDate retrieveDateForExecutionYear() {
	if (startDevelopmentDate != null) {
	    return startDevelopmentDate;
	}
	if (probateDate != null) {
	    return probateDate;
	}
	if (startProcessDate != null) {
	    return startProcessDate;
	}
	return null;
    }

    public void createCandidacyProcess(IUserView userView) {
	final PhdProgramCandidacyProcessBean candidacyBean = new PhdProgramCandidacyProcessBean();

	candidacyBean.setCandidacyDate(this.startProcessDate);
	candidacyBean.setState(PhdProgramCandidacyProcessState.STAND_BY_WITH_COMPLETE_INFORMATION);

	candidacyBean.setPersonBean(getPhdMigrationIndividualPersonalData().getPersonBean());
	candidacyBean.setMigratedProcess(true);
	candidacyBean.setProgram(this.phdProgram);
	candidacyBean.setThesisTitle(this.title);
	candidacyBean.setPhdStudentNumber(getPhdMigrationIndividualPersonalData().getNumber());
	candidacyBean.setCollaborationType(PhdIndividualProgramCollaborationType.NONE);
	candidacyBean.setExecutionYear(ExecutionYear.readByDateTime(retrieveDateForExecutionYear()));

	candidacyBean.setFocusArea((phdProgram.getPhdProgramFocusAreasCount() == 1) ? phdProgram.getPhdProgramFocusAreas().get(0)
		: null);

	final PhdIndividualProgramProcess individualProcess = (PhdIndividualProgramProcess) CreateNewProcess.run(
		PhdIndividualProgramProcess.class, candidacyBean);

    }

}
