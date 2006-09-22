package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import bibtex.dom.BibtexEntry;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;

public class IncollectionBean extends ResultPublicationBean implements Serializable {
    private String address;

    private Integer firstPage;

    private Integer lastPage;

    private String bookTitle;

    private IncollectionBean() {
        this.setPublicationType(ResultPublicationType.Incollection);
        this.setActiveSchema("result.publication.create.Incollection");
        this.setParticipationSchema("resultParticipation.simpleWithRole");
    }

    public IncollectionBean(BookPart bookPart) {
        this();
        if (bookPart != null) {
            this.fillCommonFields(bookPart);
            this.setBookTitle(bookPart.getBookTitle());
            this.setFirstPage(bookPart.getFirstPage());
            this.setLastPage(bookPart.getLastPage());
            this.setAddress(bookPart.getAddress());
        }
    }
    
    public IncollectionBean(BibtexEntry entry) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
}
