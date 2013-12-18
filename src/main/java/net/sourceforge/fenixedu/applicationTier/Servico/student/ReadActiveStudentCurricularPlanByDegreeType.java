/*
 * Created on 2004/04/14
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import org.fenixedu.bennu.core.domain.User;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

/**
 * @author Luis Cruz
 * 
 */
public class ReadActiveStudentCurricularPlanByDegreeType {

    @Atomic
    public static InfoStudentCurricularPlan run(User userView, DegreeType degreeType) {
        check(RolePredicates.STUDENT_PREDICATE);

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