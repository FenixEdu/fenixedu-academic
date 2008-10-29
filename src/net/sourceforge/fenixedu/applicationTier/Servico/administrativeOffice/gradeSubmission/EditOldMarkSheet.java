package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementEditBean;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.MarkSheetState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class EditOldMarkSheet extends FenixService {

    public void run(MarkSheetManagementEditBean markSheetManagementEditBean) throws FenixServiceException {

	MarkSheet markSheet = markSheetManagementEditBean.getMarkSheet();
	if (markSheet == null) {
	    throw new InvalidArgumentsServiceException("error.noMarkSheet");
	}

	if (markSheet.getMarkSheetState() == MarkSheetState.NOT_CONFIRMED) {
	    editNormalMarkSheet(markSheetManagementEditBean);

	} else if (markSheet.getMarkSheetState() == MarkSheetState.RECTIFICATION_NOT_CONFIRMED) {
	    // editRectificationMarkSheet(markSheetManagementEditBean);

	} else {
	    throw new InvalidArgumentsServiceException("error.markSheet.invalid.state");
	}
    }

    private void editNormalMarkSheet(MarkSheetManagementEditBean markSheetManagementEditBean) {
	Collection<MarkSheetEnrolmentEvaluationBean> filteredEnrolmentEvaluationBeansToEditList = getEnrolmentEvaluationsWithValidGrades(markSheetManagementEditBean
		.getEnrolmentEvaluationBeansToEdit());

	Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeansToAppendList = getEnrolmentEvaluationsWithValidGrades(markSheetManagementEditBean
		.getEnrolmentEvaluationBeansToAppend());

	Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeansToRemoveList = CollectionUtils.subtract(
		markSheetManagementEditBean.getEnrolmentEvaluationBeansToEdit(), filteredEnrolmentEvaluationBeansToEditList);

	markSheetManagementEditBean.getMarkSheet().editNormal(filteredEnrolmentEvaluationBeansToEditList,
		enrolmentEvaluationBeansToAppendList, enrolmentEvaluationBeansToRemoveList);
    }

    private Collection<MarkSheetEnrolmentEvaluationBean> getEnrolmentEvaluationsWithValidGrades(
	    Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeans) {

	return CollectionUtils.select(enrolmentEvaluationBeans, new Predicate() {
	    public boolean evaluate(Object arg0) {
		MarkSheetEnrolmentEvaluationBean markSheetEnrolmentEvaluationBean = (MarkSheetEnrolmentEvaluationBean) arg0;
		return markSheetEnrolmentEvaluationBean.getGradeValue() != null
			&& markSheetEnrolmentEvaluationBean.getGradeValue().length() != 0;
	    }
	});
    }

}
