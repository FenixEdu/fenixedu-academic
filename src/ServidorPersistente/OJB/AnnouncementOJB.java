package ServidorPersistente.OJB;
import java.util.Date;
import java.util.List;

import org.odmg.QueryException;

import Dominio.Announcement;
import Dominio.IAnnouncement;
import Dominio.ISection;
import Dominio.Site;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentAnnouncement;
/**
 *
 * @author  EP 15
 */
public class AnnouncementOJB extends ObjectFenixOJB implements IPersistentAnnouncement {
    public IAnnouncement readAnuncioByTituloAndDataAndSitio(String title, Date date, String siteName) throws ExcepcaoPersistencia {
        try {
            IAnnouncement announcementResult = null;
            Site site = null;
            String oqlQuery = "select site from " + Site.class.getName();
            oqlQuery += " where name = $1 ";
            query.create(oqlQuery);
            query.bind(siteName);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                site = (Site) result.get(0);
            else
                return null;
            String oqlQuery1 = "select announcement from " + Announcement.class.getName();
            oqlQuery1 += " where title = $1 ";
			oqlQuery1 += " and date = $2 ";
			oqlQuery1 += " and site.executionCourse.nome = $3";
			oqlQuery1 += " and site.executionCourse.executionPeriod.name = $4";
			oqlQuery1 += " and site.executionCourse.executionPeriod.executionYear.year = $5";
            query.create(oqlQuery1);
            query.bind(title);
            query.bind(date);
            query.bind(site.getExecutionCourse().getNome());
			query.bind(site.getExecutionCourse().getExecutionPeriod().getName());
			query.bind(site.getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());
            List result1 = (List) query.execute();
            lockRead(result1);
            if (result1.size() != 0)
			announcementResult = (IAnnouncement) result1.get(0);
            return announcementResult;
        } catch (QueryException queryEx) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, queryEx);
        }
    }
    public void lockWrite(ISection announcement) throws ExcepcaoPersistencia {
        super.lockWrite(announcement);
    }
    public void delete(IAnnouncement announcement) throws ExcepcaoPersistencia {
        super.delete(announcement);
    }
    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + Announcement.class.getName();
        super.deleteAll(oqlQuery);
    }
}
