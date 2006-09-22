package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import bibtex.dom.BibtexEntry;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;

public class InbookBean extends ResultPublicationBean implements Serializable {
    private String address;

    private String volume;

    private String series;

    private String edition;

    private Integer firstPage;

    private Integer lastPage;

    private String chapter;

    private InbookBean() {
        this.setPublicationType(ResultPublicationType.Inbook);
        this.setActiveSchema("result.publication.create.Inbook");
        this.setParticipationSchema("resultParticipation.simpleWithRole");
    }

    public InbookBean(BookPart bookPart) {
        this();
        if (bookPart != null) {
            this.fillCommonFields(bookPart);
            this.setChapter(bookPart.getChapter());
            this.setFirstPage(bookPart.getFirstPage());
            this.setLastPage(bookPart.getLastPage());
            this.setAddress(bookPart.getAddress());
            this.setVolume(bookPart.getVolume());
            this.setSeries(bookPart.getSeries());
            this.setEdition(bookPart.getEdition());
        }
    }

    public InbookBean(BibtexEntry entry) {
        this();
        setUnitFromBibtexEntry("publisher", entry);
        setYearFromBibtexEntry(entry);
        setMonthFromBibtexEntry(entry);

        setTitle(getStringValueFromBibtexEntry("title", entry));
        setChapter(getStringValueFromBibtexEntry("chapter", entry));
        setVolume(getStringValueFromBibtexEntry("volume", entry));
        setSeries(getStringValueFromBibtexEntry("series", entry));
        setAddress(getStringValueFromBibtexEntry("address", entry));
        setEdition(getStringValueFromBibtexEntry("edition", entry));
        setNote(getStringValueFromBibtexEntry("note", entry));
        if (getFirstPageFromBibtexEntry(entry) != null) {
            setFirstPage(getFirstPageFromBibtexEntry(entry));
            setLastPage(getLastPageFromBibtexEntry(entry));
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Integer getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(Integer firstPage) {
        this.firstPage = firstPage;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
}
