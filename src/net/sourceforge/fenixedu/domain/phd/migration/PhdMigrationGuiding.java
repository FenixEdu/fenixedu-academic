package net.sourceforge.fenixedu.domain.phd.migration;

import java.util.Map;
import java.util.NoSuchElementException;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean.PhdParticipantSelectType;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean.PhdParticipantType;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.IncompleteFieldsException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.PhdMigrationGuidingNotFoundException;

public class PhdMigrationGuiding extends PhdMigrationGuiding_Base {
    public static final String IST_INSTITUTION_CODE = "807";
    
    private transient Integer phdStudentNumber;
    private transient String institutionCode;
    private transient String name;
    private transient String teacherCode;

    protected PhdMigrationGuiding() {
        super();
	setRootDomainObject(RootDomainObject.getInstance());
    }
    
    protected PhdMigrationGuiding(String data) {
	super();
	setData(data);
    }

    private void parse() {
	try {
	    String[] compounds = getData().split("\\t");
	
	    this.phdStudentNumber = Integer.parseInt(compounds[0].trim());
	    this.teacherCode = compounds[2].trim();
	    this.institutionCode = compounds[3].trim();
	    this.name = compounds[4].trim();
	} catch (NoSuchElementException e) {
	    throw new IncompleteFieldsException();
	}
    }

    public void parseAndSetNumber(Map<String, String> INSTITUTION_MAP) {
	parse();

	setNumber(this.phdStudentNumber);
	setInstitution(INSTITUTION_MAP.get(institutionCode));
    }

    public boolean isExternal() {
	return !institutionCode.equals(IST_INSTITUTION_CODE);
    }

    public PhdParticipantBean getPhdParticipantBean(final PhdIndividualProgramProcess individualProcess) {

	if (isExternal()) {
	    return getExternalPhdParticipantBean(individualProcess);
	} else {
	    return getInternalPhdParticipantBean(individualProcess);
	}

    }

    private PhdParticipantBean getExternalPhdParticipantBean(final PhdIndividualProgramProcess individualProcess) {
	final PhdParticipantBean participantBean = new PhdParticipantBean();
	participantBean.setParticipantType(PhdParticipantType.EXTERNAL);
	participantBean.setParticipantSelectType(PhdParticipantSelectType.NEW);
	participantBean.setIndividualProgramProcess(individualProcess);
	participantBean.setName(name);
	participantBean.setWorkLocation(getInstitution());
	participantBean.setInstitution(getInstitution());

	return participantBean;
    }

    private PhdParticipantBean getInternalPhdParticipantBean(final PhdIndividualProgramProcess individualProcess) {
	final PhdParticipantBean participantBean = new PhdParticipantBean();
	participantBean.setIndividualProgramProcess(individualProcess);
	final Teacher teacher = Teacher.readByNumber(Integer.valueOf(teacherCode));

	if (teacher == null) {
	    throw new PhdMigrationGuidingNotFoundException("The guiding is not present in the system as a teacher");
	}

	for (PhdParticipant existingParticipant : individualProcess.getGuidingsAndAssistantGuidings()) {
	    if (!existingParticipant.isInternal()) {
		continue;
	    }

	    final InternalPhdParticipant existingInternalParticipant = (InternalPhdParticipant) existingParticipant;
	    final Person existingInternalPerson = existingInternalParticipant.getPerson();

	    if (teacher.getPerson() == existingInternalPerson) {
		// The guider is already associated with the process
		participantBean.setInternalParticipant(teacher.getPerson());
		participantBean.setParticipant(existingParticipant);
		participantBean.setParticipantSelectType(PhdParticipantSelectType.EXISTING);

		return participantBean;
	    }
	}

	// The guiding is in the system as teacher, but not yet associated with
	// the process
	participantBean.setParticipantSelectType(PhdParticipantSelectType.NEW);
	participantBean.setInternalParticipant(teacher.getPerson());
	participantBean.setInstitution(getInstitution());
	participantBean.setWorkLocation(getInstitution());
	return participantBean;
    }

}
