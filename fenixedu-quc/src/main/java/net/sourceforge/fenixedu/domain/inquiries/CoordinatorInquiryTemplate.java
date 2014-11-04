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

import java.util.Collection;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
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
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        for (InquiryBlock inquiryBlock : getInquiryBlocksSet()) {
            removeInquiryBlocks(inquiryBlock);
        }
        setExecutionPeriod(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        for (InquiryBlock inquiryBlock : getInquiryBlocksSet()) {
            for (InquiryGroupQuestion groupQuestion : inquiryBlock.getInquiryGroupsQuestionsSet()) {
                for (InquiryQuestion inquiryQuestion : groupQuestion.getInquiryQuestionsSet()) {
                    for (QuestionAnswer questionAnswer : inquiryQuestion.getQuestionAnswersSet()) {
                        InquiryCoordinatorAnswer coordinatorAnswer = (InquiryCoordinatorAnswer) questionAnswer.getInquiryAnswer();
                        if (coordinatorAnswer.getExecutionSemester() == getExecutionPeriod()) {
                            blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                                    "error.CoordinatorInquiryTemplate.hasGivenAnswers"));
                        }
                    }
                }
            }
        }
    }
}
