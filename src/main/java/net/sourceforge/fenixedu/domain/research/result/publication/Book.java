package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.util.Month;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.ResultPredicates;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;
import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.dom.BibtexPersonList;
import bibtex.dom.BibtexString;

/**
 * A book with an explicit publisher. Required fields: author or editor, title,
 * publisher, year. Optional fields: volume, series, address, edition, month,
 * note.
 * 
 * Extra from previous publications: isbn, numberPages, country, language, scope
 */
public class Book extends Book_Base {

    private static final String usedSchema = "result.publication.presentation.Book";

    private Book() {
        super();
    }

    public Book(Person participator, ResultParticipationRole participatorRole, String title, MultiLanguageString keywords,
            String publisher, Integer year, String volume, String series, String address, String edition, String isbn,
            Integer numberPages, String language, Country country, ScopeType scope, MultiLanguageString note, Month month,
            String url) {
        this();
        super.checkRequiredParameters(keywords, note);
        checkRequiredParameters(title, note, keywords, publisher, year);
        super.setCreatorParticipation(participator, participatorRole);
        fillAllAttributes(title, keywords, publisher, year, volume, series, address, edition, isbn, numberPages, language,
                country, scope, note, month, url);
    }

    public void setEditAll(String title, MultiLanguageString keywords, String publisher, Integer year, String volume,
            String series, String address, String edition, String isbn, Integer numberPages, String language, Country country,
            ScopeType scope, MultiLanguageString note, Month month, String url) {
        check(this, ResultPredicates.writePredicate);
        super.checkRequiredParameters(keywords, note);
        checkRequiredParameters(title, note, keywords, publisher, year);
        fillAllAttributes(title, keywords, publisher, year, volume, series, address, edition, isbn, numberPages, language,
                country, scope, note, month, url);
        super.setModifiedByAndDate();
    }

    private void fillAllAttributes(String title, MultiLanguageString keywords, String publisher, Integer year, String volume,
            String series, String address, String edition, String isbn, Integer numberPages, String language, Country country,
            ScopeType scope, MultiLanguageString note, Month month, String url) {
        super.setTitle(title);
        super.setPublisher(publisher);
        super.setYear(year);
        super.setVolume(volume);
        super.setSeries(series);
        super.setAddress(address);
        super.setEdition(edition);
        super.setIsbn(isbn);
        super.setNumberPages(numberPages);
        super.setLanguage(language);
        super.setCountry(country);
        super.setScope(scope);
        super.setNote(note);
        super.setMonth(month);
        super.setUrl(url);
        super.setKeywords(keywords);
    }

    private void checkRequiredParameters(String title, MultiLanguageString note, MultiLanguageString keywords, String publisher,
            Integer year) {
        if (title == null || title.length() == 0) {
            throw new DomainException("error.researcher.Book.title.null");
        }
        if (publisher == null) {
            throw new DomainException("error.researcher.Book.publisher.null");
        }
        if (year == null) {
            throw new DomainException("error.researcher.Book.year.null");
        }
    }

    @Override
    public String getResume() {
        String resume = getParticipationsAndTitleString();

        if ((getEdition() != null) && (getEdition().length() > 0)) {
            resume = resume + "Ed. " + getEdition() + ", ";
        }
        if ((getVolume() != null) && (getVolume().length() > 0)) {
            resume = resume + "Vol. " + getVolume() + ", ";
        }
        if ((getSeries() != null) && (getSeries().length() > 0)) {
            resume = resume + "Serie " + getSeries() + ", ";
        }
        if ((getYear() != null) && (getYear() > 0)) {
            resume = resume + getYear() + ", ";
        }
        if (getPublisher() != null) {
            resume = resume + getPublisher() + ", ";
        }
        if ((getAddress() != null) && (getAddress().length() > 0)) {
            resume = resume + getAddress() + ", ";
        }

        resume = finishResume(resume);
        return resume;
    }

