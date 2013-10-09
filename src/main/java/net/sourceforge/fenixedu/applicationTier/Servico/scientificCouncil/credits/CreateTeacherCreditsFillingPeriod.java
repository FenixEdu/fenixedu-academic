package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.credits;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.dataTransferObject.teacherCredits.TeacherCreditsPeriodBean;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class CreateTeacherCreditsFillingPeriod {

    @Atomic
    public static void run(TeacherCreditsPeriodBean bean) {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);
        if (bean != null) {
            if (bean.isTeacher()) {
                bean.getExecutionPeriod().editTeacherCreditsPeriod(bean.getBeginForTeacher(), bean.getEndForTeacher());
            } else {
                bean.getExecutionPeriod().editDepartmentOfficeCreditsPeriod(bean.getBeginForDepartmentAdmOffice(),
                        bean.getEndForDepartmentAdmOffice());
            }
        }
    }
}