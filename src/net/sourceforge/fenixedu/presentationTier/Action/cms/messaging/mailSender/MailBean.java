package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.taskdefs.email.EmailAddress;

import net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging.email.SendEMail.SendEMailParameters;
import net.sourceforge.fenixedu.domain.cms.messaging.email.EMailAddress;
import net.sourceforge.fenixedu.injectionCode.IGroup;

/**
 * This <tt>MailBean</tt> can be used to collect, from the user, the
 * information required to send an email. This includes the basic mail
 * information and other information like the group of people that will receive
 * the mail (or individual addresses).
 * 
 * @author cfgi
 */
public class MailBean implements Serializable {

    /**
     * Default serialization id.
     */
    private static final long serialVersionUID = 1L;

    private String fromName;
    private String fromAddress;

    private boolean copyToSenderRequested;
    private String receiversOfCopy;

    private IGroup receiversGroup;
    private List<IGroup> receiversOptions;
    
    private String subject;
    private String message;

    public String getReceiversOfCopy() {
        return this.receiversOfCopy;
    }

    public void setReceiversOfCopy(String receiversOfCopy) {
        this.receiversOfCopy = receiversOfCopy;
    }

    public String getFromAddress() {
        return this.fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getFromName() {
        return this.fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public IGroup getReceiversGroup() {
        return this.receiversGroup;
    }

    public void setReceiversGroup(IGroup receiversGroup) {
        this.receiversGroup = receiversGroup;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isCopyToSenderRequested() {
        return this.copyToSenderRequested;
    }

    public void setCopyToSenderRequested(boolean copyToSenderRequested) {
        this.copyToSenderRequested = copyToSenderRequested;
    }

    public List<IGroup> getReceiversOptions() {
        return this.receiversOptions;
    }

    public void setReceiversOptions(List<IGroup> receiversOptions) {
        this.receiversOptions = receiversOptions;
    }

    public SendEMailParameters getEmailParameters() {
        SendEMailParameters parameters = new SendEMailParameters();
        
        parameters.from    = createEmailAddress(getFromName(), getFromAddress());
        parameters.subject = getSubject();
        parameters.message = getMessage();
        
        parameters.copyToSender = isCopyToSenderRequested();
        parameters.copyTo       = parseEmailAddresses(getReceiversOfCopy());

        if (getReceiversGroup() != null) {
            parameters.toRecipients = new IGroup[] { getReceiversGroup() };
        }

        return parameters;
    }

    public int getReceiversCount() {
        int count = 0;
        
        IGroup receiversGroup = getReceiversGroup();
        if (receiversGroup != null) {
            count += receiversGroup.getElementsCount();
        }
        
        EMailAddress[] receiversOfCopy = parseEmailAddresses(getReceiversOfCopy());
        count += receiversOfCopy.length;
        
        return count;
    }
    
    private EMailAddress[] parseEmailAddresses(String listOfAddresses) {
        if (listOfAddresses == null) {
            return new EMailAddress[0];
        }
        
        String[] addresses = listOfAddresses.split(",");
        List<EMailAddress> emailAddresses = new ArrayList<EMailAddress>();
        
        for (String address : addresses) {
            String trimmed = address.trim();
            
            if (trimmed.length() == 0) {
                continue;
            }
            
            emailAddresses.add(new EMailAddress(trimmed));
        }
        
        return emailAddresses.toArray(new EMailAddress[0]);
    }

    private EMailAddress createEmailAddress(String name, String address) {
        EMailAddress emailAddress = new EMailAddress();
        
        emailAddress.setPersonalName(name);
        emailAddress.setAddress(address);
        
        return emailAddress;
    }
}
