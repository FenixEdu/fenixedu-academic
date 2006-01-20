package net.sourceforge.fenixedu.applicationTier.Servico.webSiteManager;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.domain.WebSiteItem;
import net.sourceforge.fenixedu.domain.WebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteItem;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author Fernanda Quitério 30/09/2003
 * 
 */
public class EditWebSiteItem extends ManageWebSiteItem {

    // infoItem with an infoSection
    // the return value of this service tells if the item being edited had
    // modified date whose value is used in section ordering
    // true if has mofified, false if it hasn't

    public Boolean run(Integer sectionCode, InfoWebSiteItem infoWebSiteItem, String user)
            throws FenixServiceException, ExcepcaoPersistencia {

        Boolean result = Boolean.FALSE;
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentWebSiteSection persistentWebSiteSection = persistentSupport
                .getIPersistentWebSiteSection();
        IPersistentWebSiteItem persistentWebSiteItem = persistentSupport.getIPersistentWebSiteItem();
        IPessoaPersistente persistentPerson = persistentSupport.getIPessoaPersistente();

        WebSiteSection webSiteSection = (WebSiteSection) persistentWebSiteSection.readByOID(
                WebSiteSection.class, sectionCode);
        InfoWebSiteSection infoWebSiteSection = InfoWebSiteSection.newInfoFromDomain(webSiteSection);

        checkData(infoWebSiteItem, webSiteSection);

        WebSiteItem webSiteItem = (WebSiteItem) persistentWebSiteItem.readByOID(WebSiteItem.class,
                infoWebSiteItem.getIdInternal());

        InfoWebSiteItem infoItemFromDB = InfoWebSiteItem.newInfoFromDomain(webSiteItem);
        Calendar calendarItemFromDB = Calendar.getInstance();
        Calendar calendarEditedItem = Calendar.getInstance();
        calendarItemFromDB.setTime(dateToSort(infoWebSiteSection, infoItemFromDB));
        calendarEditedItem.setTime(dateToSort(infoWebSiteSection, infoWebSiteItem));

        if (calendarEditedItem.get(Calendar.MONTH) != calendarItemFromDB.get(Calendar.MONTH)
                || calendarEditedItem.get(Calendar.YEAR) != calendarItemFromDB.get(Calendar.YEAR)) {
            result = Boolean.TRUE;
        }

        fillWebSiteItemForDB(infoWebSiteItem, user, persistentPerson, persistentWebSiteSection,
                webSiteSection, webSiteItem);

        return result;
    }
}