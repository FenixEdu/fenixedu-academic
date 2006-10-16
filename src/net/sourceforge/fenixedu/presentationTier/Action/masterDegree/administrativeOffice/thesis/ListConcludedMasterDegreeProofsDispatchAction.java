/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.thesis.ListMasterDegreeProofsBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ListConcludedMasterDegreeProofsDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	ListMasterDegreeProofsBean bean = null;

	if (RenderUtils.getViewState() != null) {
	    bean = (ListMasterDegreeProofsBean) RenderUtils.getViewState().getMetaObject().getObject();
	}

	if (bean == null) {
	    bean = new ListMasterDegreeProofsBean();
	}

	request.setAttribute("chooseDegreeAndYearBean", bean);
	return mapping.findForward("showList");
    }
}
