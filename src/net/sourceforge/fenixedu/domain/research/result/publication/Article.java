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
 * An article from a journal or magazine. Required fields: author, title,
 * journal, year. Optional fields: volume, number, pages, month, note.
 * 
 * Extra from previous publications: issn, language, publisher, country, scope
 */
public class Article extends Article_Base {

    public Article() {
        super();
    }

    public Article(Person participator, String title, String journal, Integer year, Unit publisher,
            String volume, String number, Integer firstPage, Integer lastPage, String note,
            Integer issn, String language, Country country, ScopeType scope, Month month, String url) {
        this();
        checkRequiredParameters(title, journal, year);
        super.setCreatorParticipation(participator, ResultParticipationRole.Author);
        fillAllAttributes(title, journal, year, publisher, volume, number, firstPage, lastPage, note,
                issn, language, country, scope, month, url);
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAll(String title, String journal, Integer year, Unit publisher, String volume,
            String number, Integer firstPage, Integer lastPage, String note, Integer issn,
            String language, Country country, ScopeType scope, Month month, String url) {
        checkRequiredParameters(title, journal, year);
        fillAllAttributes(title, journal, year, publisher, volume, number, firstPage, lastPage, note,
                issn, language, country, scope, month, url);
        super.setModifyedByAndDate();
    }

    private void fillAllAttributes(String title, String journal, Integer year, Unit publisher,
            String volume, String number, Integer firstPage, Integer lastPage, String note,
            Integer issn, String language, Country country, ScopeType scope, Month month, String url) {
        super.setTitle(title);
        super.setJournal(journal);
        super.setYear(year);
        super.setPublisher(publisher);
        super.setVolume(volume);
        super.setNumber(number);
        super.setFirstPage(firstPage);
        super.setLastPage(lastPage);
        super.setNote(note);
        super.setIssn(issn);
        super.setLanguage(language);
        super.setCountry(country);
        super.setScope(scope);
        super.setMonth(month);
        super.setUrl(url);
    }

    private void checkRequiredParameters(String title, String journal, Integer year) {
        if (title == null || title.length() == 0)
            throw new DomainException("error.researcher.Article.title.null");
        if (journal == null || journal.length() == 0)
            throw new DomainException("error.researcher.Article.journal.null");
        if (year == null)
            throw new DomainException("error.researcher.Article.year.null");
    }

    @Override
    public String getResume() {
        String resume = getParticipationsAndTitleString();
        if ((getJournal() != null) && (getJournal().length() > 0))
            resume = resume + getJournal() + ", ";
        if ((getNumber() != null) && (getNumber().length() > 0))
            resume = resume + "No. " + getNumber() + ", ";
        if ((getVolume() != null) && (getVolume().length() > 0))
            resume = resume + "Vol. " + getVolume() + ", ";
        if ((getFirstPage() != null) && (getFirstPage() > 0) && (getLastPage() != null)
                && (getLastPage() > 0))
            resume = resume + "Pag. " + getFirstPage() + " - " + getLastPage() + ", ";
        if ((getYear() != null) && (getYear() > 0))
            resume = resume + getYear() + ", ";
        if (getPublisher() != null)
            resume = resume + getPublisher().getName() + ", ";

        resume = finishResume(resume);
        return resume;
    }

    @Override
    public BibtexEntry exportToBibtexEntry() {
        BibtexFile bibtexFile = new BibtexFile();

        BibtexEntry bibEntry = bibtexFile.makeEntry("article", null);
        bibEntry.setField("title", bibtexFile.makeString(getTitle()));
        bibEntry.setField("journal", bibtexFile.makeString(getJournal()));
        bibEntry.setField("year", bibtexFile.makeString(getYear().toString()));
        if ((getVolume() != null) && (getVolume().length() > 0))
            bibEntry.setField("volume", bibtexFile.makeString(getVolume()));
        if ((getNumber() != null) && (getNumber().length() > 0))
            bibEntry.setField("series", bibtexFile.makeString(getNumber()));
        if ((getFirstPage() != null) && (getLastPage() != null) && (getFirstPage() < getLastPage()))
            bibEntry.setField("pages", bibtexFile.makeString(getFirstPage() + "-" + getLastPage()));
        if (getMonth() != null)
            bibEntry.setField("month", bibtexFile.makeString(getMonth().toString().toLowerCase()));
        if ((getNote() != null) && (getNote().length() > 0))
            bibEntry.setField("note", bibtexFile.makeString(getNote()));

        BibtexPersonList authorsList = getBibtexAuthorsList(bibtexFile, getAuthors());
        if (authorsList != null) {
            BibtexString bplString = bibtexFile.makeString(bibtexPersonListToString(authorsList));
            bibEntry.setField("author", bplString);
        }

        return bibEntry;
    }

    @Override
    public void setTitle(String title) {
        throw new DomainException("error.researcher.Article.call", "setTitle");
    }

    @Override
    public void setJournal(String journal) {
        throw new DomainException("error.researcher.Article.call", "setJournal");
    }

    @Override
    public void setYear(Integer year) {
        throw new DomainException("error.researcher.Article.call", "setYear");
    }

    @Override
    public void setPublisher(Unit publisher) {
        throw new DomainException("error.researcher.Article.call", "setPublisher");
    }

    @Override
    public void setVolume(String volume) {
        throw new DomainException("error.researcher.Article.call", "setVolume");
    }

    @Override
    public void setNumber(String number) {
        throw new DomainException("error.researcher.Article.call", "setNumber");
    }

    @Override
    public void setFirstPage(Integer firstPage) {
        throw new DomainException("error.researcher.Article.call", "setFirstPage");
    }

    @Override
    public void setLastPage(Integer lastPage) {
        throw new DomainException("error.researcher.Article.call", "setLastPage");
    }

    @Override
    public void setNote(String note) {
        throw new DomainException("error.researcher.Article.call", "setNote");
    }

    @Override
    public void setIssn(Integer issn) {
        throw new DomainException("error.researcher.Article.call", "setIssn");
    }

    @Override
    public void setLanguage(String language) {
        throw new DomainException("error.researcher.Article.call", "setLanguage");
    }

    @Override
    public void setCountry(Country country) {
        throw new DomainException("error.researcher.Article.call", "setCountry");
    }

    @Override
    public void setScope(ScopeType scope) {
        throw new DomainException("error.researcher.Article.call", "setScope");
    }

    @Override
    public void setMonth(Month month) {
        throw new DomainException("error.researcher.Article.call", "setMonth");
    }

    @Override
    public void setUrl(String url) {
        throw new DomainException("error.researcher.Article.call", "setUrl");
    }

    @Override
    public void setOrganization(Unit organization) {
        throw new DomainException("error.researcher.Article.call", "setOrganization");
    }
}
