/**
 * 
 */

package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging.email.SendEMail.SendEMailParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cms.messaging.email.EMailAddress;
import net.sourceforge.fenixedu.domain.cms.messaging.email.SendMailReport;
import net.sourceforge.fenixedu.domain.cms.messaging.email.Recipient.SendStatus;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.presentationTier.StartHiddenActionMessages;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.SendMailForm;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.codec.binary.Base64;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import pt.ist.utl.fenix.utils.Pair;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 11:36:35,15/Fev/2006
 * @version $Id$
 */
public abstract class MailSenderAction extends FenixDispatchAction {

    protected abstract String getNoFromAddressWarningMessageKey();

    private String serializeParameters(Map arguments) throws FenixActionException {
        String parametersString = null;
        
        if (arguments != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            
            try {
                ObjectOutputStream dos = new ObjectOutputStream(baos);
                Map<Object, Object> argumentsCopy = new HashMap<Object, Object>();
                
                for (Object key : arguments.keySet()) {
                    argumentsCopy.put(key, arguments.get(key));
                }
                
                dos.writeObject(argumentsCopy);
            } catch (IOException e) {
                throw new FenixActionException(e);
            }
            
            byte[] parametersArray = baos.toByteArray();
            parametersString = new String(Base64.encodeBase64(parametersArray));
        }
        
        return parametersString;
    }

    private Map getPreviousRequestParameters(SendMailForm form) throws FenixActionException {
        byte[] decodedParameters = Base64.decodeBase64(form.getPreviousRequestParameters().getBytes());
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(decodedParameters);
        
        try {
            ObjectInputStream stream = new ObjectInputStream(byteInputStream);
            Map parametersMap = (Map) stream.readObject();
            
            return parametersMap;
        } catch (IOException e) {
            throw new FenixActionException(e);
        } catch (ClassNotFoundException e) {
            throw new FenixActionException(e);
        }
    }

    protected abstract List<IGroup> loadPersonalGroupsToChooseFrom(HttpServletRequest request) 
        throws FenixFilterException, FenixServiceException;

    protected abstract Pair<String, Object>[] getStateRequestAttributes(IUserView userView, ActionForm actionForm, Map previousRequestParameters) 
        throws FenixActionException, FenixFilterException, FenixServiceException;

    protected EMailAddress getFromAddress(IUserView userView, SendMailForm form, Map previousRequestParameters) throws FenixFilterException, FenixServiceException, FenixActionException {
        Person person = userView.getPerson();
        EMailAddress address = new EMailAddress();
        
        if (EMailAddress.isValid(person.getEmail())) {
            String[] components = person.getEmail().split("@");
            address.setUser(components[0]);
            address.setDomain(components[1]);
            address.setPersonalName(person.getNome());
        }

        return address;
    }

    protected abstract IGroup[] getAllowedGroups(HttpServletRequest request,
            IGroup[] selectedGroups) throws FenixFilterException,
            FenixServiceException;

    protected abstract IGroup[] getGroupsToSend(IUserView userView,
            SendMailForm form, Map previousRequestParameters)
            throws FenixFilterException, FenixServiceException,
            FenixActionException;

    public final ActionForward start(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {

        IUserView userView = getUserView(request);
        SendMailForm form = (SendMailForm) actionForm;

        if (form.getPreviousRequestParameters() == null
                || form.getPreviousRequestParameters().equals("")) {
            this.saveRequestParameters(request, form);
        }
        form.setCopyToSender(true);
        this.recoverState(this.getStateRequestAttributes(userView, form, this
                .getPreviousRequestParameters(form)), request);

        List<IGroup> groups = this.loadPersonalGroupsToChooseFrom(request);

        form.setGroupsToChooseFrom(groups);
        String referer = request.getHeader("Referer");
        if (referer != null
                && !referer.equals("")
                && (form.getReturnURL() == null || form.getReturnURL().equals(
                        ""))) {
            form.setReturnURL(referer);
        }

        form.setSendMailActioName(mapping.getPath());

        EMailAddress address = this.getFromAddress(userView, form, this
                .getPreviousRequestParameters(form));

        if (address == null || !address.isValid()) {
            ActionMessages errors = new ActionMessages();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(this
                    .getNoFromAddressWarningMessageKey()));
            addErrors(request, errors);
        } else {

            form.setFromAddress(address.getAddress());
            form.setFromPersonalName(address.getPersonalName());
        }

        return mapping.findForward("sendMail");
    }

