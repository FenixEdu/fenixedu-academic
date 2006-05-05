/*
 * Created on May 2, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.gradeSubmission;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementCreateBean;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class CreateMarkSheet extends Service {

    public void run(MarkSheetManagementCreateBean markSheetManagementCreateBean) {

        Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeanList = CollectionUtils
                .select(markSheetManagementCreateBean.getEnrolmentEvaluationBeans(), new Predicate() {
                    public boolean evaluate(Object arg0) {
                        MarkSheetEnrolmentEvaluationBean markSheetEnrolmentEvaluationBean = (MarkSheetEnrolmentEvaluationBean) arg0;
                        return markSheetEnrolmentEvaluationBean.getGrade() != null
                                && markSheetEnrolmentEvaluationBean.getGrade().length() != 0;
                    }

                });

        markSheetManagementCreateBean.getCurricularCourse().createMarkSheet(
                markSheetManagementCreateBean.getExecutionPeriod(),
                markSheetManagementCreateBean.getTeacher(),
                markSheetManagementCreateBean.getEvaluationDate(),
                markSheetManagementCreateBean.getMarkSheetType(), enrolmentEvaluationBeanList);
    }

}
