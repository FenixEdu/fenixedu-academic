/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.accessControl.IGroup;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.DegreeCurricularPlanGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreeCurricularPlanStudentsGroup;
import net.sourceforge.fenixedu.domain.cms.messaging.email.EMailAddress;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.SendMailForm;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.codec.binary.Base64;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 16:46:20,17/Mar/2006
 * @version $Id$
 */
public class SendMailToDegreeCurricularPlanStudents extends MailSenderAction {

	public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws NumberFormatException, FenixFilterException,
			FenixServiceException, FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
		DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) ServiceManagerServiceFactory.executeService(userView, "ReadDomainObject", new Object[] {
				DegreeCurricularPlan.class, Integer.parseInt(degreeCurricularPlanID) });

		IGroup group = new DegreeCurricularPlanStudentsGroup(degreeCurricularPlan);

		SendMailForm sendMailForm = (SendMailForm) form;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream dos = new ObjectOutputStream(baos);
			dos.writeObject(group);
		}
		catch (IOException e) {
			throw new FenixActionException(e);
		}
		byte[] groupArray = baos.toByteArray();
		String groupString = new String(Base64.encodeBase64(groupArray));
		sendMailForm.setGroup(groupString);

		return super.start(mapping, form, request, response);
	}

	@Override
	protected List<IGroup> loadPersonalGroupsToChooseFrom(HttpServletRequest request)
			throws FenixFilterException, FenixServiceException {
		return null;
	}

	@Override
	protected EMailAddress getFromAddress(HttpServletRequest request, ActionForm form)
			throws FenixFilterException, FenixServiceException {
		SendMailForm sendMailForm = (SendMailForm) form;
		EMailAddress address = new EMailAddress();
		if (EMailAddress.isValid(sendMailForm.getFromAddress())) {
			String[] components = sendMailForm.getFromAddress().split("@");
			address.setUser(components[0]);
			address.setDomain(components[1]);
			address.setPersonalName(sendMailForm.getFromPersonalName());
		}

		return address;
	}

	@Override
	protected IGroup[] getAllowedGroups(HttpServletRequest request, IGroup[] selectedGroups)
			throws FenixFilterException, FenixServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IGroup[] getGroupsToSend(ActionForm form, HttpServletRequest request)
			throws FenixFilterException, FenixServiceException, FenixActionException {
		SendMailForm sendMailForm = (SendMailForm) form;

		byte[] decodedForm = Base64.decodeBase64(sendMailForm.getGroup().getBytes());

		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(decodedForm);
		try {
			ObjectInputStream stream = new ObjectInputStream(byteInputStream);
			IGroup group = (IGroup) stream.readObject();
			IGroup[] groups = new IGroup[1];
			groups[0] = group;
			return groups;
		}
		catch (IOException e) {
			throw new FenixActionException(e);
		}
		catch (ClassNotFoundException e) {
			throw new FenixActionException(e);
		}
	}
}