    private void recoverState(Pair<String, Object>[] stateRequestAttributes,
            HttpServletRequest request) {
        if (stateRequestAttributes != null) {
            for (int i = 0; i < stateRequestAttributes.length; i++) {
                request.setAttribute(stateRequestAttributes[i].getKey(),
                        stateRequestAttributes[i].getValue());
            }
        }
    }

    private void saveRequestParameters(HttpServletRequest request,
            SendMailForm form) throws FenixActionException {
        Map parameters = request.getParameterMap();
        form.setPreviousRequestParameters(this.serializeParameters(parameters));
    }

    public final ActionForward send(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {

        MessageResources resources = this.getResources(request, "CMS_RESOURCES");
        IUserView userView = getUserView(request);

        SendMailForm sendMailForm = (SendMailForm) actionForm;

        Map previousRequestParameters = this.getPreviousRequestParameters(sendMailForm);
        IGroup[] groupsToSend = this.getGroupsToSend(userView, sendMailForm, previousRequestParameters);

        if (groupsToSend == null || groupsToSend.length == 0) {
            ActionMessages errors = new ActionMessages();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "cms.mailSender.selectAtLeastOneGroup"));
            addErrors(request, errors);
            return this.start(mapping, actionForm, request, response);
        }

        SendEMailParameters parameters = new SendEMailParameters();
        if (sendMailForm.getCopyTo() != null
                && !sendMailForm.getCopyTo().equals("")) {
            String[] copyToString = sendMailForm.getCopyTo().split(",");
            EMailAddress[] copyTo = new EMailAddress[copyToString.length];
            for (int i = 0; i < copyToString.length; i++) {
                copyTo[i] = new EMailAddress(copyToString[i].trim());
            }

            parameters.copyTo = copyTo;
        }

