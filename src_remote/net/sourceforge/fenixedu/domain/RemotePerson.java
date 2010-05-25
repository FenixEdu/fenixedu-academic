package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.RemotePerson_Base;

public class RemotePerson extends RemotePerson_Base {
    
    public RemotePerson() {
        super();
    }

    public String getEmailForSendingEmails() {
	return (String) readRemoteMethod("getEmailForSendingEmails");
    }

}
