package net.sourceforge.fenixedu.presentationTier.Action.email;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.SendEmailBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SendEmailDA extends FenixDispatchAction {

    public ActionForward prepare(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
	    final HttpServletResponse response) throws Exception {
	SendEmailBean sendEmailBean = (SendEmailBean) getRenderedObject();
	if (sendEmailBean == null) {
	    sendEmailBean = new SendEmailBean();
	    final String fromName = request.getParameter("fromName");
	    if (fromName != null && fromName.length() > 0) {
		sendEmailBean.setFromName(fromName);
	    }
	    final String from = request.getParameter("from");
	    if (from != null && from.length() > 0) {
		sendEmailBean.setFrom(from);
	    }
	    final String allowChangeSender = request.getParameter("allowChangeSender");
	    sendEmailBean.setAllowChangeSender(allowChangeSender == null || allowChangeSender.length() == 0 ? Boolean.TRUE
		    : Boolean.valueOf(allowChangeSender));
	}
	request.setAttribute("sendEmailBean", sendEmailBean);
	return mapping.findForward("showSendEmailForm");
    }

    public ActionForward send(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
	    final HttpServletResponse response) throws Exception {
	final SendEmailBean sendEmailBean = (SendEmailBean) getRenderedObject();
	sendEmailBean.send();
	request.setAttribute("sendEmailBean", sendEmailBean);
	return mapping.findForward("showSendEmailResult");
    }

}
