package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

public interface IAnnouncementManagement {
    public String addAnnouncement(String username, String password, String subject, String body, String excerpt, String keywords,
            String authorName, String authorEmail, String eventBeginDate, String eventEndDate, String place,
            String publicationBeginDate, String publicationEndDate, String[] categories, String campus, String announcementBoard,
            String editorNotes, String fileName, String fileContents, Long fileSize, MessageContext context)
            throws NotAuthorizedException;
}
