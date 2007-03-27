package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity;

import java.text.Collator;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.MergeEventPageContainerBean;
import net.sourceforge.fenixedu.dataTransferObject.MergeResearchActivityPageContainerBean;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class MergeEventsDA extends MergeResearchActivityDA {

    public ActionForward chooseEvent(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	MergeEventPageContainerBean researchActivityPageContainerBean = (MergeEventPageContainerBean) getRenderedObject("mergeList");
	Event event = (Event) researchActivityPageContainerBean.getSelected();
	researchActivityPageContainerBean.setSelected(null);

	copyProperties(event, researchActivityPageContainerBean);

	RenderUtils.invalidateViewState();
	request.setAttribute("mergeList", researchActivityPageContainerBean);
	return mapping.findForward("show-research-activity-merge-list");
    }

    private void copyProperties(Event event,
	    MergeEventPageContainerBean researchActivityPageContainerBean) {
	researchActivityPageContainerBean.setName(event.getName());
	researchActivityPageContainerBean.setResearchActivityLocationType(event.getLocationType());
	researchActivityPageContainerBean.setEventType(event.getEventType());
	researchActivityPageContainerBean.setUrl(event.getUrl());
    }

    @Override
    protected MergeResearchActivityPageContainerBean getNewBean() {
	return new MergeEventPageContainerBean();
    }

    @Override
    protected String getServiceName() {
	return "MergeEvents";
    }

    @Override
    protected List getObjects() {
	List<Event> events = rootDomainObject.getEvents();
	Collections.sort(events, new BeanComparator("name", Collator.getInstance()));
	return events;
    }


}
