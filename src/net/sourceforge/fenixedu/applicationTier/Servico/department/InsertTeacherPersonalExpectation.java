package net.sourceforge.fenixedu.applicationTier.Servico.department;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.department.TeacherPersonalExpectationBean;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;

public class InsertTeacherPersonalExpectation extends Service {

    public TeacherPersonalExpectation run(TeacherPersonalExpectationBean bean) {
	if(bean != null) {
	    return new TeacherPersonalExpectation(bean);
	}
	return null;
    }
}
