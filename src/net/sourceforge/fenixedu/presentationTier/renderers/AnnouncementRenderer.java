package net.sourceforge.fenixedu.presentationTier.renderers;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.Face;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class AnnouncementRenderer extends OutputRenderer {

    private String viewMoreUrl;
    private String viewMoreLabel;
    private String bundle;
    private String fromLabel;
    private String toLabel;
    private String placeSeparator;
    private boolean authorShown;
    private String authorLabel;
    private String dateFormat;
    private String inLabel;
    
    private String subjectClasses;
    private String dateClasses;
    private String contentClasses;
    private String authorClasses;
    private int soonMarkerInDays;
    private String soonMarkerClasses;
    private String soonMarkerLabel;
    
    public String getSoonMarkerClasses() {
        return soonMarkerClasses;
    }

    public void setSoonMarkerClasses(String soonMarkerClasses) {
        this.soonMarkerClasses = soonMarkerClasses;
    }

    public int getSoonMarkerInDays() {
        return soonMarkerInDays;
    }

    public void setSoonMarkerInDays(int soonMarkerInDays) {
        this.soonMarkerInDays = soonMarkerInDays;
    }

    public String getSubjectClasses() {
	return subjectClasses;
    }

    public void setSubjectClasses(String subjectClasses) {
	this.subjectClasses = subjectClasses;
    }

    public String getDateClasses() {
	return dateClasses;
    }

    public void setDateClasses(String dateClasses) {
	this.dateClasses = dateClasses;
    }

    public String getContentClasses() {
	return contentClasses;
    }

    public void setContentClasses(String contentClasses) {
	this.contentClasses = contentClasses;
    }

    public String getViewMoreUrl() {
	return viewMoreUrl;
    }

    public void setViewMoreUrl(String viewMore) {
	this.viewMoreUrl = viewMore;
    }

    public String getBundle() {
	return bundle;
    }

    public void setBundle(String bundle) {
	this.bundle = bundle;
    }

    public String getFromLabel() {
	return fromLabel;
    }

    public void setFromLabel(String fromLabel) {
	this.fromLabel = fromLabel;
    }

    public String getToLabel() {
	return toLabel;
    }

    public void setToLabel(String toLabel) {
	this.toLabel = toLabel;
    }

    public String getPlaceSeparator() {
	return placeSeparator;
    }

    public void setPlaceSeparator(String placeSeparator) {
	this.placeSeparator = placeSeparator;
    }

    public boolean isAuthorShown() {
	return authorShown;
    }

    public void setAuthorShown(boolean authorShown) {
	this.authorShown = authorShown;
    }

    public String getAuthorLabel() {
	return authorLabel;
    }

    public void setAuthorLabel(String authorLabel) {
	this.authorLabel = authorLabel;
    }

    public String getAuthorClasses() {
	return authorClasses;
    }

    public void setAuthorClasses(String authorClasses) {
	this.authorClasses = authorClasses;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	Announcement announcement = (Announcement) object;
	return new AnnouncementLayout(announcement);
    }

    private class AnnouncementLayout extends Layout {

	private final Announcement announcement;

	public AnnouncementLayout(Announcement announcement) {
	    this.announcement = announcement;
	}

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    HtmlBlockContainer blockContainer = new HtmlBlockContainer();
	    blockContainer.addChild(getSubject());
	    if(isAuthorShown()) {
		blockContainer.addChild(getAuthor());
	    }
	    blockContainer.addChild(getDate());
	    blockContainer.addChild(getContent());
	    
	    return blockContainer;
	}

	private HtmlComponent getContent() {
	    HtmlBlockContainer container = new HtmlBlockContainer();
	    if(announcement.isExcerptEmpty()) {
		container.addChild(new HtmlText(ContentInjectionRewriter.BLOCK_HAS_CONTEXT_PREFIX,false));
		container.addChild(new HtmlText(announcement.getBody().getContent(),false));
		container.addChild(new HtmlText(ContentInjectionRewriter.END_BLOCK_HAS_CONTEXT_PREFIX,false));
	    }else {
		container.addChild(new HtmlText(ContentInjectionRewriter.BLOCK_HAS_CONTEXT_PREFIX,false));
		container.addChild(new HtmlText(announcement.getExcerpt().getContent(),false));
		container.addChild(new HtmlText(ContentInjectionRewriter.END_BLOCK_HAS_CONTEXT_PREFIX,false));
		HtmlLink link = new HtmlLink();
		link.setUrl(RenderUtils.getFormattedProperties(getViewMoreUrl(), announcement));
		link.setText(RenderUtils.getFormatedResourceString(getBundle(), getViewMoreLabel()));
		container.addChild(new HtmlText("<br/>",false));
		container.addChild(link);
	    }
	    container.setClasses(getContentClasses());
	    return container;
	}
	
	private HtmlComponent getDate() {
	    StringBuffer buffer = new StringBuffer("");
	    if (isCurrentAnnouncentAnEvent()) {
		buffer.append(RenderUtils.getFormattedProperties(getDateFormat(), announcement.getReferedSubjectBegin()));
		if (announcement.getReferedSubjectEnd() != null) {
		    String fromLabel = RenderUtils.getResourceString(getBundle(), getFromLabel());
		    buffer.insert(0, fromLabel);
		    buffer.insert(fromLabel.length(), " ");
		    buffer.append(" ");
		    buffer.append(RenderUtils.getResourceString(getBundle(), getToLabel()));
		    buffer.append(" ");
		    buffer.append(RenderUtils.getFormattedProperties(getDateFormat(), announcement.getReferedSubjectEnd()));
		}else {
		    String inLabel = RenderUtils.getResourceString(getBundle(), getInLabel());
		    buffer.insert(0, inLabel);
		    buffer.insert(inLabel.length(), " ");
		}
		if(!StringUtils.isEmpty(announcement.getPlace())) {
		    buffer.append(getPlaceSeparator());
		    buffer.append(announcement.getPlace());
		}
	    } else {
		buffer.append(RenderUtils.getFormattedProperties(getDateFormat(), announcement.getCreationDate()));
	    }
	    HtmlText text = new HtmlText(buffer.toString());
	    text.setClasses(getDateClasses());
	    return text;
	}

	private HtmlComponent getAuthor() {
	    Person person = announcement.getCreator();
	    StringBuffer buffer = new StringBuffer("");
	    buffer.append(RenderUtils.getResourceString(getBundle(), getAuthorLabel()));
	    buffer.append(": ");
	    buffer.append(person.getName());

	    HtmlText text = new HtmlText(buffer.toString());
	    text.setClasses(getAuthorClasses());
	    return text;
	}

	private HtmlComponent getSubject() {
	    HtmlInlineContainer inlineContainer = new HtmlInlineContainer();
	    HtmlText subject = new HtmlText(announcement.getSubject().getContent());
	    subject.setClasses(getSubjectClasses());
	    subject.setFace(Face.H3);
	    inlineContainer.addChild(subject);
	    if(needsMarker()) {
		HtmlText soonMarker = new HtmlText(RenderUtils.getResourceString(getBundle(), getSoonMarkerLabel()));
		soonMarker.setClasses(getSoonMarkerClasses());
		inlineContainer.addChild(soonMarker);
	    }
	    return inlineContainer;
	}

	private boolean needsMarker() {
	    DateTime date = announcement.getReferedSubjectBegin();
	    YearMonthDay begin = date != null ? date.toYearMonthDay() : null;
	    YearMonthDay currentDay = new YearMonthDay();
	    YearMonthDay useMarkerStartDay = begin != null ? begin.minusDays(getSoonMarkerInDays()) : null ;
	    return begin != null && (currentDay.equals(useMarkerStartDay) || (currentDay.isAfter(useMarkerStartDay) && currentDay.isBefore(begin)));   
	}

	private boolean isCurrentAnnouncentAnEvent() {
	    return announcement.getReferedSubjectBegin() != null || announcement.getReferedSubjectEnd() != null;
	}

    }

    public String getDateFormat() {
	return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
	this.dateFormat = dateFormat;
    }

    public String getViewMoreLabel() {
        return viewMoreLabel;
    }

    public void setViewMoreLabel(String viewMoreLabel) {
        this.viewMoreLabel = viewMoreLabel;
    }

    public String getInLabel() {
        return inLabel;
    }

    public void setInLabel(String inLabel) {
        this.inLabel = inLabel;
    }

    public String getSoonMarkerLabel() {
        return soonMarkerLabel;
    }

    public void setSoonMarkerLabel(String soonMarkerLabel) {
        this.soonMarkerLabel = soonMarkerLabel;
    }

}
