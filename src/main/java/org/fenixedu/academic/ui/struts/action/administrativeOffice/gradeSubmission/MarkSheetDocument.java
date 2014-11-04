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
            List<EnrolmentEvaluation> evaluations =
                    new ArrayList<EnrolmentEvaluation>(markSheet.getEnrolmentEvaluationsSet());
            Collections.sort(evaluations, EnrolmentEvaluation.SORT_BY_STUDENT_NUMBER);
            addDataSourceElements(evaluations);
        }
    }
}