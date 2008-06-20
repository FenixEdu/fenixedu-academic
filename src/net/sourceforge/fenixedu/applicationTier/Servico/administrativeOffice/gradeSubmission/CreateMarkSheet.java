/*
 * Created on May 2, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission;

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

	final Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeanList = CollectionUtils.select(
		markSheetManagementCreateBean.getAllEnrolmentEvalutionBeans(), new Predicate() {
		    public boolean evaluate(Object arg0) {
			return ((MarkSheetEnrolmentEvaluationBean) arg0).hasAnyGradeValue();
		    }

		});

	return markSheetManagementCreateBean.getCurricularCourse().createNormalMarkSheet(
		markSheetManagementCreateBean.getExecutionPeriod(), markSheetManagementCreateBean.getTeacher(),
		markSheetManagementCreateBean.getEvaluationDate(), markSheetManagementCreateBean.getMarkSheetType(),
		Boolean.FALSE, enrolmentEvaluationBeanList, employee);
    }

}
