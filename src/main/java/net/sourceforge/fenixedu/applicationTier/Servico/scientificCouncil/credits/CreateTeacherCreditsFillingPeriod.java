package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.credits;


import net.sourceforge.fenixedu.dataTransferObject.teacherCredits.TeacherCreditsPeriodBean;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateTeacherCreditsFillingPeriod {

    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    @Service
    public static void run(TeacherCreditsPeriodBean bean) {
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