        parameters.subject = sendMailForm.getSubject();
        parameters.message = sendMailForm.getMessage();
        parameters.toRecipients = groupsToSend;
        parameters.allowedSenders = this
                .getAllowedGroups(request, groupsToSend);
        parameters.copyToSender = sendMailForm.getCopyToSender();
        EMailAddress fromAddress = new EMailAddress();
        fromAddress.setAddress(sendMailForm.getFromAddress());
        fromAddress.setPersonalName(sendMailForm.getFromPersonalName());
        if (sendMailForm.getFromAddress() == null || !fromAddress.isValid()) {
            ActionMessages errors = new ActionMessages();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "cms.mailSender.provideValidFromEMailAddress"));
            addErrors(request, errors);
            return this.start(mapping, actionForm, request, response);
        } else {
            parameters.from = fromAddress;
        }

        sendMail(request, resources, userView, parameters,
                previousRequestParameters);

        return this.start(mapping, actionForm, request, response);
    }

    protected void sendMail(HttpServletRequest request,
            MessageResources resources, IUserView userView,
            SendEMailParameters parameters, Map previousRequestParameters)
            throws FenixActionException {
        try {
            SendMailReport result = (SendMailReport) ServiceManagerServiceFactory
                    .executeService(userView, "SendEMail",
                            new Object[] { parameters });
            processReport(request, resources, result);
        } catch (Exception e) {
            throw new FenixActionException(e);
        }
    }

    private void processReport(HttpServletRequest request,
            MessageResources resources, SendMailReport result) {
        StringBuffer sent = new StringBuffer();
        StringBuffer invalidAddress = new StringBuffer();
        StringBuffer transportError = new StringBuffer();
        StringBuffer invalidName = new StringBuffer();

        int invalidAddressCount = 0;
        int invalidPersonalNameCount = 0;
        int transportErrorCount = 0;
        int sentCount = 0;

        String ccAddressMessage = resources.getMessage(this.getLocale(request),
                "cms.mailSender.ccAddress");

        if (result.getPersonReport(SendStatus.INVALID_ADDRESS) != null) {
            for (Person person : result
                    .getPersonReport(SendStatus.INVALID_ADDRESS)) {
                if (invalidAddressCount == 0)
                    invalidAddress.append("<ul>");
                invalidAddressCount++;
                String addressToShow = person.getEmail();
                if (addressToShow == null || addressToShow.equals("")) {
                    addressToShow = resources.getMessage(this
                            .getLocale(request), "cms.mailSender.emptyAddress");
                }
                invalidAddress.append("<li>").append(person.getNome()).append(
                        " (").append(addressToShow).append(") </li>");
            }
        }

        if (result.getAddressReport(SendStatus.INVALID_ADDRESS) != null) {
            for (EMailAddress address : result
                    .getAddressReport(SendStatus.INVALID_ADDRESS)) {
                if (invalidAddressCount == 0)
                    invalidAddress.append("<ul>");
                invalidAddressCount++;
                String addressToShow = address.getAddress();
                if (addressToShow == null || addressToShow.equals("")) {
                    addressToShow = resources.getMessage(this
                            .getLocale(request), "cms.mailSender.emptyAddress");
                }
                invalidAddress.append("<li>").append(ccAddressMessage).append(
                        " (").append(addressToShow).append(") </li>");
            }
        }

        if (result.getPersonReport(SendStatus.INVALID_PERSONAL_NAME) != null) {
            for (Person person : result
                    .getPersonReport(SendStatus.INVALID_PERSONAL_NAME)) {
                if (invalidPersonalNameCount == 0)
                    invalidName.append("<ul> ");
                invalidPersonalNameCount++;
                invalidName.append("<li>").append(person.getNome()).append(
                        "</li>");
            }
        }

        if (result.getPersonReport(SendStatus.TRANSPORT_ERROR) != null) {
            for (Person person : result
                    .getPersonReport(SendStatus.TRANSPORT_ERROR)) {

                if (transportErrorCount == 0)
                    transportError.append("<ul>");
                transportErrorCount++;
                transportError.append("<li>").append(person.getNome()).append(
                        " (").append(person.getEmail()).append(") </li>");
            }
        }

        if (result.getAddressReport(SendStatus.TRANSPORT_ERROR) != null) {
            for (EMailAddress address : result
                    .getAddressReport(SendStatus.TRANSPORT_ERROR)) {

                if (transportErrorCount == 0)
                    transportError.append("<ul>");
                transportErrorCount++;
                transportError.append("<li>").append(ccAddressMessage).append(
                        " (").append(address.getAddress()).append(") </li>");
            }
        }

        if (result.getPersonReport(SendStatus.SENT) != null) {
            for (Person person : result.getPersonReport(SendStatus.SENT)) {
                if (sentCount == 0)
                    sent.append("<ul>");
                sentCount++;
                sent.append("<li>").append(person.getNome()).append(" (")
                        .append(person.getEmail()).append(")</li>");
            }
        }

        if (result.getAddressReport(SendStatus.SENT) != null) {
            for (EMailAddress address : result
                    .getAddressReport(SendStatus.SENT)) {
                if (sentCount == 0)
                    sent.append("<ul>");
                sentCount++;
                sent.append("<li>").append(ccAddressMessage).append(" (")
                        .append(address.getAddress()).append(")</li>");
            }
        }

        invalidAddress.append("</ul>");
        invalidName.append("</ul>");
        transportError.append("</ul>");
        sent.append("</ul>");
        ActionMessages errors = new ActionMessages();
        if (invalidAddressCount > 0) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "cms.mailSender.invalidAddress", invalidAddressCount,
                    invalidAddress));
        }
        if (invalidPersonalNameCount > 0) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "cms.mailSender.invalidPersonalName",
                    invalidPersonalNameCount, invalidName));
        }
        if (transportErrorCount > 0) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "cms.mailSender.transportError", transportErrorCount,
                    transportError));
        }
        saveErrors(request, errors);

        ActionMessages messages = new StartHiddenActionMessages();
        if (sentCount > 0) {
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "cms.mailSender.sent", sentCount, sent));
            ActionMessages existingMessages = (ActionMessages) request
                    .getAttribute(Globals.MESSAGE_KEY);
            if (existingMessages == null) {
                saveMessages(request, messages);
            } else {
                existingMessages.add(messages);
            }
        }
    }

    public ActionForward goBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixFilterException,
            FenixServiceException {
        SendMailForm sendMailForm = (SendMailForm) actionForm;
        try {
            String returnURL = sendMailForm.getReturnURL();
            response.sendRedirect(response.encodeRedirectURL(returnURL));
        } catch (IOException e) {
            throw new FenixActionException();
        }
        return null;
    }
}
