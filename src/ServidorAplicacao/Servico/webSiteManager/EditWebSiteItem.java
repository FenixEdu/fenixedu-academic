package ServidorAplicacao.Servico.webSiteManager;

import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

import DataBeans.InfoWebSiteItem;
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
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
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
public class EditWebSiteItem implements IServico {

	private static EditWebSiteItem service = new EditWebSiteItem();

	public static EditWebSiteItem getService() {

		return service;
	}

	private EditWebSiteItem() {

	}

	public final String getNome() {

		return "EditWebSiteItem";
	}

	//infoItem with an infoSection

	public boolean run(Integer sectionCode, InfoWebSiteItem infoWebSiteItem, String user) throws FenixServiceException {

		IWebSiteItem webSiteItem = null;
		IWebSiteSection webSiteSection = null;

		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentWebSiteSection persistentWebSiteSection = persistentSuport.getIPersistentWebSiteSection();
			IPersistentWebSiteItem persistentWebSiteItem = persistentSuport.getIPersistentWebSiteItem();
			IPessoaPersistente persistentPerson = persistentSuport.getIPessoaPersistente();

			webSiteSection = new WebSiteSection(sectionCode);
			webSiteSection = (IWebSiteSection) persistentWebSiteSection.readByOId(webSiteSection, false);

			if (webSiteSection.getWhatToSort().equals("itemBeginDay")) {
				if (infoWebSiteItem.getItemBeginDayCalendar() == null) {
					throw new InvalidArgumentsServiceException();
				}
			} else if (webSiteSection.getWhatToSort().equals("itemEndDay")) {
				if (infoWebSiteItem.getItemEndDayCalendar() == null) {
					throw new InvalidArgumentsServiceException();
				}
			}
			// if excerpt exceeds limit of words its invalid
			if (infoWebSiteItem.getExcerpt() != null) {
				if (StringUtils.countMatches(infoWebSiteItem.getExcerpt(), new String(" "))
					>= webSiteSection.getExcerptSize().intValue()) {
					throw new InvalidSituationServiceException();
				}
			}

			webSiteItem = new WebSiteItem(infoWebSiteItem.getIdInternal());
			webSiteItem = (IWebSiteItem) persistentWebSiteItem.readByOId(webSiteItem, true);

			IPessoa person = new Pessoa(user);
			person = (IPessoa) persistentPerson.readDomainObjectByCriteria(person);
			webSiteItem.setEditor(person);

			// treat author of item
			IPessoa authorPerson = null;
			System.out.println("username no servico: " + infoWebSiteItem.getInfoAuthor().getUsername());
			if(infoWebSiteItem.getInfoAuthor().getUsername() != null) {
				authorPerson = persistentPerson.lerPessoaPorUsername(infoWebSiteItem.getInfoAuthor().getUsername());
				if(authorPerson == null) {
					throw new NonExistingServiceException();
				}
			} else {
				// in case author was not filled editor becomes the author
				authorPerson = person;
			}
			webSiteItem.setAuthor(authorPerson);
			webSiteItem.setKeyAuthor(authorPerson.getIdInternal());

			webSiteItem.setExcerpt(infoWebSiteItem.getExcerpt());
			if (infoWebSiteItem.getItemBeginDayCalendar() != null) {
				webSiteItem.setItemBeginDay(infoWebSiteItem.getItemBeginDayCalendar().getTime());
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

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
		return true;
	}
}