package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean;
import net.sourceforge.fenixedu.domain.Summary;

public class CreateSummary extends Service {

    public void run(Integer executionCourseID, SummariesManagementBean bean) {
        if (bean.isNewSummary()) {            
            new Summary(bean.getTitle(), bean.getSummaryText(), bean.getStudentsNumber(), bean
                    .getSummaryType().equals(SummariesManagementBean.SummaryType.EXTRA_SUMMARY), bean
                    .getProfessorship(), bean.getTeacherName(), bean.getTeacher(), bean.getShift(), bean
                    .getLesson(), bean.getSummaryDate(), bean.getSummaryRoom(), bean.getSummaryTime());
        } else {
            bean.getSummary().edit(bean.getTitle(), bean.getSummaryText(), bean.getStudentsNumber(), bean
                    .getSummaryType().equals(SummariesManagementBean.SummaryType.EXTRA_SUMMARY), bean
                    .getProfessorship(), bean.getTeacherName(), bean.getTeacher(), bean.getShift(), bean
                    .getLesson(), bean.getSummaryDate(), bean.getSummaryRoom(), bean.getSummaryTime());
        }
    }
}
