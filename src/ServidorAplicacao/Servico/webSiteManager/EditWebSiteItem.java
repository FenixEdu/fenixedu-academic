package ServidorAplicacao.Servico.webSiteManager;

import java.util.Calendar;

import DataBeans.InfoWebSiteItem;
import DataBeans.InfoWebSiteSection;
import DataBeans.util.Cloner;
import Dominio.IWebSiteItem;
import Dominio.IWebSiteSection;
import Dominio.WebSiteItem;
import Dominio.WebSiteSection;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentWebSiteItem;
import ServidorPersistente.IPersistentWebSiteSection;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 * 30/09/2003
 * 
 */
public class EditWebSiteItem extends ManageWebSiteItem {

	public EditWebSiteItem() {

	}
	//infoItem with an infoSection
	// the return value of this service tells if the item being edited had modified date whose value is used in section ordering
	// true if has mofified, false if it hasn't

	public Boolean run(Integer sectionCode, InfoWebSiteItem infoWebSiteItem, String user) throws FenixServiceException {

	    Boolean result = Boolean.FALSE;
		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentWebSiteSection persistentWebSiteSection = persistentSuport.getIPersistentWebSiteSection();
			IPersistentWebSiteItem persistentWebSiteItem = persistentSuport.getIPersistentWebSiteItem();
			IPessoaPersistente persistentPerson = persistentSuport.getIPessoaPersistente();

			IWebSiteSection webSiteSection = new WebSiteSection();
			webSiteSection.setIdInternal(sectionCode);
			webSiteSection = (IWebSiteSection) persistentWebSiteSection.readByOId(webSiteSection, false);
			InfoWebSiteSection infoWebSiteSection = Cloner.copyIWebSiteSection2InfoWebSiteSection(webSiteSection);
			
			checkData(infoWebSiteItem, webSiteSection);
			
			IWebSiteItem webSiteItem = new WebSiteItem();
			webSiteItem.setIdInternal(infoWebSiteItem.getIdInternal());
			webSiteItem = (IWebSiteItem) persistentWebSiteItem.readByOId(webSiteItem, true);

			InfoWebSiteItem infoItemFromDB = Cloner.copyIWebSiteItem2InfoWebSiteItem(webSiteItem);
		    Calendar calendarItemFromDB= Calendar.getInstance();
		    Calendar calendarEditedItem=Calendar.getInstance();
		    calendarItemFromDB.setTime(dateToSort(infoWebSiteSection, infoItemFromDB));
		    calendarEditedItem.setTime(dateToSort(infoWebSiteSection, infoWebSiteItem));
		    
		    if(calendarEditedItem.get(Calendar.MONTH) != calendarItemFromDB.get(Calendar.MONTH) 
		                    || calendarEditedItem.get(Calendar.YEAR) != calendarItemFromDB.get(Calendar.YEAR)) 
		    {
		        result = Boolean.TRUE;
		    }
			
			fillWebSiteItemForDB(infoWebSiteItem, user, persistentPerson, persistentWebSiteSection, webSiteSection, webSiteItem);
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
		return result;
	}
}