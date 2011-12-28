package net.sourceforge.fenixedu.domain.contacts;

import java.util.Collections;
import java.util.UUID;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import pt.ist.fenixWebFramework.services.Service;

public class EmailValidation extends EmailValidation_Base {

    public EmailValidation(PartyContact contact) {
	super();
	super.init(contact);
    }

    public String getEmailValue() {
	return ((EmailAddress) getPartyContact()).getValue();
    }

    public void generateToken() {
	if (getToken() == null) {
	    setToken(UUID.randomUUID().toString());
	}
    }

    @Override
    @Service
    public void triggerValidationProcess() {
	if (!isValid()) {
	    generateToken();
	    sendValidationEmail();
	}
    }

    private void sendValidationEmail() {
	final String token = getToken();
	final String URL = String.format(
		"https://fenix.ist.utl.pt/external/partyContactValidation.do?method=validate&validationOID=%s&token=%s",
		getExternalId(), token);
	final SystemSender sender = RootDomainObject.getInstance().getSystemSender();
	final String subject = "Sistema Fénix @ IST : Validação de Email";
	final String body_format = "Caro Utilizador\n Deverá validar o seu email introduzindo o código %s na página de verificação ou \n carregar no seguinte link : \n %s \n Os melhores cumprimentos,\n A equipa Fénix";
	final String body = String.format(body_format, token, URL);
	new Message(sender, Collections.EMPTY_LIST, Collections.EMPTY_LIST, subject, body, getEmailValue());
    }
}
