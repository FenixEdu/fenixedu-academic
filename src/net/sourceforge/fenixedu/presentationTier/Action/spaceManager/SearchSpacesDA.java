package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.spaceManager.MoveSpaceBean;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SearchSpacesDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	MoveSpaceBean bean = new MoveSpaceBean();
	request.setAttribute("bean", bean);
	return mapping.findForward("prepareSearch");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	MoveSpaceBean bean = (MoveSpaceBean) getRenderedObject();

	List<Space> spaces = null;
	if (bean != null) {
	    String spaceName = bean.getSpaceName();
	    spaces = Space.getAllSpacesByPresentationName(spaceName);
	}

	request.setAttribute("spaces", spaces);
	request.setAttribute("bean", bean);
	return mapping.findForward("prepareSearch");
    }
}
