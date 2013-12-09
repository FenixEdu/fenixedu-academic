/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 20, 2006,8:42:24 AM
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.publico.rss.RSSAction;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.ModuleUtils;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
 *         Created on Jul 20, 2006,8:42:24 AM
 * 
 */
@Mapping(module = "external", path = "/announcementsRSS", scope = "request", validate = false, parameter = "method")
public class AnnouncementRSS extends RSSAction {

    protected String getFeedTitle(HttpServletRequest request, AnnouncementBoard board) {
        MessageResources resources = this.getResources(request, "MESSAGING_RESOURCES");
        String titlePrefix = resources.getMessage(this.getLocale(request), "messaging.announcements.channelTitlePrefix");
        StringBuilder buffer = new StringBuilder();
        if (board != null) {
            buffer.append(titlePrefix).append(" ").append(board.getName());
        }
        return buffer.toString();
    }

    private String getAuthor(Announcement announcement) {
        final Person person = announcement.getCreator();
        return person != null ? person.getNickname() : announcement.getAuthor();
    }

    @Override
    protected List<SyndEntryFenixImpl> getFeedEntries(HttpServletRequest request) throws Exception {

        final List<SyndEntryFenixImpl> entries = new ArrayList<SyndEntryFenixImpl>();

        final AnnouncementBoard board = this.getSelectedBoard(request);
        if (board != null) {
            if (board.getReaders() != null) {
                throw new FenixActionException("board.does.not.have.rss");
            }

            final List<Announcement> activeAnnouncements = board.getActiveAnnouncements();
            Collections.sort(activeAnnouncements, Announcement.NEWEST_FIRST);

            for (final Announcement announcement : activeAnnouncements) {

                SyndContent description = new SyndContentImpl();
                description.setType("text/plain");
                description.setValue(announcement.getBody().getContent());

                SyndEntryFenixImpl entry = new SyndEntryFenixImpl(announcement);
                entry.setAuthor(this.getAuthor(announcement));
                entry.setTitle(announcement.getSubject().getContent());
                entry.setPublishedDate(announcement.getCreationDate().toDate());
                entry.setUpdatedDate(announcement.getLastModification().toDate());
                entry.setLink(this.getEntryLink(request, announcement));
                entry.setDescription(description);
                entry.setUri(constructURI(request, announcement));
                entries.add(entry);
            }
        }

        return entries;

    }

    private String constructURI(final HttpServletRequest request, final Announcement announcement) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("_");
        stringBuilder.append(request.getServerName());
        stringBuilder.append("_announcement_");
        stringBuilder.append(announcement.getExternalId());
        return stringBuilder.toString();
    }

    private String getAnnouncementBoardFeedServicePrefix(HttpServletRequest request) throws FenixActionException {
        String result = null;
        String scheme = request.getScheme();
        int serverPort = request.getServerPort();
        String serverName = request.getServerName();
        String appContext = FenixConfigurationManager.getConfiguration().appContext();
        String context = appContext != null && appContext.length() > 0 ? "/" + appContext : "";
        String module = ModuleUtils.getInstance().getModuleName(request, getServlet().getServletContext());
        String actionPath = "/announcementsRSS.do";

        StringBuilder file = new StringBuilder();
        file.append(context).append(module).append(actionPath);
        try {
            URL url = new URL(scheme, serverName, serverPort, file.toString());
            result = url.toString();
        } catch (MalformedURLException e) {
            throw new FenixActionException(e);
        }

        return result;

    }

    private String getEntryLink(HttpServletRequest request, Announcement announcement) throws FenixActionException {
        return getBaseUrl(request, announcement)
                + announcement.getAnnouncementBoard().getSiteParamForAnnouncementBoard(announcement);
    }

    public String getBaseUrl(HttpServletRequest request, Announcement announcement) {

        StringBuilder actionPath = new StringBuilder(getDirectAnnouncementBaseUrl(request, announcement));

        String scheme = request.getScheme();
        int serverPort = request.getServerPort();
        String serverName = request.getServerName();
        String appContext = FenixConfigurationManager.getConfiguration().appContext();
        String context = appContext != null && appContext.length() > 0 ? "/" + appContext : "";

        if (actionPath.indexOf("?") == -1) {
            actionPath.append("?");
        }

        return scheme + "://" + serverName + (serverPort == 80 || serverPort == 443 ? "" : ":" + serverPort) + context
                + actionPath.toString();
    }

    protected String getDirectAnnouncementBaseUrl(HttpServletRequest request, Announcement announcement) {
        return "/publico/announcementManagement.do?method=viewAnnouncement";
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
        StringBuilder buffer = new StringBuilder();
        AnnouncementBoard board = this.getSelectedBoard(request);
        buffer.append(this.getAnnouncementBoardFeedServicePrefix(request)).append("?");
        if (board != null) {
            buffer.append("announcementBoardId=").append(board.getExternalId());
        }
        return buffer.toString();
    }

    protected final AnnouncementBoard getSelectedBoard(HttpServletRequest request) {
        final String id = request.getParameter("announcementBoardId");
        final DomainObject object = FenixFramework.getDomainObject(id);
        return object instanceof AnnouncementBoard ? (AnnouncementBoard) object : null;
    }

    @Override
    protected String getSiteLocation(HttpServletRequest request) throws Exception {
        return null;
    }

}
