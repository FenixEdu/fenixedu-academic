/*
 * Created on May 2, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.gradeSubmission;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementCreateBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.MarkSheet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class CreateMarkSheet extends Service {

    public MarkSheet run(MarkSheetManagementCreateBean markSheetManagementCreateBean, Employee employee) {

        Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeanList = CollectionUtils
                .select(markSheetManagementCreateBean.getEnrolmentEvaluationBeans(), new Predicate() {
                    public boolean evaluate(Object arg0) {
                        MarkSheetEnrolmentEvaluationBean markSheetEnrolmentEvaluationBean = (MarkSheetEnrolmentEvaluationBean) arg0;
                        return markSheetEnrolmentEvaluationBean.getGrade() != null
                                && markSheetEnrolmentEvaluationBean.getGrade().length() != 0;
                    }

                });

        return markSheetManagementCreateBean.getCurricularCourse().createNormalMarkSheet(
                markSheetManagementCreateBean.getExecutionPeriod(),
                markSheetManagementCreateBean.getTeacher(),
                markSheetManagementCreateBean.getEvaluationDate(),
                markSheetManagementCreateBean.getMarkSheetType(),
                Boolean.FALSE,
                enrolmentEvaluationBeanList, employee);
    }

}
