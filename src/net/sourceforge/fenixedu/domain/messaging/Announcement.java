package net.sourceforge.fenixedu.domain.messaging;

import java.util.Comparator;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class Announcement extends Announcement_Base {

    public static final Comparator<Announcement> NEWEST_FIRST = new Comparator<Announcement>() {
	public int compare(Announcement o1, Announcement o2) {
	    int result = o2.getBeginComparationDate().compareTo(o1.getBeginComparationDate());
	    return result != 0 ? result : o1.getIdInternal().compareTo(o2.getIdInternal());
	}
    };

    public static final Comparator<Announcement> SUBJECT_BEGIN_DATE = new Comparator<Announcement>() {

	public int compare(Announcement o1, Announcement o2) {
	    DateTime referedSubjectBegin = o1.getReferedSubjectBegin();
	    DateTime referedSubjectBegin2 = o2.getReferedSubjectBegin();
	    if(referedSubjectBegin != null && referedSubjectBegin2 != null) {
		return referedSubjectBegin.compareTo(referedSubjectBegin2);
	    }else {
		if(referedSubjectBegin == null && referedSubjectBegin2 == null) {
		    return 0;
		}
		else {
		  return referedSubjectBegin == null ? -1 : 1; 
		}
	    }
	    
	}
	
    };
    
    private DateTime getBeginComparationDate() {
	final DateTime publicationBegin = getPublicationBegin();
	return publicationBegin != null ? publicationBegin : getCreationDate();
    }
    
    public Announcement() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        super.setCreationDate(new DateTime());
        super.setLastModification(new DateTime());
    }

    @Override
    public void setPlace(String place) {
        super.setPlace(place);
        this.updateLastModification();
    }

    @Override
    public void setAuthor(String author) {
        super.setAuthor(author);
        this.updateLastModification();
    }

    @Override
    public void setAuthorEmail(String authorEmail) {
        super.setAuthorEmail(authorEmail);
        this.updateLastModification();
    }

    @Override
    public void setBody(MultiLanguageString body) {
        super.setBody(body);
        this.updateLastModification();
    }

    @Override
    public void setExcerpt(MultiLanguageString excerpt) {
        super.setExcerpt(excerpt);
        this.updateLastModification();
    }

    @Override
    public void setVisible(Boolean visible) {
        super.setVisible(visible);
        this.updateLastModification();
    }

    private void updateLastModification() {
        super.setLastModification(new DateTime());
    }

    public boolean getOnline() {
        return ((this.getPublicationEnd() == null || this.getPublicationEnd().isAfterNow()) && getVisible());
    }

    public boolean isExcerptEmpty() {
        return this.getExcerpt() == null || this.getExcerpt().getContent() == null
                || this.getExcerpt().getContent().equals("");
    }

    public String getBodyDigest() {
        final int numberOfWords = 30;
        StringBuffer buffer = new StringBuffer();
        if (this.getBody() != null && this.getBody().getContent() != null) {
            String[] words = this.getBody().getContent().split(" ");
            for (int i = 0; i < words.length && i <= numberOfWords; i++) {
                buffer.append(words[i]).append(" ");
            }
            if (words.length > numberOfWords) {
                buffer.append("...");
            }
        }

        return buffer.toString();

    }

    public String getShortBody() {
        return this.isExcerptEmpty() ? this.getBodyDigest() : this.getExcerpt().getContent();
    }

    public boolean wasModifiedSinceCreation() {
        return this.getCreationDate().getMillis() != this.getLastModification().getMillis();
    }

    public boolean isOriginalVersion() {
	return !wasModifiedSinceCreation();
    }
    
    public boolean isActive() {
        return this.isActiveIn(new DateTime());
    }

    public boolean isActiveIn(DateTime time) {
        boolean result = true;
        result = result && (this.getCreationDate().isBefore(time));
        result = result && (this.getPublicationEnd() == null || this.getPublicationEnd().isAfter(time));
        result = result
                && (this.getPublicationBegin() == null || this.getPublicationBegin().isBefore(time));

        return result;
    }

    public boolean isActiveIn(int month, int year) {
        boolean result = true;
        result = result
                && (this.getCreationDate().getYear() < year || (this.getCreationDate().getYear() == year && this
                        .getCreationDate().getMonthOfYear() <= month));
        result = result
                && (this.getPublicationEnd() == null || this.getPublicationEnd().getYear() > year || (this
                        .getPublicationEnd().getYear() == year && this.getPublicationEnd()
                        .getMonthOfYear() >= month));
        result = result
                && (this.getPublicationBegin() == null || this.getPublicationBegin().getYear() < year || (this
                        .getPublicationBegin().getYear() == year && this.getPublicationBegin()
                        .getMonthOfYear() <= month));

        return result;
    }

    public boolean hasCreationDateFor(int year, int monthOfYear) {
	return getCreationDate().getYear() == year && getCreationDate().getMonthOfYear() == monthOfYear;
    }

    @Override
    public boolean isAnAnnouncement() {
        return true;
    }

    public AnnouncementNode getAnnouncementNode() {
        final Set<Node> parents = getParentsSet();
        return parents.isEmpty() ? null : (AnnouncementNode) parents.iterator().next();
    }

    public AnnouncementBoard getAnnouncementBoard() {
        final AnnouncementNode announcementNode = getAnnouncementNode();
        return announcementNode == null ? null : (AnnouncementBoard) announcementNode.getParent();
    }

    public void setAnnouncementBoard(final AnnouncementBoard announcementBoard) {
        if (announcementBoard == null) {
            for (final Node node : getParentsSet()) {
                node.delete();
            }
        } else {
            final AnnouncementNode announcementNode = getAnnouncementNode();
            if (announcementNode == null) {
                new AnnouncementNode(announcementBoard, this);
            } else if (announcementNode.getParent() != announcementBoard) {
                announcementNode.setParent(announcementBoard);
            }
        }
    }
    
    @Override
    public MultiLanguageString getName() {
        return getSubject();
    }

}
