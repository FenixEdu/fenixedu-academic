package ServidorPersistente;
/**
 *
 * @author  EP15
 */
import java.util.Date;
import java.util.List;

import Dominio.IAnnouncement;
import Dominio.ISite;
public interface IPersistentAnnouncement extends IPersistentObject{
	
    public IAnnouncement readAnnouncementByTitleAndCreationDateAndSite(String title, Date date, ISite site) throws ExcepcaoPersistencia;
    public void lockWrite(IAnnouncement announcement) throws ExcepcaoPersistencia;
    public void delete(IAnnouncement announcement) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
	public List readAnnouncementsBySite(ISite site) throws ExcepcaoPersistencia;
	public IAnnouncement readLastAnnouncementForSite(ISite site) throws ExcepcaoPersistencia;	
}
