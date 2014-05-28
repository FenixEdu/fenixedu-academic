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

import java.util.Collection;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class CoordinatorInquiryTemplate extends CoordinatorInquiryTemplate_Base {

    public CoordinatorInquiryTemplate(DateTime begin, DateTime end, Boolean shared) {
        super();
        init(begin, end);
        setShared(shared);
    }

    public static CoordinatorInquiryTemplate getTemplateByExecutionPeriod(ExecutionSemester executionSemester) {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof CoordinatorInquiryTemplate
                    && executionSemester == inquiryTemplate.getExecutionPeriod()) {
                return (CoordinatorInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }

    public static CoordinatorInquiryTemplate getCurrentTemplate() {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof CoordinatorInquiryTemplate && inquiryTemplate.isOpen()) {
                return (CoordinatorInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }

    public void delete() {
        canBeDeleted();
        for (InquiryBlock inquiryBlock : getInquiryBlocksSet()) {
            removeInquiryBlocks(inquiryBlock);
        }
        setExecutionPeriod(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    private void canBeDeleted() {
        for (InquiryBlock inquiryBlock : getInquiryBlocks()) {
            for (InquiryGroupQuestion groupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
                for (InquiryQuestion inquiryQuestion : groupQuestion.getInquiryQuestions()) {
                    for (QuestionAnswer questionAnswer : inquiryQuestion.getQuestionAnswers()) {
                        InquiryCoordinatorAnswer coordinatorAnswer = (InquiryCoordinatorAnswer) questionAnswer.getInquiryAnswer();
                        if (coordinatorAnswer.getExecutionSemester() == getExecutionPeriod()) {
                            throw new DomainException("error.CoordinatorInquiryTemplate.hasGivenAnswers");
                        }
                    }
                }
            }
        }

    }

    @Deprecated
    public boolean hasShared() {
        return getShared() != null;
    }

}
