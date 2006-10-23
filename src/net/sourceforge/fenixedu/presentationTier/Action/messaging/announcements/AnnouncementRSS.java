/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 20, 2006,8:42:24 AM
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.publico.rss.RSSAction;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.ModuleUtils;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jul 20, 2006,8:42:24 AM
 * 
 */
public class AnnouncementRSS extends RSSAction {

    private String getFeedTitle(HttpServletRequest request, AnnouncementBoard board) {
        MessageResources resources = this.getResources(request, "MESSAGING_RESOURCES");
        String titlePrefix = resources.getMessage(this.getLocale(request),
                "messaging.announcements.channelTitlePrefix");
        StringBuffer buffer = new StringBuffer();
        buffer.append(titlePrefix).append(" ").append(board.getName());
        return buffer.toString();
    }

    private String getAuthor(Announcement announcement) {
        final Person person = announcement.getCreator();
        final String name;
        final String email;
        if (person != null) {
            name = person.getNickname();
            email = person.getEmail();
        } else {
            name = announcement.getAuthor();
            email = announcement.getAuthorEmail();
        }
        return autherString(name, email);
    }

    private String autherString(final String name, final String email) {
        if (name == null) {
            return "";
        }
        final StringBuffer buffer = new StringBuffer();
//        if (email != null && !email.equals("")) {
//            buffer.append("<a href=\"mailto:");
//            buffer.append(email);
//            buffer.append("\">");
//        }
        buffer.append(name);
//        if (email != null && !email.equals("")) {
//            buffer.append("</a>");
//        }
        return buffer.toString();
    }

    @Override
    protected List<SyndEntry> getFeedEntries(HttpServletRequest request) throws Exception {

        List<SyndEntry> entries = new ArrayList<SyndEntry>();

        AnnouncementBoard board = this.getSelectedBoard(request);

        if (board.getReaders() != null)
        {
            throw new FenixActionException("board.does.not.have.rss");
        }
        
        for (Announcement announcement : board.getActiveAnnouncements()) {
            SyndContent description = new SyndContentImpl();
            description.setType("text/plain");
            description.setValue(announcement.getShortBody());

            SyndEntry entry = new SyndEntryImpl();
            entry.setAuthor(this.getAuthor(announcement));
            entry.setTitle(announcement.getSubject().getContent());
            entry.setPublishedDate(announcement.getCreationDate().toDate());
            entry.setUpdatedDate(announcement.getLastModification().toDate());
            entry.setLink(this.getEntryLink(request, announcement));
            entry.setDescription(description);
            entries.add(entry);
        }

        return entries;

    }

    private String getAnnouncementBoardFeedServicePrefix(HttpServletRequest request)
            throws FenixActionException {
        String result = null;
        String scheme = request.getScheme();
        int serverPort = request.getServerPort();
        String serverName = request.getServerName();
        String appContext = PropertiesManager.getProperty("app.context");
        String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : "";
        String module = ModuleUtils.getInstance().getModuleName(request,
                getServlet().getServletContext());
        String actionPath = "/announcementsRSS.do";

        StringBuffer file = new StringBuffer();
        file.append(context).append(module).append(actionPath);
        try {
            URL url = new URL(scheme, serverName, serverPort, file.toString());
            result = url.toString();
        } catch (MalformedURLException e) {
            throw new FenixActionException(e);
        }

        return result;

    }

    private String getEntryLink(HttpServletRequest request, Announcement announcement)
            throws FenixActionException {

        String result = null;
        String scheme = request.getScheme();
        int serverPort = request.getServerPort();
        String serverName = request.getServerName();
        String appContext = PropertiesManager.getProperty("app.context");
        String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : "";

        String actionPath = "/publico/publicAnnouncements.do?method=viewAnnouncement&announcementId=";

        StringBuffer file = new StringBuffer();
        file.append(context).append(actionPath).append(announcement.getIdInternal());
        try {
            URL url = new URL(scheme, serverName, serverPort, file.toString());
            result = url.toString();
        } catch (MalformedURLException e) {
            throw new FenixActionException(e);
        }

        return result;
    }

    @Override
    protected String getFeedTitle(HttpServletRequest request) throws Exception {
        return this.getFeedTitle(request, this.getSelectedBoard(request));
    }

    @Override
    protected String getFeedDescription(HttpServletRequest request) throws Exception {
        return "";
    }

    @Override
    protected String getFeedLink(HttpServletRequest request) throws FenixActionException {
        StringBuffer buffer = new StringBuffer();
        AnnouncementBoard board = this.getSelectedBoard(request);
        buffer.append(this.getAnnouncementBoardFeedServicePrefix(request)).append("?");
        buffer.append("announcementBoardId=").append(board.getIdInternal());
        return buffer.toString();
    }

    protected final AnnouncementBoard getSelectedBoard(HttpServletRequest request) {
        String id = request.getParameter("announcementBoardId");
        AnnouncementBoard board = rootDomainObject.readAnnouncementBoardByOID(Integer.valueOf(id));
        return board;
    }

}
