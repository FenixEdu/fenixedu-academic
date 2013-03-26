package net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.manager;

import net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.academicAdminOffice.PhdThesisProcessDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdThesisProcess", module = "manager")
@Forwards({

@Forward(name = "manageThesisJuryElements", path = "/phd/thesis/manager/manageThesisJuryElements.jsp"),

@Forward(name = "manageThesisDocuments", path = "/phd/thesis/manager/manageThesisDocuments.jsp"),

@Forward(name = "scheduleThesisDiscussion", path = "/phd/thesis/manager/scheduleThesisDiscussion.jsp"),

@Forward(name = "viewMeetingSchedulingProcess", path = "/phd/thesis/manager/viewMeetingSchedulingProcess.jsp"),

@Forward(name = "manageStates", path = "/phd/thesis/manager/manageStates.jsp"),

@Forward(name = "listConclusionProcess", path = "/phd/thesis/manager/conclusion/listConclusionProcess.jsp")

})
public class PhdThesisProcessDAForManager extends PhdThesisProcessDA {

}
