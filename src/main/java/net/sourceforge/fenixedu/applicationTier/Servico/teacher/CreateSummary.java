package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.applicationTier.ServiceMonitoring;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingDepartmentAdmOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.SummaryManagementToDepartmentAdmOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.SummaryManagementToTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean;
import net.sourceforge.fenixedu.domain.Summary;
import pt.ist.fenixWebFramework.services.Service;

public class CreateSummary {

    protected void run(SummariesManagementBean bean) {

        ServiceMonitoring.logService(this.getClass(), bean);

        if (bean.isNewSummary()) {
            new Summary(bean.getTitle(), bean.getSummaryText(), bean.getStudentsNumber(), bean.getSummaryType().equals(
                    SummariesManagementBean.SummaryType.EXTRA_SUMMARY), bean.getProfessorship(), bean.getTeacherName(),
                    bean.getTeacher(), bean.getShift(), bean.getLesson(), bean.getSummaryDate(), bean.getSummaryRoom(),
                    bean.getSummaryTime(), bean.getLessonType(), bean.getTaught());
        } else {
            bean.getSummary().edit(bean.getTitle(), bean.getSummaryText(), bean.getStudentsNumber(),
                    bean.getSummaryType().equals(SummariesManagementBean.SummaryType.EXTRA_SUMMARY), bean.getProfessorship(),
                    bean.getTeacherName(), bean.getTeacher(), bean.getShift(), bean.getLesson(), bean.getSummaryDate(),
                    bean.getSummaryRoom(), bean.getSummaryTime(), bean.getLessonType(), bean.getTaught());
        }
    }

    // Service Invokers migrated from Berserk

    private static final CreateSummary serviceInstance = new CreateSummary();

    @Service
    public static void runCreateSummary(SummariesManagementBean bean) throws NotAuthorizedException {
        try {
            ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(bean);
            serviceInstance.run(bean);
        } catch (NotAuthorizedException ex1) {
            try {
                ExecutionCourseLecturingDepartmentAdmOfficeAuthorizationFilter.instance.execute(bean);
                serviceInstance.run(bean);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

    // Service Invokers migrated from Berserk

    @Service
    public static void runEditSummary(SummariesManagementBean bean) throws NotAuthorizedException {
        try {
            SummaryManagementToTeacherAuthorizationFilter.instance.execute(bean.getSummary(), bean.getProfessorshipLogged());
            serviceInstance.run(bean);
        } catch (NotAuthorizedException ex1) {
            try {
                SummaryManagementToDepartmentAdmOfficeAuthorizationFilter.instance.execute(bean.getSummary(),
                        bean.getProfessorshipLogged());
                serviceInstance.run(bean);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}