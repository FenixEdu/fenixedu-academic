package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cms.messaging.email.EMailAddress;
import net.sourceforge.fenixedu.domain.cms.messaging.email.SendMailReport;
import net.sourceforge.fenixedu.domain.cms.messaging.email.Recipient.SendStatus;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SimpleMailSenderAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RenderUtils.invalidateViewState();
        
        MailBean bean = createMailBean(request);
        request.setAttribute("mailBean", bean);
        
        return mapping.findForward("compose-mail");
    }

    protected MailBean createMailBean(HttpServletRequest request) {
        MailBean bean = new MailBean();
        
        Person person = getLoggedPerson(request);
        bean.setFromName(person.getName());
        bean.setFromAddress(person.getEmail());
     
        bean.setReceiversOptions(getPossibleReceivers(request));
        
        return bean;
    }
    
    public ActionForward sendInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("compose-mail");
    }
    
    public ActionForward send(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MailBean bean = getMailBean(request);
        
        if (bean == null) {
            return prepare(mapping, actionForm, request, response);
        }
        
        SendMailReport report;
        try {
            report = (SendMailReport) ServiceUtils.executeService(getUserView(request), "SendEMail", bean.getEmailParameters());
            processReport(request, bean, report);
        } catch (Exception e) {
            addActionMessage("error", request, "messaging.mail.send.error");
            return mapping.findForward("problem");
        }
        
        return mapping.findForward("success");
    }
 
    protected void processReport(HttpServletRequest request, MailBean bean, SendMailReport report) {
        if (report == null) {
            return;
        }

        boolean hasProblems = false;
        int sentCount = 0;
        
        for (SendStatus status : SendStatus.values()) {
            switch (status) {
            case QUEUED:
                break;
            case SENT:
                Collection<EMailAddress> addresses = report.getAddressReport(status);
                if (addresses != null) {
                    sentCount += addresses.size();
                }

                Collection<Person> persons = report.getPersonReport(status);
                if (persons != null) {
                    sentCount = persons.size();
                }
                
                break;
            default:
                if (report.getAddressReport(status) != null) {
                    for (EMailAddress address : report.getAddressReport(status)) {
                        addActionMessage("problem", request, "messaging.mail.address.problem", address.getAddress());
                        hasProblems = true;
                    }
                }

                if (report.getPersonReport(status) != null) {
                    for (Person person : report.getPersonReport(status)) {
                        addActionMessage("problem", request, "messaging.mail.address.problem", person.getEmail());
                        hasProblems = true;
                    }
                }
                
                break;
            }
        }
        
        if (! hasProblems) {
            addActionMessage("confirmation", request, "messaging.mail.send.ok", String.valueOf(sentCount));
        }
        else {
            if (bean.getReceiversCount() > 0) {
                addActionMessage("warning", request, "messaging.mail.send.count", String.valueOf(sentCount), String.valueOf(bean.getReceiversCount()));
            }
        }
    }

    protected List<IGroup> getPossibleReceivers(HttpServletRequest request) {
        return new ArrayList<IGroup>();
    }

    protected MailBean getMailBean(HttpServletRequest request) {
        return (MailBean) getRenderedObject("mailBean");
    }
}
