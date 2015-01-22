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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.MarkSheet;
import org.fenixedu.academic.report.FenixReport;
import org.fenixedu.academic.util.FenixDigestUtils;

public class MarkSheetDocument extends FenixReport {
    private static final long serialVersionUID = -1015332436546905622L;

    protected MarkSheet markSheet;

    public MarkSheetDocument(MarkSheet markSheet) {
        super();
        this.markSheet = markSheet;
        fillReport();
    }

    @Override
    public String getKey() {
        if (markSheet.isRectification()) {
            return "markSheetRectification";
        }
        return "markSheet";
    }

    @Override
    public String getReportFileName() {
        return "MarkSheet-" + markSheet.getCheckSum() + (markSheet.isRectification() ? "-rectified" : "");
    }

    @Override
    protected void fillReport() {
        addParameter("markSheet", markSheet);
        addParameter("checkSum", FenixDigestUtils.getPrettyCheckSum(markSheet.getCheckSum()));
        if (markSheet.isRectification()) {
            final EnrolmentEvaluation rectification = markSheet.getEnrolmentEvaluationsSet().iterator().next();
            addParameter("rectification", rectification);
            addParameter("rectified", rectification.getRectified());
        } else {
            List<EnrolmentEvaluation> evaluations = new ArrayList<EnrolmentEvaluation>(markSheet.getEnrolmentEvaluationsSet());
            Collections.sort(evaluations, EnrolmentEvaluation.SORT_BY_STUDENT_NUMBER);
            addDataSourceElements(evaluations);
        }
    }
}