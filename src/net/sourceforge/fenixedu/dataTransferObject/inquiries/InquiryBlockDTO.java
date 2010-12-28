package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGroupQuestion;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class InquiryBlockDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private InquiryBlock inquiryBlock;

    private SortedSet<InquiryGroupQuestionBean> inquiryGroups;

    public InquiryBlockDTO(InquiryBlock inquiryBlock) {
	setInquiryBlock(inquiryBlock);
	ComparatorChain comparatorChain = new ComparatorChain();
	comparatorChain.addComparator(new BeanComparator("inquiryGroupQuestion.groupOrder"));
	comparatorChain.addComparator(new BeanComparator("order"));
	setInquiryGroups(new TreeSet<InquiryGroupQuestionBean>(comparatorChain));
	for (InquiryGroupQuestion inquiryGroupQuestion : inquiryBlock.getInquiryGroupsQuestionsSet()) {
	    getInquiryGroups().add(new InquiryGroupQuestionBean(inquiryGroupQuestion, this));
	}
    }

    public InquiryBlock getInquiryBlock() {
	return inquiryBlock;
    }

    public void setInquiryBlock(InquiryBlock inquiryBlock) {
	this.inquiryBlock = inquiryBlock;
    }

    public void setInquiryGroups(SortedSet<InquiryGroupQuestionBean> inquiryGroups) {
	this.inquiryGroups = inquiryGroups;
    }

    public SortedSet<InquiryGroupQuestionBean> getInquiryGroups() {
	return inquiryGroups;
    }

    public boolean validate() {
	Set<InquiryGroupQuestionBean> groups = getInquiryGroups();
	for (InquiryGroupQuestionBean group : groups) {
	    if (!group.validate()) {
		return false;
	    }
	}
	return true;
    }
}
