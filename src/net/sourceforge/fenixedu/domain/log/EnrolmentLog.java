/*
 * Created on Nov 4, 2004
 */
package net.sourceforge.fenixedu.domain.log;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.EnrolmentAction;

/**
 * @author nmgo
 * @author lmre
 */
public class EnrolmentLog extends EnrolmentLog_Base {

	protected EnrolmentLog() {
		super();
	}

	public EnrolmentLog(final EnrolmentAction action, final Registration registration, final CurricularCourse curricularCourse,
			final ExecutionSemester executionSemester, final String who) {
		this();
		init(action, registration, curricularCourse, executionSemester, who);
	}

	@Override
	public CurricularCourse getDegreeModule() {
		return (CurricularCourse) super.getDegreeModule();
	}

	@Override
	public String getDescription() {
		return getDegreeModule().getName(getExecutionPeriod());
	}

}
