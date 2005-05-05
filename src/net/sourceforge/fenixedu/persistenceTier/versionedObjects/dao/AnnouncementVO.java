package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IAnnouncement;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAnnouncement;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * 
 * @author Luis Cruz
 */
public class AnnouncementVO extends VersionedObjectsBase implements IPersistentAnnouncement {

    public List readAnnouncementsBySite(final Integer siteOID) throws ExcepcaoPersistencia {
        final ISite site = (ISite) readByOID(Site.class, siteOID);
        return (site != null) ? site.getAssociatedAnnouncements() : new ArrayList();
    }

    public IAnnouncement readAnnouncementByTitleAndCreationDateAndSite(final String title,
            final Date cDate, final Integer siteOID) throws ExcepcaoPersistencia {
        final List announcements = readAnnouncementsBySite(siteOID);
        for (final Iterator iterator = announcements.iterator(); iterator.hasNext();) {
            final IAnnouncement announcement = (IAnnouncement) iterator.next();
            if (announcement.getTitle().equals(title) && announcement.getCreationDate().equals(cDate)) {
                return announcement;
            }
        }
        return null;
    }

    public IAnnouncement readLastAnnouncementForSite(final Integer siteOID) throws ExcepcaoPersistencia {
        IAnnouncement lastAnnouncement = null;

        final List announcements = readAnnouncementsBySite(siteOID);
        for (final Iterator iterator = announcements.iterator(); iterator.hasNext(); ) {
            final IAnnouncement announcement = (IAnnouncement) iterator.next();
            if (lastAnnouncement == null || announcement.getCreationDate().after(lastAnnouncement.getCreationDate())) {
                lastAnnouncement = announcement;
            }
        }

        return lastAnnouncement;
    }

}