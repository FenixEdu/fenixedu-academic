package net.sourceforge.fenixedu.applicationTier.Servico.department;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.department.TeacherPersonalExpectationBean;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;

public class InsertTeacherPersonalExpectation extends Service {

    public void run(TeacherPersonalExpectationBean bean) {
	if(bean != null) {
	    new TeacherPersonalExpectation(bean);
	}
    }
}
