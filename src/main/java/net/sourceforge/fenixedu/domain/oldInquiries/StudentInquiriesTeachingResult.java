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
package net.sourceforge.fenixedu.domain.oldInquiries;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.UploadStudentInquiriesTeachingResultsBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class StudentInquiriesTeachingResult extends StudentInquiriesTeachingResult_Base {

    public StudentInquiriesTeachingResult() {
        super();
        setRootDomainObject(Bennu.getInstance());
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

    public Double getAverage_P6_1ForPresentation() {
        return getValueForPresentation(super.getAverage_P6_1());
    }

    public Double getStandardDeviation_P6_1ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P6_1());
    }

    public Double getP6_1_greater_3ForPresentation() {
        return getValueForPresentation(super.getP6_1_greater_3());
    }

    public Double getPerc_P6_1_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_1_1());
    }

    public Double getPerc_P6_1_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_1_2());
    }

    public Double getPerc_P6_1_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_1_3());
    }

    public Double getPerc_P6_1_4ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_1_4());
    }

    public Double getPerc_P6_1_5ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_1_5());
    }

    public Double getPerc_P6_1_6ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_1_6());
    }

    public Double getPerc_P6_1_7ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_1_7());
    }

    public Double getP6_1_1_aForPresentation() {
        return getValueForPresentation(super.getP6_1_1_a());
    }

    public Double getP6_1_1_bForPresentation() {
        return getValueForPresentation(super.getP6_1_1_b());
    }

    public Double getP6_1_1_cForPresentation() {
        return getValueForPresentation(super.getP6_1_1_c());
    }

    public Double getP6_1_1_dForPresentation() {
        return getValueForPresentation(super.getP6_1_1_d());
    }

    public Double getP6_1_1_eForPresentation() {
        return getValueForPresentation(super.getP6_1_1_e());
    }

    public Double getPerc_P6_1_aForPresentation() {
        return getValueForPresentation(super.getPerc_P6_1_a());
    }

    public Double getPerc_P6_1_bForPresentation() {
        return getValueForPresentation(super.getPerc_P6_1_b());
    }

    public Double getPerc_P6_1_cForPresentation() {
        return getValueForPresentation(super.getPerc_P6_1_c());
    }

    public Double getPerc_P6_1_dForPresentation() {
        return getValueForPresentation(super.getPerc_P6_1_d());
    }

    public Double getPerc_P6_1_eForPresentation() {
        return getValueForPresentation(super.getPerc_P6_1_e());
    }

    public Double getAverage_P6_2ForPresentation() {
        return getValueForPresentation(super.getAverage_P6_2());
    }

    public Double getStandardDeviation_P6_2ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P6_2());
    }

    public Double getPerc_P6_2_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_2_1());
    }

    public Double getPerc_P6_2_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_2_2());
    }

    public Double getPerc_P6_2_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_2_3());
    }

    public Double getPerc_P6_2_4ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_2_4());
    }

    public Double getPerc_P6_2_5ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_2_5());
    }

    public Double getPerc_P6_2_6ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_2_6());
    }

    public Double getPerc_P6_2_7ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_2_7());
    }

    public Double getPerc_P6_2_8ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_2_8());
    }

    public Double getPerc_P6_2_9ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_2_9());
    }

    public Double getAverage_P6_3ForPresentation() {
        return getValueForPresentation(super.getAverage_P6_3());
    }

    public Double getStandardDeviation_P6_3ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P6_3());
    }

    public Double getPerc_P6_3_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_3_1());
    }

    public Double getPerc_P6_3_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_3_2());
    }

    public Double getPerc_P6_3_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_3_3());
    }

    public Double getPerc_P6_3_4ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_3_4());
    }

    public Double getPerc_P6_3_5ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_3_5());
    }

    public Double getPerc_P6_3_6ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_3_6());
    }

    public Double getPerc_P6_3_7ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_3_7());
    }

    public Double getPerc_P6_3_8ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_3_8());
    }

    public Double getPerc_P6_3_9ForPresentation() {
        return getValueForPresentation(super.getPerc_P6_3_9());
    }

    public Double getAverage_P7_1ForPresentation() {
        return getValueForPresentation(super.getAverage_P7_1());
    }

    public Double getStandardDeviation_P7_1ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P7_1());
    }

    public Double getPerc_P7_1_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_1_1());
    }

    public Double getPerc_P7_1_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_1_2());
    }

    public Double getPerc_P7_1_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_1_3());
    }

    public Double getPerc_P7_1_4ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_1_4());
    }

    public Double getPerc_P7_1_5ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_1_5());
    }

    public Double getPerc_P7_1_6ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_1_6());
    }

    public Double getPerc_P7_1_7ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_1_7());
    }

    public Double getPerc_P7_1_8ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_1_8());
    }

    public Double getPerc_P7_1_9ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_1_9());
    }

    public Double getAverage_P7_2ForPresentation() {
        return getValueForPresentation(super.getAverage_P7_2());
    }

    public Double getStandardDeviation_P7_2ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P7_2());
    }

    public Double getPerc_P7_2_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_2_1());
    }

    public Double getPerc_P7_2_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_2_2());
    }

    public Double getPerc_P7_2_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_2_3());
    }

    public Double getPerc_P7_2_4ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_2_4());
    }

    public Double getPerc_P7_2_5ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_2_5());
    }

    public Double getPerc_P7_2_6ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_2_6());
    }

    public Double getPerc_P7_2_7ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_2_7());
    }

    public Double getPerc_P7_2_8ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_2_8());
    }

    public Double getPerc_P7_2_9ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_2_9());
    }

    public Double getAverage_P7_3ForPresentation() {
        return getValueForPresentation(super.getAverage_P7_3());
    }

    public Double getStandardDeviation_P7_3ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P7_3());
    }

    public Double getPerc_P7_3_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_3_1());
    }

    public Double getPerc_P7_3_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_3_2());
    }

    public Double getPerc_P7_3_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_3_3());
    }

    public Double getPerc_P7_3_4ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_3_4());
    }

    public Double getPerc_P7_3_5ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_3_5());
    }

    public Double getPerc_P7_3_6ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_3_6());
    }

    public Double getPerc_P7_3_7ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_3_7());
    }

    public Double getPerc_P7_3_8ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_3_8());
    }

    public Double getPerc_P7_3_9ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_3_9());
    }

    public Double getAverage_P7_4ForPresentation() {
        return getValueForPresentation(super.getAverage_P7_4());
    }

    public Double getStandardDeviation_P7_4ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P7_4());
    }

    public Double getPerc_P7_4_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_4_1());
    }

    public Double getPerc_P7_4_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_4_2());
    }

    public Double getPerc_P7_4_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_4_3());
    }

    public Double getPerc_P7_4_4ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_4_4());
    }

    public Double getPerc_P7_4_5ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_4_5());
    }

    public Double getPerc_P7_4_6ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_4_6());
    }

    public Double getPerc_P7_4_7ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_4_7());
    }

    public Double getPerc_P7_4_8ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_4_8());
    }

    public Double getPerc_P7_4_9ForPresentation() {
        return getValueForPresentation(super.getPerc_P7_4_9());
    }

    public Double getAverage_P8_1ForPresentation() {
        return getValueForPresentation(super.getAverage_P8_1());
    }

    public Double getStandardDeviation_P8_1ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P8_1());
    }

    public Double getPerc_P8_1_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P8_1_1());
    }

    public Double getPerc_P8_1_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P8_1_2());
    }

    public Double getPerc_P8_1_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P8_1_3());
    }

    public Double getPerc_P8_1_4ForPresentation() {
        return getValueForPresentation(super.getPerc_P8_1_4());
    }

    public Double getPerc_P8_1_5ForPresentation() {
        return getValueForPresentation(super.getPerc_P8_1_5());
    }

    public Double getPerc_P8_1_6ForPresentation() {
        return getValueForPresentation(super.getPerc_P8_1_6());
    }

    public Double getPerc_P8_1_7ForPresentation() {
        return getValueForPresentation(super.getPerc_P8_1_7());
    }

    public Double getPerc_P8_1_8ForPresentation() {
        return getValueForPresentation(super.getPerc_P8_1_8());
    }

    public Double getPerc_P8_1_9ForPresentation() {
        return getValueForPresentation(super.getPerc_P8_1_9());
    }

    public Double getAverage_P8_2ForPresentation() {
        return getValueForPresentation(super.getAverage_P8_2());
    }

    public Double getStandardDeviation_P8_2ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P8_2());
    }

    public Double getPerc_P8_2_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P8_2_1());
    }

    public Double getPerc_P8_2_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P8_2_2());
    }

    public Double getPerc_P8_2_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P8_2_3());
    }

    public Double getPerc_P8_2_4ForPresentation() {
        return getValueForPresentation(super.getPerc_P8_2_4());
    }

    public Double getPerc_P8_2_5ForPresentation() {
        return getValueForPresentation(super.getPerc_P8_2_5());
    }

    public Double getPerc_P8_2_6ForPresentation() {
        return getValueForPresentation(super.getPerc_P8_2_6());
    }

    public Double getPerc_P8_2_7ForPresentation() {
        return getValueForPresentation(super.getPerc_P8_2_7());
    }

    public Double getPerc_P8_2_8ForPresentation() {
        return getValueForPresentation(super.getPerc_P8_2_8());
    }

    public Double getPerc_P8_2_9ForPresentation() {
        return getValueForPresentation(super.getPerc_P8_2_9());
    }

    public Double getAverage_P9ForPresentation() {
        return getValueForPresentation(super.getAverage_P9());
    }

    public Double getStandardDeviation_P9ForPresentation() {
        return getValueForPresentation(super.getStandardDeviation_P9());
    }

    public Double getPerc_P9_1ForPresentation() {
        return getValueForPresentation(super.getPerc_P9_1());
    }

    public Double getPerc_P9_2ForPresentation() {
        return getValueForPresentation(super.getPerc_P9_2());
    }

    public Double getPerc_P9_3ForPresentation() {
        return getValueForPresentation(super.getPerc_P9_3());
    }

    public Map<String, String> getValuesMap() {
        final Map<String, String> tmpMap = new HashMap<String, String>();
        if (!StringUtils.isEmpty(getHeaders()) && !StringUtils.isEmpty(getRawValues())) {
            String[] headers = getHeaders().split("\t");
            String[] values = getRawValues().split("\t");
            for (int i = 0; i < values.length; i++) {
                tmpMap.put(headers[i], values[i]);
            }
        }
        return tmpMap;
    }

    private static int getHeaderIndex(String headerToFind, String[] headersSplitted) {
        for (int i = 0; i < headersSplitted.length; i++) {
            if (headerToFind.equals(headersSplitted[i])) {
                return i;
            }
        }
        throw new DomainException("error.StudentInquiriesTeachingResult.headerNotFound", headerToFind);
    }

    private static Boolean fieldToBoolean(String field) {
        return !StringUtils.isEmpty(field) && (field.equalsIgnoreCase("true") || field.equals("1"));
    }

    public boolean isUnsatisfactory() {
        return getUnsatisfactoryResultsAssiduity() || getUnsatisfactoryResultsPedagogicalCapacity()
                || getUnsatisfactoryResultsPresencialLearning() || getUnsatisfactoryResultsStudentInteraction();
    }

    public boolean isExcellent() {
        return getExcellentResultsAssiduity() || getExcellentResultsPedagogicalCapacity()
                || getExcellentResultsPresencialLearning() || getExcellentResultsStudentInteraction();
    }

    @Override
    public Boolean getUnsatisfactoryResultsAssiduity() {
        return super.getUnsatisfactoryResultsAssiduity() != null && super.getUnsatisfactoryResultsAssiduity();
    }

    @Override
    public Boolean getUnsatisfactoryResultsAuditable() {
        return super.getUnsatisfactoryResultsAuditable() != null && super.getUnsatisfactoryResultsAuditable();
    }

    @Override
    public Boolean getUnsatisfactoryResultsPedagogicalCapacity() {
        return super.getUnsatisfactoryResultsPedagogicalCapacity() != null && super.getUnsatisfactoryResultsPedagogicalCapacity();
    }

    @Override
    public Boolean getUnsatisfactoryResultsPresencialLearning() {
        return super.getUnsatisfactoryResultsPresencialLearning() != null && super.getUnsatisfactoryResultsPresencialLearning();
    }

    @Override
    public Boolean getUnsatisfactoryResultsStudentInteraction() {
        return super.getUnsatisfactoryResultsStudentInteraction() != null && super.getUnsatisfactoryResultsStudentInteraction();
    }

    @Override
    public Boolean getInternalDegreeDisclosure() {
        return super.getInternalDegreeDisclosure() != null && super.getInternalDegreeDisclosure();
    }

    public Boolean getExcellentResultsAssiduity() {
        return fieldToBoolean(getValuesMap().get("Res_excelentes_assiduidade"));
    }

    public Boolean getExcellentResultsPedagogicalCapacity() {
        return fieldToBoolean(getValuesMap().get("Res_excelentes_cap_pedag"));
    }

    public Boolean getExcellentResultsPresencialLearning() {
        return fieldToBoolean(getValuesMap().get("Res_excelentes_prov_aprend_pres"));
    }

    public Boolean getExcellentResultsStudentInteraction() {
        return fieldToBoolean(getValuesMap().get("Res_excelentes_int_alunos"));
    }

    @Override
    public Boolean getPublicDegreeDisclosure() {
        if (super.getPublicDegreeDisclosure() != null) {
            return super.getPublicDegreeDisclosure();
        }
        return getValuesMap().containsKey("Repres_doc_curso_UC_publica") ? fieldToBoolean(getValuesMap().get(
                "Repres_doc_curso_UC_publica")) : false;
    }

    @Atomic
    public static void importResults(String headers, String values, UploadStudentInquiriesTeachingResultsBean resultsBean) {

        String[] headersSplitted = headers.split("\t");

        int executionCourseHeaderIndex = getHeaderIndex(resultsBean.getKeyExecutionCourseHeader(), headersSplitted);
        int executionDegreeHeaderIndex = getHeaderIndex(resultsBean.getKeyExecutionDegreeHeader(), headersSplitted);
        int professorshipHeaderIndex = getHeaderIndex(resultsBean.getKeyTeacherHeader(), headersSplitted);
        int shiftTypeHeaderIndex = getHeaderIndex(resultsBean.getShiftTypeHeader(), headersSplitted);

        int unsatisfactoryResultsAssiduityIndex =
                getHeaderIndex(resultsBean.getUnsatisfactoryResultsAssiduityHeader(), headersSplitted);
        int unsatisfactoryResultsPedagogicalCapacityIndex =
                getHeaderIndex(resultsBean.getUnsatisfactoryResultsPedagogicalCapacityHeader(), headersSplitted);
        int unsatisfactoryResultsPresencialLearningIndex =
                getHeaderIndex(resultsBean.getUnsatisfactoryResultsPresencialLearningHeader(), headersSplitted);
        int unsatisfactoryResultsStudentInteractionIndex =
                getHeaderIndex(resultsBean.getUnsatisfactoryResultsStudentInteractionHeader(), headersSplitted);
        int internalDegreeDisclosureIndex = getHeaderIndex(resultsBean.getInternalDegreeDisclosureHeader(), headersSplitted);

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

            Professorship professorship = FenixFramework.getDomainObject(columns[professorshipHeaderIndex]);
            if (professorship == null) {
                throw new DomainException("error.StudentInquiriesCourseResult.professorshipNotFound",
                        columns[professorshipHeaderIndex], columns[executionCourseHeaderIndex]);
            }

            final ShiftType shiftType = ShiftType.valueOf(columns[shiftTypeHeaderIndex]);
            StudentInquiriesTeachingResult studentInquiriesTeachingResult =
                    professorship.getStudentInquiriesTeachingResult(executionDegree, shiftType);

            if (studentInquiriesTeachingResult == null) {
                studentInquiriesTeachingResult = new StudentInquiriesTeachingResult();
                studentInquiriesTeachingResult.setShiftType(shiftType);
                studentInquiriesTeachingResult.setExecutionDegree(executionDegree);
                studentInquiriesTeachingResult.setProfessorship(professorship);
            }

            studentInquiriesTeachingResult.setRawValues(row);
            studentInquiriesTeachingResult.setHeaders(headers);
            studentInquiriesTeachingResult.setUploadDateTime(new DateTime());
            studentInquiriesTeachingResult.setResultsDate(resultsBean.getResultsDate());

            studentInquiriesTeachingResult
                    .setUnsatisfactoryResultsAssiduity(fieldToBoolean(columns[unsatisfactoryResultsAssiduityIndex]));
            studentInquiriesTeachingResult
                    .setUnsatisfactoryResultsPedagogicalCapacity(fieldToBoolean(columns[unsatisfactoryResultsPedagogicalCapacityIndex]));
            studentInquiriesTeachingResult
                    .setUnsatisfactoryResultsPresencialLearning(fieldToBoolean(columns[unsatisfactoryResultsPresencialLearningIndex]));
            studentInquiriesTeachingResult
                    .setUnsatisfactoryResultsStudentInteraction(fieldToBoolean(columns[unsatisfactoryResultsStudentInteractionIndex]));
            studentInquiriesTeachingResult.setInternalDegreeDisclosure(fieldToBoolean(columns[internalDegreeDisclosureIndex]));

        }
    }

    @Atomic
    public void delete() {
        setExecutionDegree(null);
        setProfessorship(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Atomic
    public static Boolean deleteTeachingResults(UploadStudentInquiriesTeachingResultsBean teachingBean) {
        boolean deletedItems = false;
        List<StudentInquiriesTeachingResult> toDelete = new ArrayList<StudentInquiriesTeachingResult>();
        for (StudentInquiriesTeachingResult teachingResult : Bennu.getInstance().getStudentInquiriesTeachingResultsSet()) {
            if (StringUtils.isEmpty(teachingBean.getKeyExecutionCourseHeader())) {
                if (teachingBean.getResultsDate().equals(teachingResult.getResultsDate())) {
                    toDelete.add(teachingResult);
                    deletedItems = true;
                }
            } else {
                ExecutionCourse executionCourse = FenixFramework.getDomainObject(teachingBean.getKeyExecutionCourseHeader());
                if (executionCourse == null) {
                    throw new DomainException("error.StudentInquiriesCourseResult.executionCourseNotFound",
                            teachingBean.getKeyExecutionCourseHeader());
                }
                if (executionCourse != null && teachingResult.getProfessorship().getExecutionCourse() == executionCourse) {
                    toDelete.add(teachingResult);
                    deletedItems = true;
                }
            }
        }
        for (StudentInquiriesTeachingResult teachingResult : toDelete) {
            teachingResult.delete();
        }
        return deletedItems;
    }

    public void resetValues() {
        setAverage_P6_1(null);
        setAverage_P6_2(null);
        setAverage_P6_3(null);
        setAverage_P7_1(null);
        setAverage_P7_2(null);
        setAverage_P7_3(null);
        setAverage_P7_4(null);
        setAverage_P8_1(null);
        setAverage_P8_2(null);
        setAverage_P9(null);
        setNumber_P6_1(null);
        setNumber_P6_1_values(null);
        setNumber_P6_2(null);
        setNumber_P6_3(null);
        setNumber_P7_1(null);
        setNumber_P7_2(null);
        setNumber_P7_3(null);
        setNumber_P7_4(null);
        setNumber_P8_1(null);
        setNumber_P8_2(null);
        setNumber_P9(null);
        setNumberOfAnswers(null);
        setP6_1_1_a(null);
        setP6_1_1_b(null);
        setP6_1_1_c(null);
        setP6_1_1_d(null);
        setP6_1_1_e(null);
        setP6_1_greater_3(null);
        setPerc_P6_1_1(null);
        setPerc_P6_1_2(null);
        setPerc_P6_1_3(null);
        setPerc_P6_1_4(null);
        setPerc_P6_1_5(null);
        setPerc_P6_1_6(null);
        setPerc_P6_1_7(null);
        setPerc_P6_1_a(null);
        setPerc_P6_1_b(null);
        setPerc_P6_1_c(null);
        setPerc_P6_1_d(null);
        setPerc_P6_1_e(null);
        setPerc_P6_2_1(null);
        setPerc_P6_2_2(null);
        setPerc_P6_2_3(null);
        setPerc_P6_2_4(null);
        setPerc_P6_2_5(null);
        setPerc_P6_2_6(null);
        setPerc_P6_2_7(null);
        setPerc_P6_2_8(null);
        setPerc_P6_2_9(null);
        setPerc_P6_3_1(null);
        setPerc_P6_3_2(null);
        setPerc_P6_3_3(null);
        setPerc_P6_3_4(null);
        setPerc_P6_3_5(null);
        setPerc_P6_3_6(null);
        setPerc_P6_3_7(null);
        setPerc_P7_1_1(null);
        setPerc_P7_1_2(null);
        setPerc_P7_1_3(null);
        setPerc_P7_1_4(null);
        setPerc_P7_1_5(null);
        setPerc_P7_1_6(null);
        setPerc_P7_1_7(null);
        setPerc_P7_1_8(null);
        setPerc_P7_1_9(null);
        setPerc_P7_2_1(null);
        setPerc_P7_2_2(null);
        setPerc_P7_2_3(null);
        setPerc_P7_2_4(null);
        setPerc_P7_2_5(null);
        setPerc_P7_2_6(null);
        setPerc_P7_2_7(null);
        setPerc_P7_2_8(null);
        setPerc_P7_2_9(null);
        setPerc_P7_3_1(null);
        setPerc_P7_3_2(null);
        setPerc_P7_3_3(null);
        setPerc_P7_3_4(null);
        setPerc_P7_3_5(null);
        setPerc_P7_3_6(null);
        setPerc_P7_3_7(null);
        setPerc_P7_3_8(null);
        setPerc_P7_3_9(null);
        setPerc_P7_4_1(null);
        setPerc_P7_4_2(null);
        setPerc_P7_4_3(null);
        setPerc_P7_4_4(null);
        setPerc_P7_4_5(null);
        setPerc_P7_4_6(null);
        setPerc_P7_4_7(null);
        setPerc_P7_4_8(null);
        setPerc_P7_4_9(null);
        setPerc_P8_1_1(null);
        setPerc_P8_1_2(null);
        setPerc_P8_1_3(null);
        setPerc_P8_1_4(null);
        setPerc_P8_1_5(null);
        setPerc_P8_1_6(null);
        setPerc_P8_1_7(null);
        setPerc_P8_1_8(null);
        setPerc_P8_1_9(null);
        setPerc_P8_2_1(null);
        setPerc_P8_2_2(null);
        setPerc_P8_2_3(null);
        setPerc_P8_2_4(null);
        setPerc_P8_2_5(null);
        setPerc_P8_2_6(null);
        setPerc_P8_2_7(null);
        setPerc_P8_2_8(null);
        setPerc_P8_2_9(null);
        setPerc_P9_1(null);
        setPerc_P9_2(null);
        setPerc_P9_3(null);
        setRawValues(null);
        setStandardDeviation_P6_1(null);
        setStandardDeviation_P6_2(null);
        setStandardDeviation_P6_3(null);
        setStandardDeviation_P7_1(null);
        setStandardDeviation_P7_2(null);
        setStandardDeviation_P7_3(null);
        setStandardDeviation_P7_4(null);
        setStandardDeviation_P8_1(null);
        setStandardDeviation_P8_2(null);
        setStandardDeviation_P9(null);
        setUnsatisfactoryResultsAssiduity(false);
        setUnsatisfactoryResultsAuditable(false);
        setUnsatisfactoryResultsPedagogicalCapacity(false);
        setUnsatisfactoryResultsPresencialLearning(false);
        setUnsatisfactoryResultsStudentInteraction(false);
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

    @Deprecated
    public boolean hasPublicDisclosure() {
        return getPublicDisclosure() != null;
    }

    @Deprecated
    public boolean hasNumberOfAnswers() {
        return getNumberOfAnswers() != null;
    }

    @Deprecated
    public boolean hasStandardDeviation_P9() {
        return getStandardDeviation_P9() != null;
    }

    @Deprecated
    public boolean hasInternalDegreeDisclosure() {
        return getInternalDegreeDisclosure() != null;
    }

    @Deprecated
    public boolean hasUnsatisfactoryResultsStudentInteraction() {
        return getUnsatisfactoryResultsStudentInteraction() != null;
    }

    @Deprecated
    public boolean hasExecutionDegree() {
        return getExecutionDegree() != null;
    }

    @Deprecated
    public boolean hasNumber_P6_1_values() {
        return getNumber_P6_1_values() != null;
    }

    @Deprecated
    public boolean hasPerc_P8_1_6() {
        return getPerc_P8_1_6() != null;
    }

    @Deprecated
    public boolean hasHeaders() {
        return getHeaders() != null;
    }

    @Deprecated
    public boolean hasPerc_P8_1_7() {
        return getPerc_P8_1_7() != null;
    }

    @Deprecated
    public boolean hasP6_1_1_e() {
        return getP6_1_1_e() != null;
    }

    @Deprecated
    public boolean hasPerc_P8_1_4() {
        return getPerc_P8_1_4() != null;
    }

    @Deprecated
    public boolean hasPerc_P8_1_5() {
        return getPerc_P8_1_5() != null;
    }

    @Deprecated
    public boolean hasPerc_P8_1_8() {
        return getPerc_P8_1_8() != null;
    }

    @Deprecated
    public boolean hasPerc_P8_1_9() {
        return getPerc_P8_1_9() != null;
    }

    @Deprecated
    public boolean hasPerc_P8_1_2() {
        return getPerc_P8_1_2() != null;
    }

    @Deprecated
    public boolean hasPerc_P8_1_3() {
        return getPerc_P8_1_3() != null;
    }

    @Deprecated
    public boolean hasPerc_P8_1_1() {
        return getPerc_P8_1_1() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_4_6() {
        return getPerc_P7_4_6() != null;
    }

    @Deprecated
    public boolean hasNumber_P9() {
        return getNumber_P9() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_4_7() {
        return getPerc_P7_4_7() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_4_8() {
        return getPerc_P7_4_8() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_4_9() {
        return getPerc_P7_4_9() != null;
    }

    @Deprecated
    public boolean hasAverage_P8_2() {
        return getAverage_P8_2() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_4_2() {
        return getPerc_P7_4_2() != null;
    }

    @Deprecated
    public boolean hasAverage_P8_1() {
        return getAverage_P8_1() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_4_3() {
        return getPerc_P7_4_3() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_4_4() {
        return getPerc_P7_4_4() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_4_5() {
        return getPerc_P7_4_5() != null;
    }

    @Deprecated
    public boolean hasAverage_P7_3() {
        return getAverage_P7_3() != null;
    }

    @Deprecated
    public boolean hasAverage_P7_2() {
        return getAverage_P7_2() != null;
    }

    @Deprecated
    public boolean hasInternalDisclosure() {
        return getInternalDisclosure() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_4_1() {
        return getPerc_P7_4_1() != null;
    }

    @Deprecated
    public boolean hasAverage_P7_4() {
        return getAverage_P7_4() != null;
    }

    @Deprecated
    public boolean hasP6_1_1_a() {
        return getP6_1_1_a() != null;
    }

    @Deprecated
    public boolean hasP6_1_1_b() {
        return getP6_1_1_b() != null;
    }

    @Deprecated
    public boolean hasP6_1_1_c() {
        return getP6_1_1_c() != null;
    }

    @Deprecated
    public boolean hasAverage_P7_1() {
        return getAverage_P7_1() != null;
    }

    @Deprecated
    public boolean hasP6_1_1_d() {
        return getP6_1_1_d() != null;
    }

    @Deprecated
    public boolean hasP6_1_greater_3() {
        return getP6_1_greater_3() != null;
    }

    @Deprecated
    public boolean hasUnsatisfactoryResultsAuditable() {
        return getUnsatisfactoryResultsAuditable() != null;
    }

    @Deprecated
    public boolean hasAverage_P6_2() {
        return getAverage_P6_2() != null;
    }

    @Deprecated
    public boolean hasAverage_P6_1() {
        return getAverage_P6_1() != null;
    }

    @Deprecated
    public boolean hasAverage_P6_3() {
        return getAverage_P6_3() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_2_1() {
        return getPerc_P6_2_1() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_3_4() {
        return getPerc_P6_3_4() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_2_2() {
        return getPerc_P6_2_2() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_3_5() {
        return getPerc_P6_3_5() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_2_3() {
        return getPerc_P6_2_3() != null;
    }

    @Deprecated
    public boolean hasUploadDateTime() {
        return getUploadDateTime() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_3_6() {
        return getPerc_P6_3_6() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_2_4() {
        return getPerc_P6_2_4() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_3_7() {
        return getPerc_P6_3_7() != null;
    }

    @Deprecated
    public boolean hasNumber_P6_2() {
        return getNumber_P6_2() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_3_8() {
        return getPerc_P6_3_8() != null;
    }

    @Deprecated
    public boolean hasNumber_P6_1() {
        return getNumber_P6_1() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_3_9() {
        return getPerc_P6_3_9() != null;
    }

    @Deprecated
    public boolean hasUnsatisfactoryResultsPresencialLearning() {
        return getUnsatisfactoryResultsPresencialLearning() != null;
    }

    @Deprecated
    public boolean hasPublicDegreeDisclosure() {
        return getPublicDegreeDisclosure() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_2_9() {
        return getPerc_P6_2_9() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_2_5() {
        return getPerc_P6_2_5() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_2_6() {
        return getPerc_P6_2_6() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_3_1() {
        return getPerc_P6_3_1() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_2_7() {
        return getPerc_P6_2_7() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_3_2() {
        return getPerc_P6_3_2() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_2_8() {
        return getPerc_P6_2_8() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_3_3() {
        return getPerc_P6_3_3() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_1_e() {
        return getPerc_P6_1_e() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_1_d() {
        return getPerc_P6_1_d() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_1_c() {
        return getPerc_P6_1_c() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_1_b() {
        return getPerc_P6_1_b() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_1_a() {
        return getPerc_P6_1_a() != null;
    }

    @Deprecated
    public boolean hasNumber_P6_3() {
        return getNumber_P6_3() != null;
    }

    @Deprecated
    public boolean hasResultsDate() {
        return getResultsDate() != null;
    }

    @Deprecated
    public boolean hasUnsatisfactoryResultsPedagogicalCapacity() {
        return getUnsatisfactoryResultsPedagogicalCapacity() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_2_7() {
        return getPerc_P7_2_7() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_3_2() {
        return getPerc_P7_3_2() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_2_6() {
        return getPerc_P7_2_6() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_3_1() {
        return getPerc_P7_3_1() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_2_5() {
        return getPerc_P7_2_5() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_2_4() {
        return getPerc_P7_2_4() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_2_9() {
        return getPerc_P7_2_9() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_2_8() {
        return getPerc_P7_2_8() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_3_9() {
        return getPerc_P7_3_9() != null;
    }

    @Deprecated
    public boolean hasAverage_P9() {
        return getAverage_P9() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_3_8() {
        return getPerc_P7_3_8() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_3_7() {
        return getPerc_P7_3_7() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_2_3() {
        return getPerc_P7_2_3() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_3_6() {
        return getPerc_P7_3_6() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_2_2() {
        return getPerc_P7_2_2() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_3_5() {
        return getPerc_P7_3_5() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_2_1() {
        return getPerc_P7_2_1() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_3_4() {
        return getPerc_P7_3_4() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_3_3() {
        return getPerc_P7_3_3() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_1_9() {
        return getPerc_P7_1_9() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_1_7() {
        return getPerc_P7_1_7() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_1_8() {
        return getPerc_P7_1_8() != null;
    }

    @Deprecated
    public boolean hasPerc_P8_2_2() {
        return getPerc_P8_2_2() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_1_5() {
        return getPerc_P7_1_5() != null;
    }

    @Deprecated
    public boolean hasPerc_P8_2_1() {
        return getPerc_P8_2_1() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_1_6() {
        return getPerc_P7_1_6() != null;
    }

    @Deprecated
    public boolean hasPerc_P8_2_4() {
        return getPerc_P8_2_4() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_1_3() {
        return getPerc_P7_1_3() != null;
    }

    @Deprecated
    public boolean hasPerc_P8_2_3() {
        return getPerc_P8_2_3() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_1_4() {
        return getPerc_P7_1_4() != null;
    }

    @Deprecated
    public boolean hasPerc_P8_2_6() {
        return getPerc_P8_2_6() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_1_1() {
        return getPerc_P7_1_1() != null;
    }

    @Deprecated
    public boolean hasPerc_P8_2_5() {
        return getPerc_P8_2_5() != null;
    }

    @Deprecated
    public boolean hasPerc_P7_1_2() {
        return getPerc_P7_1_2() != null;
    }

    @Deprecated
    public boolean hasPerc_P8_2_8() {
        return getPerc_P8_2_8() != null;
    }

    @Deprecated
    public boolean hasPerc_P8_2_7() {
        return getPerc_P8_2_7() != null;
    }

    @Deprecated
    public boolean hasPerc_P8_2_9() {
        return getPerc_P8_2_9() != null;
    }

    @Deprecated
    public boolean hasStandardDeviation_P6_1() {
        return getStandardDeviation_P6_1() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasStandardDeviation_P6_2() {
        return getStandardDeviation_P6_2() != null;
    }

    @Deprecated
    public boolean hasStandardDeviation_P6_3() {
        return getStandardDeviation_P6_3() != null;
    }

    @Deprecated
    public boolean hasStandardDeviation_P7_3() {
        return getStandardDeviation_P7_3() != null;
    }

    @Deprecated
    public boolean hasStandardDeviation_P7_4() {
        return getStandardDeviation_P7_4() != null;
    }

    @Deprecated
    public boolean hasStandardDeviation_P7_1() {
        return getStandardDeviation_P7_1() != null;
    }

    @Deprecated
    public boolean hasStandardDeviation_P7_2() {
        return getStandardDeviation_P7_2() != null;
    }

    @Deprecated
    public boolean hasRawValues() {
        return getRawValues() != null;
    }

    @Deprecated
    public boolean hasNumber_P7_1() {
        return getNumber_P7_1() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_1_1() {
        return getPerc_P6_1_1() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_1_2() {
        return getPerc_P6_1_2() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_1_3() {
        return getPerc_P6_1_3() != null;
    }

    @Deprecated
    public boolean hasProfessorship() {
        return getProfessorship() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_1_4() {
        return getPerc_P6_1_4() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_1_5() {
        return getPerc_P6_1_5() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_1_6() {
        return getPerc_P6_1_6() != null;
    }

    @Deprecated
    public boolean hasShiftType() {
        return getShiftType() != null;
    }

    @Deprecated
    public boolean hasPerc_P6_1_7() {
        return getPerc_P6_1_7() != null;
    }

    @Deprecated
    public boolean hasUnsatisfactoryResultsAssiduity() {
        return getUnsatisfactoryResultsAssiduity() != null;
    }

    @Deprecated
    public boolean hasStandardDeviation_P8_1() {
        return getStandardDeviation_P8_1() != null;
    }

    @Deprecated
    public boolean hasStandardDeviation_P8_2() {
        return getStandardDeviation_P8_2() != null;
    }

    @Deprecated
    public boolean hasNumber_P8_1() {
        return getNumber_P8_1() != null;
    }

    @Deprecated
    public boolean hasNumber_P8_2() {
        return getNumber_P8_2() != null;
    }

    @Deprecated
    public boolean hasPerc_P9_3() {
        return getPerc_P9_3() != null;
    }

    @Deprecated
    public boolean hasPerc_P9_2() {
        return getPerc_P9_2() != null;
    }

    @Deprecated
    public boolean hasPerc_P9_1() {
        return getPerc_P9_1() != null;
    }

    @Deprecated
    public boolean hasNumber_P7_4() {
        return getNumber_P7_4() != null;
    }

    @Deprecated
    public boolean hasNumber_P7_2() {
        return getNumber_P7_2() != null;
    }

    @Deprecated
    public boolean hasNumber_P7_3() {
        return getNumber_P7_3() != null;
    }

}
