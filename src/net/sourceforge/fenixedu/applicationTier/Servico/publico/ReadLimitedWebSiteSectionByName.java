package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.domain.WebSiteItem;
import net.sourceforge.fenixedu.domain.WebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteItem;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Fernanda Quitério 02/10/2003
 * 
 */
public class ReadLimitedWebSiteSectionByName extends Service {

	public Object run(String sectionName) throws ExcepcaoInexistente, FenixServiceException,
			ExcepcaoPersistencia {

		WebSiteSection webSiteSection = null;
		InfoWebSiteSection infoWebSiteSection = new InfoWebSiteSection();

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentWebSiteSection persistentWebSiteSection = sp.getIPersistentWebSiteSection();
		IPersistentWebSiteItem persistentWebSiteItem = sp.getIPersistentWebSiteItem();

		webSiteSection = persistentWebSiteSection.readByName(sectionName);

		if (webSiteSection == null) {
			throw new NonExistingServiceException();
		}

		List webSiteItems = persistentWebSiteItem
				.readPublishedWebSiteItemsByWebSiteSection(webSiteSection);

		if (webSiteItems == null || webSiteItems.size() == 0) {
			throw new NonExistingServiceException();
		}

		// get items with valid dates of publishment
		CollectionUtils.filter(webSiteItems, new Predicate() {
			public boolean evaluate(Object arg0) {
				WebSiteItem webSiteItem = (WebSiteItem) arg0;
				if (!webSiteItem.getOnlineBeginDay().after(Calendar.getInstance().getTime())
						&& !webSiteItem.getOnlineEndDay().before(Calendar.getInstance().getTime())) {
					return true;
				}

				return false;
			}
		});
		if (webSiteItems.size() == 0) {
			throw new NonExistingServiceException();
		}

		List infoWebSiteItems = (List) CollectionUtils.collect(webSiteItems, new Transformer() {
			public Object transform(Object arg0) {
				WebSiteItem webSiteItem = (WebSiteItem) arg0;
				InfoWebSiteItem infoWebSiteItem = InfoWebSiteItem.newInfoFromDomain(webSiteItem);

				return infoWebSiteItem;
			}
		});

		Collections.sort(infoWebSiteItems, new BeanComparator("creationDate"));
		if (webSiteSection.getSortingOrder().equals("descendent")) {
			Collections.reverse(infoWebSiteItems);
		}

		// limits number of items to mandatory section size in website
		if (infoWebSiteItems.size() > webSiteSection.getSize().intValue()) {
			List limitedList = new ArrayList();
			ListIterator iterItems = infoWebSiteItems.listIterator();
			while (iterItems.previousIndex() <= webSiteSection.getSize().intValue()) {
				InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) iterItems.next();
				limitedList.add(infoWebSiteItem);
			}
			infoWebSiteSection.setInfoItemsList(limitedList);
		} else {
			infoWebSiteSection.setInfoItemsList(infoWebSiteItems);
		}

		infoWebSiteSection.setIdInternal(webSiteSection.getIdInternal());
		infoWebSiteSection.setExcerptSize(webSiteSection.getExcerptSize());
		infoWebSiteSection.setInfoWebSite(InfoWebSite.newInfoFromDomain(webSiteSection.getWebSite()));
		infoWebSiteSection.setName(webSiteSection.getName());
		infoWebSiteSection.setSize(webSiteSection.getSize());
		infoWebSiteSection.setSortingOrder(webSiteSection.getSortingOrder());

		return infoWebSiteSection;
	}
}