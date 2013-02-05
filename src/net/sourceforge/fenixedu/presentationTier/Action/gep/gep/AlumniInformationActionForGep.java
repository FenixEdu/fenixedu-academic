package net.sourceforge.fenixedu.presentationTier.Action.gep.gep;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "gep", path = "/alumni", scope = "session", parameter = "method")
@Forwards(value = {
        @Forward(name = "removeRecipients", path = "/gep/alumni/removeRecipients.jsp"),
        @Forward(name = "manageRecipients", path = "/gep/alumni/manageRecipients.jsp", tileProperties = @Tile(
                title = "private.gep.alumni.recipientmanagement")),
        @Forward(name = "alumni.showAlumniStatistics", path = "/gep/alumni/alumniStatistics.jsp", tileProperties = @Tile(
                title = "private.gep.alumni.statistics")),
        @Forward(name = "addRecipients", path = "/gep/alumni/addRecipients.jsp"),
        @Forward(name = "alumni.showAlumniDetails", path = "/gep/alumni/alumniDetails.jsp", tileProperties = @Tile(
                title = "private.gep.alumni.search")) })
public class AlumniInformationActionForGep extends net.sourceforge.fenixedu.presentationTier.Action.gep.AlumniInformationAction {
}