package net.sourceforge.fenixedu.domain.research.result.publication;

import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.dom.BibtexPersonList;
import bibtex.dom.BibtexString;
import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.util.Month;

/**
 * Inbook A part of a book, which may be a chapter and/or a range of pages.
 * Required fields: author or editor, title, chapter and/or pages, publisher,
 * year. Optional fields: volume, series, address, edition, month, note.
 * Incollection A part of a book with its own title. Required fields: author,
 * title, booktitle, publisher, year. Optional fields: editor, pages,
 * organization, publisher, address, month, note.
 */
public class BookPart extends BookPart_Base {

    public enum BookPartType {
        Inbook, Incollection;
    }

    public BookPart() {
        super();
    }

    /**
     * BookPart - Inbook constructor
     */
    public BookPart(Person participator, ResultParticipationRole participatorRole,
            BookPartType bookPartType, String title, String chapter, Integer firstPage,
            Integer lastPage, Unit publisher, Integer year, String volume, String series,
            String edition, Country country, String address, String note, Month month, String url) {
        this();
        checkInbookRequiredParameters(bookPartType, title, chapter, firstPage, lastPage, publisher, year);
        super.setCreatorParticipation(participator, participatorRole);
        fillAllInbookAttributes(bookPartType, title, chapter, firstPage, lastPage, publisher, year,
                volume, series, edition, country, address, note, month, url);
    }

