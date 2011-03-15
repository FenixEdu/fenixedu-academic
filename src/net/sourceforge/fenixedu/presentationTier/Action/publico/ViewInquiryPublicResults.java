package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.GroupResultsSummaryBean;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGroupQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

public abstract class ViewInquiryPublicResults extends FenixDispatchAction {

    protected static GroupResultsSummaryBean getGeneralResults(List<InquiryResult> results, List<InquiryBlock> resultsBlocks,
	    int blockOrder, int groupOrder) {
	for (InquiryBlock inquiryBlock : resultsBlocks) {
	    if (inquiryBlock.getBlockOrder() == blockOrder) {
		for (InquiryGroupQuestion groupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
		    if (groupQuestion.getGroupOrder() == groupOrder) {
			return new GroupResultsSummaryBean(groupQuestion, results, null, null);
		    }
		}
	    }
	}
	return null;
    }
}
