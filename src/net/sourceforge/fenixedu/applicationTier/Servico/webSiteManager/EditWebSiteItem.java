package net.sourceforge.fenixedu.applicationTier.Servico.webSiteManager;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IWebSiteItem;
import net.sourceforge.fenixedu.domain.IWebSiteSection;
import net.sourceforge.fenixedu.domain.WebSiteItem;
import net.sourceforge.fenixedu.domain.WebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteItem;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

/**
 * @author Fernanda Quitério 30/09/2003
 *  
 */
public class EditWebSiteItem extends ManageWebSiteItem {

    public EditWebSiteItem() {

    }

    //infoItem with an infoSection
    // the return value of this service tells if the item being edited had
    // modified date whose value is used in section ordering
    // true if has mofified, false if it hasn't

    public Boolean run(Integer sectionCode, InfoWebSiteItem infoWebSiteItem, String user)
            throws FenixServiceException {

        Boolean result = Boolean.FALSE;
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentWebSiteSection persistentWebSiteSection = persistentSuport
                    .getIPersistentWebSiteSection();
            IPersistentWebSiteItem persistentWebSiteItem = persistentSuport.getIPersistentWebSiteItem();
            IPessoaPersistente persistentPerson = persistentSuport.getIPessoaPersistente();

            IWebSiteSection webSiteSection;
            webSiteSection = (IWebSiteSection) persistentWebSiteSection.readByOID(WebSiteSection.class,
                    sectionCode);
            InfoWebSiteSection infoWebSiteSection = Cloner
                    .copyIWebSiteSection2InfoWebSiteSection(webSiteSection);

            checkData(infoWebSiteItem, webSiteSection);

            IWebSiteItem webSiteItem = (IWebSiteItem) persistentWebSiteItem.readByOID(WebSiteItem.class,
                    infoWebSiteItem.getIdInternal(), true);

            InfoWebSiteItem infoItemFromDB = Cloner.copyIWebSiteItem2InfoWebSiteItem(webSiteItem);
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

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
        return result;
    }
}