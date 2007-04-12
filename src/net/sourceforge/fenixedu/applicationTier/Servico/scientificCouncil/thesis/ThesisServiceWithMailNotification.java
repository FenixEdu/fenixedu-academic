package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.thesis;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging.email.SendEMail;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.cms.messaging.email.SendMailReport;
import net.sourceforge.fenixedu.domain.cms.messaging.email.EMailSender.SenderNotAllowed;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender.MailBean;

public abstract class ThesisServiceWithMailNotification extends Service {
    
    public SendMailReport run(Thesis thesis) throws FenixServiceException, SenderNotAllowed {
        process(thesis);
        return sendEmail(thesis);
    }
    
    abstract void process(Thesis thesis);

    private SendMailReport sendEmail(Thesis thesis) throws SenderNotAllowed {
        MailBean bean = createMailBean(thesis);
        
        SendEMail sendEmailService = new SendEMail();
        return sendEmailService.run(bean.getEmailParameters());
    }

    private MailBean createMailBean(Thesis thesis) {
        MailBean bean = new MailBean();

        Person person = AccessControl.getPerson();
        bean.setFromName(person.getNickname());
        bean.setFromAddress(person.getEmail());
        
        setMessage(thesis, bean);
        setReceivers(thesis, bean);
        
        return bean;
    }

    protected abstract void setMessage(Thesis thesis, MailBean bean);
    
    protected String getMessage(String key, Object ... args) {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.MessagingResources", new Locale("pt"));
        
        String message = bundle.getString(key);
        return MessageFormat.format(message, args);
    }

    private void setReceivers(Thesis thesis, MailBean bean) {
        Collection<Person> receivers = getReceivers(thesis);

        Iterator<Person> iterator = receivers.iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            
            if (person.getEmail() == null) {
                iterator.remove();
            }
        }
        
        bean.setReceiversGroup(new FixedSetGroup(receivers));
    }

    protected abstract Collection<Person> getReceivers(Thesis thesis);

    //
    // Utility methods
    //
    
    protected static Set<Person> personSet(Person ... persons) {
        Set<Person> result = new HashSet<Person>();
        
        for (Person person : persons) {
            if (person != null) {
                result.add(person);
            }
        }
        
        return result;
    }

    protected static Person getPerson(ThesisEvaluationParticipant participant) {
        if (participant == null) {
            return null;
        }
        else {
            return participant.getPerson();
        }
    }
    
}
