package ServidorAplicacao.Servico.webSiteManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoWebSiteItem;
import DataBeans.InfoWebSiteSection;
import DataBeans.util.Cloner;
import Dominio.IWebSiteItem;
import Dominio.IWebSiteSection;
import Dominio.WebSiteSection;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentWebSiteItem;
import ServidorPersistente.IPersistentWebSiteSection;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.Ftp;
import Util.Mes;

/**
 * @author Fernanda Quitério
 * 06/Out/2003
 * 
 */
public class SendWebSiteSectionFileToServer implements IServico {

	private static SendWebSiteSectionFileToServer service = new SendWebSiteSectionFileToServer();

	public static SendWebSiteSectionFileToServer getService() {

		return service;
	}

	private SendWebSiteSectionFileToServer() {

	}

	public final String getNome() {

		return "SendWebSiteSectionFileToServer";
	}

	public Boolean run(final Integer sectionCode, Boolean itemsDeleted) throws FenixServiceException {

		try {
			SuportePersistenteOJB persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentWebSiteSection persistentWebSiteSection = persistentSupport.getIPersistentWebSiteSection();
			IPersistentWebSiteItem persistentWebSiteItem = persistentSupport.getIPersistentWebSiteItem();

			IWebSiteSection webSiteSection = new WebSiteSection(sectionCode);
			webSiteSection = (IWebSiteSection) persistentWebSiteSection.readByOId(webSiteSection, false);

			List webSiteItems = persistentWebSiteItem.readPublishedWebSiteItemsByWebSiteSection(webSiteSection);
			List infoWebSiteItems = (List) CollectionUtils.collect(webSiteItems, new Transformer() {
				public Object transform(Object arg0) {
					IWebSiteItem webSiteItem = (IWebSiteItem) arg0;
					InfoWebSiteItem infoWebSiteItem = Cloner.copyIWebSiteItem2InfoWebSiteItem(webSiteItem);

					return infoWebSiteItem;
				}
			});

			InfoWebSiteSection infoWebSiteSection = Cloner.copyIWebSiteSection2InfoWebSiteSection(webSiteSection);
			infoWebSiteSection.setInfoItemsList(infoWebSiteItems);

			BeanComparator beanComparator = getBeanComparator(infoWebSiteSection);
			Collections.sort(infoWebSiteSection.getInfoItemsList(), beanComparator);
			if (infoWebSiteSection.getSortingOrder().equals("descendent")) {
				Collections.reverse(infoWebSiteSection.getInfoItemsList());
			}

			Calendar currentMonth = Calendar.getInstance();
			String currentMonthFileName = infoWebSiteSection.getName() + ".html";

			//****************************** build excerpts file ********************************************
			ArrayList excerptsList = new ArrayList();
			excerptsList.addAll(infoWebSiteSection.getInfoItemsList());

			// beginning of file 
			String excerptsFile = new String();

			if (excerptsList.size() == 0) {
				// build no items file
				excerptsFile = excerptsFile.concat("<p>\n\t\t");
				excerptsFile = excerptsFile.concat("Não existem " + infoWebSiteSection.getName() + "\n");
				excerptsFile = excerptsFile.concat("</p>\n");

			} else {
				// limits number of items to mandatory section size in website
				if (excerptsList.size() > infoWebSiteSection.getSize().intValue()) {
					List limitedList = new ArrayList();
					int i = 0;
					ListIterator iterItems = excerptsList.listIterator();
					while (i < infoWebSiteSection.getSize().intValue()) {
						InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) iterItems.next();
						limitedList.add(infoWebSiteItem);
						i++;
					}
					excerptsList = (ArrayList) limitedList;
				}

				Iterator iterItems = excerptsList.iterator();
				while (iterItems.hasNext()) {
					InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) iterItems.next();
					excerptsFile = putBegginingOfItem(excerptsFile, infoWebSiteItem);
					excerptsFile = putExcerpt(infoWebSiteSection, excerptsFile, infoWebSiteItem, currentMonth, currentMonthFileName);
				}
			}

