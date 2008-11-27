/*
 * Created on 2004/04/14
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * @author Luis Cruz
 * 
 */
public class ReadActiveStudentCurricularPlanByDegreeType extends FenixService {

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static InfoStudentCurricularPlan run(IUserView userView, DegreeType degreeType) {

	final Person person = userView.getPerson();
	final Registration registration = person.getStudentByType(degreeType);

	if (registration != null) {
	    final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
	    if (studentCurricularPlan != null) {
		final InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan(studentCurricularPlan);
		return infoStudentCurricularPlan;
	    }
	}

	return null;
    }

}