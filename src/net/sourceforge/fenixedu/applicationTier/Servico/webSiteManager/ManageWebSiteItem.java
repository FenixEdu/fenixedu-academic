package net.sourceforge.fenixedu.applicationTier.Servico.webSiteManager;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IWebSiteItem;
import net.sourceforge.fenixedu.domain.IWebSiteSection;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;

/**
 * @author Fernanda Quitério 13/04/2004
 *  
 */
abstract public class ManageWebSiteItem implements IService {

    //infoItem with an infoSection

    protected void checkData(InfoWebSiteItem infoWebSiteItem, IWebSiteSection webSiteSection)
            throws FenixServiceException {
        if (webSiteSection.getWhatToSort().equals("ITEM_BEGIN_DAY")) {
            if (infoWebSiteItem.getItemBeginDayCalendar() == null) {
                throw new InvalidArgumentsServiceException("label.itemValidity");
            }
        } else if (webSiteSection.getWhatToSort().equals("ITEM_END_DAY")) {
            if (infoWebSiteItem.getItemEndDayCalendar() == null) {
                throw new InvalidArgumentsServiceException("label.itemValidity");
            }
        }

        if (infoWebSiteItem.getCreationDate() == null) {
            throw new InvalidArgumentsServiceException("label.ceation.date");
        }

        if (infoWebSiteItem.getItemBeginDayCalendar() != null
                && infoWebSiteItem.getItemEndDayCalendar() != null
                && infoWebSiteItem.getItemEndDayCalendar().before(
                        infoWebSiteItem.getItemBeginDayCalendar())) {
            throw new InvalidArgumentsServiceException("label.itemValidity");
        }

        if (infoWebSiteItem.getExcerpt() != null) {
            if (StringUtils.countMatches(infoWebSiteItem.getExcerpt(), " ") >= webSiteSection
                    .getExcerptSize().intValue()) {
                throw new InvalidSituationServiceException();
            }
        }

        if (infoWebSiteItem.getOnlineBeginDay() != null && infoWebSiteItem.getOnlineEndDay() != null
                && infoWebSiteItem.getOnlineBeginDay().after(infoWebSiteItem.getOnlineEndDay())) {
            throw new InvalidArgumentsServiceException("message.onlineDay");
        }
    }

    protected void fillWebSiteItemForDB(InfoWebSiteItem infoWebSiteItem, String user,
            IPessoaPersistente persistentPerson, IPersistentWebSiteSection persistentWebSiteSection,
            IWebSiteSection webSiteSection, IWebSiteItem webSiteItem) throws FenixServiceException {
        try {

            IPerson person = (IPerson) persistentPerson.lerPessoaPorUsername(user);
            webSiteItem.setEditor(person);

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

            Calendar calendar = Calendar.getInstance();
            if (infoWebSiteItem.getCreationDate() != null) {
                if (webSiteItem.getCreationDate() != null) {
                    // we just want to look at day that was modified and not at
                    // hours
                    Calendar dayToModifyTo = Calendar.getInstance();
                    dayToModifyTo.setTime(infoWebSiteItem.getCreationDate());
                    calendar.setTime(webSiteItem.getCreationDate());
                    calendar.set(Calendar.DAY_OF_MONTH, dayToModifyTo.get(Calendar.DAY_OF_MONTH));
                    calendar.set(Calendar.MONTH, dayToModifyTo.get(Calendar.MONTH));
                    calendar.set(Calendar.YEAR, dayToModifyTo.get(Calendar.YEAR));
                } else {
                    calendar.setTime(infoWebSiteItem.getCreationDate());
                }
                webSiteItem.setCreationDate(new Timestamp(calendar.getTimeInMillis()));
            } else {
                if (webSiteItem.getCreationDate() == null) {
                    // there is no information about creation so put today
                    webSiteItem.setCreationDate(new Timestamp(calendar.getTimeInMillis()));
                }
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

                    StringTokenizer stringTokenizer = new StringTokenizer(infoWebSiteItem
                            .getMainEntryText(), " ");
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
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    protected Date dateToSort(InfoWebSiteSection infoWebSiteSection, InfoWebSiteItem infoWebSiteItem) {
        Date dateToSort = infoWebSiteItem.getCreationDate();
        if (infoWebSiteSection.getWhatToSort().equals("ITEM_BEGIN_DAY")) {
            dateToSort = infoWebSiteItem.getItemBeginDayCalendar().getTime();
        } else if (infoWebSiteSection.getWhatToSort().equals("ITEM_END_DAY")) {
            dateToSort = infoWebSiteItem.getItemEndDayCalendar().getTime();
        }
        return dateToSort;
    }
}