package net.sourceforge.fenixedu.applicationTier.Servico.webSiteManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IWebSiteItem;
import net.sourceforge.fenixedu.domain.IWebSiteSection;
import net.sourceforge.fenixedu.domain.WebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteItem;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.Ftp;
import net.sourceforge.fenixedu.util.Mes;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Fernanda Quitério 06/Out/2003
 */
public class SendWebSiteSectionFileToServer extends ManageWebSiteItem {

    public SendWebSiteSectionFileToServer() {

    }

    public Boolean run(final Integer sectionCode, InfoWebSiteItem lastInfoWebSiteItem)
            throws FenixServiceException {

        try {
            SuportePersistenteOJB persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentWebSiteSection persistentWebSiteSection = persistentSupport
                    .getIPersistentWebSiteSection();
            IPersistentWebSiteItem persistentWebSiteItem = persistentSupport.getIPersistentWebSiteItem();

            List sections = new ArrayList();
            if (sectionCode == null) {
                // in case of configuration we have to update all sections
                sections = persistentWebSiteSection.readAll();
            } else {
                IWebSiteSection webSiteSectionTmp;
                webSiteSectionTmp = (IWebSiteSection) persistentWebSiteSection.readByOID(
                        WebSiteSection.class, sectionCode);
                sections.add(webSiteSectionTmp);
            }
            Iterator iterSections = sections.iterator();
            while (iterSections.hasNext()) {
                IWebSiteSection webSiteSection = (IWebSiteSection) iterSections.next();

                List webSiteItems = persistentWebSiteItem
                        .readPublishedWebSiteItemsByWebSiteSection(webSiteSection);
                List infoWebSiteItems = (List) CollectionUtils.collect(webSiteItems, new Transformer() {

                    public Object transform(Object arg0) {
                        IWebSiteItem webSiteItem = (IWebSiteItem) arg0;
                        InfoWebSiteItem infoWebSiteItem = Cloner
                                .copyIWebSiteItem2InfoWebSiteItem(webSiteItem);

                        return infoWebSiteItem;
                    }
                });

                InfoWebSiteSection infoWebSiteSection = Cloner
                        .copyIWebSiteSection2InfoWebSiteSection(webSiteSection);
                infoWebSiteSection.setInfoItemsList(infoWebSiteItems);

                BeanComparator beanComparator = getBeanComparator(infoWebSiteSection);
                Collections.sort(infoWebSiteSection.getInfoItemsList(), beanComparator);
                if (infoWebSiteSection.getSortingOrder().equals("descendent")) {
                    Collections.reverse(infoWebSiteSection.getInfoItemsList());
                }

                Calendar currentMonth = Calendar.getInstance();
                String currentMonthFileName = infoWebSiteSection.getFtpName() + ".html";

                //------------------------------------------------//-------------------------------------------------
                // create excerpts file; this file has those items whose
                // publication
                // date has today's date
                // and the number of items to show is limited by size of section

                List excerptsList = new ArrayList();
                excerptsList.addAll(infoWebSiteSection.getInfoItemsList());

                // beginning of file
                String excerptsFile = new String();

                if (excerptsList.size() == 0) {
                    // build no items file
                    excerptsFile = excerptsFile.concat("<p>\n\t\t");
                    excerptsFile = excerptsFile.concat("Não existem " + infoWebSiteSection.getName()
                            + "\n");
                    excerptsFile = excerptsFile.concat("</p>\n");

                } else {
                    // limits number of items to mandatory section size in
                    // website
                    if (excerptsList.size() > infoWebSiteSection.getSize().intValue()) {
                        Calendar today = Calendar.getInstance();
                        List limitedList = new ArrayList();
                        int i = 0;
                        ListIterator iterItems = excerptsList.listIterator();
                        while (i < infoWebSiteSection.getSize().intValue() && iterItems.hasNext()) {
                            InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) iterItems.next();
                            // show only published items that have to be
                            // published
                            // today: according to online begin and end day
                            if (!infoWebSiteItem.getOnlineBeginDay().after(today.getTime())
                                    && !infoWebSiteItem.getOnlineEndDay().before(today.getTime())) {
                                limitedList.add(infoWebSiteItem);
                                i++;
                            }
                        }
                        excerptsList = (ArrayList) limitedList;

                        // be sure that we have at least one excerpt
                        if (excerptsList.size() == 0) {
                            excerptsList.add(infoWebSiteSection.getInfoItemsList().get(0));
                        }
                    }

                    Iterator iterItems = excerptsList.iterator();
                    while (iterItems.hasNext()) {
                        InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) iterItems.next();
                        excerptsFile = putBegginingOfItem(excerptsFile, infoWebSiteItem);
                        excerptsFile = putExcerpt(infoWebSiteSection, excerptsFile, infoWebSiteItem,
                                currentMonth, currentMonthFileName);
                    }
                }

                // build file
                File excerpts;
                try {
                    excerpts = buildFile(excerptsFile, infoWebSiteSection.getFtpName()
                            + "_excerpts.html");
                } catch (Exception e) {
                    e.printStackTrace();
                    return Boolean.FALSE;
                }

                try {
                    // send file to server by ftp
                    //                    Ftp.enviarFicheiro("/IstFtpServerConfig.properties",
                    // excerpts.getName(),
                    //                            infoWebSiteSection.getFtpName() + "/");
                    Ftp.enviarFicheiroScp("/IstFtpServerConfig.properties", excerpts.getName(),
                            infoWebSiteSection.getFtpName() + "/");
                } catch (IOException e1) {
                    throw new FenixServiceException();
                }
                // delete created file
                excerpts.delete();

                //------------------------------------------------//-------------------------------------------------
                // create file of month corresponding to item created or
                // create all files in case this item is from a new month or in
                // case
                // some item was deleted

                List items = new ArrayList();
                items.addAll(infoWebSiteSection.getInfoItemsList());

                List monthList = new ArrayList();
                HashMap monthsToCreateLinks = new HashMap();
                HashMap monthsToCreateFiles = new HashMap();
                Calendar calendarCycle = Calendar.getInstance();
                Calendar calendarLast = Calendar.getInstance();
                if (lastInfoWebSiteItem != null) {
                    // need to know what to sort
                    calendarLast.setTime(dateToSort(infoWebSiteSection, lastInfoWebSiteItem));

                    // get items with the same month as last inserted
                    Iterator iterItems = infoWebSiteSection.getInfoItemsList().iterator();
                    while (iterItems.hasNext()) {
                        InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) iterItems.next();
                        calendarCycle.clear();
                        calendarCycle.setTime(dateToSort(infoWebSiteSection, infoWebSiteItem));
                        if (calendarCycle.get(Calendar.MONTH) == calendarLast.get(Calendar.MONTH)
                                && calendarCycle.get(Calendar.YEAR) == calendarLast.get(Calendar.YEAR)) {
                            monthList.add(infoWebSiteItem);
                        }
                        findMonthsForArchive(monthsToCreateLinks, calendarCycle);
                    }
                    if (monthList.size() > 1) {
                        // file already exists so only this file needs to be
                        // refreshed
                        List monthToRefresh = new ArrayList();
                        monthToRefresh.add(new Integer(calendarLast.get(Calendar.MONTH)));
                        monthsToCreateFiles.put(new Integer(calendarLast.get(Calendar.YEAR)),
                                monthToRefresh);

                        items = monthList;
                    } else {
                        // there is a new month to send to server, so build file
                        // and
                        // refresh links on other files
                        copyNewHashmapForFiles(monthsToCreateLinks, monthsToCreateFiles);
                    }
                } else {
                    Iterator iterItems = infoWebSiteSection.getInfoItemsList().iterator();
                    while (iterItems.hasNext()) {
                        InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) iterItems.next();
                        calendarCycle.clear();
                        calendarCycle.setTime(dateToSort(infoWebSiteSection, infoWebSiteItem));

                        findMonthsForArchive(monthsToCreateLinks, calendarCycle);
                    }
                    copyNewHashmapForFiles(monthsToCreateLinks, monthsToCreateFiles);
                }

                List monthLinks = null;
                Integer year = null;
                Iterator iterYears = monthsToCreateFiles.entrySet().iterator();
                while (iterYears.hasNext()) {
                    Map.Entry monthMap = (Map.Entry) iterYears.next();
                    year = (Integer) monthMap.getKey();
                    monthLinks = (List) monthMap.getValue();

                    Iterator iterLinks = monthLinks.iterator();
                    while (iterLinks.hasNext()) {
                        Integer monthLink = (Integer) iterLinks.next();
                        //Mes thisMonthString = new Mes(monthLink.intValue() +
                        // 1);
                        String fileName = infoWebSiteSection.getFtpName() + year.toString() + "_"
                                + new Integer(monthLink.intValue() + 1).toString() + ".html";

                        List thisMonthList = new ArrayList();
                        // if month of last item is new we have to recreate all
                        // files for other months
                        if (monthList.size() <= 1) {
                            Iterator iterAllItems = items.iterator();
                            while (iterAllItems.hasNext()) {
                                InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) iterAllItems.next();
                                calendarCycle.clear();
                                calendarCycle.setTime(dateToSort(infoWebSiteSection, infoWebSiteItem));
                                if (calendarCycle.get(Calendar.MONTH) == monthLink.intValue()
                                        && calendarCycle.get(Calendar.YEAR) == year.intValue()) {
                                    thisMonthList.add(infoWebSiteItem);
                                }
                            }
                        } else {
                            thisMonthList = monthList;
                        }

                        String keywordsList = createKeywordsList(thisMonthList);

                        // build body for items
                        String itemsFile = new String();
                        itemsFile = putBegginingOfItemFile(itemsFile, infoWebSiteSection, keywordsList);
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
                        itemsFile = itemsFile
                                .concat("Eventuais incoerências nesta página deverão ser comunicadas afim de se efectuar a respectiva correcção.");
                        itemsFile = itemsFile.concat("</span>");
                        itemsFile = itemsFile.concat("\n</p>\n");

                        itemsFile = itemsFile.concat("<h2>Arquivo de " + infoWebSiteSection.getName()
                                + "</h2>\n<p>\n");

                        itemsFile = buildLinksForArchive(infoWebSiteSection, currentMonth,
                                currentMonthFileName, monthsToCreateLinks, year, monthLink, itemsFile);
                        itemsFile = itemsFile.concat("</p>");
                        itemsFile = putEndOfItemFile(itemsFile);

                        // prepare file for transfer
                        File itemsFileToTransfer;
                        try {
                            if (monthLink.intValue() == currentMonth.get(Calendar.MONTH)
                                    && year.intValue() == currentMonth.get(Calendar.YEAR)) {
                                itemsFileToTransfer = buildFile(itemsFile, currentMonthFileName);
                            } else {
                                itemsFileToTransfer = buildFile(itemsFile, fileName);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return Boolean.FALSE;
                        }

                        try {
                            //                            Ftp.enviarFicheiro("/IstFtpServerConfig.properties",
                            // itemsFileToTransfer
                            //                                    .getName(), infoWebSiteSection.getFtpName() +
                            // "/");
                            Ftp.enviarFicheiroScp("/IstFtpServerConfig.properties", itemsFileToTransfer
                                    .getName(), infoWebSiteSection.getFtpName() + "/");

                        } catch (IOException e2) {
                            throw new FenixServiceException(e2);
                        }
                        // delete created file
                        itemsFileToTransfer.delete();
                    }
                }
            }
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
        return Boolean.TRUE;
    }

    /**
     * @param thisMonthList
     * @return
     */
    private String createKeywordsList(List thisMonthList) {

        // keywords should be separated by ', '

        String keywordsList = new String();
        if (thisMonthList.size() > 0) {
            Iterator thisMonthIter = thisMonthList.iterator();
            while (thisMonthIter.hasNext()) {
                InfoWebSiteItem element = (InfoWebSiteItem) thisMonthIter.next();
                if (element.getKeywords() != null && element.getKeywords().length() > 0) {
                    keywordsList = keywordsList.concat(", ");
                    keywordsList = keywordsList.concat(element.getKeywords());
                }
            }
        }
        return keywordsList;
    }

    /**
     * @param monthsToCreateLinks
     * @param monthsToCreateFiles
     */
    private void copyNewHashmapForFiles(HashMap monthsToCreateLinks, HashMap monthsToCreateFiles) {
        List allMonthLinks = null;
        Integer yearMap = null;
        Iterator iterYearsMap = monthsToCreateLinks.entrySet().iterator();
        while (iterYearsMap.hasNext()) {
            Map.Entry monthsMap = (Map.Entry) iterYearsMap.next();
            yearMap = (Integer) monthsMap.getKey();
            allMonthLinks = (List) monthsMap.getValue();

            List newList = new ArrayList();
            newList.addAll(allMonthLinks);
            monthsToCreateFiles.put(new Integer(yearMap.intValue()), newList);
        }
    }

    /**
     * @param infoWebSiteSection
     * @param currentMonth
     * @param currentMonthFileName
     * @param monthsToCreateLinks
     * @param year
     * @param monthLink
     * @param itemsFile
     * @return
     */
    private String buildLinksForArchive(InfoWebSiteSection infoWebSiteSection, Calendar currentMonth,
            String currentMonthFileName, HashMap monthsToCreateLinks, Integer year, Integer monthLink,
            String itemsFile) {
        List allMonthLinks = null;
        Integer yearMap = null;
        Iterator iterYearsMap = monthsToCreateLinks.entrySet().iterator();

        itemsFile = itemsFile.concat("<table cellspacing=\"5\" cellpadding=\"5\"><tr>");
        while (iterYearsMap.hasNext()) {
            Map.Entry monthsMap = (Map.Entry) iterYearsMap.next();
            yearMap = (Integer) monthsMap.getKey();
            allMonthLinks = (List) monthsMap.getValue();

            Collections.sort(allMonthLinks);

            itemsFile = itemsFile.concat("<td valign=\"top\"><strong>" + yearMap.toString()
                    + "</strong><br />\n");

            Iterator iterMonths = allMonthLinks.iterator();
            while (iterMonths.hasNext()) {
                Integer monthElem = (Integer) iterMonths.next();

                // we don't want to create a link for the same file
                if (monthLink.intValue() != monthElem.intValue() || !year.equals(yearMap)) {
                    Mes monthLinkString = new Mes(monthElem.intValue() + 1);
                    // in case this month is the current month the link must
                    // follow to current month items
                    if (monthElem.intValue() == currentMonth.get(Calendar.MONTH)
                            && yearMap.intValue() == currentMonth.get(Calendar.YEAR)) {
                        itemsFile = itemsFile.concat("<a href=\"" + currentMonthFileName + "\">"
                                + monthLinkString.toString() + "</a>\n");
                    } else {
                        itemsFile = itemsFile.concat("<a href=\"" + infoWebSiteSection.getFtpName()
                                + yearMap.toString() + "_"
                                + new Integer(monthElem.intValue() + 1).toString() + ".html\">"
                                + monthLinkString.toString() + "</a>\n");
                    }
                    itemsFile = itemsFile.concat("<br />\n");
                }
                if (!iterMonths.hasNext()) {
                    itemsFile = itemsFile.concat("</td>\n");
                }
            }
        }
        itemsFile = itemsFile.concat("</tr></table>");

        return itemsFile;
    }

    /**
     * @param monthsToCreateLinks
     * @param calendarCycle
     */
    private void findMonthsForArchive(HashMap monthsToCreateLinks, Calendar calendarCycle) {
        Integer monthToCreateLink = new Integer(calendarCycle.get(Calendar.MONTH));
        Integer yearOfMonthToCreateLink = new Integer(calendarCycle.get(Calendar.YEAR));
        if (monthsToCreateLinks.containsKey(yearOfMonthToCreateLink)) {
            List months = (List) monthsToCreateLinks.get(yearOfMonthToCreateLink);
            if (!months.contains(monthToCreateLink)) {
                months.add(monthToCreateLink);
            }
        } else {
            List months = new ArrayList();
            months.add(monthToCreateLink);
            monthsToCreateLinks.put(yearOfMonthToCreateLink, months);
        }
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
        stringFile = stringFile
                .concat("\t\t    <!-- END CONTENTS -->\n"
                        + "\t\t    </td>\n"
                        + "\t  </tr>\n"
                        + "</table>\n"
                        + "</div>\n"
                        + "<!--BEGIN SITEWIDE FOOTER -->\n"
                        + "<div id=\"footer\">\n"
                        + "<div id=\"foot_links\">\n"
                        + "<a href=\"http://www.ist.utl.pt/contactos/index-pt.shtml\">contactos</a> | \n"
                        + "<a href=\"mailto:ja@ist.utl.pt\">webmaster</a>\n"
                        + "</div>\n"
                        + "<div id=\"foot_copy\">&copy;2004, Instituto Superior Técnico. Todos os direitos reservados.</div>\n"
                        + "<div class=\"clear\"></div>\n" + "</div>\n" + "<!--END SITEWIDE FOOTER -->\n"
                        + "</body>\n" + "</html>\n");
        return stringFile;
    }

    private String putBegginingOfItemFile(String stringFile, InfoWebSiteSection infoWebSiteSection,
            String keywordsList) {
        stringFile = stringFile
                .concat("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
                        + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                        + "<head>\n"
                        + "<title>Instituto Superior T&eacute;cnico | "
                        + infoWebSiteSection.getName()
                        + "</title>\n"
                        + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />\n"
                        + "<meta name=\"keywords\" content=\"ensino,  ensino superior, universidade, instituto, ciência, instituto superior técnico, investigação e desenvolvimento"
                        + keywordsList
                        + "\" />\n"
                        + "<meta name=\"description\" content=\"O Instituto Superior Técnico é a maior escola de engenharia, ciência e tecnologia em Portugal.\" />\n"
                        + "<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"http://www.ist.utl.pt/css/iststyle.css\" />\n"
                        + "<link rel=\"stylesheet\" type=\"text/css\" media=\"print\" href=\"http://www.ist.utl.pt/css/print.css\" />\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<!-- UPGRADE MESSAGE FOR OLD BROWSERS -->\n"
                        + "<div class=\"browser_upgrade\">\n"
                        + "<p><strong>Aviso:</strong>\n"
                        + "Se est&aacute; a ler esta mensagem, provavelmente, o browser que utiliza n&atilde;o &eacute; compat&iacute;vel\n"
                        + "com os &quot;standards&quot; recomendados pela <a href=\"http://www.w3.org\">W3C</a>.\n"
                        + "Sugerimos vivamente que actualize o seu browser para ter uma melhor experi&ecirc;ncia\n"
                        + "de utiliza&ccedil;&atilde;o deste &quot;website&quot;.\n"
                        + "Mais informa&ccedil;&otilde;es em <a href=\"http://www.webstandards.org/upgrade/\">webstandards.org</a>.</p>\n"
                        + "<p><strong>Warning:</strong> If you are reading this message, probably, your\n"
                        + "browser is not compliant with the standards recommended by the <a href=\"http://www.w3.org\">W3C</a>. We suggest\n"
                        + "that you upgrade your browser to enjoy a better user experience of this website.\n"
                        + "More informations on <a href=\"http://www.webstandards.org/upgrade/\">webstandards.org</a>.</p>\n"
                        + "</div>\n"
                        + "<!-- END UPGRADE MESSAGE FOR OLD BROWSERS -->\n"
                        + "<div id=\"header\">"
                        + "<div id=\"logoist\"><a href=\"http://www.ist.utl.pt/\"><img src=\"/img/wwwist.gif\" width=\"234\" height=\"51\" border=\"0\" alt=\"[Logo] Instituto Superior Técnico\" /></a></div>"
                        + "<div id=\"header_links\"><a href=\"https://fenix.ist.utl.pt/loginPage.jsp\">Login .IST</a> | <a href=\"http://www.ist.utl.pt/html/instituto/index.shtml#cont\">Contactos</a> | <a href=\"http://www.ist.utl.pt/html/mapadosite/\">Mapa do Site</a> </div>"
                        + "<div id=\"search\">"
                        + "		<form method=\"get\" action=\"http://www.google.com/u/wwwist\">"
                        + "		<input type=\"hidden\" name=\"ie\" value=\"iso-8859-1\" />"
                        + "		<input type=\"hidden\" name=\"domains\" value=\"ist.utl.pt\" />"
                        + "		<input type=\"hidden\" name=\"sitesearch\" value=\"ist.utl.pt\" />"
                        + "		Pesquisar:"
                        + "		<input type=\"text\" id=\"textfield\" name=\"q\" size=\"17\" />"
                        + "		<input type=\"submit\" id=\"submit\" name=\"sa\" value=\"Google\" />"
                        + "		</form>"
                        + "</div>"
                        + "</div>"
                        + "<!--BEGIN SITEWIDE PROFILE NAVIGATION -->\n"
                        + "<div id=\"perfnav\">\n"
                        + "\t  <ul>\n"
                        + "\t\t    <li><a href=\"http://www.ist.utl.pt/\">In&iacute;cio</a></li>\n"
                        + "\t\t    <li><a href=\"http://www.ist.utl.pt/html/perfil/aluno.shtml\">Aluno</a></li>\n"
                        + "\t\t    <li><a href=\"http://www.ist.utl.pt/html/perfil/docente.shtml\">Docente</a></li>\n"
                        + "\t\t    <li><a href=\"http://www.ist.utl.pt/html/perfil/funcionario.shtml\">N&atilde;o Docente</a></li>\n"
                        + "\t\t    <li><a href=\"http://www.ist.utl.pt/html/perfil/international.shtml\">International</a></li>\n"
                        + "\t  </ul>\n"
                        + "</div>\n"
                        + "<!--END SITEWIDE PROFILE NAVIGATION -->\n"
                        + "<div id=\"holder\">\n"
                        + "<table id=\"bigtable\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n"
                        + "\t  <tr>\n"
                        + "\t\t    <td id=\"latnav_container\" width=\"155px\" nowrap=\"nowrap\">\n"
                        + "\t\t    <!--BEGIN MAIN NAVIGATION -->\n"
                        + "\t\t\t      <div id=\"latnav\">\n"
                        + "\t\t\t        <ul>\n"
                        + "\t\t\t\t          <li><a href=\"/html/destaques/\">Destaques</a></li>\n"
                        + "\t\t\t\t          <li><a href=\"/html/instituto/\">Instituto</a></li>\n"
                        + "\t\t\t\t          <li><a href=\"/html/estrutura/\">Estrutura</a></li>\n"
                        + "\t\t\t\t          <li><a href=\"/html/ensino/\">Ensino</a></li>\n"
                        + "\t\t\t\t          <li><a href=\"/html/id/\">I &amp; D</a></li>\n"
                        + "\t\t\t\t          <li><a href=\"/html/sociedade/\">Liga&ccedil;&atilde;o &agrave; Sociedade </a></li>\n"
                        + "\t\t\t\t          <li><a href=\"/html/viverist/\">Viver no IST</a></li>\n"
                        + "\t\t\t\t          <li><a href=\"/html/recursos/\">Recursos</a></li>\n"
                        + "\t\t\t        </ul>\n"
                        + "\t\t\t      </div>\n"
                        + "\t\t    <!--END MAIN NAVIGATION -->\n"
                        + "\t\t    </td>\n"
                        + "\t\t    <td width=\"100%\" colspan=\"3\" id=\"main\">\n"
                        + "\t\t    <!-- START MAIN PAGE CONTENTS HERE -->\n");

        return stringFile;
    }

    private String putItem(String itemFile, InfoWebSiteItem infoWebSiteItem) {
        // item\"s main entry
        itemFile = itemFile.concat("<p>");
        itemFile = itemFile.concat(infoWebSiteItem.getMainEntryText());
        itemFile = itemFile.concat("\n<br /><br />\n");
        itemFile = itemFile.concat("<span class=\"greytxt\">");
        itemFile = itemFile.concat("<strong>Contacto: </strong><a href=\"mailto:"
                + infoWebSiteItem.getAuthorEmail() + "\">" + infoWebSiteItem.getAuthorName() + "</a>");
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
        if (infoWebSiteItem.getInfoWebSiteSection().getWhatToSort().equals("ITEM_BEGIN_DAY")
                || infoWebSiteItem.getInfoWebSiteSection().getWhatToSort().equals("ITEM_END_DAY")) {
            if (infoWebSiteItem.getItemEndDayCalendar() == null) {
                stringFile = stringFile.concat(infoWebSiteItem.getItemBeginDayCalendar().get(
                        Calendar.DAY_OF_MONTH)
                        + " ");
                Mes month = new Mes(infoWebSiteItem.getItemBeginDayCalendar().get(Calendar.MONTH) + 1);
                stringFile = stringFile.concat(month.toString());
                stringFile = stringFile.concat(" "
                        + infoWebSiteItem.getItemBeginDayCalendar().get(Calendar.YEAR));
            } else {
                stringFile = stringFile.concat("De "
                        + infoWebSiteItem.getItemBeginDayCalendar().get(Calendar.DAY_OF_MONTH) + " ");
                if (infoWebSiteItem.getItemBeginDayCalendar().get(Calendar.MONTH) != infoWebSiteItem
                        .getItemEndDayCalendar().get(Calendar.MONTH)) {
                    Mes month = new Mes(
                            infoWebSiteItem.getItemBeginDayCalendar().get(Calendar.MONTH) + 1);
                    stringFile = stringFile.concat(month.toString() + " ");
                }

                if (infoWebSiteItem.getItemBeginDayCalendar().get(Calendar.YEAR) != infoWebSiteItem
                        .getItemEndDayCalendar().get(Calendar.YEAR)) {
                    stringFile = stringFile.concat(infoWebSiteItem.getItemBeginDayCalendar().get(
                            Calendar.YEAR)
                            + " ");
                }
                stringFile = stringFile.concat("a "
                        + infoWebSiteItem.getItemEndDayCalendar().get(Calendar.DAY_OF_MONTH) + " ");
                Mes month = new Mes(infoWebSiteItem.getItemEndDayCalendar().get(Calendar.MONTH) + 1);
                stringFile = stringFile.concat(month.toString() + " ");
                stringFile = stringFile.concat(String.valueOf(infoWebSiteItem.getItemEndDayCalendar()
                        .get(Calendar.YEAR)));
            }
        } else if (infoWebSiteItem.getInfoWebSiteSection().getWhatToSort().equals("CREATION_DATE")) {
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

    private String putExcerpt(InfoWebSiteSection infoWebSiteSection, String excerptsFile,
            InfoWebSiteItem infoWebSiteItem, Calendar currentMonth, String currentMonthFileName) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateToSort(infoWebSiteSection, infoWebSiteItem));

        String fileName = null;
        if (calendar.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH)
                && calendar.get(Calendar.YEAR) == currentMonth.get(Calendar.YEAR)) {
            fileName = currentMonthFileName;
        } else {
            fileName = infoWebSiteSection.getFtpName() + calendar.get(Calendar.YEAR) + "_"
                    + new Integer(calendar.get(Calendar.MONTH) + 1).toString() + ".html";
        }

        // item's excerpt
        excerptsFile = excerptsFile.concat("<p>");
        excerptsFile = excerptsFile.concat(infoWebSiteItem.getExcerpt());
        excerptsFile = excerptsFile.concat(" (" + "<a href=\"" + "/News/"
                + infoWebSiteSection.getFtpName() + "/" + fileName + "#anchor"
                + infoWebSiteItem.getIdInternal() + "\">" + "mais" + "</a>" + ")");
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