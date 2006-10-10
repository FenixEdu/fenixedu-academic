/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 28, 2006,12:26:20 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jul 28, 2006,12:26:20 PM
 * 
 */
public enum AnnouncementArchiveAnnouncementsVisibility {
    ALL, 
    ACTIVE, /* annoumcements for whose the today date is between the date of begin and end of publication AND they are visible*/ 
    VISIBLE; /* all visible announcements independently of the publication begin and end dates*/;
}
