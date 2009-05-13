package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator;

import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.coordinator.ReadCoordinatedDegrees;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

public class ReadCoordinatedDegreesAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	HttpSession session = request.getSession(false);

	if (session != null) {
	    IUserView userView = UserView.getUser();

	    try {

		List<InfoDegreeCurricularPlan> degrees = (List<InfoDegreeCurricularPlan>) ReadCoordinatedDegrees.run(userView);
		session.setAttribute(PresentationConstants.MASTER_DEGREE_LIST, degrees);
	    } catch (FenixServiceException e) {
		throw new FenixActionException(e);
	    }

	    return mapping.findForward("ChooseDegree");
	} else {
	    throw new FenixActionException();
	}
    }

}