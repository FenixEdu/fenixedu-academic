package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import de.nava.informa.core.ItemIF;

public class GenerateAnnoucementsRSS extends GenerateExecutionCourseRSS {

	@Override
	public String getDescriptionPrefix() {
		return "Anúncios";
	}

	@Override
	public String getMethodName() {
		return "announcements";
	}

	@Override
	public Set getObjects(final ExecutionCourse executionCourse) {
		return executionCourse.getSite().getAssociatedAnnouncementsSet();
	}

	@Override
	public void fillItem(final ItemIF item, final DomainObject domainObject) {
		final Announcement announcement = (Announcement) domainObject;
		item.setTitle(announcement.getTitle());
		item.setDate(announcement.getLastModifiedDate());
		item.setDescription(announcement.getInformation());
	}

    @Override
    public String getIdPrefix() {
        return "a";
    }

}
