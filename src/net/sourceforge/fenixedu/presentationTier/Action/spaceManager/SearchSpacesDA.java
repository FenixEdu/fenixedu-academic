package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.space.SearchSpaceEvents;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.MoveSpaceBean;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.SearchSpaceEventsBean;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.SpaceOccupationEventBean;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "SpaceManager", path = "/searchSpace", scope = "session", parameter = "method")
@Forwards(value = {
	@Forward(name = "prepareSearch", path = "/spaceManager/searchSpaces.jsp", tileProperties = @Tile(  title = "private.spacemanagement.searchspaces")),
	@Forward(name = "prepareSearchEvents", path = "/spaceManager/searchSpaceEvents.jsp", tileProperties = @Tile(  title = "private.spacemanagement.searchoccupations")) })
public class SearchSpacesDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	MoveSpaceBean bean = new MoveSpaceBean();
	request.setAttribute("bean", bean);
	return mapping.findForward("prepareSearch");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	MoveSpaceBean bean = getRenderedObject();

	List<Space> spaces = null;
	if (bean != null) {
	    String spaceName = bean.getSpaceName();
	    spaces = Space.getAllSpacesByPresentationName(spaceName);
	}

	request.setAttribute("spaces", spaces);
	request.setAttribute("bean", bean);
	return mapping.findForward("prepareSearch");
    }

    public ActionForward prepareSearchEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	SearchSpaceEventsBean bean = new SearchSpaceEventsBean();
	request.setAttribute("bean", bean);
	return mapping.findForward("prepareSearchEvents");
    }

    public ActionForward searchSpaceEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	SearchSpaceEventsBean bean = getRenderedObject();
	if (bean.getStart().isAfter(bean.getEnd())) {
	    request.setAttribute("bean", bean);
	    request.setAttribute("startAfterEnd", true);
	} else {
	    Collection<SpaceOccupationEventBean> results = SearchSpaceEvents.run(bean);
	    request.setAttribute("results", results);
	}
	return mapping.findForward("prepareSearchEvents");
    }

}