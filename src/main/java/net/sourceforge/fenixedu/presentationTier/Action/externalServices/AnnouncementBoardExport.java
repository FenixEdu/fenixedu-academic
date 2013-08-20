/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 31, 2006,3:31:26 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.util.HostAccessControl;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.IllegalFieldValueException;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.i18n.Language;

import com.thoughtworks.xstream.XStream;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
 *         Created on Jul 31, 2006,3:31:26 PM
 * 
 */
@Mapping(module = "external", path = "/exportAnnouncementBoard", scope = "request", validate = false, parameter = "method")
public class AnnouncementBoardExport extends ExternalInterfaceDispatchAction {

    public ActionForward getAnnouncements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String responseCode = SERVICE_NOT_EXECUTED;
        String responseString = String.valueOf("");

        if (getRequestedAnnouncementBoard(request).getReaders() == null) {
            final AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
            responseString = buildInfo(buildDTOCollection((List<Announcement>) board.getVisibleAnnouncements(), request));
            responseCode = SUCCESS_CODE;

        } else {
            responseCode = NOT_AUTHORIZED_CODE;
        }

        super.writeResponse(response, responseCode, responseString);

        return null;
    }

    public ActionForward getStickyAnnouncements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String responseCode = SERVICE_NOT_EXECUTED;
        String responseString = String.valueOf("");

        if (HostAccessControl.isAllowed(this, request) && getRequestedAnnouncementBoard(request).getReaders() == null) {
            final AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
            responseString = buildInfo(buildStickyDTOCollection((List<Announcement>) board.getVisibleAnnouncements(), request));
            responseCode = SUCCESS_CODE;

        } else {
            responseCode = NOT_AUTHORIZED_CODE;
        }

        super.writeResponse(response, responseCode, responseString);

        return null;
    }

    protected AnnouncementBoard getRequestedAnnouncementBoard(final HttpServletRequest request) {
        return getDomainObject(request, "announcementBoardId");
    }

    protected String getRequestedLanguageString(HttpServletRequest request) {
        return request.getParameter("language");
    }

    protected Language getRequestedLanguage(HttpServletRequest request) {
        final String language = getRequestedLanguageString(request);
        return (language == null) ? Language.pt : Language.valueOf(getRequestedLanguageString(request));
    }

    private Integer getSelectedMonth(HttpServletRequest request) {
        return getRequestParameterAsInteger(request, "selectedMonth");
    }

    private Integer getSelectedYear(HttpServletRequest request) {
        return getRequestParameterAsInteger(request, "selectedYear");
    }

    protected Integer getRequestParameterAsInteger(HttpServletRequest request, String parameterName) {
        final String requestParameter = request.getParameter(parameterName);

        if (!StringUtils.isEmpty(requestParameter)) {
            return Integer.valueOf(requestParameter);
        } else {
            return null;
        }
    }

    private static final Comparator<Announcement> EXTERNAL_ANNOUNCEMENTS_COMPARATOR_BY_NEWEST_FIRST =
            new Comparator<Announcement>() {
                @Override
                public int compare(Announcement o1, Announcement o2) {
                    final DateTime d1 = getDateTime(o1);
                    final DateTime d2 = getDateTime(o2);
                    final int c = d2.compareTo(d1);
                    return c == 0 ? o1.getExternalId().compareTo(o2.getExternalId()) : c;
                }

                private DateTime getDateTime(final Announcement a) {
                    final DateTime rdt = a.getReferedSubjectBegin();
                    final DateTime pdt = a.getPublicationBegin();
                    return rdt == null ? (pdt == null ? a.getCreationDate() : pdt) : rdt;
                }
            };

    private static final Comparator<Announcement> EXTERNAL_ANNOUNCEMENTS_COMPARATOR_BY_PRIORITY_AND_NEWEST_FIRST =
            new Comparator<Announcement>() {
                @Override
                public int compare(Announcement o1, Announcement o2) {
                    if (o1.getSticky() && o2.getSticky()) {
                        final int p = o1.getPriority() - o2.getPriority();
                        return p == 0 ? EXTERNAL_ANNOUNCEMENTS_COMPARATOR_BY_NEWEST_FIRST.compare(o1, o2) : p;
                    } else if (o1.getSticky()) {
                        return -1;
                    } else if (o2.getSticky()) {
                        return 1;
                    } else {
                        return EXTERNAL_ANNOUNCEMENTS_COMPARATOR_BY_NEWEST_FIRST.compare(o1, o2);
                    }
                }
            };

    private Collection<AnnouncementDTO> buildDTOCollection(final List<Announcement> announcements,
            final HttpServletRequest request) {

        Collections.sort(announcements, EXTERNAL_ANNOUNCEMENTS_COMPARATOR_BY_PRIORITY_AND_NEWEST_FIRST);

        final Language language = getRequestedLanguage(request);
        final Integer selectedYear = getSelectedYear(request);
        final Integer selectedMonth = getSelectedMonth(request);

        final Collection<AnnouncementDTO> result = new ArrayList<AnnouncementDTO>(announcements.size());
        for (final Announcement announcement : announcements) {
            try {
                if ((announcement.getPublication() == null ? false : announcement.getPublication())
                        && (selectedYear == null || selectedMonth == null
                                || (announcement.isActiveIn(selectedYear, selectedMonth)) || announcement.getSticky())
                        && testContentAvailabilityForLanguage(announcement, language)) {
                    result.add(new AnnouncementDTO(announcement, language));
                }
            } catch (IllegalFieldValueException e) {
            }
        }
        return result;
    }

    /**
     * deprecated.. (not in use so..: remove)
     * 
     * @param announcements
     * @param request
     * @return
     */
    private Collection<AnnouncementDTO> buildStickyDTOCollection(final List<Announcement> announcements,
            final HttpServletRequest request) {

        final Language language = getRequestedLanguage(request);
        // filter sticky announcements
        final Collection<Announcement> stickyAnnouncements = new ArrayList<Announcement>();
        for (Announcement announcement : announcements) {
            if (announcement.getSticky()) {
                stickyAnnouncements.add(announcement);
            }
        }
        // build DTO collection
        final Collection<AnnouncementDTO> result = new ArrayList<AnnouncementDTO>(stickyAnnouncements.size());
        for (final Announcement announcement : stickyAnnouncements) {
            result.add(new AnnouncementDTO(announcement, language));
        }
        return result;
    }

    private String buildInfo(Collection announcements) {
        return new XStream().toXML(announcements);
    }

    private boolean testContentAvailabilityForLanguage(Announcement announcement, Language language) {
        if (announcement.getSubject() != null && !announcement.getSubject().hasLanguage(language)) {
            return false;
        }
        if (announcement.getBody() != null && !announcement.getBody().hasLanguage(language)) {
            return false;
        }
        if (announcement.getExcerpt() != null && !announcement.getExcerpt().hasLanguage(language)) {
            return false;
        }
        if (announcement.getKeywords() != null && !announcement.getKeywords().hasLanguage(language)) {
            return false;
        }
        return true;
    }
}
