package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;
import bibtex.dom.BibtexEntry;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication.ScopeType;

public class BookBean extends ResultPublicationBean implements Serializable {
    private String address;

    private String volume;

    private String series;

    private String edition;

    private Integer isbn;

    private Integer numberPages;

    private String language;

    private ScopeType scope;

    private BookBean() {
        this.setPublicationType(ResultPublicationType.Book);
        this.setActiveSchema("result.publication.create.Book");
        this.setParticipationSchema("resultParticipation.simpleWithRole");
    }

    public BookBean(Book book) {
        this();
        if (book != null) {
            this.fillCommonFields(book);
            this.setAddress(book.getAddress());
            this.setVolume(book.getVolume());
            this.setSeries(book.getSeries());
            this.setEdition(book.getEdition());
            this.setIsbn(book.getIsbn());
            this.setNumberPages(book.getNumberPages());
            this.setLanguage(book.getLanguage());
            this.setScope(book.getScope());
        }
    }

    public BookBean(BibtexEntry entry) {
        this();
        setUnitFromBibtexEntry("publisher", entry);
        setYearFromBibtexEntry(entry);
        setMonthFromBibtexEntry(entry);

        setTitle(getStringValueFromBibtexEntry("title", entry));
        setVolume(getStringValueFromBibtexEntry("volume", entry));
        setSeries(getStringValueFromBibtexEntry("series", entry));
        setAddress(getStringValueFromBibtexEntry("address", entry));
        setEdition(getStringValueFromBibtexEntry("edition", entry));
        setNote(getStringValueFromBibtexEntry("note", entry));
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

    public Integer getIsbn() {
        return isbn;
    }

    public void setIsbn(Integer isbn) {
        this.isbn = isbn;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getNumberPages() {
        return numberPages;
    }

    public void setNumberPages(Integer numberPages) {
        this.numberPages = numberPages;
    }

    public ScopeType getScope() {
        return scope;
    }

    public void setScope(ScopeType scope) {
        this.scope = scope;
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
}
