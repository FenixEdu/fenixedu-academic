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
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
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
public class EditItem implements IServico {

	private static EditItem service = new EditItem();

	public static EditItem getService() {

		return service;
	}

	private EditItem() {

	}

	public final String getNome() {

		return "EditItem";
	}

	//infoItem with an infoSection

	public boolean run(Integer sectionCode, InfoWebSiteItem infoWebSiteItem, String user) throws FenixServiceException {

		IWebSiteItem webSiteItem = null;
		IWebSiteSection webSiteSection = null;

		System.out.println("no servico");

		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentWebSiteSection persistentWebSiteSection = persistentSuport.getIPersistentWebSiteSection();
			IPersistentWebSiteItem persistentWebSiteItem = persistentSuport.getIPersistentWebSiteItem();
			IPessoaPersistente persistentPerson = persistentSuport.getIPessoaPersistente();

			webSiteSection = new WebSiteSection(sectionCode);
			webSiteSection = (IWebSiteSection) persistentWebSiteSection.readByOId(webSiteSection, false);

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

			webSiteItem.setExcerpt(infoWebSiteItem.getExcerpt());

			webSiteItem.setItemBeginDay(infoWebSiteItem.getItemBeginDayCalendar().getTime());
			webSiteItem.setItemEndDay(infoWebSiteItem.getItemEndDayCalendar().getTime());
			
			System.out.println("info: " + infoWebSiteItem.getItemBeginDayCalendar().getTime());
			System.out.println("info: " + infoWebSiteItem.getItemEndDayCalendar().getTime());
			System.out.println(webSiteItem.getItemBeginDay());
			System.out.println(webSiteItem.getItemEndDay());
			
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
						excerpt = excerpt.concat(stringTokenizer.nextToken().trim());
						if (size != 1) {
							excerpt = excerpt.concat(" ");
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