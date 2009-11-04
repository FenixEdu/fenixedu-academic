
UPDATE EVENT SET EVENT.OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.phd.debts.PhdRegistrationFee" WHERE EVENT.OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.phd.PhdRegistrationFee";
UPDATE POSTING_RULE SET POSTING_RULE.OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.phd.debts.PhdRegistrationFeePR" WHERE POSTING_RULE.OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.phd.PhdRegistrationFeePR";
UPDATE EXEMPTION SET EXEMPTION.OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.phd.debts.PhdRegistrationFeePenaltyExemption" WHERE EXEMPTION.OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.phd.PhdRegistrationFeePenaltyExemption";

UPDATE FF$DOMAIN_CLASS_INFO SET DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.phd.debts.PhdRegistrationFee" WHERE DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.phd.PhdRegistrationFee";
UPDATE FF$DOMAIN_CLASS_INFO SET DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.phd.debts.PhdRegistrationFeePR" WHERE DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.phd.PhdRegistrationFeePR";
UPDATE FF$DOMAIN_CLASS_INFO SET DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.phd.debts.PhdRegistrationFeePenaltyExemption" WHERE DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.phd.PhdRegistrationFeePenaltyExemption";
