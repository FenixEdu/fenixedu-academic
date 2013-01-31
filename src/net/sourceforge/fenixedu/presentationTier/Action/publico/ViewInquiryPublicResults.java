package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.GroupResultsSummaryBean;
import net.sourceforge.fenixedu.domain.inquiries.GroupResultType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGroupQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

public abstract class ViewInquiryPublicResults extends FenixDispatchAction {

	protected static GroupResultsSummaryBean getGeneralResults(List<InquiryResult> results, List<InquiryBlock> resultsBlocks,
			GroupResultType groupResultType) {
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
