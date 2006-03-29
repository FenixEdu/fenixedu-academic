/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.accessControl.IGroup;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.DegreeCurricularPlanActiveOrSchoolPartConcludedStudentsGroup;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.SendMailForm;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 16:46:20,17/Mar/2006
 * @version $Id$
 */
public class SendMailToDegreeCurricularPlanStudents extends ContextualGroupMailSenderAction {

	public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws NumberFormatException, FenixFilterException,
			FenixServiceException, FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
		DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) ServiceManagerServiceFactory.executeService(userView, "ReadDomainObject", new Object[] {
				DegreeCurricularPlan.class, Integer.parseInt(degreeCurricularPlanID) });

		IGroup group = new DegreeCurricularPlanActiveOrSchoolPartConcludedStudentsGroup(degreeCurricularPlan);
		
		SendMailForm sendMailForm = (SendMailForm) form;
		sendMailForm.setGroup(super.getSerializedGroup(group));
					

		return super.start(mapping, form, request, response);
	}

	@Override
	protected List<IGroup> loadPersonalGroupsToChooseFrom(HttpServletRequest request)
			throws FenixFilterException, FenixServiceException {
		return null;
	}

	@Override
	protected IGroup[] getAllowedGroups(HttpServletRequest request, IGroup[] selectedGroups)
			throws FenixFilterException, FenixServiceException {
		// TODO Auto-generated method stub
		return null;
	}


}
