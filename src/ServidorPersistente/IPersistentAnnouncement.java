package ServidorPersistente;
/**
 *
 * @author  EP15
 */
import java.util.Date;

import Dominio.IAnnouncement;
import Dominio.ISection;
import Dominio.ISite;
public interface IPersistentAnnouncement extends IPersistentObject{
	
    public IAnnouncement readAnnouncementByTitleAndCreationDateAndSite(String title, Date date, ISite site) throws ExcepcaoPersistencia;
    public void lockWrite(ISection announcement) throws ExcepcaoPersistencia;
    public void delete(IAnnouncement announcement) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
}
