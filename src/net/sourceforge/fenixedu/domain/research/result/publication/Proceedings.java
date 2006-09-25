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
 * (collection: A collection of works. Same as Proceedings.) The proceedings of
 * a conference. Required fields: title, year. Optional fields: editor,
 * publisher, organization, address, month, note.
 * 
 * Extra from previous publications: conference
 */
public class Proceedings extends Proceedings_Base {

    public Proceedings() {
        super();
    }

    public Proceedings(Person participator, String title, Integer year, Event event, Unit publisher,
            Unit organization, String address, String note, Month month, String url) {
        this();
        checkRequiredParameters(title, year, event);
        super.setCreatorParticipation(participator, ResultParticipationRole.Editor);
        fillAllAttributes(title, year, event, publisher, organization, address, note, month, url);
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAll(String title, Integer year, Event event, Unit publisher, Unit organization,
            String address, String note, Month month, String url) {
        checkRequiredParameters(title, year, event);
        fillAllAttributes(title, year, event, publisher, organization, address, note, month, url);
        super.setModifyedByAndDate();
    }

    private void fillAllAttributes(String title, Integer year, Event event, Unit publisher,
            Unit organization, String address, String note, Month month, String url) {
        super.setTitle(title);
        super.setYear(year);
        super.setEvent(event);
        super.setPublisher(publisher);
        super.setOrganization(organization);
        super.setAddress(address);
        super.setNote(note);
        super.setMonth(month);
        super.setUrl(url);
    }

    private void checkRequiredParameters(String title, Integer year, Event event) {
        if ((title == null) || (title.length() == 0))
            throw new DomainException("error.researcher.Proceedings.title.null");
        if (year == null)
            throw new DomainException("error.researcher.Proceedings.year.null");
        if (event == null)
            throw new DomainException("error.researcher.Proceedings.event.null");
    }

    @Override
    public String getResume() {
        String resume = getParticipationsAndTitleString();
        if (getPublisher() != null)
            resume = resume + getPublisher().getName() + ", ";
        if ((getYear() != null) && (getYear() > 0))
            resume = resume + getYear() + ", ";
        if (getEvent() != null)
            resume = resume + getEvent().getName().getContent() + ", ";

        resume = finishResume(resume);
        return resume;
    }

    @Override
    public BibtexEntry exportToBibtexEntry() {
        BibtexFile bibtexFile = new BibtexFile();

        BibtexEntry bibEntry = bibtexFile.makeEntry("proceedings", generateBibtexKey());
        bibEntry.setField("title", bibtexFile.makeString(getTitle()));
        bibEntry.setField("year", bibtexFile.makeString(getYear().toString()));
        if (getPublisher() != null)
            bibEntry.setField("publisher", bibtexFile.makeString(getPublisher().getName()));
        if (getOrganization() != null)
            bibEntry.setField("organization", bibtexFile.makeString(getOrganization().getName()));
        if ((getAddress() != null) && (getAddress().length() > 0))
            bibEntry.setField("address", bibtexFile.makeString(getAddress()));
        if (getMonth() != null)
            bibEntry.setField("month", bibtexFile.makeString(getMonth().toString().toLowerCase()));
        if ((getNote() != null) && (getNote().length() > 0))
            bibEntry.setField("note", bibtexFile.makeString(getNote()));

        BibtexPersonList editorsList = getBibtexEditorsList(bibtexFile, getEditors());
        if (editorsList != null) {
            BibtexString bplString = bibtexFile.makeString(bibtexPersonListToString(editorsList));
            bibEntry.setField("editor", bplString);
        }

        return bibEntry;
    }

    @Override
    public void setTitle(String title) {
        throw new DomainException("error.researcher.Proceedings.call", "setTitle");
    }

    @Override
    public void setYear(Integer year) {
        throw new DomainException("error.researcher.Proceedings.call", "setYear");
    }

    @Override
    public void setEvent(Event event) {
        throw new DomainException("error.researcher.Proceedings.call", "setEvent");
    }

    @Override
    public void setPublisher(Unit publisher) {
        throw new DomainException("error.researcher.Proceedings.call", "setPublisher");
    }

    @Override
    public void setOrganization(Unit organization) {
        throw new DomainException("error.researcher.Proceedings.call", "setOrganization");
    }

    @Override
    public void setAddress(String address) {
        throw new DomainException("error.researcher.Proceedings.call", "setAddress");
    }

    @Override
    public void setNote(String note) {
        throw new DomainException("error.researcher.Proceedings.call", "setNote");
    }

    @Override
    public void setMonth(Month month) {
        throw new DomainException("error.researcher.Proceedings.call", "setMonth");
    }

    @Override
    public void setUrl(String url) {
        throw new DomainException("error.researcher.Proceedings.call", "setUrl");
    }

    @Override
    public void setCountry(Country country) {
        throw new DomainException("error.researcher.Proceedings.call", "setCountry");
    }
}