    @Override
    public BibtexEntry exportToBibtexEntry() {
        BibtexFile bibtexFile = new BibtexFile();

        BibtexEntry bibEntry = bibtexFile.makeEntry("book", generateBibtexKey());
        bibEntry.setField("title", bibtexFile.makeString(getTitle()));
        if (getPublisher() != null) {
            bibEntry.setField("publisher", bibtexFile.makeString(getPublisher()));
        }
        bibEntry.setField("year", bibtexFile.makeString(getYear().toString()));
        if ((getVolume() != null) && (getVolume().length() > 0)) {
            bibEntry.setField("volume", bibtexFile.makeString(getVolume()));
        }
        if ((getSeries() != null) && (getSeries().length() > 0)) {
            bibEntry.setField("series", bibtexFile.makeString(getSeries()));
        }
        if ((getAddress() != null) && (getAddress().length() > 0)) {
            bibEntry.setField("address", bibtexFile.makeString(getAddress()));
        }
        if ((getEdition() != null) && (getEdition().length() > 0)) {
            bibEntry.setField("edition", bibtexFile.makeString(getEdition()));
        }
        if (getMonth() != null) {
            bibEntry.setField("month", bibtexFile.makeString(getMonth().toString().toLowerCase()));
        }
        if ((getNote() != null) && (getNote().hasContent())) {
            bibEntry.setField("note", bibtexFile.makeString(getNote().getContent()));
        }

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
    public void setTitle(String title) {
        throw new DomainException("error.researcher.Book.call", "setTitle");
    }

    @Override
    public void setPublisher(String publisher) {
        throw new DomainException("error.researcher.Book.call", "setPublisher");
    }

    @Override
    public void setYear(Integer year) {
        throw new DomainException("error.researcher.Book.call", "setYear");
    }

    @Override
    public void setVolume(String volume) {
        throw new DomainException("error.researcher.Book.call", "setVolume");
    }

    @Override
    public void setSeries(String series) {
        throw new DomainException("error.researcher.Book.call", "setSeries");
    }

    @Override
    public void setAddress(String address) {
        throw new DomainException("error.researcher.Book.call", "setAddress");
    }

    @Override
    public void setEdition(String edition) {
        throw new DomainException("error.researcher.Book.call", "setEdition");
    }

    @Override
    public void setNote(MultiLanguageString note) {
        throw new DomainException("error.researcher.Book.call", "setNote");
    }

    @Override
    public void setIsbn(String isbn) {
        throw new DomainException("error.researcher.Book.call", "setIsbn");
    }

    @Override
    public void setLanguage(String language) {
        throw new DomainException("error.researcher.Book.call", "setLanguage");
    }

    @Override
    public void setCountry(Country country) {
        throw new DomainException("error.researcher.Book.call", "setCountry");
    }

    @Override
    public void setScope(ScopeType scope) {
        throw new DomainException("error.researcher.Book.call", "setScope");
    }

    @Override
    public void setUrl(String url) {
        throw new DomainException("error.researcher.Book.call", "setUrl");
    }

    @Override
    public void setMonth(Month month) {
        throw new DomainException("error.researcher.Book.call", "setMonth");
    }

    @Override
    public void setNumberPages(Integer numberPages) {
        throw new DomainException("error.researcher.Book.call", "setNumberPages");
    }

    @Override
    public void setOrganization(String organization) {
        throw new DomainException("error.researcher.Book.call", "setOrganization");
    }

    @Override
    public String getSchema() {
        return usedSchema;
    }

    @Override
    public Boolean getIsPossibleSelectPersonRole() {
        return true;
    }
    @Deprecated
    public boolean hasEdition() {
        return getEdition() != null;
    }

    @Deprecated
    public boolean hasVolume() {
        return getVolume() != null;
    }

    @Deprecated
    public boolean hasAddress() {
        return getAddress() != null;
    }

    @Deprecated
    public boolean hasSeries() {
        return getSeries() != null;
    }

    @Deprecated
    public boolean hasLanguage() {
        return getLanguage() != null;
    }

    @Deprecated
    public boolean hasIsbn() {
        return getIsbn() != null;
    }

    @Deprecated
    public boolean hasNumberPages() {
        return getNumberPages() != null;
    }

    @Deprecated
    public boolean hasScope() {
        return getScope() != null;
    }

}
