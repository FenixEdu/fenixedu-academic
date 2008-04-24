package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender.UnitMailSenderAction;

public class SendEmailDA extends UnitMailSenderAction {

    private final static String fromName = "Conselho Científico";

    @Override
    protected String getFromName(HttpServletRequest request) {
	return fromName;
    }

}
