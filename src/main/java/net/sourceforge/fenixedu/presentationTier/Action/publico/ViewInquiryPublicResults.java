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
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.GroupResultsSummaryBean;
import net.sourceforge.fenixedu.domain.inquiries.GroupResultType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGroupQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

public abstract class ViewInquiryPublicResults extends FenixDispatchAction {

    protected static GroupResultsSummaryBean getGeneralResults(List<InquiryResult> results,
            Collection<InquiryBlock> resultsBlocks, GroupResultType groupResultType) {
        for (InquiryBlock inquiryBlock : resultsBlocks) {
            for (InquiryGroupQuestion groupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
                if (groupResultType.equals(groupQuestion.getGroupResultType())) {
                    return new GroupResultsSummaryBean(groupQuestion, results, null, null);
                }
            }
        }
        return null;
    }
}
