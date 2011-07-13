package net.sourceforge.fenixedu.presentationTier.Action.gep.gep;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
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

@Mapping(module = "gep", path = "/alumni", scope = "session", parameter = "method")
@Forwards(value = {
		@Forward(name = "removeRecipients", path = "/gep/alumni/removeRecipients.jsp"),
		@Forward(name = "manageRecipients", path = "/gep/alumni/manageRecipients.jsp"),
		@Forward(name = "alumni.showAlumniStatistics", path = "/gep/alumni/alumniStatistics.jsp"),
		@Forward(name = "addRecipients", path = "/gep/alumni/addRecipients.jsp"),
		@Forward(name = "alumni.showAlumniDetails", path = "/gep/alumni/alumniDetails.jsp") })
public class AlumniInformationActionForGep extends net.sourceforge.fenixedu.presentationTier.Action.gep.AlumniInformationAction {
}