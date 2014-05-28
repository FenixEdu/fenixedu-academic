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

import org.fenixedu.bennu.core.domain.Bennu;

public class InquiryBlock extends InquiryBlock_Base {

    public InquiryBlock() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public boolean isResultBlock(ExecutionSemester executionSemester) {
        return getInquiry(executionSemester) instanceof ResultsInquiryTemplate;
    }

    public InquiryTemplate getInquiry(ExecutionSemester executionSemester) {
        for (InquiryTemplate inquiryTemplate : getInquiries()) {
            if (inquiryTemplate.getExecutionPeriod() == executionSemester) {
                return inquiryTemplate;
            }
        }
        return null;
    }

    public void delete() {
        for (; !getInquiryGroupsQuestions().isEmpty(); getInquiryGroupsQuestions().iterator().next().delete()) {
            setRootDomainObject(null);
        }
        if (hasInquiryQuestionHeader()) {
            getInquiryQuestionHeader().delete();
        }
        setResultQuestion(null);
        setGroupResultQuestion(null);
        for (InquiryTemplate inquiryTemplate : getInquiries()) {
            removeInquiries(inquiryTemplate);
        }
        super.deleteDomainObject();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryTemplate> getInquiries() {
        return getInquiriesSet();
    }

    @Deprecated
    public boolean hasAnyInquiries() {
        return !getInquiriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryGroupQuestion> getInquiryGroupsQuestions() {
        return getInquiryGroupsQuestionsSet();
    }

    @Deprecated
    public boolean hasAnyInquiryGroupsQuestions() {
        return !getInquiryGroupsQuestionsSet().isEmpty();
    }

    @Deprecated
    public boolean hasGroupResultQuestion() {
        return getGroupResultQuestion() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasResultQuestion() {
        return getResultQuestion() != null;
    }

    @Deprecated
    public boolean hasBlockOrder() {
        return getBlockOrder() != null;
    }

    @Deprecated
    public boolean hasInquiryQuestionHeader() {
        return getInquiryQuestionHeader() != null;
    }

}
