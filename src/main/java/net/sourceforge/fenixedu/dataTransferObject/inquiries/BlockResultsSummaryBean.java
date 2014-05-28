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
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGroupQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.ResultClassification;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;

import org.apache.commons.beanutils.BeanComparator;

public class BlockResultsSummaryBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private InquiryBlock inquiryBlock;
    private ResultClassification blockResultClassification;
    private List<GroupResultsSummaryBean> groupsResults = new ArrayList<GroupResultsSummaryBean>();

    public BlockResultsSummaryBean(InquiryBlock inquiryBlock, List<InquiryResult> inquiryResults, Person person,
            ResultPersonCategory personCategory) {
        setInquiryBlock(inquiryBlock);
        setBlockResultClassification(getInquiryResultQuestion(inquiryResults));
        for (InquiryGroupQuestion inquiryGroupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
            if (inquiryGroupQuestion.isToPresentStandardResults()) {
                getGroupsResults().add(new GroupResultsSummaryBean(inquiryGroupQuestion, inquiryResults, person, personCategory));
            }
        }
        Collections.sort(getGroupsResults(), new BeanComparator("inquiryGroupQuestion.groupOrder"));
        setLeftRightGroups();
    }

    private void setLeftRightGroups() {
        GroupResultsSummaryBean currentGroup = null;
        GroupResultsSummaryBean previousGroup = null;
        for (Iterator<GroupResultsSummaryBean> iterator = getGroupsResults().iterator(); iterator.hasNext();) {
            previousGroup = currentGroup;
            currentGroup = iterator.next();
            if (currentGroup.getInquiryGroupQuestion().isCheckbox()) {
                if (previousGroup != null && previousGroup.getInquiryGroupQuestion().isCheckbox()) {
                    currentGroup.setLeft(false);
                }
            }
        }
    }

    public boolean isMandatoryComments() {
        for (GroupResultsSummaryBean groupResultsSummaryBean : getGroupsResults()) {
            for (QuestionResultsSummaryBean questionResultsSummaryBean : groupResultsSummaryBean.getQuestionsResults()) {
                if (questionResultsSummaryBean.getResultClassification() != null
                        && questionResultsSummaryBean.getResultClassification().isMandatoryComment()) {
                    return true;
                }
            }
        }
        return false;
    }

    private InquiryResult getInquiryResultQuestion(List<InquiryResult> inquiryResults) {
        for (InquiryResult inquiryResult : inquiryResults) {
            if (inquiryResult.getInquiryQuestion() == getInquiryBlock().getResultQuestion()) {
                return inquiryResult;
            }
        }
        return null;
    }

    public InquiryBlock getInquiryBlock() {
        return inquiryBlock;
    }

    public void setInquiryBlock(InquiryBlock inquiryBlock) {
        this.inquiryBlock = inquiryBlock;
    }

    public List<GroupResultsSummaryBean> getGroupsResults() {
        return groupsResults;
    }

    public void setGroupsResults(List<GroupResultsSummaryBean> groupsResults) {
        this.groupsResults = groupsResults;
    }

    private void setBlockResultClassification(InquiryResult inquiryResultQuestion) {
        if (getInquiryBlock().getResultQuestion() != null && inquiryResultQuestion != null) {
            setBlockResultClassification(inquiryResultQuestion.getResultClassification());
        }
    }

    public void setBlockResultClassification(ResultClassification resultClassification) {
        this.blockResultClassification = resultClassification;
    }

    public ResultClassification getBlockResultClassification() {
        return blockResultClassification;
    }
}
