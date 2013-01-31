package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class SeniorStatute extends SeniorStatute_Base {

	private SeniorStatute() {
		super();
	}

	public SeniorStatute(Student student, Registration registration, StudentStatuteType statuteType,
			ExecutionSemester beginExecutionPeriod, ExecutionSemester endExecutionPeriod) {
		this();
		setBeginExecutionPeriod(beginExecutionPeriod);
		setEndExecutionPeriod(endExecutionPeriod);
		setStatuteType(statuteType);

		for (StudentStatute statute : student.getStudentStatutes()) {
			if (!statute.overlapsWith(this)) {
				continue;
			}
			if (!statute.hasSeniorStatuteForRegistration(getRegistration())) {
				continue;
			}

			throw new DomainException("error.studentStatute.alreadyExistsOneOverlapingStatute");

		}

		setStudent(student);

		if (registration == null) {
			throw new DomainException("error.studentStatute.mustDefineValidRegistrationMatchingSeniorStatute");
		}

		setRegistration(registration);
	}

	@Override
	public void delete() {
		checkRules();
		removeBeginExecutionPeriod();
		removeEndExecutionPeriod();
		removeStudent();
		removeRegistration();
		removeRootDomainObject();
		super.deleteDomainObject();
	}

	@Override
	public boolean hasSeniorStatuteForRegistration(Registration registration) {
		return (this.getRegistration() == registration);
	}

}
