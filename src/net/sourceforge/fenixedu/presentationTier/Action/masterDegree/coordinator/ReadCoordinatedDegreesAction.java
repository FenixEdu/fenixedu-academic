package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator;

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
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ReadCoordinatedDegreesAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(false);

        if (session != null) {
			IUserView userView = SessionUtils.getUserView(request);

			try {
                Object args[] = { userView };
                List<InfoDegreeCurricularPlan> degrees = (List<InfoDegreeCurricularPlan>) ServiceManagerServiceFactory.executeService(userView, "ReadCoordinatedDegrees", args);
                session.setAttribute(SessionConstants.MASTER_DEGREE_LIST, degrees);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
			
			return mapping.findForward("ChooseDegree");
		} else {
            throw new FenixActionException();    
        }
	}

}