			// build file
			File excerpts;
			try {
				excerpts = buildFile(excerptsFile, infoWebSiteSection.getName() + "-excerpts.html");
			} catch (Exception e) {
				e.printStackTrace();
				return Boolean.FALSE;
			}

			try
            {
                // send file to server by ftp
                Ftp.enviarFicheiro(
                	"/IstFtpServerConfig.properties",
                	infoWebSiteSection.getName() + "-excerpts.html",
                	infoWebSiteSection.getName() + "_principal/");
            }
            catch (IOException e1)
            {
                throw new FenixServiceException();
            }
			// delete created file
			excerpts.delete();

			//************************* create file of items month corresponding to item created ****************
			List items = new ArrayList();
			items.addAll(infoWebSiteSection.getInfoItemsList());

			List allMonthLinks = new ArrayList();
			ArrayList monthLinks = new ArrayList();
			List monthList = new ArrayList();
			Calendar calendarCycle = Calendar.getInstance();
			Calendar calendarLast = Calendar.getInstance();
			if (!itemsDeleted.booleanValue()) {
				// obtain last item inserted
				Collections.sort(items, new BeanComparator("creationDate"));
				Collections.reverse(items);
				InfoWebSiteItem lastInfoWebSiteItem = (InfoWebSiteItem) items.get(0);

				// need to know what to sort
				calendarLast.setTime(dateToSort(infoWebSiteSection, lastInfoWebSiteItem));

				// get items with the same month as last
				Iterator iterItems = infoWebSiteSection.getInfoItemsList().iterator();
				while (iterItems.hasNext()) {
					InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) iterItems.next();
					calendarCycle.clear();
					calendarCycle.setTime(dateToSort(infoWebSiteSection, infoWebSiteItem));
					//					calendarCycle.setTime(infoWebSiteItem.getOnlineBeginDay());
					if (calendarCycle.get(Calendar.MONTH) == calendarLast.get(Calendar.MONTH)) {
						monthList.add(infoWebSiteItem);
					}

					// get collection of months present in database
					Integer monthToCreateLink = new Integer(calendarCycle.get(Calendar.MONTH));
					if (!allMonthLinks.contains(monthToCreateLink)) {
						allMonthLinks.add(monthToCreateLink);
					}
				}

