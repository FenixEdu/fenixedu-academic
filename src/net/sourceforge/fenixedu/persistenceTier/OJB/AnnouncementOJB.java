package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.sql.Timestamp;
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
    public IAnnouncement readAnnouncementByTitleAndCreationDateAndSite(String title, Timestamp cDate,
            ISite site) throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("title", title);
        crit.addEqualTo("creationDate", cDate);
        crit.addEqualTo("site.idInternal", site.getIdInternal());
        return (IAnnouncement) queryObject(Announcement.class, crit);

    }

    public void delete(IAnnouncement announcement) throws ExcepcaoPersistencia {
        super.delete(announcement);
    }

    public List readAnnouncementsBySite(ISite site) throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("site.idInternal", site.getIdInternal());
        return queryList(Announcement.class, crit, "lastModifiedDate", false);

    }

    /**
     * @see ServidorPersistente.IPersistentAnnouncement#readLastAnnouncement()
     */
    public IAnnouncement readLastAnnouncementForSite(ISite site) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("site.idInternal", site.getIdInternal());
        List result = queryList(Announcement.class, crit, "lastModifiedDate", false);
        if (result != null && !result.isEmpty()) {
            return (IAnnouncement) result.get(0);
        }

        return null;

    }
    
    /**
     * except the (real) last one
     */
    public List readLastAnnouncementsForSite(ISite site, int n) throws ExcepcaoPersistencia
	{
    		Criteria crit = new Criteria();
    		crit.addEqualTo("site.idInternal", site.getIdInternal());
    		// [ccfp] - change this method 
    		crit.addOrderBy("lastModifiedDate", false);
    		List result = queryList(Announcement.class, crit);
    		
    		if (result != null && !result.isEmpty())	 {
    			if (result.size() < n) {	
    				return result.subList(1,result.size());
    			}
    			else {	
    				return result.subList(1,n);
    			}
    		}
    		else {
    			return null;
    		}
    }

}