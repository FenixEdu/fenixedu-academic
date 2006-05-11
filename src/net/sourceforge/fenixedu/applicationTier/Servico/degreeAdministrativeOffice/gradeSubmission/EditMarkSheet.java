package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.gradeSubmission;

import java.util.Collection;
import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementEditBean;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class EditMarkSheet extends Service {

    public void run(MarkSheet markSheet, Teacher responsibleTeacher, Date evaluationDate)
            throws FenixServiceException {

        if (markSheet == null) {
            throw new InvalidArgumentsServiceException("error.noMarkSheet");
        }
        markSheet.edit(responsibleTeacher, evaluationDate);
    }

    public void run(MarkSheetManagementEditBean markSheetManagementEditBean) throws FenixServiceException {

        if (markSheetManagementEditBean.getMarkSheet() == null) {
            throw new InvalidArgumentsServiceException("error.noMarkSheet");
        }
        
        Collection<MarkSheetEnrolmentEvaluationBean> filteredEnrolmentEvaluationBeansToEditList =
                getEnrolmentEvaluationsWithValidGrades(markSheetManagementEditBean.getEnrolmentEvaluationBeansToEdit());
        
        Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeansToAppendList =
                getEnrolmentEvaluationsWithValidGrades(markSheetManagementEditBean.getEnrolmentEvaluationBeansToAppend());
        
        Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeansToRemoveList = CollectionUtils
                .subtract(markSheetManagementEditBean.getEnrolmentEvaluationBeansToEdit(), filteredEnrolmentEvaluationBeansToEditList);

        markSheetManagementEditBean.getMarkSheet().edit(filteredEnrolmentEvaluationBeansToEditList,
                enrolmentEvaluationBeansToAppendList, enrolmentEvaluationBeansToRemoveList);
    }

    private Collection<MarkSheetEnrolmentEvaluationBean> getEnrolmentEvaluationsWithValidGrades(
            Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeans) {

        return CollectionUtils.select(enrolmentEvaluationBeans, new Predicate() {
            public boolean evaluate(Object arg0) {
                MarkSheetEnrolmentEvaluationBean markSheetEnrolmentEvaluationBean = (MarkSheetEnrolmentEvaluationBean) arg0;
                return markSheetEnrolmentEvaluationBean.getGrade() != null
                        && markSheetEnrolmentEvaluationBean.getGrade().length() != 0;
            }
        });
    }

}
