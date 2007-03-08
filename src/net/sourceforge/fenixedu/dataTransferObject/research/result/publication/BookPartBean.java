package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import bibtex.dom.BibtexEntry;

public class BookPartBean extends ResultPublicationBean implements Serializable {
    private String address;

    private String volume;

    private String series;

    private String edition;

    private Integer firstPage;

    private Integer lastPage;

    private String chapter;
    
    private String bookTitle;

    public BookPartBean() {
	setPublicationType(ResultPublicationType.BookPart);
	this.setActiveSchema("result.publication.create.BookPart");
	this.setParticipationSchema("resultParticipation.simpleWithRole");
    }
    
    public BookPartBean(ResultPublicationBean bean) {
	this();
	fillCommonBeanFields(bean);
    }
    
    public BookPartBean(BookPart part) {
	this();
	fillCommonFields(part);
	fillSpecificFields(part);
    }
    
    public BookPartBean(BibtexEntry bibtexEntry) {
	this();
	fillBibTeXFields(bibtexEntry);
	this.setActiveSchema("result.publication.import.BookPart");
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

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
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

    @Override
    public ResultPublicationBean convertTo(ResultPublicationType type) {
	return ResultPublicationBeanConversions.BookPartTo(this, type);
    }

    @Override
    protected void fillBibTeXFields(BibtexEntry bibtexEntry) {
	setYearFromBibtexEntry(bibtexEntry);
	setMonthFromBibtexEntry(bibtexEntry);

	setTitle(getStringValueFromBibtexEntry("title", bibtexEntry));
	setChapter(getStringValueFromBibtexEntry("chapter", bibtexEntry));
	setVolume(getStringValueFromBibtexEntry("volume", bibtexEntry));
	setSeries(getStringValueFromBibtexEntry("series", bibtexEntry));
	setAddress(getStringValueFromBibtexEntry("address", bibtexEntry));
	setEdition(getStringValueFromBibtexEntry("edition", bibtexEntry));
	setBookTitle(getStringValueFromBibtexEntry("booktitle", bibtexEntry));
	setNote(getStringValueFromBibtexEntry("note", bibtexEntry));
	if (getFirstPageFromBibtexEntry(bibtexEntry) != null) {
	    setFirstPage(getFirstPageFromBibtexEntry(bibtexEntry));
	    setLastPage(getLastPageFromBibtexEntry(bibtexEntry));
	}
	
    }

    @Override
    protected void fillSpecificFields(ResearchResultPublication publication) {
	this.setChapter(((BookPart) publication).getChapter());
	this.setFirstPage(((BookPart) publication).getFirstPage());
	this.setLastPage(((BookPart) publication).getLastPage());
	this.setAddress(((BookPart) publication).getAddress());
	this.setVolume(((BookPart) publication).getVolume());
	this.setSeries(((BookPart) publication).getSeries());
	this.setEdition(((BookPart) publication).getEdition());
	this.setBookTitle(((BookPart) publication).getBookTitle());
    }

}
