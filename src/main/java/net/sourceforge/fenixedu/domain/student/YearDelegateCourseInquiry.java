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
package net.sourceforge.fenixedu.domain.student;

import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InquiriesQuestion;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.YearDelegateCourseInquiryDTO;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class YearDelegateCourseInquiry extends YearDelegateCourseInquiry_Base {

    public YearDelegateCourseInquiry() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setResponseDateTime(new DateTime());
    }

    private static void setAnswers(final YearDelegateCourseInquiryDTO inquiryDTO, final YearDelegateCourseInquiry inquiry) {
        Map<String, InquiriesQuestion> answersMap = inquiryDTO.buildAnswersMap(false);
        inquiry.setWorkLoadClassification(answersMap.get("workLoadClassification").getValueAsInteger());
        inquiry.setWorkLoadClassificationReasons(answersMap.get("workLoadClassificationReasons").getValue());
        inquiry.setEnoughOnlineCUInformation(answersMap.get("enoughOnlineCUInformation").getValueAsInteger());
        inquiry.setEnoughOnlineCUInformationReasons(answersMap.get("enoughOnlineCUInformationReasons").getValue());
        inquiry.setClearOnlineCUInformation(answersMap.get("clearOnlineCUInformation").getValueAsInteger());
        inquiry.setClearOnlineCUInformationReasons(answersMap.get("clearOnlineCUInformationReasons").getValue());
        inquiry.setExplicitEvaluationMethods(answersMap.get("explicitEvaluationMethods").getValueAsInteger());
        inquiry.setExplicitEvaluationMethodsReasons(answersMap.get("explicitEvaluationMethodsReasons").getValue());
        inquiry.setEvaluationMethodsWellApplied(answersMap.get("evaluationMethodsWellApplied").getValueAsInteger());
        inquiry.setEvaluationMethodsWellAppliedReasons(answersMap.get("evaluationMethodsWellAppliedReasons").getValue());
        inquiry.setEvaluationMethodsDisclosedToWorkingStudents(answersMap.get("evaluationMethodsDisclosedToWorkingStudents")
                .getValue());
        inquiry.setEvaluationMethodsDisclosedToSpecialSeasonStudents(answersMap.get(
                "evaluationMethodsDisclosedToSpecialSeasonStudents").getValue());
        inquiry.setEvaluationDatesScheduleActiveParticipation(answersMap.get("evaluationDatesScheduleActiveParticipation")
                .getValueAsInteger());
        inquiry.setEvaluationDatesScheduleActiveParticipationReasons(answersMap.get(
                "evaluationDatesScheduleActiveParticipationReasons").getValue());
        inquiry.setSupportMaterialAvailableOnTime(answersMap.get("supportMaterialAvailableOnTime").getValueAsInteger());
        inquiry.setSupportMaterialAvailableOnTimeReasons(answersMap.get("supportMaterialAvailableOnTimeReasons").getValue());
        inquiry.setPreviousKnowlegdeArticulation(answersMap.get("previousKnowlegdeArticulation").getValueAsInteger());
        inquiry.setPreviousKnowlegdeArticulationReasons(answersMap.get("previousKnowlegdeArticulationReasons").getValue());
        inquiry.setSuggestedBestPractices(answersMap.get("suggestedBestPractices").getValue());
        inquiry.setStrongAndWeakPointsOfCUTeachingProcess(answersMap.get("strongAndWeakPointsOfCUTeachingProcess").getValue());
        inquiry.setFinalCommentsAndImproovements(answersMap.get("finalCommentsAndImproovements").getValue());
        inquiry.setReportDisclosureAuthorization(answersMap.get("reportDisclosureAuthorization").getValue());
    }

    @Atomic
    public static YearDelegateCourseInquiry makeNew(final YearDelegateCourseInquiryDTO inquiryDTO) {

        YearDelegateCourseInquiry inquiry = new YearDelegateCourseInquiry();
        inquiry.setAnswerDuration(inquiryDTO.getAnswerDuration());
        inquiry.setExecutionCourse(inquiryDTO.getExecutionCourse());
        inquiry.setDelegate(inquiryDTO.getDelegate());

        setAnswers(inquiryDTO, inquiry);

        return inquiry;
    }

    @Deprecated
    public java.util.Date getResponse() {
        org.joda.time.DateTime dt = getResponseDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setResponse(java.util.Date date) {
        if (date == null) {
            setResponseDateTime(null);
        } else {
            setResponseDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

}
