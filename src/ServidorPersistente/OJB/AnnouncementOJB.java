package ServidorPersistente.OJB;
import java.sql.Timestamp;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Announcement;
import Dominio.IAnnouncement;
import Dominio.ISite;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentAnnouncement;
/**
 *  @author EP15
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a>
 */
public class AnnouncementOJB extends ObjectFenixOJB implements IPersistentAnnouncement
{
    public IAnnouncement readAnnouncementByTitleAndCreationDateAndSite(
        String title,
        Timestamp cDate,
        ISite site)
        throws ExcepcaoPersistencia
    {

        Criteria crit = new Criteria();
        crit.addEqualTo("title", title);
        crit.addEqualTo("creationDate", cDate);
        crit.addEqualTo("site.idInternal", site.getIdInternal());
        return (IAnnouncement) queryObject(Announcement.class, crit);

    }

    
    public void delete(IAnnouncement announcement) throws ExcepcaoPersistencia
    {
        super.delete(announcement);
    }

    public List readAnnouncementsBySite(ISite site) throws ExcepcaoPersistencia
    {

        Criteria crit = new Criteria();
        crit.addEqualTo("site.idInternal", site.getIdInternal());
        return queryList(Announcement.class, crit, "lastModifiedDate", false);

    }

    /**
	 * @see ServidorPersistente.IPersistentAnnouncement#readLastAnnouncement()
	 */
    public IAnnouncement readLastAnnouncementForSite(ISite site) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("site.idInternal", site.getIdInternal());
        List result = queryList(Announcement.class, crit, "lastModifiedDate", false);
        if (result != null && !result.isEmpty())
        {
            return (IAnnouncement) result.get(0);
        }
        else
        {

            return null;
        }
    }

}