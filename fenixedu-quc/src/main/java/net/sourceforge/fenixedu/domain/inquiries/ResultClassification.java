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
package org.fenixedu.academic.domain.inquiries;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;

public enum ResultClassification {

    RED(true, true), YELLOW(true, true), GREEN(true, false), CYAN(true, false), BLUE(true, false), PURPLE(true, true), GREY(
            false, false);

    private boolean relevantResult;
    private boolean mandatoryComment;

    private ResultClassification(boolean relevantResult, boolean mandatoryComment) {
        setRelevantResult(relevantResult);
        setMandatoryComment(mandatoryComment);
    }

    public boolean isRelevantResult() {
        return relevantResult;
    }

    public void setRelevantResult(boolean relevantResult) {
        this.relevantResult = relevantResult;
    }

    public void setMandatoryComment(boolean mandatoryComment) {
        this.mandatoryComment = mandatoryComment;
    }

    public boolean isMandatoryComment() {
        return mandatoryComment;
    }

    public static ResultClassification getForAudit(ExecutionCourse executionCourse, ExecutionDegree executionDegree) {
        for (InquiryResult inquiryResult : executionCourse.getInquiryResultsSet()) {
            if (inquiryResult.getExecutionDegree() == executionDegree
                    && InquiryResultType.AUDIT.equals(inquiryResult.getResultType())) {
                return inquiryResult.getResultClassification();
            }
        }
        return null;
    }
}
