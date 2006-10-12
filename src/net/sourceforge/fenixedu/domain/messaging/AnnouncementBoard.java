package net.sourceforge.fenixedu.domain.messaging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;

public abstract class AnnouncementBoard extends AnnouncementBoard_Base {

    public static final Comparator<AnnouncementBoard> NEWEST_FIRST = new Comparator<AnnouncementBoard>() {
	public int compare(AnnouncementBoard o1, AnnouncementBoard o2) {
	    int result = -o1.getCreationDate().compareTo(o2.getCreationDate());
	    return (result == 0) ? o1.getIdInternal().compareTo(o2.getIdInternal()) : result;
	}
    };
    
    public static final Comparator<AnnouncementBoard> BY_NAME = new Comparator<AnnouncementBoard>() {
	public int compare(AnnouncementBoard o1, AnnouncementBoard o2) {
	    int result = o1.getName().compareTo(o2.getName());
	    return (result == 0) ? o1.getIdInternal().compareTo(o2.getIdInternal()) : result;
	}
    };


    public static class AcessLevelPredicate implements Predicate {
	AnnouncementBoardAccessLevel level;
	Person person;

	public AcessLevelPredicate(AnnouncementBoardAccessLevel level, Person person) {
	    this.level = level;
	    this.person = person;
	}

	public boolean evaluate(Object arg0) {
	    final AnnouncementBoard board = (AnnouncementBoard) arg0;
	    boolean result = false;
	    switch (level) {
	    case ABAL_ALL:
		if (board.getManagers() == null || board.getWriters() == null
			|| board.getReaders() == null || board.getManagers().isMember(person)
			|| board.getWriters().isMember(person) || board.getReaders().isMember(person)) {
		    result = true;
		}
		break;
	    case ABAL_MANAGE:
		if (board.getManagers() == null || board.getManagers().isMember(person)) {
		    result = true;
		}
		break;
	    case ABAL_READ:
		if (board.getReaders() == null || board.getReaders().isMember(person)) {
		    result = true;
		}
		break;
	    case ABAL_WRITE:
		if (board.getWriters() == null || board.getWriters().isMember(person)) {
		    result = true;
		}
		break;
	    }

	    return result;
	}
    }

    public static class AcessTypePredicate implements Predicate {
	AnnouncementBoardAccessType type;
	Person person;

	public AcessTypePredicate(AnnouncementBoardAccessType type, Person person) {
	    this.type = type;
	    this.person = person;
	}

	public boolean evaluate(Object arg0) {
	    final AnnouncementBoard board = (AnnouncementBoard) arg0;
	    switch (type) {
	    case ABAT_PUBLIC:
		return board.isPublicToRead();
		
	    case ABAT_PRIVATE:
		return (board.getReaders() != null);
		
	    case ABAT_ALL:
		return true;
		
	    default:
		return false;
	    }
	}
    }

    public AnnouncementBoard() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(this.getClass().getName());
	setCreationDate(new DateTime());
    }

    @Override
    public java.util.List<Announcement> getAnnouncements() {
	Collection<Announcement> orderedAnnouncements = new TreeSet<Announcement>(Announcement.NEWEST_FIRST);
	orderedAnnouncements.addAll(super.getAnnouncements());
	return new ArrayList<Announcement>(orderedAnnouncements); 
    }

    public Collection<Announcement> getActiveAnnouncements() {
	final Collection<Announcement> activeAnnouncements = new ArrayList<Announcement>();
	for (final Announcement announcement : this.getAnnouncements()) {
	    if (announcement.isActive() && announcement.getVisible()) {
		activeAnnouncements.add(announcement);
	    }
	}
	return activeAnnouncements;
    }

    public Collection<Announcement> getVisibleAnnouncements() {
	final Collection<Announcement> activeAnnouncements = new ArrayList<Announcement>();
	for (final Announcement announcement : this.getAnnouncements()) {
	    if (announcement.getVisible()) {
		activeAnnouncements.add(announcement);
	    }
	}
	return activeAnnouncements;
    }

    public int getActiveAnnouncementsCount() {
	return this.getActiveAnnouncements().size();
    }

    public void delete() {
	if (!canDelete()) {
	    throw new DomainException("error.messaging.announcementBoard.cannot.delete");
	}
	
	removeBookmarkedBoards();
	removeRootDomainObject();
	deleteDomainObject();
    }

    public boolean canDelete() {
	return !hasAnyAnnouncements();
    }

    private void removeBookmarkedBoards() {
	for (final Person person : this.getBookmarkOwner()) {
	    person.removeBookmarkedBoards(this);
	}
    }
    
    private boolean isPublicToRead() {
	return getReaders() == null;
    }
    
    private boolean isPublicToWrite() {
	return getWriters() == null;
    }
    
    private boolean isPublicToManage() {
	return getManagers() == null;
    }
    
    public boolean hasReader(Person person) {
	return (isPublicToRead() || getReaders().isMember(person));
    }
    
    public boolean hasWriter(Person person) {
	return (isPublicToWrite() || getWriters().isMember(person));
    }
    
    public boolean hasManager(Person person) {
	return (isPublicToManage() || getManagers().isMember(person));
    }
    
    abstract public String getFullName();

    abstract public String getQualifiedName();
}
