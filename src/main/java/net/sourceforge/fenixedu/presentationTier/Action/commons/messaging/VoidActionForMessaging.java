package net.sourceforge.fenixedu.presentationTier.Action.commons.messaging;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "messaging", path = "/index", scope = "session")
@Forwards(value = { @Forward(name = "Success", path = "/announcements/announcementsStartPageHandler.do?method=news") })
public class VoidActionForMessaging extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}