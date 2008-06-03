package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.MergeEventEditionPageContainerBean;
import net.sourceforge.fenixedu.dataTransferObject.MergeResearchActivityPageContainerBean;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class MergeEventEditionsDA extends MergeResearchActivityDA {

    public static final Comparator<EventEdition> COMPARE_BY_EDITION = new ComparatorChain();
    static {
	((ComparatorChain) COMPARE_BY_EDITION).addComparator(new BeanComparator("edition"));
    }
    
    
    public ActionForward chooseEventEdition(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	MergeEventEditionPageContainerBean researchActivityPageContainerBean = (MergeEventEditionPageContainerBean) getRenderedObject("mergeList");
	EventEdition eventEdition = (EventEdition) researchActivityPageContainerBean.getSelected();
	researchActivityPageContainerBean.setSelected(null);

	copyProperties(eventEdition, researchActivityPageContainerBean);

	RenderUtils.invalidateViewState();
	request.setAttribute("mergeList", researchActivityPageContainerBean);
	return mapping.findForward("show-research-activity-merge-list");
    }

    private void copyProperties(EventEdition eventEdition,
	    MergeEventEditionPageContainerBean researchActivityPageContainerBean) {
	researchActivityPageContainerBean.setUrl(eventEdition.getUrl());
	researchActivityPageContainerBean.setEdition(eventEdition.getEdition());
	researchActivityPageContainerBean.setEventLocation(eventEdition.getEventLocation());
	researchActivityPageContainerBean.setOrganization(eventEdition.getOrganization());
	researchActivityPageContainerBean.setStartDate(eventEdition.getStartDate());
	researchActivityPageContainerBean.setEndDate(eventEdition.getEndDate());
    }


    @Override
    protected MergeResearchActivityPageContainerBean getNewBean() {
	return null;
    }

    @Override
    protected List getObjects(MergeResearchActivityPageContainerBean researchActivityPageContainerBean) {
	MergeEventEditionPageContainerBean mergeEventEditionPageContainerBean = (MergeEventEditionPageContainerBean) researchActivityPageContainerBean;
	List<EventEdition> eventEditions = new ArrayList<EventEdition>(mergeEventEditionPageContainerBean.getEvent().getEventEditionsSet());
	Collections.sort(eventEditions, COMPARE_BY_EDITION);
	return eventEditions;
    }

    @Override
    protected String getServiceName() {
	return "MergeEventEditions";
    }

}
