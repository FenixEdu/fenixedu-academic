package net.sourceforge.fenixedu.webServices;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.ConferenceArticles;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.Manual;
import net.sourceforge.fenixedu.domain.research.result.publication.OtherPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis.ThesisType;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;
import pt.utl.ist.sotis.bibtex.Fieldtype;
import pt.utl.ist.sotis.bibtex.Item;
import pt.utl.ist.sotis.bibtex.Item.Property;
import pt.utl.ist.sotis.bibtex.Items;
import pt.utl.ist.sotis.bibtex.Itemtype;
import pt.utl.ist.sotis.conversion.ConversionException;
import pt.utl.ist.sotis.conversion.SotisMarshaller;

public class ExportPublications {
    public byte[] harverst() {
        try {
            SotisMarshaller marshaller = new SotisMarshaller();

            Items items = marshaller.createItems();

            Set<JournalIssue> issues = new HashSet<JournalIssue>();
            Set<ScientificJournal> journals = new HashSet<ScientificJournal>();
            Set<EventEdition> eventEditions = new HashSet<EventEdition>();
            Set<ResearchEvent> researchEvents = new HashSet<ResearchEvent>();

            for (ResearchResult result : RootDomainObject.getInstance().getResultsSet()) {
                if (result instanceof ResearchResultPublication) {
                    ResearchResultPublication publication = (ResearchResultPublication) result;
                    String type = publication.getClass().getSimpleName().toLowerCase();
                    type =
                            type.replace("bookpart", "inbook").replace("otherpublication", "misc")
                                    .replace("technicalreport", "techreport").replace("conferenceedition", "proceedings")
                                    .replace("thesis", "dissertation");
                    try {
                        Item item =
                                marshaller.insertItem(items, publication.getExternalId(), Itemtype.fromValue(type),
                                        publication.getLastModificationDate());
                        Map<ResultParticipationRole, Property> entityProperties =
                                new HashMap<ResultParticipationRole, Property>();
                        for (ResultParticipation participation : publication.getOrderedResultParticipations()) {
                            if (!entityProperties.containsKey(participation.getRole())) {
                                entityProperties.put(
                                        participation.getRole(),
                                        marshaller.insertField(item,
                                                Fieldtype.valueOf(participation.getRole().name().toUpperCase())));
                            }
                            marshaller.insertEntity(entityProperties.get(participation.getRole()), participation.getPerson()
                                    .getName(), participation.getPersonOrder(), participation.getPerson().getIstUsername());
                        }
                        if (publication.hasCountry()) {
                            marshaller.insertText(marshaller.insertField(item, Fieldtype.COUNTRY), publication.getCountry()
                                    .getName(), null, null);
                        }
                        if (StringUtils.isNotBlank(publication.getOrganization())) {
                            marshaller.insertText(marshaller.insertField(item, Fieldtype.ORGANIZATION),
                                    publication.getOrganization(), null, null);
                        }
                        if (multilanguageHasNonEmptyContent(publication.getKeywords())) {
                            marshaller.insertText(marshaller.insertField(item, Fieldtype.KEYWORDS),
                                    extractBestMLS(publication.getKeywords()), null, null);
                        }
                        if (multilanguageHasNonEmptyContent(publication.getNote())) {
                            marshaller.insertText(marshaller.insertField(item, Fieldtype.NOTE),
                                    extractBestMLS(publication.getNote()), null, null);
                        }
                        if (StringUtils.isNotBlank(publication.getPublisher())) {
                            marshaller.insertEntity(marshaller.insertField(item, Fieldtype.PUBLISHER),
                                    publication.getPublisher(), null, null);
                        }
                        if (StringUtils.isNotBlank(publication.getTitle())) {
                            marshaller.insertText(marshaller.insertField(item, Fieldtype.TITLE), publication.getTitle(), null,
                                    null);
                        }
                        if (StringUtils.isNotBlank(publication.getUrl())) {
                            marshaller.insertUri(marshaller.insertField(item, Fieldtype.URL), publication.getUrl(), null);
                        }
                        if (!(publication instanceof Inproceedings || publication instanceof Proceedings)) {
                            if (!(publication instanceof Article)) {
                                if (publication.getYear() != null && publication.getYear() > 0) {
                                    marshaller.insertDate(marshaller.insertField(item, Fieldtype.DATE), publication.getYear(),
                                            publication.getMonth() != null ? publication.getMonth().getNumberOfMonth() : null,
                                            null);
                                }
                            }
                        }
                        if (result instanceof Article) {
                            Article article = (Article) result;
                            insertPages(marshaller, item, article.getFirstPage(), article.getLastPage());
                            if (StringUtils.isNotBlank(article.getLanguage())) {
                                insertLanguage(marshaller, item, article.getLanguage());
                            }
                            if (article.getJournalIssue() != null) {
                                marshaller.insertItemRef(marshaller.insertField(item, Fieldtype.JOURNAL), article
                                        .getJournalIssue().getNameWithScientificJournal(), article.getJournalIssue()
                                        .getExternalId(), null);
                                issues.add(article.getJournalIssue());
                            }
                            if (article.getIssn() != null) {
                                insertISSN(marshaller, item, article.getIssn().toString());
                            }
                        } else if (result instanceof Book) {
                            Book book = (Book) result;
                            if (StringUtils.isNotBlank(book.getAddress())) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.ADDRESS), book.getAddress(), null,
                                        null);
                            }
                            if (StringUtils.isNotBlank(book.getEdition())) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.EDITION), book.getEdition(), null,
                                        null);
                            }
                            if (StringUtils.isNotBlank(book.getIsbn())) {
                                insertISBN(marshaller, item, book.getIsbn());
                            }
                            if (StringUtils.isNotBlank(book.getLanguage())) {
                                insertLanguage(marshaller, item, book.getLanguage());
                            }
                            if (book.getNumberPages() != null) {
                                marshaller.insertNumber(marshaller.insertField(item, Fieldtype.NUMBEROFPAGES), new BigInteger(
                                        book.getNumberPages().toString()), null);
                            }
                            if (book.getScope() != null) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.SCOPE), book.getScope().name(),
                                        null, null);
                            }
                            if (StringUtils.isNotBlank(book.getSeries())) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.SERIES), book.getSeries(), null,
                                        null);
                            }
                            if (StringUtils.isNotBlank(book.getVolume())) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.VOLUME), book.getVolume(), null,
                                        null);
                            }
                        } else if (result instanceof BookPart) {
                            BookPart part = (BookPart) result;
                            if (StringUtils.isNotBlank(part.getAddress())) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.ADDRESS), part.getAddress(), null,
                                        null);
                            }
                            if (StringUtils.isNotBlank(part.getBookTitle())) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.BOOKTITLE), part.getBookTitle(),
                                        null, null);
                            }
                            if (StringUtils.isNotBlank(part.getChapter())) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.CHAPTER), part.getChapter(), null,
                                        null);
                            }
                            if (StringUtils.isNotBlank(part.getEdition())) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.EDITION), part.getEdition(), null,
                                        null);
                            }
                            insertPages(marshaller, item, part.getFirstPage(), part.getLastPage());
                            if (StringUtils.isNotBlank(part.getSeries())) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.SERIES), part.getSeries(), null,
                                        null);
                            }
                            if (StringUtils.isNotBlank(part.getVolume())) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.VOLUME), part.getVolume(), null,
                                        null);
                            }
                        } else if (result instanceof ConferenceArticles) {
                            ConferenceArticles conferenceArticle = (ConferenceArticles) result;
                            if (conferenceArticle.getScope() != null) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.SCOPE), conferenceArticle.getScope()
                                        .name(), null, null);
                            }
                            if (conferenceArticle.hasEventConferenceArticlesAssociation()) {
                                EventEdition eventEdition =
                                        conferenceArticle.getEventConferenceArticlesAssociation().getEventEdition();
                                marshaller.insertItemRef(marshaller.insertField(item, Fieldtype.EVENT),
                                        eventEdition.getFullName(), eventEdition.getExternalId(), null);
                                eventEditions.add(eventEdition);
                            }
                            if (result instanceof Inproceedings) {
                                Inproceedings inproceedings = (Inproceedings) result;
                                if (StringUtils.isNotBlank(inproceedings.getAddress())) {
                                    marshaller.insertText(marshaller.insertField(item, Fieldtype.ADDRESS),
                                            inproceedings.getAddress(), null, null);
                                }
                                insertPages(marshaller, item, inproceedings.getFirstPage(), inproceedings.getLastPage());
                                if (StringUtils.isNotBlank(inproceedings.getLanguage())) {
                                    insertLanguage(marshaller, item, inproceedings.getLanguage());
                                }
                            } else if (result instanceof Proceedings) {
                                Proceedings proceedings = (Proceedings) result;
                                if (StringUtils.isNotBlank(proceedings.getAddress())) {
                                    marshaller.insertText(marshaller.insertField(item, Fieldtype.ADDRESS),
                                            proceedings.getAddress(), null, null);
                                }
                            }
                        } else if (result instanceof Manual) {
                            Manual manual = (Manual) result;
                            if (StringUtils.isNotBlank(manual.getAddress())) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.ADDRESS), manual.getAddress(), null,
                                        null);
                            }
                            if (StringUtils.isNotBlank(manual.getEdition())) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.EDITION), manual.getEdition(), null,
                                        null);
                            }
                        } else if (result instanceof OtherPublication) {
                            OtherPublication other = (OtherPublication) result;
                            if (StringUtils.isNotBlank(other.getAddress())) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.ADDRESS), other.getAddress(), null,
                                        null);
                            }
                            if (StringUtils.isNotBlank(other.getHowPublished())) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.HOWPUBLISHED),
                                        other.getHowPublished(), null, null);
                            }
                            if (StringUtils.isNotBlank(other.getLanguage())) {
                                insertLanguage(marshaller, item, other.getLanguage());
                            }
                            if (other.getNumberPages() != null) {
                                marshaller.insertNumber(marshaller.insertField(item, Fieldtype.NUMBEROFPAGES), new BigInteger(
                                        other.getNumberPages().toString()), null);
                            }
                            if (StringUtils.isNotBlank(other.getOtherPublicationType())) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.TYPE),
                                        other.getOtherPublicationType(), null, null);
                            }
                        } else if (result instanceof TechnicalReport) {
                            TechnicalReport technicalReport = (TechnicalReport) result;
                            if (StringUtils.isNotBlank(technicalReport.getAddress())) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.ADDRESS),
                                        technicalReport.getAddress(), null, null);
                            }
                            if (StringUtils.isNotBlank(technicalReport.getLanguage())) {
                                insertLanguage(marshaller, item, technicalReport.getLanguage());
                            }
                            if (StringUtils.isNotBlank(technicalReport.getNumber())) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.NUMBER),
                                        technicalReport.getNumber(), null, null);
                            }
                            if (technicalReport.getNumberPages() != null) {
                                marshaller.insertNumber(marshaller.insertField(item, Fieldtype.NUMBEROFPAGES), new BigInteger(
                                        technicalReport.getNumberPages().toString()), null);
                            }
                            if (StringUtils.isNotBlank(technicalReport.getTechnicalReportType())) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.TYPE),
                                        technicalReport.getTechnicalReportType(), null, null);
                            }
                        } else if (result instanceof Thesis) {
                            Thesis thesis = (Thesis) result;
                            if (thesis.getThesisType() != null) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.DISSERTATIONTYPE),
                                        convertThesisType(thesis.getThesisType()), null, null);
                            }
                            if (StringUtils.isNotBlank(thesis.getAddress())) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.ADDRESS), thesis.getAddress(), null,
                                        null);
                            }
                            if (StringUtils.isNotBlank(thesis.getLanguage())) {
                                insertLanguage(marshaller, item, thesis.getLanguage());
                            }
                            if (thesis.getNumberPages() != null) {
                                marshaller.insertNumber(marshaller.insertField(item, Fieldtype.NUMBEROFPAGES), new BigInteger(
                                        thesis.getNumberPages().toString()), null);
                            }
                            // thesis.getScientificArea();
                        }
                        for (ResearchResultDocumentFile documentFile : publication.getAllResultDocumentFiles()) {
                            if (!documentFile.getFilename().equals("default.txt")) {
                                marshaller.insertText(marshaller.insertField(item, Fieldtype.FILE),
                                        documentFile.getExternalStorageIdentification(), null, null);
                            }
                        }
                    } catch (ConversionException e) {
                        e.printStackTrace();
                    }
                }
            }

            for (JournalIssue issue : issues) {
                Item item = marshaller.insertItem(items, issue.getExternalId(), Itemtype.JOURNALISSUE, new DateTime());
                if (issue.getScientificJournal() != null) {
                    marshaller.insertItemRef(marshaller.insertField(item, Fieldtype.JOURNAL), issue.getScientificJournal()
                            .getName(), issue.getScientificJournal().getExternalId(), null);
                    journals.add(issue.getScientificJournal());
                }
                if (issue.getYear() != null && issue.getYear() > 0) {
                    marshaller.insertDate(marshaller.insertField(item, Fieldtype.DATE), issue.getYear(),
                            issue.getMonth() != null ? issue.getMonth().getNumberOfMonth() : null, null);
                }
                if (StringUtils.isNotBlank(issue.getNumber())) {
                    marshaller.insertText(marshaller.insertField(item, Fieldtype.NUMBER), issue.getNumber(), null, null);
                }
                if (StringUtils.isNotBlank(issue.getVolume())) {
                    marshaller.insertText(marshaller.insertField(item, Fieldtype.VOLUME), issue.getVolume(), null, null);
                }
                if (StringUtils.isNotBlank(issue.getUrl())) {
                    marshaller.insertUri(marshaller.insertField(item, Fieldtype.URL), issue.getUrl(), null);
                }
                // for (JournalIssueParticipation participation :
                // issue.getParticipationsSet()) {
                // Party party = participation.getParty();
                // ResearchActivityParticipationRole role =
                // participation.getRole();
                // }
            }

            for (ScientificJournal journal : journals) {
                Item item = marshaller.insertItem(items, journal.getExternalId(), Itemtype.JOURNAL, new DateTime());
                if (StringUtils.isNotBlank(journal.getIssn()) && !journal.getIssn().trim().equals("0")) {
                    insertISSN(marshaller, item, journal.getIssn().toString());
                }
                if (journal.getLocationType() != null) {
                    marshaller.insertText(marshaller.insertField(item, Fieldtype.SCOPE), journal.getLocationType().name(), null,
                            null);
                }
                if (StringUtils.isNotBlank(journal.getName())) {
                    marshaller.insertText(marshaller.insertField(item, Fieldtype.TITLE), journal.getName(), null, null);
                }
                // journal.getParticipationsSet();
                if (StringUtils.isNotBlank(journal.getPublisher())) {
                    marshaller
                            .insertEntity(marshaller.insertField(item, Fieldtype.PUBLISHER), journal.getPublisher(), null, null);
                }
                if (StringUtils.isNotBlank(journal.getUrl())) {
                    marshaller.insertUri(marshaller.insertField(item, Fieldtype.URL), journal.getUrl(), null);
                }
            }

            for (EventEdition edition : eventEditions) {
                Item item = marshaller.insertItem(items, edition.getExternalId(), Itemtype.PROCEEDINGS, new DateTime());
                if (edition.getEvent() != null) {
                    marshaller.insertItemRef(marshaller.insertField(item, Fieldtype.EVENT), edition.getEvent().getName(), edition
                            .getEvent().getExternalId(), null);
                    researchEvents.add(edition.getEvent());
                }
                if (StringUtils.isNotBlank(edition.getEdition())) {
                    marshaller.insertText(marshaller.insertField(item, Fieldtype.EDITION), edition.getEdition(), null, null);
                }
                if (StringUtils.isNotBlank(edition.getEventLocation())) {
                    marshaller.insertText(marshaller.insertField(item, Fieldtype.LOCATION), edition.getEventLocation(), null,
                            null);
                }
                if (StringUtils.isNotBlank(edition.getOrganization())) {
                    marshaller.insertText(marshaller.insertField(item, Fieldtype.ORGANIZATION), edition.getOrganization(), null,
                            null);
                }
                if (edition.getStartDate() != null || edition.getEndDate() != null) {
                    LocalDate start =
                            edition.getStartDate() != null ? edition.getStartDate().toLocalDate() : edition.getEndDate()
                                    .toLocalDate();
                    LocalDate end =
                            edition.getEndDate() != null ? edition.getEndDate().toLocalDate() : edition.getStartDate()
                                    .toLocalDate();
                    marshaller.insertInterval(marshaller.insertField(item, Fieldtype.TIMEINTERVAL), start, end, null);
                }
                if (StringUtils.isNotBlank(edition.getUrl())) {
                    marshaller.insertUri(marshaller.insertField(item, Fieldtype.URL), edition.getUrl(), null);
                }
            }

            for (ResearchEvent event : researchEvents) {
                Item item = marshaller.insertItem(items, event.getExternalId(), Itemtype.CONFERENCE, new DateTime());
                if (event.getEventType() != null) {
                    marshaller.insertText(marshaller.insertField(item, Fieldtype.TYPE), event.getEventType().getName(), null,
                            null);
                }
                if (event.getLocationType() != null) {
                    marshaller.insertText(marshaller.insertField(item, Fieldtype.SCOPE), event.getLocationType().name(), null,
                            null);
                }
                if (StringUtils.isNotBlank(event.getName())) {
                    marshaller.insertText(marshaller.insertField(item, Fieldtype.TITLE), event.getName(), null, null);
                }
                if (StringUtils.isNotBlank(event.getUrl())) {
                    marshaller.insertUri(marshaller.insertField(item, Fieldtype.URL), event.getUrl(), null);
                }
            }
            return marshaller.marshallToByteArray(items);
        } catch (ConversionException e) {
            throw new RuntimeException(e);
        }
    }

    private String convertThesisType(ThesisType type) {
        switch (type) {
        case Graduation_Thesis:
            return "msc";
        case Masters_Thesis:
            return "msc";
        case PhD_Thesis:
            return "phd";
        }
        return null;
    }

    private void insertISBN(SotisMarshaller marshaller, Item item, String isbn) {
        isbn = isbn.trim();
        if (!isbn.equals("0")) {
            if (isbn.matches("(\\d{9}[0-9X])|(\\d{1,5}(\\-|\\s)\\d{1,7}(\\-|\\s)\\d{1,6}(\\-|\\s)[0-9X])|((978|979)(\\d{10}|((\\-|\\s)\\d{1,5}(\\-|\\s)\\d{1,7}(\\-|\\s)\\d{1,6}(\\-|\\s)[0-9])))")) {
                marshaller.insertISBN(marshaller.insertField(item, Fieldtype.ISBN), isbn);
            } else {
                System.err.println("Uninterpretable isbn: " + isbn);
                marshaller.insertText(marshaller.insertField(item, Fieldtype.ISBN), isbn, null, null);
            }
        }
    }

    private void insertISSN(SotisMarshaller marshaller, Item item, String issn) {
        issn = issn.trim();
        if (!issn.equals("0")) {
            if (issn.matches("[0-9]{4}-[0-9]{3}[0-9X]")) {
                marshaller.insertISSN(marshaller.insertField(item, Fieldtype.ISSN), issn);
            } else {
                System.err.println("Uninterpretable issn: " + issn);
                marshaller.insertText(marshaller.insertField(item, Fieldtype.ISSN), issn, null, null);
            }
        }
    }

    private void insertLanguage(SotisMarshaller marshaller, Item item, String language) {
        language = language.trim().toLowerCase();
        String lang = null;
        if (language.matches("ingl.s") || language.equals("english") || language.equals("en")) {
            lang = Language.en.name();
        } else if (language.matches("portug(.*)s") || language.equalsIgnoreCase("portuguese") || language.equalsIgnoreCase("pt")) {
            lang = Language.pt.name();
        } else if (language.matches("franc.s") || language.equalsIgnoreCase("french")) {
            lang = Language.fr.name();
        } else if (language.equalsIgnoreCase("italian") || language.equalsIgnoreCase("italiano")) {
            lang = Language.it.name();
        } else if (language.matches("alem.o") || language.equalsIgnoreCase("german") || language.equalsIgnoreCase("de")) {
            lang = Language.de.name();
        } else if (language.equalsIgnoreCase("espanhol") || language.endsWith("spanish")) {
            lang = Language.es.name();
        }
        if (lang != null) {
            marshaller.insertLanguage(marshaller.insertField(item, Fieldtype.LANGUAGE), lang, null);
        } else {
            System.err.println("Uninterpretable language: " + language);
            marshaller.insertText(marshaller.insertField(item, Fieldtype.LANGUAGE), language, null, null);
        }
    }

    private boolean multilanguageHasNonEmptyContent(MultiLanguageString mls) {
        if (mls == null) {
            return false;
        }
        for (String content : mls.getAllContents()) {
            if (content.length() > 0) {
                return true;
            }
        }
        return false;
    }

    private String extractBestMLS(MultiLanguageString mls) {
        if (mls.hasLanguage(Language.en)) {
            return mls.getContent(Language.en);
        }
        if (mls.hasLanguage(Language.pt)) {
            return mls.getContent(Language.pt);
        }
        return mls.getContent();
    }

    private void insertPages(SotisMarshaller marshaller, Item item, Integer first, Integer last) {
        StringBuilder builder = new StringBuilder();
        if (first != null && first >= 0) {
            builder.append(first);
        }
        if (first != null && first > 0 && last != null && last >= 0) {
            builder.append("-");
        }
        if (last != null && last >= 0) {
            builder.append(last);
        }
        if (builder.length() > 0) {
            marshaller.insertPages(marshaller.insertField(item, Fieldtype.PAGES), builder.toString(), null);
        }
    }

    public byte[] fetchFile(String storageId) {
        File file = File.readByExternalStorageIdentification(storageId);
        if (file != null) {
            return file.getContents();
        }
        return null;
    }

    public String getFilename(String storageId) {
        File file = File.readByExternalStorageIdentification(storageId);
        if (file != null) {
            return file.getFilename();
        }
        return null;
    }

    public String getFilePermissions(String storageId) {
        File file = File.readByExternalStorageIdentification(storageId);
        if (file != null) {
            ResearchResultDocumentFile result = (ResearchResultDocumentFile) file;
            return result.getFileResultPermittedGroupType().name();
        }
        return null;
    }
}
