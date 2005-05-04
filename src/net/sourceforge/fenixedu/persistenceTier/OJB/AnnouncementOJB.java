package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.IAnnouncement;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAnnouncement;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author EP15
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a>
 */
public class AnnouncementOJB extends PersistentObjectOJB implements IPersistentAnnouncement {
    
    public IAnnouncement readAnnouncementByTitleAndCreationDateAndSite(String title, Date cDate,
            Integer siteOID) throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("title", title);
        crit.addEqualTo("creationDate", cDate);
        crit.addEqualTo("site.idInternal", siteOID);
        return (IAnnouncement) queryObject(Announcement.class, crit);

    }

    public List readAnnouncementsBySite(Integer siteOID) throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("site.idInternal", siteOID);
        return queryList(Announcement.class, crit, "lastModifiedDate", false);

    }

    /**
     * @see ServidorPersistente.IPersistentAnnouncement#readLastAnnouncement()
     */
    public IAnnouncement readLastAnnouncementForSite(Integer siteOID) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("site.idInternal", siteOID);
        List result = queryList(Announcement.class, crit, "lastModifiedDate", false);
        if (result != null && !result.isEmpty()) {
            return (IAnnouncement) result.get(0);
        }

        return null;

    }

}