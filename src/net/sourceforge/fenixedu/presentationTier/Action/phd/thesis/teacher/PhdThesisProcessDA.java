package net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.teacher;

import net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.CommonPhdThesisProcessDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/phdThesisProcess", module = "teacher")
@Forwards(tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp"), value = {

@Forward(name = "manageThesisDocuments", path = "/phd/thesis/teacher/manageThesisDocuments.jsp"),

@Forward(name = "juryReporterFeedbackUpload", path = "/phd/thesis/teacher/juryReporterFeedbackUpload.jsp"),

@Forward(name = "scheduleThesisMeeting", path = "/phd/thesis/teacher/scheduleThesisMeeting.jsp")

})
public class PhdThesisProcessDA extends CommonPhdThesisProcessDA {

}
