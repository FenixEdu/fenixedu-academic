package net.sourceforge.fenixedu.applicationTier.Servico.webSiteManager;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.domain.WebSiteItem;
import net.sourceforge.fenixedu.domain.WebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

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

        WebSiteSection webSiteSection = rootDomainObject.readWebSiteSectionByOID(sectionCode);
        InfoWebSiteSection infoWebSiteSection = InfoWebSiteSection.newInfoFromDomain(webSiteSection);

        checkData(infoWebSiteItem, webSiteSection);

        WebSiteItem webSiteItem = rootDomainObject.readWebSiteItemByOID(infoWebSiteItem.getIdInternal());

        InfoWebSiteItem infoItemFromDB = InfoWebSiteItem.newInfoFromDomain(webSiteItem);
        Calendar calendarItemFromDB = Calendar.getInstance();
        Calendar calendarEditedItem = Calendar.getInstance();
        calendarItemFromDB.setTime(dateToSort(infoWebSiteSection, infoItemFromDB));
        calendarEditedItem.setTime(dateToSort(infoWebSiteSection, infoWebSiteItem));

        if (calendarEditedItem.get(Calendar.MONTH) != calendarItemFromDB.get(Calendar.MONTH)
                || calendarEditedItem.get(Calendar.YEAR) != calendarItemFromDB.get(Calendar.YEAR)) {
            result = Boolean.TRUE;
        }

        fillWebSiteItemForDB(infoWebSiteItem, user, webSiteSection, webSiteItem);

        return result;
    }
}