package net.sourceforge.fenixedu.applicationTier.Servico.department;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.department.TeacherPersonalExpectationBean;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;

public class InsertTeacherPersonalExpectation extends FenixService {

	public TeacherPersonalExpectation run(TeacherPersonalExpectationBean bean) {
		if (bean != null) {
			return new TeacherPersonalExpectation(bean);
		}
		return null;
	}
}
