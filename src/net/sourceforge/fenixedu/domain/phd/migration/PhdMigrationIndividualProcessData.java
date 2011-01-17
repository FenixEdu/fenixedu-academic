package net.sourceforge.fenixedu.domain.phd.migration;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessNumber;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.migration.common.ConversionUtilities;
import net.sourceforge.fenixedu.domain.phd.migration.common.PhdProgramTranslator;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.IndividualProcessNotFoundException;
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
    private transient Integer classification;
    private transient LocalDate probateDate;
    private transient LocalDate annulmentDate;
    private transient LocalDate limitToFinishDate;
    private transient LocalDate extensionToLimitToFnishDate;

    private PhdMigrationIndividualProcessData() {
        super();
    }
    
    protected PhdMigrationIndividualProcessData(String data) {
	setData(data);
    }

    public void parseAndSetNumber() {
	parse();

	setNumber(processNumber);
    }

    public boolean parse() {
	String[] fields = getData().split("\t");

	try {
	    processNumber = Integer.valueOf(fields[0].trim());
	    phdProgram = PhdProgramTranslator.translate(fields[1].trim());
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
	    classification = Integer.valueOf(fields[12].trim());
	    probateDate = ConversionUtilities.parseDate(fields[13].trim());
	    annulmentDate = ConversionUtilities.parseDate(fields[14].trim());
	    limitToFinishDate = ConversionUtilities.parseDate(fields[15].trim());
	    extensionToLimitToFnishDate = ConversionUtilities.parseDate(fields[16].trim());

	    return true;
	} catch (Exception e) {
	    return false;
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

    public PhdIndividualProgramProcess getIndividualProgramProcess() {
	for (PhdIndividualProgramProcessNumber phdNumber : RootDomainObject.getInstance().getPhdIndividualProcessNumbers()) {
	    if (phdNumber.getNumber() != processNumber) {
		continue;
	    }

	    return phdNumber.getProcess();
	}

	throw new IndividualProcessNotFoundException();
    }

}
