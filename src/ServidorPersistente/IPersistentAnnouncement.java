package ServidorPersistente;
/**
 *
 * @author  EP15
 */
import java.util.Date;

import Dominio.IAnnouncement;
import Dominio.ISection;
public interface IPersistentAnnouncement extends IPersistentObject{
	
    public IAnnouncement readAnuncioByTituloAndDataAndSitio(String title, Date date, String siteName) throws ExcepcaoPersistencia;
    public void lockWrite(ISection announcement) throws ExcepcaoPersistencia;
    public void delete(IAnnouncement announcement) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
}