    /**
     * BookPart - Incollection constructor
     */
    public BookPart(Person participator, ResultParticipationRole participatorRole,
            BookPartType bookPartType, String title, String bookTitle, Unit publisher, Integer year,
            Integer firstPage, Integer lastPage, Unit organization, Country country, String address,
            String note, Month month, String url) {
        this();
        checkIncollectionRequiredParameters(bookPartType, title, bookTitle, publisher, year);
        super.setCreatorParticipation(participator, participatorRole);
        fillAllIncollectionAttributes(bookPartType, title, bookTitle, publisher, year, firstPage,
                lastPage, organization, country, address, note, month, url);
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAllInbook(BookPartType bookPartType, String title, String chapter,
            Integer firstPage, Integer lastPage, Unit publisher, Integer year, String volume,
            String series, String edition, Country country, String address, String note, Month month,
            String url) {
        checkInbookRequiredParameters(bookPartType, title, chapter, firstPage, lastPage, publisher, year);
        checkBookPartTypeChange(bookPartType);
        fillAllInbookAttributes(bookPartType, title, chapter, firstPage, lastPage, publisher, year,
                volume, series, edition, country, address, note, month, url);
        super.setModifiedByAndDate();
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAllIncollection(BookPartType bookPartType, String title, String bookTitle,
            Unit publisher, Integer year, Integer firstPage, Integer lastPage, Unit organization,
            Country country, String address, String note, Month month, String url) {
        checkIncollectionRequiredParameters(bookPartType, title, bookTitle, publisher, year);
        checkBookPartTypeChange(bookPartType);
        fillAllIncollectionAttributes(bookPartType, title, bookTitle, publisher, year, firstPage,
                lastPage, organization, country, address, note, month, url);
        super.setModifiedByAndDate();
    }

    private void fillAllInbookAttributes(BookPartType bookPartType, String title, String chapter,
            Integer firstPage, Integer lastPage, Unit publisher, Integer year, String volume,
            String series, String edition, Country country, String address, String note, Month month,
            String url) {
        super.setBookPartType(bookPartType);
        super.setTitle(title);
        super.setPublisher(publisher);
        super.setYear(year);
        super.setChapter(chapter);
        super.setFirstPage(firstPage);
        super.setLastPage(lastPage);
        super.setVolume(volume);
        super.setSeries(series);
        super.setEdition(edition);
        super.setCountry(country);
        super.setAddress(address);
        super.setNote(note);
        super.setMonth(month);
        super.setUrl(url);
    }

    private void checkInbookRequiredParameters(BookPartType bookPartType, String title, String chapter,
            Integer firstPage, Integer lastPage, Unit publisher, Integer year) {
        if (!bookPartType.equals(BookPartType.Inbook))
            throw new DomainException("error.researcher.BookPart.wrongType");
        checkCommonRequired(bookPartType, title, publisher, year);
        if ((chapter == null) || (chapter.length() == 0)) {
            if ((firstPage == null) || (firstPage <= 0) || (lastPage == null) || (lastPage <= 0))
                throw new DomainException("error.researcher.BookPart.needChapterOrPages");
        }
    }

    private void fillAllIncollectionAttributes(BookPartType bookPartType, String title,
            String bookTitle, Unit publisher, Integer year, Integer firstPage, Integer lastPage,
            Unit organization, Country country, String address, String note, Month month, String url) {
        super.setBookPartType(bookPartType);
        super.setTitle(title);
        super.setBookTitle(bookTitle);
        super.setPublisher(publisher);
        super.setYear(year);
        super.setFirstPage(firstPage);
        super.setLastPage(lastPage);
        super.setOrganization(organization);
        super.setCountry(country);
        super.setAddress(address);
        super.setNote(note);
        super.setMonth(month);
        super.setUrl(url);
    }

    private void checkIncollectionRequiredParameters(BookPartType bookPartType, String title,
            String bookTitle, Unit publisher, Integer year) {
        if (!bookPartType.equals(BookPartType.Incollection))
            throw new DomainException("error.researcher.BookPart.wrongType");
        checkCommonRequired(bookPartType, title, publisher, year);
        if (bookTitle == null || bookTitle.length() == 0)
            throw new DomainException("error.researcher.BookPart.bookTitle.null");
    }

    private void checkCommonRequired(BookPartType bookPartType, String title, Unit publisher,
            Integer year) {
        if (bookPartType == null)
            throw new DomainException("error.researcher.BookPart.bookPartType.null");
        if (title == null || title.length() == 0)
            throw new DomainException("error.researcher.BookPart.title.null");
        if (publisher == null)
            throw new DomainException("error.researcher.BookPart.publisher.null");
        if (year == null)
            throw new DomainException("error.researcher.BookPart.year.null");
    }

    private void checkBookPartTypeChange(BookPartType bookPartType) {
        // if changing type of BookPart remove fields that don't belong to the
        // new type
        if (!this.getBookPartType().equals(bookPartType)) {
            switch (bookPartType) {
            case Inbook:
                super.setBookTitle(null);
                super.setOrganization(null);
                break;

            case Incollection:
                super.setChapter(null);
                super.setVolume(null);
                super.setSeries(null);
                super.setEdition(null);
                break;

            default:
                break;
            }
        }
    }

    @Override
    public String getResume() {
        String resume = getParticipationsAndTitleString();

        if ((getBookTitle() != null) && (getBookTitle().length() > 0))
            resume = resume + getBookTitle() + ", ";
        if ((getChapter() != null) && (getChapter().length() > 0))
            resume = resume + getChapter() + ", ";
        if ((getFirstPage() != null) && (getFirstPage() > 0) && (getLastPage() != null)
                && (getLastPage() > 0))
            resume = resume + "Pag. " + getFirstPage() + " - " + getLastPage() + ", ";
        if ((getEdition() != null) && (getEdition().length() > 0))
            resume = resume + "Ed. " + getEdition() + ", ";
        if ((getVolume() != null) && (getVolume().length() > 0))
            resume = resume + "Vol. " + getVolume() + ", ";
        if ((getSeries() != null) && (getSeries().length() > 0))
            resume = resume + "Serie " + getSeries() + ", ";
        if ((getYear() != null) && (getYear() > 0))
            resume = resume + getYear() + ", ";
        if (getPublisher() != null)
            resume = resume + getPublisher().getName() + ", ";
        if ((getAddress() != null) && (getAddress().length() > 0))
            resume = resume + getAddress() + ", ";

        resume = finishResume(resume);
        return resume;
    }

    @Override
    public BibtexEntry exportToBibtexEntry() {
        BibtexFile bibtexFile = new BibtexFile();

        BibtexEntry bibEntry;
        if (getBookPartType().equals(BookPartType.Inbook))
            bibEntry = bibtexFile.makeEntry("inbook", generateBibtexKey());
        else
            bibEntry = bibtexFile.makeEntry("incollection", generateBibtexKey());

        bibEntry.setField("title", bibtexFile.makeString(getTitle()));
        bibEntry.setField("year", bibtexFile.makeString(getYear().toString()));
        if ((getBookTitle() != null) && (getBookTitle().length() > 0))
            bibEntry.setField("booktitle", bibtexFile.makeString(getBookTitle()));
        if ((getChapter() != null) && (getChapter().length() > 0))
            bibEntry.setField("chapter", bibtexFile.makeString(getChapter()));
        if ((getVolume() != null) && (getVolume().length() > 0))
            bibEntry.setField("volume", bibtexFile.makeString(getVolume()));
        if ((getSeries() != null) && (getSeries().length() > 0))
            bibEntry.setField("series", bibtexFile.makeString(getSeries()));
        if ((getEdition() != null) && (getEdition().length() > 0))
            bibEntry.setField("edition", bibtexFile.makeString(getEdition()));
        if (getPublisher() != null)
            bibEntry.setField("publisher", bibtexFile.makeString(getPublisher().getName()));
        if (getOrganization() != null)
            bibEntry.setField("organization", bibtexFile.makeString(getOrganization().getName()));
        if ((getFirstPage() != null) && (getLastPage() != null) && (getFirstPage() < getLastPage()))
            bibEntry.setField("pages", bibtexFile.makeString(getFirstPage() + "-" + getLastPage()));
        if ((getAddress() != null) && (getAddress().length() > 0))
            bibEntry.setField("address", bibtexFile.makeString(getAddress()));
        if (getMonth() != null)
            bibEntry.setField("month", bibtexFile.makeString(getMonth().toString().toLowerCase()));
        if ((getNote() != null) && (getNote().length() > 0))
            bibEntry.setField("note", bibtexFile.makeString(getNote()));

        BibtexPersonList authorsList = getBibtexAuthorsList(bibtexFile, getAuthors());
        if (authorsList != null) {
            BibtexString bplString = bibtexFile.makeString(bibtexPersonListToString(authorsList));
            bibEntry.setField("author", bplString);
        }

        BibtexPersonList editorsList = getBibtexEditorsList(bibtexFile, getEditors());
        if (editorsList != null) {
            BibtexString bplString = bibtexFile.makeString(bibtexPersonListToString(editorsList));
            bibEntry.setField("editor", bplString);
        }

        return bibEntry;
    }

    @Override
    public void setBookPartType(BookPartType bookPartType) {
        throw new DomainException("error.researcher.BookPart.call", "setBookPartType");
    }

    @Override
    public void setTitle(String title) {
        throw new DomainException("error.researcher.BookPart.call", "setTitle");
    }

    @Override
    public void setPublisher(Unit publisher) {
        throw new DomainException("error.researcher.BookPart.call", "setPublisher");
    }

    @Override
    public void setYear(Integer year) {
        throw new DomainException("error.researcher.BookPart.call", "setYear");
    }

    @Override
    public void setChapter(String chapter) {
        throw new DomainException("error.researcher.BookPart.call", "setChapter");
    }

    @Override
    public void setFirstPage(Integer firstPage) {
        throw new DomainException("error.researcher.BookPart.call", "setFirstPage");
    }

    @Override
    public void setLastPage(Integer lastPage) {
        throw new DomainException("error.researcher.BookPart.call", "setLastPage");
    }

    @Override
    public void setVolume(String volume) {
        throw new DomainException("error.researcher.BookPart.call", "setVolume");
    }

    @Override
    public void setSeries(String series) {
        throw new DomainException("error.researcher.BookPart.call", "setSeries");
    }

    @Override
    public void setEdition(String edition) {
        throw new DomainException("error.researcher.BookPart.call", "setEdition");
    }

    @Override
    public void setCountry(Country country) {
        throw new DomainException("error.researcher.BookPart.call", "setCountry");
    }

    @Override
    public void setAddress(String address) {
        throw new DomainException("error.researcher.BookPart.call", "setTitle");
    }

    @Override
    public void setNote(String note) {
        throw new DomainException("error.researcher.BookPart.call", "setNote");
    }

    @Override
    public void setMonth(Month month) {
        throw new DomainException("error.researcher.BookPart.call", "setMonth");
    }

    @Override
    public void setUrl(String url) {
        throw new DomainException("error.researcher.BookPart.call", "setUrl");
    }

    @Override
    public void setBookTitle(String bookTitle) {
        throw new DomainException("error.researcher.BookPart.call", "setBookTitle");
    }

    @Override
    public void setOrganization(Unit organization) {
        throw new DomainException("error.researcher.BookPart.call", "setOrganization");
    }
}
