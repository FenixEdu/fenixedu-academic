package ServidorAplicacao.Servico.webSiteManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

	public Boolean run(final Integer sectionCode) throws FenixServiceException {

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

			//****************************** build excerpts file ********************************************
			ArrayList excerptsList = new ArrayList();
			excerptsList.addAll(infoWebSiteSection.getInfoItemsList());

			// beginning of file 
			String excerptsFile = new String();
			excerptsFile = putBegginingOfFile(excerptsFile);

			if (excerptsList.size() == 0) {
				// build no items file
				excerptsFile = excerptsFile.concat("<tr>\n\t<td align='left'>\n\t\t");
				excerptsFile = excerptsFile.concat("Não existem " + infoWebSiteSection.getName() + "\n");
				excerptsFile = excerptsFile.concat("\n\t</td>\n</tr>\n");

			} else {
				BeanComparator beanComparator = getBeanComparator(infoWebSiteSection);
				Collections.sort(excerptsList, beanComparator);
				if (infoWebSiteSection.getSortingOrder().equals("descendent")) {
					Collections.reverse(excerptsList);
				}

				// limits number of items to mandatory section size in website
				if (excerptsList.size() > infoWebSiteSection.getSize().intValue()) {
					List limitedList = new ArrayList();
					ListIterator iterItems = excerptsList.listIterator();
					while (iterItems.previousIndex() <= infoWebSiteSection.getSize().intValue()) {
						InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) iterItems.next();
						limitedList.add(infoWebSiteItem);
					}
					excerptsList = (ArrayList) limitedList;
				}

				Iterator iterItems = excerptsList.iterator();
				while (iterItems.hasNext()) {
					InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) iterItems.next();
					excerptsFile = putBegginingOfItem(excerptsFile, infoWebSiteItem);
					excerptsFile = putExcerpt(infoWebSiteSection, excerptsFile, infoWebSiteItem);
				}
			}
			excerptsFile = excerptsFile.concat("</table>\n</body>\n</html>");

			// build file
			File excerpts;
			try {
				excerpts = buildFile(excerptsFile, infoWebSiteSection.getName() + "-excerpts.html");
			} catch (Exception e) {
				e.printStackTrace();
				return Boolean.FALSE;
			}

			// send file to server by ftp
				Ftp.enviarFicheiro("/IstFtpServerConfig.properties", infoWebSiteSection.getName() + "-excerpts.html");
			// delete created file
			excerpts.delete();

			//************************* create file of items month corresponding to item created ****************
			List items = new ArrayList();
			items.addAll(infoWebSiteSection.getInfoItemsList());

			// obtain last item inserted
			Collections.sort(items, new BeanComparator("creationDate"));
			Collections.reverse(items);
			InfoWebSiteItem lastInfoWebSiteItem = (InfoWebSiteItem) items.get(0);

			Calendar calendarCycle = Calendar.getInstance();
			Calendar calendarLast = Calendar.getInstance();
			calendarLast.setTime(lastInfoWebSiteItem.getOnlineBeginDay());
			List monthList = new ArrayList();
			List allMonthLinks = new ArrayList();

			// get items with the same month as last
			Iterator iterItems = infoWebSiteSection.getInfoItemsList().iterator();
			while (iterItems.hasNext()) {
				InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) iterItems.next();
				calendarCycle.clear();
				calendarCycle.setTime(infoWebSiteItem.getOnlineBeginDay());
				if (calendarCycle.get(Calendar.MONTH) == calendarLast.get(Calendar.MONTH)) {
					monthList.add(infoWebSiteItem);
				}

				// get collection of months present in database
				Integer monthToCreateLink = new Integer(calendarCycle.get(Calendar.MONTH));
				if (!allMonthLinks.contains(monthToCreateLink)) {
					allMonthLinks.add(monthToCreateLink);
				}
			}
			ArrayList monthLinks = new ArrayList();
			if (monthList.size() > 1) {
				// file already exists so only this file needs to be refreshed
				monthLinks.add(new Integer(calendarLast.get(Calendar.MONTH)));
				items = monthList;
			} else {
				// there is a new month to send to server, so build file and refresh links on other files
				monthLinks.addAll(allMonthLinks);
			}
			Iterator iterLinks = monthLinks.iterator();
			while (iterLinks.hasNext()) {
				Integer monthLink = (Integer) iterLinks.next();
				Mes thisMonthString = new Mes(monthLink.intValue() + 1);

				List thisMonthList = new ArrayList();
				if (monthList.size() == 1) {
					Iterator iterAllItems = items.iterator();
					while (iterAllItems.hasNext()) {
						InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) iterAllItems.next();
						calendarCycle.clear();
						calendarCycle.setTime(infoWebSiteItem.getOnlineBeginDay());
						if (calendarCycle.get(Calendar.MONTH) == monthLink.intValue()) {
							thisMonthList.add(infoWebSiteItem);
						}
					}
				} else {
					thisMonthList = monthList;
				}
				// build body for items
				String itemsFile = new String();
				itemsFile = putBegginingOfFile(itemsFile);

				Iterator iterItemsForFile = thisMonthList.iterator();
				while (iterItemsForFile.hasNext()) {
					InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) iterItemsForFile.next();
					itemsFile = putBegginingOfItem(itemsFile, infoWebSiteItem);
					itemsFile = putItem(itemsFile, infoWebSiteItem);
				}
				itemsFile = itemsFile.concat("<tr>\n\t<td align='left'>\n\t\t");
				itemsFile =
					itemsFile.concat(
						"Eventuais incoerências nesta página deverão ser comunicadas afim de se efectuar a respectiva correcção.");
				itemsFile = itemsFile.concat("\n\t</td>\n</tr>\n");

				Iterator iterMonths = allMonthLinks.iterator();
				while (iterMonths.hasNext()) {
					Integer monthElem = (Integer) iterMonths.next();

					if (monthLink.intValue() != monthElem.intValue()) {
						Mes monthLinkString = new Mes(monthElem.intValue() + 1);
						itemsFile = itemsFile.concat("<tr>\n\t<td align='left'>\n\t\t");
						itemsFile =
							itemsFile.concat(
								"<a href='"
									+ infoWebSiteSection.getName()
									+ "-"
									+ monthLinkString.toString()
									+ ".html'>"
									+ monthLinkString.toString()
									+ "</a>");
						itemsFile = itemsFile.concat("\n\t</td>\n</tr>\n");
					}
				}
				itemsFile = itemsFile.concat("</table>\n</body>\n</html>");

				// prepare file for transfer
				File itemsFileToTransfer;
				try {
					itemsFileToTransfer =
						buildFile(itemsFile, infoWebSiteSection.getName() + "-" + thisMonthString.toString() + ".html");
				} catch (Exception e) {
					e.printStackTrace();
					return Boolean.FALSE;
				}
				Ftp.enviarFicheiro("/IstFtpServerConfig.properties", infoWebSiteSection.getName() + "-" + thisMonthString.toString() + ".html");
				// delete created file
				itemsFileToTransfer.delete();
			}

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
		return Boolean.TRUE;
	}

	private File buildFile(String fileContent, String fileName) throws Exception {
		File excerpts = null;
		BufferedWriter escritor = null;
		try {
			excerpts = new File(fileName);
			escritor = new BufferedWriter(new FileWriter(excerpts));
			escritor.write(fileContent);
			escritor.close();
		} catch (Exception e) {
			throw new Exception();
		}
		return excerpts;
	}

	private String putBegginingOfFile(String excerptsFile) {
		excerptsFile = excerptsFile.concat("<html>\n<head>\n<title></title>\n</head>\n<body>\n");
		excerptsFile = excerptsFile.concat("<table>\n");
		return excerptsFile;
	}

	private String putItem(String itemFile, InfoWebSiteItem infoWebSiteItem) {
		// item's main entry
		itemFile = itemFile.concat("<tr>\n\t<td align='left'>\n\t\t");
		itemFile = itemFile.concat(infoWebSiteItem.getMainEntryText());
		itemFile = itemFile.concat("\n\t</td>\n</tr>\n");
		itemFile = itemFile.concat("<tr>\n\t<td align='left'>\n\t\t");
		itemFile = itemFile.concat("Autor: " + infoWebSiteItem.getInfoEditor().getNome());
		itemFile = itemFile.concat("\n\t</td>\n</tr>\n");
		itemFile = itemFile.concat("<tr>\n\t<td align='left'>\n\t\t");
		itemFile = itemFile.concat("Contacto: " + infoWebSiteItem.getInfoEditor().getEmail());
		itemFile = itemFile.concat("<br/><br/>\n");
		itemFile = itemFile.concat("\n\t</td>\n</tr>\n");
		return itemFile;
	}

	private String putExcerpt(InfoWebSiteSection infoWebSiteSection, String excerptsFile, InfoWebSiteItem infoWebSiteItem) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(infoWebSiteItem.getOnlineBeginDay());
		// item's excerpt
		excerptsFile = excerptsFile.concat("<tr>\n\t<td align='left'>\n\t\t");
		excerptsFile = excerptsFile.concat(infoWebSiteItem.getExcerpt());
		excerptsFile =
			excerptsFile.concat(
				" ("
					+ "<a href='"
					+ infoWebSiteSection.getName()
					+ "-"
					+ new Mes(calendar.get(Calendar.MONTH) + 1)
					+ ".html'>"
					+ "mais"
					+ "</a>"
					+ ")");
		excerptsFile = excerptsFile.concat("<br/><br/>\n");
		excerptsFile = excerptsFile.concat("\n\t</td>\n</tr>\n");
		return excerptsFile;
	}

	private String putBegginingOfItem(String excerptsFile, InfoWebSiteItem infoWebSiteItem) {
		// item's title
		excerptsFile = excerptsFile.concat("<tr>\n\t<td align='left'>\n\t\t");
		excerptsFile = excerptsFile.concat(infoWebSiteItem.getTitle());
		excerptsFile = excerptsFile.concat("\n\t</td>\n</tr>\n");

		// item's dates
		if (infoWebSiteItem.getItemBeginDayCalendar() != null) {
			excerptsFile = excerptsFile.concat("<tr>\n\t<td align='left'>\n\t\t");
			excerptsFile = excerptsFile.concat("De " + infoWebSiteItem.getItemBeginDayCalendar().get(Calendar.DAY_OF_MONTH) + " ");

			if (infoWebSiteItem.getItemBeginDayCalendar().get(Calendar.MONTH)
				!= infoWebSiteItem.getItemEndDayCalendar().get(Calendar.MONTH)) {
				Mes month = new Mes(infoWebSiteItem.getItemBeginDayCalendar().get(Calendar.MONTH) + 1);
				excerptsFile = excerptsFile.concat(month.toString() + " ");
			}
			if (infoWebSiteItem.getItemBeginDayCalendar().get(Calendar.YEAR)
				!= infoWebSiteItem.getItemEndDayCalendar().get(Calendar.YEAR)) {
				excerptsFile = excerptsFile.concat(infoWebSiteItem.getItemBeginDayCalendar().get(Calendar.YEAR) + " ");
			}
			excerptsFile = excerptsFile.concat("a " + infoWebSiteItem.getItemEndDayCalendar().get(Calendar.DAY_OF_MONTH) + " ");
			Mes month = new Mes(infoWebSiteItem.getItemEndDayCalendar().get(Calendar.MONTH) + 1);
			excerptsFile = excerptsFile.concat(month.toString() + " ");
			excerptsFile = excerptsFile.concat(String.valueOf(infoWebSiteItem.getItemEndDayCalendar().get(Calendar.YEAR)));

			excerptsFile = excerptsFile.concat("\n\t</td>\n</tr>\n");
		}

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