package ServidorPersistente.OJB;
import java.sql.Timestamp;
import java.util.List;

import org.odmg.QueryException;

import Dominio.Announcement;
import Dominio.IAnnouncement;
import Dominio.ISite;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentAnnouncement;
/**
 *
 * @author  EP 15
 */
public class AnnouncementOJB extends ObjectFenixOJB implements IPersistentAnnouncement {
    public IAnnouncement readAnnouncementByTitleAndCreationDateAndSite(String title, Timestamp cDate, ISite site) throws ExcepcaoPersistencia {
        try {
            IAnnouncement announcementResult = null;
           
            String oqlQuery1 = "select announcement from " + Announcement.class.getName();
            oqlQuery1 += " where title = $1";
			oqlQuery1 += " and creationDate = $2";
			oqlQuery1 += " and site.executionCourse.sigla = $3";
			oqlQuery1 += " and site.executionCourse.executionPeriod.name = $4";
			oqlQuery1 += " and site.executionCourse.executionPeriod.executionYear.year = $5";
            query.create(oqlQuery1);
            query.bind(title);
            query.bind(cDate);
            query.bind(site.getExecutionCourse().getSigla());
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
    public void lockWrite(IAnnouncement announcement) throws ExcepcaoPersistencia {
        super.lockWrite(announcement);
    }
    public void delete(IAnnouncement announcement) throws ExcepcaoPersistencia {
        super.delete(announcement);
    }
    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + Announcement.class.getName();
        super.deleteAll(oqlQuery);
    }

	public List readAnnouncementsBySite(ISite site) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select announcement from " + Announcement.class.getName();
			oqlQuery += " where site.executionCourse.sigla = $1";
			oqlQuery += " and site.executionCourse.executionPeriod.name = $2";
			oqlQuery += " and site.executionCourse.executionPeriod.executionYear.year = $3";
			oqlQuery += " order by lastModifiedDate desc";

			query.create(oqlQuery);
			query.bind(site.getExecutionCourse().getSigla());
			query.bind(site.getExecutionCourse().getExecutionPeriod().getName());
			query.bind(site.getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());

			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException queryEx) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, queryEx);
		}
	}

	/**
	 * @see ServidorPersistente.IPersistentAnnouncement#readLastAnnouncement()
	 */
	public IAnnouncement readLastAnnouncementForSite(ISite site) throws ExcepcaoPersistencia {
		try {
			IAnnouncement announcementResult = null;

			String oqlQuery = "select announcement from " + Announcement.class.getName();
			oqlQuery += " where site.executionCourse.sigla = $1";
			oqlQuery += " and site.executionCourse.executionPeriod.name = $2";
			oqlQuery += " and site.executionCourse.executionPeriod.executionYear.year = $3";
			oqlQuery += " order by lastModifiedDate desc";

			//should be done with max to reduce overhead

			query.create(oqlQuery);
			query.bind(site.getExecutionCourse().getSigla());
			query.bind(site.getExecutionCourse().getExecutionPeriod().getName());
			query.bind(site.getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());

			List result = (List) query.execute();
			lockRead(result);
			
			if (!(result.isEmpty())) announcementResult = (IAnnouncement) result.get(0);
			return announcementResult;
		} catch (QueryException queryEx) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, queryEx);
		}
	}
	public List readAll()throws ExcepcaoPersistencia {
	
			try {
				String oqlQuery = "select all from " + Announcement.class.getName();
				query.create(oqlQuery);
				List result = (List) query.execute();
				lockRead(result);
				return result;
			} catch (QueryException ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
		}
}
