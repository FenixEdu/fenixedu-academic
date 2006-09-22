package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;
import bibtex.dom.BibtexEntry;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;

public class InproceedingsBean extends ConferenceArticlesBean implements Serializable {
    private String address;

    private Integer firstPage;

    private Integer lastPage;

    private String bookTitle;

    private String language;

    private InproceedingsBean() {
        this.setPublicationType(ResultPublicationType.Inproceedings);
        this.setActiveSchema("result.publication.create.Inproceedings");
        this.setParticipationSchema("resultParticipation.simpleWithRole");
    }

    public InproceedingsBean(Inproceedings inproceedings) {
        this();
        if (inproceedings != null) {
            this.fillCommonFields(inproceedings);
            this.setBookTitle(inproceedings.getBookTitle());
            this.setAddress(inproceedings.getAddress());
            this.setFirstPage(inproceedings.getFirstPage());
            this.setLastPage(inproceedings.getLastPage());
            this.setLanguage(inproceedings.getLanguage());
            this.setEvent(inproceedings.getEvent());
        }
    }

    public InproceedingsBean(BibtexEntry entry) {
        this();
        setUnitFromBibtexEntry("publisher", entry);
        setUnitFromBibtexEntry("organization", entry);
        setYearFromBibtexEntry(entry);
        setMonthFromBibtexEntry(entry);

        setTitle(getStringValueFromBibtexEntry("title", entry));
        setBookTitle(getStringValueFromBibtexEntry("booktitle", entry));
        setAddress(getStringValueFromBibtexEntry("address", entry));
        setNote(getStringValueFromBibtexEntry("note", entry));
        if (getFirstPageFromBibtexEntry(entry) != null) {
            setFirstPage(getFirstPageFromBibtexEntry(entry));
            setLastPage(getLastPageFromBibtexEntry(entry));
        }
    }

    @Override
    public void setCreateEvent(Boolean createEvent) {
        this.setActiveSchema("result.publication.create.InproceedingsAndEvent");
        super.setCreateEvent(createEvent);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Integer getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(Integer firstPage) {
        this.firstPage = firstPage;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }

}
