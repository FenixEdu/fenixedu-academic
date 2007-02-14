package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication.ScopeType;
import bibtex.dom.BibtexEntry;

public class BookBean extends ResultPublicationBean implements Serializable {
    private String address;

    private String volume;

    private String series;

    private String edition;

    private Integer isbn;

    private Integer numberPages;

    private String language;

    private ScopeType scope;

    public BookBean() {
	this.setPublicationType(ResultPublicationType.Book);
	this.setActiveSchema("result.publication.create.Book");
	this.setParticipationSchema("resultParticipation.simpleWithRole");
    }

    public BookBean(Book book) {
	this();
	fillCommonFields(book);
	fillSpecificFields(book);
    }

    public BookBean(ResultPublicationBean bean) {
	this();
	fillCommonBeanFields(bean);
    }

    public BookBean(BibtexEntry bibtexEntry) {
	this();
	fillBibTeXFields(bibtexEntry);
	this.setActiveSchema("result.publication.import.Book");
    }

    @Override
    public ResultPublicationBean convertTo(ResultPublicationType type) {
	return ResultPublicationBeanConversions.bookTo(this, type);
    }

    @Override
    protected void fillSpecificFields(ResearchResultPublication publication) {
	this.setAddress(((Book) publication).getAddress());
	this.setVolume(((Book) publication).getVolume());
	this.setSeries(((Book) publication).getSeries());
	this.setEdition(((Book) publication).getEdition());
	this.setIsbn(((Book) publication).getIsbn());
	this.setNumberPages(((Book) publication).getNumberPages());
	this.setLanguage(((Book) publication).getLanguage());
	this.setScope(((Book) publication).getScope());
    }

    @Override
    protected void fillBibTeXFields(BibtexEntry bibtexEntry) {
	//setUnitFromBibtexEntry("publisher", bibtexEntry);
	setYearFromBibtexEntry(bibtexEntry);
	setMonthFromBibtexEntry(bibtexEntry);

	setTitle(getStringValueFromBibtexEntry("title", bibtexEntry));
	setVolume(getStringValueFromBibtexEntry("volume", bibtexEntry));
	setSeries(getStringValueFromBibtexEntry("series", bibtexEntry));
	setAddress(getStringValueFromBibtexEntry("address", bibtexEntry));
	setEdition(getStringValueFromBibtexEntry("edition", bibtexEntry));
	setNote(getStringValueFromBibtexEntry("note", bibtexEntry));
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
