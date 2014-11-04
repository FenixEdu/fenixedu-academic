/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementEditBean;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.MarkSheetState;
import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixframework.Atomic;

public class EditMarkSheet {

    @Atomic
    public static void run(MarkSheet markSheet, Teacher responsibleTeacher, Date evaluationDate) throws FenixServiceException {

        if (markSheet == null) {
            throw new InvalidArgumentsServiceException("error.noMarkSheet");
        }
        markSheet.editNormal(responsibleTeacher, evaluationDate);
    }

    @Atomic
    public static void run(MarkSheetManagementEditBean markSheetManagementEditBean) throws FenixServiceException {

        MarkSheet markSheet = markSheetManagementEditBean.getMarkSheet();
        if (markSheet == null) {
            throw new InvalidArgumentsServiceException("error.noMarkSheet");
        }

        if (markSheet.getMarkSheetState() == MarkSheetState.NOT_CONFIRMED) {
            editNormalMarkSheet(markSheetManagementEditBean);

        } else if (markSheet.getMarkSheetState() == MarkSheetState.RECTIFICATION_NOT_CONFIRMED) {
            editRectificationMarkSheet(markSheetManagementEditBean);

        } else {
            throw new InvalidArgumentsServiceException("error.markSheet.invalid.state");
        }
    }

    private static void editRectificationMarkSheet(MarkSheetManagementEditBean markSheetManagementEditBean) {

        Collection<MarkSheetEnrolmentEvaluationBean> filteredEnrolmentEvaluationBeansToEditList =
                getEnrolmentEvaluationsWithValidGrades(markSheetManagementEditBean.getEnrolmentEvaluationBeansToEdit());

        /*
         * Rectification MarkSheet MUST have ONLY ONE EnrolmentEvaluation
         */
        Iterator<MarkSheetEnrolmentEvaluationBean> iterator = filteredEnrolmentEvaluationBeansToEditList.iterator();
        markSheetManagementEditBean.getMarkSheet().editRectification(iterator.hasNext() ? iterator.next() : null);
    }

    private static void editNormalMarkSheet(MarkSheetManagementEditBean markSheetManagementEditBean) {
        Collection<MarkSheetEnrolmentEvaluationBean> filteredEnrolmentEvaluationBeansToEditList =
                getEnrolmentEvaluationsWithValidGrades(markSheetManagementEditBean.getEnrolmentEvaluationBeansToEdit());

        Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeansToAppendList =
                getEnrolmentEvaluationsWithValidGrades(markSheetManagementEditBean.getEnrolmentEvaluationBeansToAppend());

        Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeansToRemoveList =
                CollectionUtils.subtract(markSheetManagementEditBean.getEnrolmentEvaluationBeansToEdit(),
                        filteredEnrolmentEvaluationBeansToEditList);

        markSheetManagementEditBean.getMarkSheet().editNormal(filteredEnrolmentEvaluationBeansToEditList,
                enrolmentEvaluationBeansToAppendList, enrolmentEvaluationBeansToRemoveList);
    }

    private static Collection<MarkSheetEnrolmentEvaluationBean> getEnrolmentEvaluationsWithValidGrades(
            Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeans) {

        return CollectionUtils.select(enrolmentEvaluationBeans, new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                MarkSheetEnrolmentEvaluationBean markSheetEnrolmentEvaluationBean = (MarkSheetEnrolmentEvaluationBean) arg0;
                return markSheetEnrolmentEvaluationBean.getGradeValue() != null
                        && markSheetEnrolmentEvaluationBean.getGradeValue().length() != 0;
            }
        });
    }

}
