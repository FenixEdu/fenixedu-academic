package net.sourceforge.fenixedu.domain.oldInquiries;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.UploadStudentInquiriesCourseResultsBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class StudentInquiriesCourseResult extends StudentInquiriesCourseResult_Base {

    public static final Comparator<StudentInquiriesCourseResult> EXECUTION_COURSE_NAME_COMPARATOR =
            new Comparator<StudentInquiriesCourseResult>() {

                @Override
                public int compare(StudentInquiriesCourseResult o1, StudentInquiriesCourseResult o2) {
                    final int c =
                            Collator.getInstance().compare(o1.getExecutionCourse().getNome(), o2.getExecutionCourse().getNome());
                    return c == 0 ? AbstractDomainObject.COMPARATOR_BY_ID.compare(o1, o2) : c;
                }

            };

    transient private Map<String, String> valuesMap = null;

    public StudentInquiriesCourseResult() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    private Double getValueForPresentation(Double value) {
        // TODO: ugly hack, refactor
        if (value == null) {
            return new Double(0);
        }
        BigDecimal round = new BigDecimal(value);
        round.setScale(2, RoundingMode.HALF_EVEN);
        return round.doubleValue();
    }

    public Double getEvaluatedRatioForPresentation() {
        return getValueForPresentation(super.getEvaluatedRatio());
    }

    public Double getApprovedRatioForPresentation() {
        return getValueForPresentation(super.getApprovedRatio());
    }

    public Double getValidInitialFormAnswersNumberForPresentation() {
        return getValueForPresentation(super.getValidInitialFormAnswersNumber());
    }

    public Double getValidInitialFormAnswersRatioForPresentation() {
        return getValueForPresentation(super.getValidInitialFormAnswersRatio());
    }

    public Double getValidInquiryAnswersNumberForPresentation() {
        return getValueForPresentation(super.getValidInquiryAnswersNumber());
    }

    public Double getValidInquiryAnswersRatioForPresentation() {
        return getValueForPresentation(super.getValidInquiryAnswersRatio());
    }

    public Double getNoInquiryAnswersNumberForPresentation() {
        return getValueForPresentation(super.getNoInquiryAnswersNumber());
    }

    public Double getNoInquiryAnswersRatioForPresentation() {
        return getValueForPresentation(super.getNoInquiryAnswersRatio());
    }

    public Double getInvalidInquiryAnswersNumberForPresentation() {
        return getValueForPresentation(super.getInvalidInquiryAnswersNumber());
    }

    public Double getInvalidInquiryAnswersRatioForPresentation() {
        return getValueForPresentation(super.getInvalidInquiryAnswersRatio());
    }

    public Double getGradeAverageForPresentation() {
        return getValueForPresentation(super.getGradeAverage());
    }

    public Double getScheduleLoadForPresentation() {
        return getValueForPresentation(super.getScheduleLoad());
    }

    public Double getEctsForPresentation() {
        return getValueForPresentation(super.getEcts());
    }

    public Double getEstimatedEctsAverageForPresentation() {
        return getValueForPresentation(super.getEstimatedEctsAverage());
    }

    public Double getEstimatedEctsStandardDeviationForPresentation() {
        return getValueForPresentation(super.getEstimatedEctsStandardDeviation());
    }

    public Double getAverage_NHTAForPresentation() {
        return getValueForPresentation(super.getAverage_NHTA());
    }

    public Double getStandardDeviation_NHTAForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_NHTA());
    }

    public Double getAverage_perc_weeklyHoursForPresentation() {
        return getValueForPresentation(super.getAverage_perc_weeklyHours());
    }

    public Double getStandardDeviation_perc_NHTAForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_perc_NHTA());
    }

    public Double getAverage_NDEForPresentation() {
        return getValueForPresentation(super.getAverage_NDE());
    }

    public Double getStandardDeviation_NDEForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_NDE());
    }

    public Double getNumber_P1_1ForPresentation() {
        return getValueForPresentation(super.getNumber_P1_1());
    }

    public Double getPerc_10_12ForPresentation() {
        return getValueForPresentation(super.getPerc_10_12());
    }

    public Double getPerc_13_14ForPresentation() {
        return getValueForPresentation(super.getPerc_13_14());
    }

    public Double getPerc_15_16ForPresentation() {
        return getValueForPresentation(super.getPerc_15_16());
    }

    public Double getPerc_17_18ForPresentation() {
        return getValueForPresentation(super.getPerc_17_18());
    }

    public Double getPerc_19_20ForPresentation() {
        return getValueForPresentation(super.getPerc_19_20());
    }

    public Double getPerc_flunkedForPresentation() {
        return getValueForPresentation(super.getPerc_flunked());
    }

    public Double getPerc_nonEvaluatedForPresentation() {
        return getValueForPresentation(super.getPerc_nonEvaluated());
    }

    public Double getNumber_P1_2_aForPresentation() {
        return getValueForPresentation(super.getNumber_P1_2_a());
    }

    public Double getPerc__P1_2_aForPresentation() {
        return getValueForPresentation(super.getPerc__P1_2_a());
    }

    public Double getNumber_P1_2_bForPresentation() {
        return getValueForPresentation(super.getNumber_P1_2_b());
    }

    public Double getPerc__P1_2_bForPresentation() {
        return getValueForPresentation(super.getPerc__P1_2_b());
    }

    public Double getNumber_P1_2_cForPresentation() {
        return getValueForPresentation(super.getNumber_P1_2_c());
    }

    public Double getPerc__P1_2_cForPresentation() {
        return getValueForPresentation(super.getPerc__P1_2_c());
    }

    public Double getNumber_P1_2_dForPresentation() {
        return getValueForPresentation(super.getNumber_P1_2_d());
    }

    public Double getPerc__P1_2_dForPresentation() {
        return getValueForPresentation(super.getPerc__P1_2_d());
    }

    public Double getNumber_P1_2_eForPresentation() {
        return getValueForPresentation(super.getNumber_P1_2_e());
    }

    public Double getPerc__P1_2_eForPresentation() {
        return getValueForPresentation(super.getPerc__P1_2_e());
    }

    public Double getNumber_P_1_2_fForPresentation() {
        return getValueForPresentation(super.getNumber_P_1_2_f());
    }

    public Double getPerc__P1_2_fForPresentation() {
        return getValueForPresentation(super.getPerc__P1_2_f());
    }

    public Double getNumber_P1_2_gForPresentation() {
        return getValueForPresentation(super.getNumber_P1_2_g());
    }

    public Double getPerc__P1_2_gForPresentation() {
        return getValueForPresentation(super.getPerc__P1_2_g());
    }

    public Double getAverage_P1_3ForPresentation() {
        return getValueForPresentation(super.getAverage_P1_3());
    }

    public Double getStandardDeviation_P1_3ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P1_3());
    }

    public Double getPerc_P1_3_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P1_3_1());
    }

    public Double getPerc_P1_3_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P1_3_2());
    }

    public Double getPerc_P1_3_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P1_3_3());
    }

    public Double getPerc_P1_3_4ForPresentation() {
        return getValueForPresentation(super.getPerc_P1_3_4());
    }

    public Double getPerc_P1_3_5ForPresentation() {
        return getValueForPresentation(super.getPerc_P1_3_5());
    }

    public Double getPerc_P1_3_6ForPresentation() {
        return getValueForPresentation(super.getPerc_P1_3_6());
    }

    public Double getPerc_P1_3_7ForPresentation() {
        return getValueForPresentation(super.getPerc_P1_3_7());
    }

    public Double getPerc_P1_3_8ForPresentation() {
        return getValueForPresentation(super.getPerc_P1_3_8());
    }

    public Double getPerc_P1_3_9ForPresentation() {
        return getValueForPresentation(super.getPerc_P1_3_9());
    }

    public Double getAverage_P1_4ForPresentation() {
        return getValueForPresentation(super.getAverage_P1_4());
    }

    public Double getStandardDeviation_P1_4ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P1_4());
    }

    public Double getPerc_P1_4_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P1_4_1());
    }

    public Double getPerc_P1_4_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P1_4_2());
    }

    public Double getPerc_P1_4_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P1_4_3());
    }

    public Double getAverage_P2_1ForPresentation() {
        return getValueForPresentation(super.getAverage_P2_1());
    }

    public Double getStandardDeviation_P2_1ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P2_1());
    }

    public Double getPerc_P2_1_0ForPresentation() {
        return getValueForPresentation(super.getPerc_P2_1_0());
    }

    public Double getPerc_P2_1_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P2_1_1());
    }

    public Double getPerc_P2_1_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P2_1_2());
    }

    public Double getPerc_P2_1_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P2_1_3());
    }

    public Double getAverage_P2_2ForPresentation() {
        return getValueForPresentation(super.getAverage_P2_2());
    }

    public Double getStandardDeviation_P2_2ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P2_2());
    }

    public Double getPerc_P2_2_0ForPresentation() {
        return getValueForPresentation(super.getPerc_P2_2_0());
    }

    public Double getPerc_P2_2_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P2_2_1());
    }

    public Double getPerc_P2_2_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P2_2_2());
    }

    public Double getPerc_P2_2_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P2_2_3());
    }

    public Double getAverage_P2_3ForPresentation() {
        return getValueForPresentation(super.getAverage_P2_3());
    }

    public Double getStandardDeviation_P2_3ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P2_3());
    }

    public Double getPerc_P2_3_0ForPresentation() {
        return getValueForPresentation(super.getPerc_P2_3_0());
    }

    public Double getPerc_P2_3_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P2_3_1());
    }

    public Double getPerc_P2_3_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P2_3_2());
    }

    public Double getPerc_P2_3_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P2_3_3());
    }

    public Double getAverage_P2_4ForPresentation() {
        return getValueForPresentation(super.getAverage_P2_4());
    }

    public Double getStandardDeviation_P2_4ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P2_4());
    }

    public Double getPerc_P2_4_0ForPresentation() {
        return getValueForPresentation(super.getPerc_P2_4_0());
    }

    public Double getPerc_P2_4_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P2_4_1());
    }

    public Double getPerc_P2_4_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P2_4_2());
    }

    public Double getPerc_P2_4_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P2_4_3());
    }

    public Double getAverage_P3_1ForPresentation() {
        return getValueForPresentation(super.getAverage_P3_1());
    }

    public Double getStandardDeviation_P3_1ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P3_1());
    }

    public Double getPerc_P3_1_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_1_1());
    }

    public Double getPerc_P3_1_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_1_2());
    }

    public Double getPerc_P3_1_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_1_3());
    }

    public Double getPerc_P3_1_4ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_1_4());
    }

    public Double getPerc_P3_1_5ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_1_5());
    }

    public Double getPerc_P3_1_6ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_1_6());
    }

    public Double getPerc_P3_1_7ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_1_7());
    }

    public Double getPerc_P3_1_8ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_1_8());
    }

    public Double getPerc_P3_1_9ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_1_9());
    }

    public Double getAverage_P3_2ForPresentation() {
        return getValueForPresentation(super.getAverage_P3_2());
    }

    public Double getStandardDeviation_P3_2ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P3_2());
    }

    public Double getPerc_P3_2_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_2_1());
    }

    public Double getPerc_P3_2_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_2_2());
    }

    public Double getPerc_P3_2_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_2_3());
    }

    public Double getPerc_P3_2_4ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_2_4());
    }

    public Double getPerc_P3_2_5ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_2_5());
    }

    public Double getPerc_P3_2_6ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_2_6());
    }

    public Double getPerc_P3_2_7ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_2_7());
    }

    public Double getPerc_P3_2_8ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_2_8());
    }

    public Double getPerc_P3_2_9ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_2_9());
    }

    public Double getAverage_P3_3ForPresentation() {
        return getValueForPresentation(super.getAverage_P3_3());
    }

    public Double getStandardDeviation_P3_3ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P3_3());
    }

    public Double getPerc_P3_3_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_3_1());
    }

    public Double getPerc_P3_3_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_3_2());
    }

    public Double getPerc_P3_3_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_3_3());
    }

    public Double getPerc_P3_3_4ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_3_4());
    }

    public Double getPerc_P3_3_5ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_3_5());
    }

    public Double getPerc_P3_3_6ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_3_6());
    }

    public Double getPerc_P3_3_7ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_3_7());
    }

    public Double getPerc_P3_3_8ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_3_8());
    }

    public Double getPerc_P3_3_9ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_3_9());
    }

    public Double getAverage_P3_4ForPresentation() {
        return getValueForPresentation(super.getAverage_P3_4());
    }

    public Double getStandardDeviation_P3_4ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P3_4());
    }

    public Double getPerc_P3_4_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_4_1());
    }

    public Double getPerc_P3_4_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_4_2());
    }

    public Double getPerc_P3_4_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_4_3());
    }

    public Double getPerc_P3_4_4ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_4_4());
    }

    public Double getPerc_P3_4_5ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_4_5());
    }

    public Double getPerc_P3_4_6ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_4_6());
    }

    public Double getPerc_P3_4_7ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_4_7());
    }

    public Double getPerc_P3_4_8ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_4_8());
    }

    public Double getPerc_P3_4_9ForPresentation() {
        return getValueForPresentation(super.getPerc_P3_4_9());
    }

    public Double getAverage_P4ForPresentation() {
        return getValueForPresentation(super.getAverage_P4());
    }

    public Double getStandardDeviation_P4ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P4());
    }

    public Double getPerc_P4_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P4_1());
    }

    public Double getPerc_P4_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P4_2());
    }

    public Double getPerc_P4_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P4_3());
    }

    public Double getPerc_P4_4ForPresentation() {
        return getValueForPresentation(super.getPerc_P4_4());
    }

    public Double getPerc_P4_5ForPresentation() {
        return getValueForPresentation(super.getPerc_P4_5());
    }

    public Double getPerc_P4_6ForPresentation() {
        return getValueForPresentation(super.getPerc_P4_6());
    }

    public Double getPerc_P4_7ForPresentation() {
        return getValueForPresentation(super.getPerc_P4_7());
    }

    public Double getPerc_P4_8ForPresentation() {
        return getValueForPresentation(super.getPerc_P4_8());
    }

    public Double getPerc_P4_9ForPresentation() {
        return getValueForPresentation(super.getPerc_P4_9());
    }

    public Double getAverage_P5ForPresentation() {
        return getValueForPresentation(super.getAverage_P5());
    }

    public Double getStandardDeviation_P5ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P5());
    }

    public Double getPerc_P5_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P5_1());
    }

    public Double getPerc_P5_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P5_2());
    }

    public Double getPerc_P5_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P5_3());
    }

    public Double getPerc_P5_4ForPresentation() {
        return getValueForPresentation(super.getPerc_P5_4());
    }

    public Double getPerc_P5_5ForPresentation() {
        return getValueForPresentation(super.getPerc_P5_5());
    }

    public Double getPerc_P5_6ForPresentation() {
        return getValueForPresentation(super.getPerc_P5_6());
    }

    public Double getPerc_P5_7ForPresentation() {
        return getValueForPresentation(super.getPerc_P5_7());
    }

    public Double getPerc_P5_8ForPresentation() {
        return getValueForPresentation(super.getPerc_P5_8());
    }

    public Double getPerc_P5_9ForPresentation() {
        return getValueForPresentation(super.getPerc_P5_9());
    }

    public Map<String, String> getValuesMap() {
        if (this.valuesMap == null) {
            synchronized (this) {
                if (this.valuesMap == null) {
                    Map<String, String> tmpMap = new HashMap<String, String>();
                    if (!StringUtils.isEmpty(getHeaders()) && !StringUtils.isEmpty(getRawValues())) {
                        String[] headers = getHeaders().split("\t");
                        String[] values = getRawValues().split("\t");
                        for (int i = 0; i < values.length; i++) {
                            tmpMap.put(headers[i], values[i]);
                        }
                    }
                    this.valuesMap = Collections.unmodifiableMap(tmpMap);
                }
            }
        }
        return valuesMap;
    }

    @Override
    public Boolean getInternalDisclosure() {
        if (super.getInternalDisclosure() != null) {
            return super.getInternalDisclosure();
        }
        return getValuesMap().containsKey("Repres_div_interna") ? fieldToBoolean(getValuesMap().get("Repres_div_interna")) : false;
    }

    private static int getHeaderIndex(String headerToFind, String[] headersSplitted) {
        for (int i = 0; i < headersSplitted.length; i++) {
            if (headerToFind.equals(headersSplitted[i])) {
                return i;
            }
        }
        throw new DomainException("error.StudentInquiriesCourseResult.headerNotFound", headerToFind);
    }

    private static Boolean fieldToBoolean(String field) {
        return !StringUtils.isEmpty(field) && (field.equalsIgnoreCase("true") || field.equals("1"));
    }

    public boolean isUnsatisfactory() {
        return getUnsatisfactoryResultsCUEvaluation() || getUnsatisfactoryResultsCUOrganization();
    }

    @Override
    public Boolean getUnsatisfactoryResultsCUEvaluation() {
        return super.getUnsatisfactoryResultsCUEvaluation() != null && super.getUnsatisfactoryResultsCUEvaluation();
    }

    @Override
    public Boolean getUnsatisfactoryResultsCUOrganization() {
        return super.getUnsatisfactoryResultsCUOrganization() != null && super.getUnsatisfactoryResultsCUOrganization();
    }

    public boolean isExcellent() {
        return getExcellentResultsCUEvaluation() || getExcellentResultsCUOrganization();
    }

    public Boolean getExcellentResultsCUEvaluation() {
        return fieldToBoolean(getValuesMap().get("ResExcelent_AvaliacaoUC"));
    }

    public Boolean getExcellentResultsCUOrganization() {
        return fieldToBoolean(getValuesMap().get("ResExcelent_OrganizacaoUC"));
    }

    @Override
    public Boolean getAuditCU() {
        if (super.getAuditCU() != null) {
            return super.getAuditCU();
        }
        return getValuesMap().containsKey("UC_auditoria") ? fieldToBoolean(getValuesMap().get("UC_auditoria")) : false;
    }

    @Override
    public Boolean getPublicDisclosure() {
        if (super.getPublicDisclosure() != null) {
            return super.getPublicDisclosure();
        }
        return getValuesMap().containsKey("Repres_div_publica") ? fieldToBoolean(getValuesMap().get("Repres_div_publica")) : false;
    }

    @Atomic
    public static void importResults(String headers, String values, UploadStudentInquiriesCourseResultsBean resultsBean) {

        String[] headersSplitted = headers.split("\t");

        int executionCourseHeaderIndex = getHeaderIndex(resultsBean.getKeyExecutionCourseHeader(), headersSplitted);
        int executionDegreeHeaderIndex = getHeaderIndex(resultsBean.getKeyExecutionDegreeHeader(), headersSplitted);
        int unsatisfactoryResultsCUEvaluationIndex =
                getHeaderIndex(resultsBean.getUnsatisfactoryResultsCUEvaluationHeader(), headersSplitted);
        int unsatisfactoryResultsCUOrganizationIndex =
                getHeaderIndex(resultsBean.getUnsatisfactoryResultsCUOrganizationHeader(), headersSplitted);

        for (String row : values.split("\n")) {
            String[] columns = row.split("\t");

            ExecutionCourse executionCourse = FenixFramework.getDomainObject(columns[executionCourseHeaderIndex]);
            if (executionCourse == null) {
                throw new DomainException("error.StudentInquiriesCourseResult.executionCourseNotFound",
                        columns[executionCourseHeaderIndex]);
            }
            ExecutionDegree executionDegree = FenixFramework.getDomainObject(columns[executionDegreeHeaderIndex]);
            if (executionDegree == null) {
                throw new DomainException("error.StudentInquiriesCourseResult.executionDegreeNotFound",
                        columns[executionDegreeHeaderIndex]);
            }

            if (executionDegree.getExecutionYear() != executionCourse.getExecutionYear()) {
                throw new DomainException("error.StudentInquiriesCourseResult.executionDegreeAndCourseYearDoesntMatch",
                        executionDegree.getExecutionYear().getName(), executionDegree.getPresentationName(), executionCourse
                                .getExecutionYear().getName(), executionCourse.getNome());
            }

            StudentInquiriesCourseResult studentInquiriesCourseResult =
                    executionCourse.getStudentInquiriesCourseResult(executionDegree);

            if (studentInquiriesCourseResult == null) {
                studentInquiriesCourseResult = new StudentInquiriesCourseResult();
                studentInquiriesCourseResult.setExecutionCourse(executionCourse);
                studentInquiriesCourseResult.setExecutionDegree(executionDegree);
            }

            studentInquiriesCourseResult.setRawValues(row);
            studentInquiriesCourseResult.setHeaders(headers);
            studentInquiriesCourseResult.setUploadDateTime(new DateTime());
            studentInquiriesCourseResult.setResultsDate(resultsBean.getResultsDate());
            studentInquiriesCourseResult
                    .setUnsatisfactoryResultsCUEvaluation(fieldToBoolean(columns[unsatisfactoryResultsCUEvaluationIndex]));
            studentInquiriesCourseResult
                    .setUnsatisfactoryResultsCUOrganization(fieldToBoolean(columns[unsatisfactoryResultsCUOrganizationIndex]));
            studentInquiriesCourseResult.valuesMap = null;

        }

    }

    @Atomic
    public static Boolean resetCourseAndTeachingResults(UploadStudentInquiriesCourseResultsBean coursesBean) {
        boolean resetedItems = false;
        Set<Professorship> professorships = new HashSet<Professorship>();
        for (StudentInquiriesCourseResult courseResult : RootDomainObject.getInstance().getStudentInquiriesCourseResults()) {
            if (StringUtils.isEmpty(coursesBean.getKeyExecutionCourseHeader())) {
                if (coursesBean.getResultsDate().equals(courseResult.getResultsDate())) {
                    professorships.addAll(courseResult.getExecutionCourse().getProfessorships());
                    courseResult.resetValues();
                    resetedItems = true;
                }
            } else {
                ExecutionCourse executionCourse = FenixFramework.getDomainObject(coursesBean.getKeyExecutionCourseHeader());
                if (executionCourse == null) {
                    throw new DomainException("error.StudentInquiriesCourseResult.executionCourseNotFound",
                            coursesBean.getKeyExecutionCourseHeader());
                }
                if (executionCourse != null && courseResult.getExecutionCourse() == executionCourse) {
                    professorships.addAll(courseResult.getExecutionCourse().getProfessorships());
                    courseResult.resetValues();
                    resetedItems = true;
                }
            }
        }
        List<StudentInquiriesTeachingResult> toDelete = new ArrayList<StudentInquiriesTeachingResult>();
        for (Professorship professorship : professorships) {
            for (StudentInquiriesTeachingResult teachingResult : professorship.getStudentInquiriesTeachingResults()) {
                toDelete.add(teachingResult);
            }
        }
        for (StudentInquiriesTeachingResult teachingResult : toDelete) {
            teachingResult.delete();
        }
        return resetedItems;
    }

    @Atomic
    public void delete() {
        if (hasCoordinatorComment()) {
            throw new DomainException("error.StudentInquiriesCourseResult.cannotDelete.hasCoordinatorComment");
        }
        setExecutionCourse(null);
        setExecutionDegree(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Override
    public void setCourseResultsCoordinatorComment(String courseResultsCoordinatorComment) {
        super.setCourseResultsCoordinatorComment(courseResultsCoordinatorComment);
        setCoordinatorComment(getExecutionDegree().getCoordinatorByTeacher(AccessControl.getPerson()));
        setCourseResultsCoordinatorCommentDate(new DateTime());
    }

    private void resetValues() {
        this.valuesMap = null; // invalidate cache
        setApprovedRatio(null);
        setAuditCU(false);
        setAvailableToInquiry(false);
        setAverage_NDE(null);
        setAverage_NHTA(null);
        setAverage_P1_3(null);
        setAverage_P1_4(null);
        setAverage_P2_1(null);
        setAverage_P2_2(null);
        setAverage_P2_3(null);
        setAverage_P2_4(null);
        setAverage_P3_1(null);
        setAverage_P3_2(null);
        setAverage_P3_3(null);
        setAverage_P3_4(null);
        setAverage_P4(null);
        setAverage_P5(null);
        setAverage_perc_weeklyHours(null);
        setEcts(null);
        setEstimatedEctsAverage(null);
        setEstimatedEctsNumber(null);
        setEstimatedEctsStandardDeviation(null);
        setEvaluatedRatio(null);
        setGradeAverage(null);
        setInvalidInquiryAnswersRatio(null);
        setNoInquiryAnswersNumber(null);
        setNoInquiryAnswersRatio(null);
        setNumber_NDE(null);
        setNumber_NHTA(null);
        setNumber_P1_1(null);
        setNumber_P1_2_a(null);
        setNumber_P1_2_b(null);
        setNumber_P1_2_c(null);
        setNumber_P1_2_d(null);
        setNumber_P1_2_e(null);
        setNumber_P1_2_g(null);
        setNumber_P1_3(null);
        setNumber_P1_4(null);
        setNumber_P2_1(null);
        setNumber_P2_2(null);
        setNumber_P2_3(null);
        setNumber_P2_4(null);
        setNumber_P3_1(null);
        setNumber_P3_2(null);
        setNumber_P3_3(null);
        setNumber_P3_4(null);
        setNumber_P4(null);
        setNumber_P5(null);
        setNumber_P_1_2_f(null);
        setNumber_perc_NHTA(null);
        setNumberOfEnrolled(null);
        setPerc_10_12(null);
        setPerc_13_14(null);
        setPerc_15_16(null);
        setPerc_17_18(null);
        setPerc_19_20(null);
        setPerc__P1_2_a(null);
        setPerc__P1_2_b(null);
        setPerc__P1_2_c(null);
        setPerc__P1_2_d(null);
        setPerc__P1_2_e(null);
        setPerc__P1_2_f(null);
        setPerc__P1_2_g(null);
        setPerc_flunked(null);
        setPerc_nonEvaluated(null);
        setPerc_P1_3_1(null);
        setPerc_P1_3_2(null);
        setPerc_P1_3_3(null);
        setPerc_P1_3_4(null);
        setPerc_P1_3_5(null);
        setPerc_P1_3_6(null);
        setPerc_P1_3_7(null);
        setPerc_P1_3_8(null);
        setPerc_P1_3_9(null);
        setPerc_P1_4_1(null);
        setPerc_P1_4_2(null);
        setPerc_P1_4_3(null);
        setPerc_P2_1_0(null);
        setPerc_P2_1_1(null);
        setPerc_P2_1_2(null);
        setPerc_P2_1_3(null);
        setPerc_P2_2_0(null);
        setPerc_P2_2_1(null);
        setPerc_P2_2_2(null);
        setPerc_P2_2_3(null);
        setPerc_P2_3_0(null);
        setPerc_P2_3_1(null);
        setPerc_P2_3_2(null);
        setPerc_P2_3_3(null);
        setPerc_P2_4_0(null);
        setPerc_P2_4_1(null);
        setPerc_P2_4_2(null);
        setPerc_P2_4_3(null);
        setPerc_P3_1_1(null);
        setPerc_P3_1_2(null);
        setPerc_P3_1_3(null);
        setPerc_P3_1_4(null);
        setPerc_P3_1_5(null);
        setPerc_P3_1_6(null);
        setPerc_P3_1_7(null);
        setPerc_P3_1_8(null);
        setPerc_P3_1_9(null);
        setPerc_P3_2_1(null);
        setPerc_P3_2_2(null);
        setPerc_P3_2_3(null);
        setPerc_P3_2_4(null);
        setPerc_P3_2_5(null);
        setPerc_P3_2_6(null);
        setPerc_P3_2_7(null);
        setPerc_P3_2_8(null);
        setPerc_P3_2_9(null);
        setPerc_P3_3_1(null);
        setPerc_P3_3_2(null);
        setPerc_P3_3_3(null);
        setPerc_P3_3_4(null);
        setPerc_P3_3_5(null);
        setPerc_P3_3_6(null);
        setPerc_P3_3_7(null);
        setPerc_P3_3_8(null);
        setPerc_P3_3_9(null);
        setPerc_P3_4_1(null);
        setPerc_P3_4_2(null);
        setPerc_P3_4_3(null);
        setPerc_P3_4_4(null);
        setPerc_P3_4_5(null);
        setPerc_P3_4_6(null);
        setPerc_P3_4_7(null);
        setPerc_P3_4_8(null);
        setPerc_P3_4_9(null);
        setPerc_P4_1(null);
        setPerc_P4_2(null);
        setPerc_P4_3(null);
        setPerc_P4_4(null);
        setPerc_P4_5(null);
        setPerc_P4_6(null);
        setPerc_P4_7(null);
        setPerc_P4_8(null);
        setPerc_P4_9(null);
        setPerc_P5_1(null);
        setPerc_P5_2(null);
        setPerc_P5_3(null);
        setPerc_P5_4(null);
        setPerc_P5_5(null);
        setPerc_P5_6(null);
        setPerc_P5_7(null);
        setPerc_P5_9(null);
        setRawValues(null);
        setScheduleLoad(null);
        setStandardDeviation_NDE(null);
        setStandardDeviation_NHTA(null);
        setStandardDeviation_P1_3(null);
        setStandardDeviation_P1_4(null);
        setStandardDeviation_P2_1(null);
        setStandardDeviation_P2_2(null);
        setStandardDeviation_P2_3(null);
        setStandardDeviation_P2_4(null);
        setStandardDeviation_P3_1(null);
        setStandardDeviation_P3_2(null);
        setStandardDeviation_P3_3(null);
        setStandardDeviation_P3_4(null);
        setStandardDeviation_P4(null);
        setStandardDeviation_P5(null);
        setStandardDeviation_perc_NHTA(null);
        setUnsatisfactoryResultsCUEvaluation(false);
        setUnsatisfactoryResultsCUOrganization(false);
        setUnsatisfactoryResultsEsfECTSCU(false);
    }

    @Deprecated
    public java.util.Date getUpload() {
        org.joda.time.DateTime dt = getUploadDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setUpload(java.util.Date date) {
        if (date == null) {
            setUploadDateTime(null);
        } else {
            setUploadDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

}
