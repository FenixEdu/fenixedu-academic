/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 31, 2006,3:31:26 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.util.HostAccessControl;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.thoughtworks.xstream.XStream;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jul 31, 2006,3:31:26 PM
 * 
 */
public class AnnouncementBoardExport extends ExternalInterfaceDispatchAction {

    protected Integer getAnnouncementBoardId(HttpServletRequest request) {
	return request.getParameter("announcementBoardId") == null ? null : Integer.valueOf(request
		.getParameter("announcementBoardId"));
    }

    protected AnnouncementBoard getRequestedAnnouncementBoard(HttpServletRequest request) {
	return rootDomainObject.readAnnouncementBoardByOID(this.getAnnouncementBoardId(request));
    }

    protected String getRequestedLanguageString(HttpServletRequest request) {
	return request.getParameter("language");
    }

    protected Language getRequestedLanguage(HttpServletRequest request) {
	if (this.getRequestedLanguageString(request) == null) {
	    return Language.pt;
	} else {
	    return Language.valueOf(getRequestedLanguageString(request));
	}
    }

    public ActionForward getAnnouncements(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	String responseCode = SERVICE_NOT_EXECUTED;
	String responseString = String.valueOf("");

	if (HostAccessControl.isAllowed(this, request)
		&& getRequestedAnnouncementBoard(request).getReaders() == null) {
	    final AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
	    responseString = buildInfo(buildDTOCollection((List<Announcement>) board
		    .getVisibleAnnouncements(), request));
	    responseCode = SUCCESS_CODE;

	} else {
	    responseCode = NOT_AUTHORIZED_CODE;
	}

	super.writeResponse(response, responseCode, responseString);

	return null;
    }

    private Integer getSelectedMonth(HttpServletRequest request) {
	return request.getParameter("selectedMonth") == null ? null : Integer.valueOf(request
		.getParameter("selectedMonth"));
    }

    private Integer getSelectedYear(HttpServletRequest request) {
	return request.getParameter("selectedYear") == null ? null : Integer.valueOf(request
		.getParameter("selectedYear"));
    }

    private static final Comparator<Announcement> EXTERNAL_ANNOUNCEMENTS_COMPARATOR_BY_NEWEST_FIRST = new Comparator<Announcement>() {
	public int compare(Announcement o1, Announcement o2) {
	    if (o1.getReferedSubjectBegin() != null && o2.getReferedSubjectBegin() != null) {
		int result = o1.getReferedSubjectBegin().compareTo(o2.getReferedSubjectBegin());
		return (result != 0) ? -(result) : o1.getIdInternal().compareTo(o2.getIdInternal());
	    }
	    return Announcement.NEWEST_FIRST.compare(o1, o2);
	}
    };

    private Collection<AnnouncementDTO> buildDTOCollection(List<Announcement> announcements,
	    HttpServletRequest request) throws ParseException {

	Collections.sort(announcements, EXTERNAL_ANNOUNCEMENTS_COMPARATOR_BY_NEWEST_FIRST);

	final Collection<AnnouncementDTO> result = new ArrayList<AnnouncementDTO>(announcements.size());

	final Language language = this.getRequestedLanguage(request);
	final String dateFormat = "dd/MM/yyyy HH:mm";
	final Integer selectedYear = this.getSelectedYear(request);
	final Integer selectedMonth = this.getSelectedMonth(request);

	for (final Announcement announcement : announcements) {

	    if (selectedYear == null || selectedMonth == null || (announcement.isActiveIn(selectedMonth, selectedYear))) {

		final AnnouncementDTO dto = new AnnouncementDTO();
		
		dto.setCreationDate(announcement.getCreationDate().toString(dateFormat));
		dto.setLastModification(announcement.getLastModification().toString(dateFormat));
		
		dto.setReferedSubjectBegin(announcement.getReferedSubjectBegin() == null ? null : announcement.getReferedSubjectBegin().toString(dateFormat));
		dto.setReferedSubjectEnd(announcement.getReferedSubjectEnd() == null ? null : announcement.getReferedSubjectEnd().toString(dateFormat));
		dto.setPublicationBegin(announcement.getPublicationBegin() == null ? null : announcement.getPublicationBegin().toString(dateFormat));
		dto.setPublicationEnd(announcement.getPublicationEnd() == null ? null : announcement.getPublicationEnd().toString(dateFormat));
		
		dto.setAuthor(announcement.getAuthor());
		dto.setAuthorEmail(announcement.getAuthorEmail());
		
		dto.setSubject(announcement.getSubject() == null ? null : announcement.getSubject().hasLanguage(language) ? announcement.getSubject().getContent(language) : announcement.getSubject().getContent());
		dto.setBody(announcement.getBody().hasLanguage(language) ? announcement.getBody().getContent(language) : announcement.getBody().getContent());
		dto.setExcerpt(announcement.getExcerpt() == null ? null : announcement.getExcerpt().hasLanguage(language) ? announcement.getExcerpt().getContent(language) : announcement.getExcerpt().getContent(language));
		dto.setKeywords(announcement.getKeywords() == null ? null : announcement.getKeywords().hasLanguage(language) ? announcement.getKeywords().getContent(language) : announcement.getKeywords().getContent());
		
		dto.setPlace(announcement.getPlace());
		
		dto.setVisible(announcement.getVisible().toString());
		dto.setId(announcement.getIdInternal().toString());
		
		result.add(dto);
	    }
	}

	return result;
    }
    

    private String buildInfo(Collection announcements) {
        return new XStream().toXML(announcements);
    }
}
