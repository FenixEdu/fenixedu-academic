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
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public abstract class InquiryTemplate extends InquiryTemplate_Base {

    protected void init(DateTime begin, DateTime end) {
        validateInquiryPeriod(begin, end);
        setResponsePeriodBegin(begin);
        setResponsePeriodEnd(end);
        setRootDomainObject(Bennu.getInstance());
    }

    protected void validateInquiryPeriod(DateTime begin, DateTime end) {
        if (begin == null || begin.isAfter(end)) {
            throw new DomainException("error.invalid.period.defined");
        }
    }

    public boolean isOpen() {
        return !getResponsePeriodBegin().isAfterNow()
                && (getResponsePeriodEnd() == null || !getResponsePeriodEnd().isBeforeNow());
    }

    public int getNumberOfQuestions() {
        int count = 0;
        for (InquiryBlock inquiryBlock : getInquiryBlocks()) {
            for (InquiryGroupQuestion groupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
                if (groupQuestion.isCheckbox()) {
                    count++;
                } else {
                    count += groupQuestion.getInquiryQuestionsSet().size();
                }
            }
        }
        return count;
    }

    public int getNumberOfRequiredQuestions() {
        int count = 0;
        for (InquiryBlock inquiryBlock : getInquiryBlocks()) {
            for (InquiryGroupQuestion groupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
                if (groupQuestion.isCheckbox() && groupQuestion.getRequired()) {
                    count++;
                } else {
                    count += groupQuestion.getNumberOfMandatoryQuestions();
                }
            }
        }
        return count;
    }

    public static InquiryTemplate getInquiryTemplateByTypeAndExecutionSemester(ExecutionSemester executionSemester,
            InquiryResponsePeriodType type) {

        switch (type) {
        case STUDENT:
            return CurricularCourseInquiryTemplate.getTemplateByExecutionPeriod(executionSemester);
        case DELEGATE:
            return DelegateInquiryTemplate.getTemplateByExecutionPeriod(executionSemester);
        case TEACHING:
            return TeacherInquiryTemplate.getTemplateByExecutionPeriod(executionSemester);
        case REGENT:
            return RegentInquiryTemplate.getTemplateByExecutionPeriod(executionSemester);
        case COORDINATOR:
            return CoordinatorInquiryTemplate.getTemplateByExecutionPeriod(executionSemester);
        default:
            return null;
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryBlock> getInquiryBlocks() {
        return getInquiryBlocksSet();
    }

    @Deprecated
    public boolean hasAnyInquiryBlocks() {
        return !getInquiryBlocksSet().isEmpty();
    }

    @Deprecated
    public boolean hasInquiryMessage() {
        return getInquiryMessage() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasResponsePeriodBegin() {
        return getResponsePeriodBegin() != null;
    }

    @Deprecated
    public boolean hasResponsePeriodEnd() {
        return getResponsePeriodEnd() != null;
    }

}
