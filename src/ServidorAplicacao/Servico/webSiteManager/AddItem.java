package ServidorAplicacao.Servico.webSiteManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

import DataBeans.InfoWebSite;
import DataBeans.InfoWebSiteItem;
import DataBeans.InfoWebSiteSection;
import DataBeans.util.Cloner;
import Dominio.IPessoa;
import Dominio.IWebSiteItem;
import Dominio.IWebSiteSection;
import Dominio.Pessoa;
import Dominio.WebSiteItem;
import Dominio.WebSiteSection;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentWebSiteItem;
import ServidorPersistente.IPersistentWebSiteSection;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 * 25/09/2003
 * 
 */
public class AddItem implements IServico {

	private static AddItem service = new AddItem();

	public static AddItem getService() {

		return service;
	}

	private AddItem() {

	}

	public final String getNome() {

		return "AddItem";
	}

	//infoItem with an infoSection

	public InfoWebSite run(Integer sectionCode, InfoWebSiteItem infoWebSiteItem, String user) throws FenixServiceException {

		IWebSiteItem webSiteItem = new WebSiteItem();
		IWebSiteSection webSiteSection = null;
		InfoWebSiteSection infoWebSiteSection = null;
		List infoWebSiteSections = new ArrayList();

		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentWebSiteSection persistentWebSiteSection = persistentSuport.getIPersistentWebSiteSection();
			IPersistentWebSiteItem persistentWebSiteItem = persistentSuport.getIPersistentWebSiteItem();
			IPessoaPersistente persistentPerson = persistentSuport.getIPessoaPersistente();

			webSiteSection = new WebSiteSection(sectionCode);
			webSiteSection = (IWebSiteSection) persistentWebSiteSection.readByOId(webSiteSection, false);
			infoWebSiteSection = Cloner.copyIWebSiteSection2InfoWebSiteSection(webSiteSection);

			persistentWebSiteItem.lockWrite(webSiteItem);

			Calendar today = Calendar.getInstance();
			webSiteItem.setCreationDate(new Timestamp(today.getTimeInMillis()));

			IPessoa person = new Pessoa(user);
			person = (IPessoa) persistentPerson.readDomainObjectByCriteria(person);
			webSiteItem.setEditor(person);

			if (webSiteSection.getWhatToSort().equals("ITEM_BEGIN_DAY")) {
				if (infoWebSiteItem.getItemBeginDayCalendar() == null) {
					throw new InvalidArgumentsServiceException();
				}
			} else if (webSiteSection.getWhatToSort().equals("ITEM_END_DAY")) {
				if (infoWebSiteItem.getItemEndDayCalendar() == null) {
					throw new InvalidArgumentsServiceException();
				}
			}
			if (infoWebSiteItem.getExcerpt() != null) {
				if (StringUtils.countMatches(infoWebSiteItem.getExcerpt(), " ") >= webSiteSection.getExcerptSize().intValue()) {
					throw new InvalidSituationServiceException();
				}
			}

			// treat author of item
			String authorName = infoWebSiteItem.getAuthorName();
			String authorEmail = infoWebSiteItem.getAuthorEmail();
			if ((authorName == null || authorName.length() == 0)
				&& (authorEmail == null || authorEmail.length() == 0)) {
					// in case author was not filled editor becomes the author
					authorName = person.getNome();
					authorEmail = person.getEmail();
			}
			webSiteItem.setAuthorName(authorName);
			webSiteItem.setAuthorEmail(authorEmail);

			webSiteItem.setExcerpt(infoWebSiteItem.getExcerpt());

			if (infoWebSiteItem.getItemBeginDayCalendar() != null) {
				webSiteItem.setItemBeginDay(infoWebSiteItem.getItemBeginDayCalendar().getTime());
			}
			if (infoWebSiteItem.getItemEndDayCalendar() != null) {
				webSiteItem.setItemEndDay(infoWebSiteItem.getItemEndDayCalendar().getTime());
			}
			webSiteItem.setKeyEditor(person.getIdInternal());
			webSiteItem.setKeyWebSiteSection(webSiteSection.getIdInternal());
			webSiteItem.setKeywords(infoWebSiteItem.getKeywords());
			webSiteItem.setMainEntryText(infoWebSiteItem.getMainEntryText());

			if (infoWebSiteItem.getPublished() != null) {
				webSiteItem.setPublished(infoWebSiteItem.getPublished());
				webSiteItem.setOnlineBeginDay(infoWebSiteItem.getOnlineBeginDay());
				webSiteItem.setOnlineEndDay(infoWebSiteItem.getOnlineEndDay());

				// fill excerpt in case it was not written
				if (infoWebSiteItem.getExcerpt() == null || infoWebSiteItem.getExcerpt().length() == 0) {

					StringTokenizer stringTokenizer = new StringTokenizer(infoWebSiteItem.getMainEntryText(), " ");
					String excerpt = new String();
					for (int size = webSiteSection.getExcerptSize().intValue(); size != 0; size--) {
						if (stringTokenizer.hasMoreTokens()) {
							excerpt = excerpt.concat(stringTokenizer.nextToken().trim());
							if (size != 1) {
								excerpt = excerpt.concat(" ");
							}
						} else {
							break;
						}
					}
					webSiteItem.setExcerpt(excerpt);
				}
			} else {
				webSiteItem.setPublished(Boolean.FALSE);
				webSiteItem.setOnlineBeginDay(null);
				webSiteItem.setOnlineEndDay(null);
			}
			webSiteItem.setTitle(infoWebSiteItem.getTitle());
			webSiteItem.setWebSiteSection(webSiteSection);

			List webSiteSections = persistentWebSiteSection.readByWebSite(webSiteSection.getWebSite());
			Iterator iterSections = webSiteSections.iterator();
			while (iterSections.hasNext()) {
				WebSiteSection section = (WebSiteSection) iterSections.next();

				InfoWebSiteSection infoWebSiteSection2 = Cloner.copyIWebSiteSection2InfoWebSiteSection(section);

				infoWebSiteSections.add(infoWebSiteSection2);
			}
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
		InfoWebSite infoWebSite = infoWebSiteSection.getInfoWebSite();
		infoWebSite.setSections(infoWebSiteSections);

		return infoWebSite;
	}
}