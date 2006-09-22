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
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.util.Month;

/**
 * (conference: The same as Inproceedings.) An article in a conference
 * proceedings. Required fields: author, title, booktitle, year. Optional
 * fields: editor, pages, organization, publisher, address, month, note.
 * 
 * Extra from previous publications: event, language
 */
public class Inproceedings extends Inproceedings_Base {

    public Inproceedings() {
        super();
    }

    public Inproceedings(Person participator, ResultParticipationRole participatorRole, String title,
            String bookTitle, Integer year, Event event, Unit publisher, Unit organization,
            String address, Integer firstPage, Integer lastPage, String note, String language,
            Month month, String url) {
        this();
        checkRequiredParameters(title, bookTitle, year, event);
        super.setCreatorParticipation(participator, participatorRole);
        fillAllAttributes(title, bookTitle, year, event, publisher, organization, address, firstPage,
                lastPage, note, language, month, url);
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAll(String title, String bookTitle, Integer year, Event event, Unit publisher,
            Unit organization, String address, Integer firstPage, Integer lastPage, String note,
            String language, Month month, String url) {
        checkRequiredParameters(title, bookTitle, year, event);
        fillAllAttributes(title, bookTitle, year, event, publisher, organization, address, firstPage,
                lastPage, note, language, month, url);
        super.setModifyedByAndDate();
    }

    private void fillAllAttributes(String title, String bookTitle, Integer year, Event event,
            Unit publisher, Unit organization, String address, Integer firstPage, Integer lastPage,
            String note, String language, Month month, String url) {
        super.setTitle(title);
        super.setBookTitle(bookTitle);
        super.setYear(year);
        super.setEvent(event);
        super.setPublisher(publisher);
        super.setOrganization(organization);
        super.setAddress(address);
        super.setFirstPage(firstPage);
        super.setLastPage(lastPage);
        super.setNote(note);
        super.setLanguage(language);
        super.setMonth(month);
        super.setUrl(url);
    }

    private void checkRequiredParameters(String title, String bookTitle, Integer year, Event event) {
        if ((title == null) || (title.length() == 0))
            throw new DomainException("error.researcher.Inproceedings.title.null");
        if ((bookTitle == null) || (bookTitle.length() == 0))
            throw new DomainException("error.researcher.Inproceedings.bookTitle.null");
        if (year == null)
            throw new DomainException("error.researcher.Inproceedings.year.null");
        if (event == null)
            throw new DomainException("error.researcher.Inproceedings.event.null");
    }

    @Override
    public String getResume() {
        String resume = getParticipationsAndTitleString();
        if (getPublisher() != null)
            resume = resume + getPublisher().getName() + ", ";
        if ((getYear() != null) && (getYear() > 0))
            resume = resume + getYear() + ", ";
        if ((getFirstPage() != null) && (getFirstPage() > 0) && (getLastPage() != null)
                && (getLastPage() > 0))
            resume = resume + "Pag. " + getFirstPage() + " - " + getLastPage() + ", ";
        if ((getBookTitle() != null) && (getBookTitle().length() > 0))
            resume = resume + getBookTitle() + ", ";
        if (getEvent() != null && getEvent().getName() != null)
            resume = resume + getEvent().getName().getContent() + ", ";
        if (getOrganization() != null)
            resume = resume + getOrganization().getName() + ", ";

        resume = finishResume(resume);
        return resume;
    }

    @Override
    public BibtexEntry exportToBibtexEntry() {
        BibtexFile bibtexFile = new BibtexFile();

        BibtexEntry bibEntry = bibtexFile.makeEntry("inproceedings", null);
        bibEntry.setField("title", bibtexFile.makeString(getTitle()));
        bibEntry.setField("booktitle", bibtexFile.makeString(getBookTitle()));
        bibEntry.setField("year", bibtexFile.makeString(getYear().toString()));
        if (getPublisher() != null)
            bibEntry.setField("publisher", bibtexFile.makeString(getPublisher().getName()));
        if ((getFirstPage() != null) && (getLastPage() != null) && (getFirstPage() < getLastPage()))
            bibEntry.setField("pages", bibtexFile.makeString(getFirstPage() + "-" + getLastPage()));
        if (getOrganization() != null)
            bibEntry.setField("organization", bibtexFile.makeString(getOrganization().getName()));
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
    public void setTitle(String title) {
        throw new DomainException("error.researcher.Inproceedings.call", "setTitle");
    }

    @Override
    public void setBookTitle(String bookTitle) {
        throw new DomainException("error.researcher.Inproceedings.call", "setBookTitle");
    }

    @Override
    public void setYear(Integer year) {
        throw new DomainException("error.researcher.Inproceedings.call", "setYear");
    }

    @Override
    public void setEvent(Event event) {
        throw new DomainException("error.researcher.Inproceedings.call", "setEvent");
    }

    @Override
    public void setPublisher(Unit publisher) {
        throw new DomainException("error.researcher.Inproceedings.call", "setPublisher");
    }

    @Override
    public void setOrganization(Unit organization) {
        throw new DomainException("error.researcher.Inproceedings.call", "setOrganization");
    }

    @Override
    public void setAddress(String address) {
        throw new DomainException("error.researcher.Inproceedings.call", "setAddress");
    }

    @Override
    public void setFirstPage(Integer firstPage) {
        throw new DomainException("error.researcher.Inproceedings.call", "setFirstPage");
    }

    @Override
    public void setLastPage(Integer lastPage) {
        throw new DomainException("error.researcher.Inproceedings.call", "setLastPage");
    }

    @Override
    public void setNote(String note) {
        throw new DomainException("error.researcher.Inproceedings.call", "setNote");
    }

    @Override
    public void setLanguage(String language) {
        throw new DomainException("error.researcher.Inproceedings.call", "setLanguage");
    }

    @Override
    public void setMonth(Month month) {
        throw new DomainException("error.researcher.Inproceedings.call", "setMonth");
    }

    @Override
    public void setUrl(String url) {
        throw new DomainException("error.researcher.Inproceedings.call", "setUrl");
    }

    @Override
    public void setCountry(Country country) {
        throw new DomainException("error.researcher.Inproceedings.call", "setCountry");
    }
}
