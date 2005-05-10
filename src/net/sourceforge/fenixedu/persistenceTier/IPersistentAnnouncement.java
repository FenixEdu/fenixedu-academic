package net.sourceforge.fenixedu.persistenceTier;

/**
 * @author EP15
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a>
 */
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IAnnouncement;

public interface IPersistentAnnouncement extends IPersistentObject {

    public IAnnouncement readAnnouncementByTitleAndCreationDateAndSite(String title, Date date,
            Integer siteOID) throws ExcepcaoPersistencia;

    public List readAnnouncementsBySite(Integer siteOID) throws ExcepcaoPersistencia;

    public IAnnouncement readLastAnnouncementForSite(Integer siteOID) throws ExcepcaoPersistencia;

}
