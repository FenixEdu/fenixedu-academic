	update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = "org.fenixedu.messaging.core.domain.Sender" WHERE DOMAIN_CLASS_NAME like "%DelegateSender%";
	

	delete from SENDER;
	delete from SENDER_PERSISTENT_GROUP_RECIPIENTS;
	delete from MESSAGE;
	delete from MESSAGE_RECIPIENT_TO;
	delete from MESSAGE_RECIPIENT_CC;
	delete from MESSAGE_RECIPIENT_BCC;
	delete from MESSAGE_DISPATCH_REPORT;
	delete from MESSAGING_SYSTEM;
	

	update PARTY set OID_SENDER = NULL;
	update ACADEMIC_PROGRAM set OID_SENDER = NULL;
	update EXECUTION_COURSE set OID_SENDER = NULL;
	update DELEGATE set OID_SENDER = NULL;
	update BENNU set OID_MESSAGING_SYSTEM = NULL;
	update BENNU set OID_SYSTEM_SENDER = NULL;