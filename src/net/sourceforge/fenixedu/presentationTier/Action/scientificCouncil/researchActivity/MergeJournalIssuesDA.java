package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.MergeJournalIssuePageContainerBean;
import net.sourceforge.fenixedu.dataTransferObject.MergeResearchActivityPageContainerBean;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class MergeJournalIssuesDA extends MergeResearchActivityDA {

    public static final Comparator<JournalIssue> COMPARE_BY_VOLUME_AND_NUMBER = new ComparatorChain();
    static {
	((ComparatorChain) COMPARE_BY_VOLUME_AND_NUMBER).addComparator(new BeanComparator("volume"));
	((ComparatorChain) COMPARE_BY_VOLUME_AND_NUMBER).addComparator(new BeanComparator("number"));
    }
    
    
    public ActionForward chooseJournalIssue(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	MergeJournalIssuePageContainerBean researchActivityPageContainerBean = (MergeJournalIssuePageContainerBean) getRenderedObject("mergeList");
	JournalIssue journalIssue = (JournalIssue) researchActivityPageContainerBean.getSelected();
	researchActivityPageContainerBean.setSelected(null);

	copyProperties(journalIssue, researchActivityPageContainerBean);

	RenderUtils.invalidateViewState();
	request.setAttribute("mergeList", researchActivityPageContainerBean);
	return mapping.findForward("show-research-activity-merge-list");
    }

    private void copyProperties(JournalIssue journalIssue,
	    MergeJournalIssuePageContainerBean researchActivityPageContainerBean) {
	researchActivityPageContainerBean.setVolume(journalIssue.getVolume());
	researchActivityPageContainerBean.setYear(journalIssue.getYear());
	researchActivityPageContainerBean.setNumber(journalIssue.getNumber());
	researchActivityPageContainerBean.setMonth(journalIssue.getMonth());
	researchActivityPageContainerBean.setUrl(journalIssue.getUrl());
	researchActivityPageContainerBean.setSpecialIssue(journalIssue.getSpecialIssue());
	researchActivityPageContainerBean.setSpecialIssueComment(journalIssue.getSpecialIssueComment());	
    }


    @Override
    protected MergeResearchActivityPageContainerBean getNewBean() {
	return null;
    }

    @Override
    protected List getObjects(MergeResearchActivityPageContainerBean researchActivityPageContainerBean) {
	MergeJournalIssuePageContainerBean mergeJournalIssuePageContainerBean = (MergeJournalIssuePageContainerBean) researchActivityPageContainerBean;
	List<JournalIssue> journalIssues = new ArrayList<JournalIssue>(mergeJournalIssuePageContainerBean.getScientificJournal().getJournalIssues());
	Collections.sort(journalIssues, COMPARE_BY_VOLUME_AND_NUMBER);
	return journalIssues;
    }

    @Override
    protected String getServiceName() {
	return "MergeJournalIssues";
    }

}
