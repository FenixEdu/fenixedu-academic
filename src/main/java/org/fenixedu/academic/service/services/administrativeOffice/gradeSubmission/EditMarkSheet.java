/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.service.services.administrativeOffice.gradeSubmission;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.academic.domain.MarkSheet;
import org.fenixedu.academic.domain.MarkSheetState;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementEditBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;

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
