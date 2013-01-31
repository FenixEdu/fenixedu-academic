package net.sourceforge.fenixedu.domain.teacher.evaluation;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class FacultyEvaluationProcessYear extends FacultyEvaluationProcessYear_Base {

	public FacultyEvaluationProcessYear(final FacultyEvaluationProcess facultyEvaluationProcess, final String year) {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		setFacultyEvaluationProcess(facultyEvaluationProcess);
		setYear(year);
	}

}
