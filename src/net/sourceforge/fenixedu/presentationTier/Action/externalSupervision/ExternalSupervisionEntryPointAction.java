package net.sourceforge.fenixedu.presentationTier.Action.externalSupervision;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.RegistrationProtocol;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/welcome", module = "externalSupervision")
@Forwards({ @Forward(name = "welcome", path = "/externalSupervision/externalSupervisionGreetings.jsp"),
		@Forward(name = "welcome_AFA", path = "/externalSupervision/externalSupervisionGreetingsAFA.jsp"),
		@Forward(name = "welcome_MA", path = "/externalSupervision/externalSupervisionGreetingsMA.jsp"),
		@Forward(name = "selectProtocol", path = "/externalSupervision/selectProtocol.jsp") })
public class ExternalSupervisionEntryPointAction extends FenixDispatchAction {

	/*public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
	final IUserView userView = UserView.getUser();
	Person supervisor = userView.getPerson();
	
	if(supervisor.getRegistrationProtocolsCount() > 1) {
	    return mapping.findForward("selectProtocol");
	}
	
	return welcome(mapping, form, request, response);
	}*/

	public ActionForward welcome(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		/*//Pick up user so that you can find out which
		//agreement greeting is supposed to be shown.
		//(for single agreement supervisors only)
		final IUserView userView = UserView.getUser();
		Person supervisor = userView.getPerson();
		
		//If action is being called after Protocol Selection, it means
		//this supervisor is responsible for several agreements.
		//In that case the agreement greeting supposed to be shown
		//is attainable through the request.
		final String registrationProtocolId = request.getParameter("registrationProtocolId");
		RegistrationProtocol registrationProtocol = AbstractDomainObject.fromExternalId(registrationProtocolId);
		RegistrationAgreement registrationAgreement;
		
		//If coming from selectProtocol, read agreement from request. Else read from user. Plain simple.
		if(registrationProtocol != null){
		    registrationAgreement = registrationProtocol.getRegistrationAgreement();
		} else {
		    registrationAgreement = supervisor.getRegistrationProtocols().get(0).getRegistrationAgreement();
		}*/

		final IUserView userView = UserView.getUser();
		Person supervisor = userView.getPerson();
		RegistrationProtocol registrationProtocol = supervisor.getOnlyRegistrationProtocol();

		if (registrationProtocol == null) {
			return mapping.findForward("welcome");
		}

		switch (registrationProtocol.getRegistrationAgreement()) {

		case AFA:
			return mapping.findForward("welcome_AFA");

		case MA:
			return mapping.findForward("welcome_MA");

		default:
			return mapping.findForward("welcome");
		}
	}

}
