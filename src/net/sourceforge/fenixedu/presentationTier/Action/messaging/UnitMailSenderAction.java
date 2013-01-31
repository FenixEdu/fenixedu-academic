package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class UnitMailSenderAction extends FenixDispatchAction {

	public Unit getUnit(HttpServletRequest request) {
		return DomainObject.fromExternalId(request.getParameter("unitExternalId"));
	}

	public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return EmailsDA.sendEmail(request, getUnit(request).getOneUnitBasedSender());
	}
}
