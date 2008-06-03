package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.MergeEventEditionPageContainerBean;
import net.sourceforge.fenixedu.dataTransferObject.PageContainerBean;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EditEventDA extends EditResearchActivityDA {
    
    @Override
    protected List getObjects() {
	List<ResearchEvent> events = new ArrayList<ResearchEvent>(rootDomainObject.getEvents());
	Collections.sort(events, new BeanComparator("name", Collator.getInstance()));
        return events;
    }
    
    public ActionForward prepareChooseEventEditionToMerge(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	PageContainerBean pageContainerBean = (PageContainerBean) getRenderedObject("pageContainerBean");
	RenderUtils.invalidateViewState();

	MergeEventEditionPageContainerBean mergeEventEditionPageContainerBean = new MergeEventEditionPageContainerBean(
		(ResearchEvent) pageContainerBean.getSelected());
	request.setAttribute("mergeBean", mergeEventEditionPageContainerBean);

	return mapping.findForward("prepareChooseEventEditionToMerge");
    }
}