				if (monthList.size() > 1) {
					// file already exists so only this file needs to be refreshed
					monthLinks.add(new Integer(calendarLast.get(Calendar.MONTH)));
					items = monthList;
				} else {
					// there is a new month to send to server, so build file and refresh links on other files
					monthLinks.addAll(allMonthLinks);
				}
			} else {
				Iterator iterItems = infoWebSiteSection.getInfoItemsList().iterator();
				while (iterItems.hasNext()) {
					InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) iterItems.next();
					calendarCycle.clear();
					calendarCycle.setTime(dateToSort(infoWebSiteSection, infoWebSiteItem));

					// get collection of months present in database
					Integer monthToCreateLink = new Integer(calendarCycle.get(Calendar.MONTH));
					if (!allMonthLinks.contains(monthToCreateLink)) {
						allMonthLinks.add(monthToCreateLink);
					}
				}
				monthLinks.addAll(allMonthLinks);
			}
			Iterator iterLinks = monthLinks.iterator();
			while (iterLinks.hasNext()) {
				Integer monthLink = (Integer) iterLinks.next();
				Mes thisMonthString = new Mes(monthLink.intValue() + 1);
				List thisMonthList = new ArrayList();
				if (monthList.size() <= 1) {
					Iterator iterAllItems = items.iterator();
					while (iterAllItems.hasNext()) {
						InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) iterAllItems.next();
						calendarCycle.clear();
						calendarCycle.setTime(dateToSort(infoWebSiteSection, infoWebSiteItem));
						if (calendarCycle.get(Calendar.MONTH) == monthLink.intValue()) {
							thisMonthList.add(infoWebSiteItem);
						}
					}
				} else {
					thisMonthList = monthList;
				}

				// build body for items
				String itemsFile = new String();
				itemsFile = putBegginingOfItemFile(itemsFile, infoWebSiteSection);
				itemsFile = itemsFile.concat("<h1>" + infoWebSiteSection.getName() + "</h1>");
				itemsFile = itemsFile.concat("\n<br />\n");

				Iterator iterItemsForFile = thisMonthList.iterator();
				while (iterItemsForFile.hasNext()) {
					InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) iterItemsForFile.next();
					itemsFile = putBegginingOfItem(itemsFile, infoWebSiteItem);
					itemsFile = putItem(itemsFile, infoWebSiteItem);
				}
				itemsFile = itemsFile.concat("<p>\n\t");
				itemsFile = itemsFile.concat("<span class=\"greytxt\">");
				itemsFile =
					itemsFile.concat("Eventuais incoerências nesta página deverão ser comunicadas afim de se efectuar a respectiva correcção.");
				itemsFile = itemsFile.concat("</span>");
				itemsFile = itemsFile.concat("\n</p>\n");

				itemsFile = itemsFile.concat("<h2>Arquivo de " + infoWebSiteSection.getName() + "</h2>\n<p>\n");
				Iterator iterMonths = allMonthLinks.iterator();
				while (iterMonths.hasNext()) {
					Integer monthElem = (Integer) iterMonths.next();

					if (monthLink.intValue() != monthElem.intValue()) {
						Mes monthLinkString = new Mes(monthElem.intValue() + 1);
						// in case this month is the current month the link must follow to current month items
						if (monthElem.intValue() == currentMonth.get(Calendar.MONTH)) {
							itemsFile = itemsFile.concat("<a href=\"" + currentMonthFileName + "\">" + monthLinkString.toString() + "</a>");
						} else {
							itemsFile =
								itemsFile.concat(
									"<a href=\""
										+ infoWebSiteSection.getName()
										+ "-"
										+ monthLinkString.toString()
										+ ".html\">"
										+ monthLinkString.toString()
										+ "</a>");
						}
						itemsFile = itemsFile.concat("<br />\n");
					}
				}
				itemsFile = itemsFile.concat("</p>");
				itemsFile = putEndOfItemFile(itemsFile);

				// prepare file for transfer
				File itemsFileToTransfer;
				try {
					if (monthLink.intValue() == currentMonth.get(Calendar.MONTH)) {
						itemsFileToTransfer = buildFile(itemsFile, currentMonthFileName);
					} else {
						//						shtml
						itemsFileToTransfer = buildFile(itemsFile, infoWebSiteSection.getName() + "-" + thisMonthString.toString() + ".html");
					}
				} catch (Exception e) {
					e.printStackTrace();
					return Boolean.FALSE;
				}

				String fileForFTP = null;
				if (monthLink.intValue() == currentMonth.get(Calendar.MONTH)) {
					fileForFTP = currentMonthFileName;
				} else {
					fileForFTP = infoWebSiteSection.getName() + "-" + thisMonthString.toString() + ".html";
				}
				try
                {
                    Ftp.enviarFicheiro("/IstFtpServerConfig.properties", fileForFTP, infoWebSiteSection.getName() + "_principal/");
                }
                catch (IOException e2)
                {
                   throw new FenixServiceException(e2);
                }
				// delete created file
				itemsFileToTransfer.delete();
			}

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
		return Boolean.TRUE;
	}

	private Date dateToSort(InfoWebSiteSection infoWebSiteSection, InfoWebSiteItem infoWebSiteItem) {
		Date dateToSort = infoWebSiteItem.getCreationDate();
		if (infoWebSiteSection.getWhatToSort().equals("ITEM_BEGIN_DAY")) {
			dateToSort = infoWebSiteItem.getItemBeginDayCalendar().getTime();
		} else if (infoWebSiteSection.getWhatToSort().equals("ITEM_END_DAY")) {
			dateToSort = infoWebSiteItem.getItemEndDayCalendar().getTime();
		}
		return dateToSort;
	}

	private File buildFile(String fileContent, String fileName) throws Exception {
		File excerpts = null;
		BufferedWriter escritor = null;
		try {

			excerpts = new File(System.getProperty("java.io.tmpdir") + File.separator + fileName);
			escritor = new BufferedWriter(new FileWriter(excerpts));
			escritor.write(fileContent);
			escritor.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
		return excerpts;
	}

	private String putEndOfItemFile(String stringFile) {
		stringFile =
			stringFile.concat(
				"\t\t</td>\n"
					+ "\t  </tr>\n"
					+ "</table>\n"
					+ "<div id=\"footer\">\n"
					+ "\t  <div id=\"foot_links\"> <a href=\"http://www.ist.utl.pt/contactos/index-pt.shtml\">contactos</a> | <a href=\"mailto:webmaster@ist.utl.pt\">webmaster</a> <!--| <a href=\"http://www.ist.utl.pt/pt/\">mapa\n"
					+ "\t      do site</a>--> | <a href=\"/html/international.shtml\"> ingl&ecirc;s</a> </div>\n"
					+ "\t  <div id=\"foot_copy\">&copy;2003, Instituto Superior Técnico. Todos os direitos reservados.</div>\n"
					+ "\t  <div class=\"clear\"></div>\n"
					+ "</div>\n"
					+ "</body>\n"
					+ "</html>\n");
		return stringFile;
	}

	private String putBegginingOfItemFile(String stringFile, InfoWebSiteSection infoWebSiteSection) {
		stringFile =
			stringFile.concat(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
					+ "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
					+ "<head>\n"
					+ "<title>www.ist.utl.pt -"
					+ infoWebSiteSection.getName()
					+ "</title>\n"
					+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />\n"
					+ "<meta name=\"keywords\" content=\"ensino,  ensino superior, universidade, instituto, ciência, instituto superior técnico, investigação e desenvolvimento\" />\n"
					+ "<meta name=\"description\" content=\"O Instituto Superior Técnico é a maior escola de engenharia, ciência e tecnologia em Portugal.\" />\n"
					+ "<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"../../css/iststyle.css\" />\n"
					+ "<link rel=\"stylesheet\" type=\"text/css\" media=\"print\" href=\"../../css/print.css\" />\n"
					+ "<script type=\"text/javascript\" src=\"../../scripts/cssbox_script.js\"></script>\n"
					+ "</head>\n"
					+ "<body>\n"
					+ "<div id=\"header\">\n"
					+ "\t  <div id=\"logoist\"><img alt=\"Logo: Instituto Superior T&eacute;cnico\" height=\"51\" src=\"/img/logo_ist_class.gif\" width=\"38\" /></div>\n"
					+ "\t  <div id=\"login_dotist\"><a href=\"https://fenix.ist.utl.pt/loginPage.jsp\"><img alt=\"Icon: Login - dotist\" border=\"0\" src=\"../../img/login_dotist.gif\" /></a></div>\n"
					+ "\t  <div id=\"logoutl\"><a href=\"http://www.utl.pt/\"><img src=\"../../img/utl_logo_40.gif\" alt=\"Universidade T&eacute;cnica de Lisboa\" border=\"0\" title=\"UTL\" /></a></div>\n"
					+ "</div>\n"
					+ "<div id=\"nav\">"
					+ "\t  <ul id=\"perfnav\">\n"
					+ "\t\t    <li><a href=\"../../index.shtml\">Instituto</a></li>\n"
					+ "\t\t    <li><a href=\"/html/aluno.shtml\">Aluno</a></li>\n"
					+ "\t\t    <li><a href=\"/html/docente.shtml\">Docente</a></li>\n"
					+ "\t\t    <li><a href=\"/html/funcionario.shtml\">Funcion&aacute;rio</a></li>\n"
					+ "\t\t    <li><a href=\"/html/international.shtml\">International</a></li>\n"
					+ "\t  </ul>\n"
					+ "</div>\n"
					+ "<table id=\"bigtable\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n"
					+ "\t  <tr>\n"
					+ "\t\t    <td id=\"latnav_container\" width=\"155px\" nowrap=\"nowrap\">\n"
					+ "\t\t\t      <div id=\"latnav\">\n"
					+ "\t\t\t        <ul>\n"
					+ "\t\t\t\t          <li><a href=\"http://www.ist.utl.pt/pt/informacoes/\">Informação</a></li>\n"
					+ "\t\t\t\t          <li><a href=\"http://www.ist.utl.pt/pt/estrutura_interna/\">Estrutura</a></li>\n"
					+ "\t\t\t\t          <li><a href=\"http://www.ist.utl.pt/pt/servicos/\">Serviços</a></li>\n"
					+ "\t\t\t\t          <li><a href=\"http://www.ist.utl.pt/pt/ensino/\">Ensino</a></li>\n"
					+ "\t\t\t\t          <li><a href=\"http://www.ist.utl.pt/pt/investigacao/\">I &amp; D</a></li>\n"
					+ "\t\t\t        </ul>\n"
					+ "\t\t\t        <ul>\n"
					+ "\t\t\t\t          <li><a href=\"http://gape.ist.utl.pt/acesso/\">Ingressos</a></li>\n"
					+ "\t\t\t\t          <li><a href=\"http://alumni.ist.utl.pt/\">Saídas</a></li>\n"
					+ "\t\t\t        </ul>\n"
					+ "\t\t\t        <ul>\n"
					+ "\t\t\t\t          <li><a href=\"http://istpress.ist.utl.pt/\">IST Press</a></li>\n"
					+ "\t\t\t\t          <li><a href=\"http://www.ist.utl.pt/pt/ligacao_sociedade/\">Sociedade &amp; IST</a></li>\n"
					+ "\t\t\t\t          <li><a href=\"http://www.ist.utl.pt/pt/viver_ist/\">Viver no IST</a></li>\n"
					+ "\t\t\t\t          <li><a href=\"http://www.utl.pt/\">Universidade</a></li>\n"
					+ "\t\t\t        </ul>\n"
					+ "\t\t\t      </div>\n"
					+ "\t\t    </td>\n"
					+ "\t\t    <td width=\"100%\" colspan=\"3\" id=\"main\">\n");

		return stringFile;
	}

	private String putItem(String itemFile, InfoWebSiteItem infoWebSiteItem) {
		//		String authorName =
		//			StringUtils.substringBefore(infoWebSiteItem.getAuthorName(), " ").concat(" ").concat(
		//				StringUtils.substringAfterLast(infoWebSiteItem.getAuthorName(), " "));
		// item\"s main entry
		itemFile = itemFile.concat("<p>");
		itemFile = itemFile.concat(infoWebSiteItem.getMainEntryText());
		itemFile = itemFile.concat("\n<br /><br />\n");
		itemFile = itemFile.concat("<span class=\"greytxt\">");
		itemFile =
			itemFile.concat(
				"<strong>Contacto: </strong><a href=\"mailto:"
					+ infoWebSiteItem.getAuthorEmail()
					+ "\">"
					+ infoWebSiteItem.getAuthorName()
					+ "</a>");
		itemFile = itemFile.concat("</span>\n");
		itemFile = itemFile.concat("</p>\n");
		itemFile = itemFile.concat("<br />\n");

		return itemFile;
	}

	private String putBegginingOfItem(String stringFile, InfoWebSiteItem infoWebSiteItem) {
		// item's title
		stringFile = stringFile.concat("<h3 id=\"anchor" + infoWebSiteItem.getIdInternal() + "\">");
		stringFile = stringFile.concat(infoWebSiteItem.getTitle());
		stringFile = stringFile.concat("</h3>\n");
		stringFile = stringFile.concat("<p class=\"post_date\">");

		// item's dates
		if (infoWebSiteItem.getItemBeginDayCalendar() != null) {

			if (infoWebSiteItem.getItemEndDayCalendar() == null) {
				stringFile = stringFile.concat(infoWebSiteItem.getItemBeginDayCalendar().get(Calendar.DAY_OF_MONTH) + " ");
				Mes month = new Mes(infoWebSiteItem.getItemBeginDayCalendar().get(Calendar.MONTH) + 1);
				stringFile = stringFile.concat(month.toString());
				stringFile = stringFile.concat(" " + infoWebSiteItem.getItemBeginDayCalendar().get(Calendar.YEAR));
			} else {
				stringFile = stringFile.concat("De " + infoWebSiteItem.getItemBeginDayCalendar().get(Calendar.DAY_OF_MONTH) + " ");
				if (infoWebSiteItem.getItemBeginDayCalendar().get(Calendar.MONTH)
					!= infoWebSiteItem.getItemEndDayCalendar().get(Calendar.MONTH)) {
					Mes month = new Mes(infoWebSiteItem.getItemBeginDayCalendar().get(Calendar.MONTH) + 1);
					stringFile = stringFile.concat(month.toString() + " ");
				}

				if (infoWebSiteItem.getItemBeginDayCalendar().get(Calendar.YEAR)
					!= infoWebSiteItem.getItemEndDayCalendar().get(Calendar.YEAR)) {
					stringFile = stringFile.concat(infoWebSiteItem.getItemBeginDayCalendar().get(Calendar.YEAR) + " ");
				}
				stringFile = stringFile.concat("a " + infoWebSiteItem.getItemEndDayCalendar().get(Calendar.DAY_OF_MONTH) + " ");
				Mes month = new Mes(infoWebSiteItem.getItemEndDayCalendar().get(Calendar.MONTH) + 1);
				stringFile = stringFile.concat(month.toString() + " ");
				stringFile = stringFile.concat(String.valueOf(infoWebSiteItem.getItemEndDayCalendar().get(Calendar.YEAR)));
			}
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(infoWebSiteItem.getCreationDate().getTime());
			stringFile = stringFile.concat(calendar.get(Calendar.DAY_OF_MONTH) + " ");

			Mes month = new Mes(calendar.get(Calendar.MONTH) + 1);
			stringFile = stringFile.concat(month.toString() + " ");
			stringFile = stringFile.concat(String.valueOf(calendar.get(Calendar.YEAR)) + " - ");
			if (String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)).length() == 1) {
				stringFile = stringFile.concat("0");
			}
			stringFile = stringFile.concat(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":");
			if (String.valueOf(calendar.get(Calendar.MINUTE)).length() == 1) {
				stringFile = stringFile.concat("0");
			}
			stringFile = stringFile.concat(String.valueOf(calendar.get(Calendar.MINUTE)));
		}
		stringFile = stringFile.concat("</p>");

		return stringFile;
	}

	private String putExcerpt(
		InfoWebSiteSection infoWebSiteSection,
		String excerptsFile,
		InfoWebSiteItem infoWebSiteItem,
		Calendar currentMonth,
		String currentMonthFileName) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateToSort(infoWebSiteSection, infoWebSiteItem));

		String fileName = null;
		if (calendar.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH)) {
			fileName = currentMonthFileName;
		} else {
			fileName = infoWebSiteSection.getName() + "-" + new Mes(calendar.get(Calendar.MONTH) + 1) + ".html";
		}

		// item's excerpt
		excerptsFile = excerptsFile.concat("<p>");
		excerptsFile = excerptsFile.concat(infoWebSiteItem.getExcerpt());
		excerptsFile =
			excerptsFile.concat(
				" ("
					+ "<a href=\""
					+ "/News/"
					+ infoWebSiteSection.getName()
					+ "_principal/"
					+ fileName
					+ "#anchor"
					+ infoWebSiteItem.getIdInternal()
					+ "\">"
					+ "mais"
					+ "</a>"
					+ ")");
		excerptsFile = excerptsFile.concat("</p>\n");
		return excerptsFile;
	}

	private BeanComparator getBeanComparator(InfoWebSiteSection infoWebSiteSection) {
		BeanComparator beanComparator = new BeanComparator("creationDate");
		if (infoWebSiteSection.getWhatToSort().equals("ITEM_BEGIN_DAY")) {
			beanComparator = new BeanComparator("itemBeginDayCalendar.time");
		} else if (infoWebSiteSection.getWhatToSort().equals("ITEM_END_DAY")) {
			beanComparator = new BeanComparator("itemEndDayCalendar.time");
		}
		return beanComparator;
	}
}