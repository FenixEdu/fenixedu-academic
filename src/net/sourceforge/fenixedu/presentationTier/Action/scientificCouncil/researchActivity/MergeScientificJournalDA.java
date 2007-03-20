package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.MergeResearchActivityPageContainerBean;
import net.sourceforge.fenixedu.dataTransferObject.MergeScientificJournalPageContainerBean;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class MergeScientificJournalDA extends MergeResearchActivityDA {
    
    public ActionForward chooseScientificJournal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	MergeScientificJournalPageContainerBean researchActivityPageContainerBean = 
	    (MergeScientificJournalPageContainerBean) getRenderedObject("mergeList");
	ScientificJournal scientificJournal = (ScientificJournal) researchActivityPageContainerBean.getSelected();
	researchActivityPageContainerBean.setSelected(null);
	
	copyProperties(scientificJournal, researchActivityPageContainerBean);
	
	RenderUtils.invalidateViewState();
	request.setAttribute("mergeList", researchActivityPageContainerBean);
	return mapping.findForward("show-research-activity-merge-list");
    }

    private void copyProperties(ScientificJournal scientificJournal, MergeScientificJournalPageContainerBean researchActivityPageContainerBean) {
	researchActivityPageContainerBean.setName(scientificJournal.getName());
	researchActivityPageContainerBean.setIssn(scientificJournal.getIssn());
	researchActivityPageContainerBean.setUrl(scientificJournal.getUrl());
	researchActivityPageContainerBean.setResearchActivityLocationType(scientificJournal.getLocationType());
    }
    
    
    @Override
    protected MergeResearchActivityPageContainerBean getNewBean() {
        return new MergeScientificJournalPageContainerBean(new BeanComparator("name"));
    }

    @Override
    protected String getServiceName() {
	return "MergeScientificJournals";
    }
    
    @Override
    protected List getObjects() {
        return ScientificJournal.readAll();
    }

    
    
}
