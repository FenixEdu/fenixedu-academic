package net.sourceforge.fenixedu.domain.messaging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;

public abstract class AnnouncementBoard extends AnnouncementBoard_Base {

    public static class AnnouncementPresentationBean extends TreeSet<Announcement> {

        private int maxElements = 0;

        private Comparator<Announcement> comparator;

        public AnnouncementPresentationBean(final int maxElements, final Comparator<Announcement> comparator) {
            super(comparator);
            this.maxElements = maxElements;
            this.comparator = comparator;
        }

        @Override
        public boolean add(Announcement announcement) {
            if (size() < maxElements) {
                return super.add(announcement);
            }
            final Announcement oldestAnnouncement = last();
            if (comparator.compare(announcement, oldestAnnouncement) < 0) {
                remove(oldestAnnouncement);
                return super.add(announcement);
            }
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends Announcement> c) {
            for (final Announcement announcement : c) {
                add(announcement);
            }
            return true;
        }

    }

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

    public List<Announcement> getActiveAnnouncements() {
	final List<Announcement> activeAnnouncements = new ArrayList<Announcement>();
	for (final Announcement announcement : getAnnouncementsSet()) {
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

    public void addVisibleAnnouncements(final AnnouncementPresentationBean announcementPresentationBean) {
        for (final Announcement announcement : this.getAnnouncements()) {
            if (announcement.getVisible()) {
                announcementPresentationBean.add(announcement);
            }
        }
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
    
    public boolean hasReaderOrWriter(Person person) {
	return hasReader(person) || hasWriter(person);
    }
    
    abstract public String getFullName();

    abstract public String getQualifiedName();

}