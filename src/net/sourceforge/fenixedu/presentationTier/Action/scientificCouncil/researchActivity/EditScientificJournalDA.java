package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.MergeJournalIssuePageContainerBean;
import net.sourceforge.fenixedu.dataTransferObject.PageContainerBean;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class EditScientificJournalDA extends EditResearchActivityDA {


    @Override
    protected List getObjects() {
	List<ScientificJournal> scientificJournals = new ArrayList<ScientificJournal>(rootDomainObject.getScientificJournals());
	Collections.sort(scientificJournals, new BeanComparator("name", Collator.getInstance()));
        return scientificJournals;
    }
    
    public ActionForward prepareChooseJournalIssueToMerge(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	PageContainerBean pageContainerBean = (PageContainerBean) getRenderedObject("pageContainerBean");
	RenderUtils.invalidateViewState();

	MergeJournalIssuePageContainerBean mergeJournalIssuePageContainerBean = new MergeJournalIssuePageContainerBean(
		(ScientificJournal) pageContainerBean.getSelected());
	request.setAttribute("mergeBean", mergeJournalIssuePageContainerBean);

	return mapping.findForward("prepareChooseJournalIssueToMerge");
    }

}
