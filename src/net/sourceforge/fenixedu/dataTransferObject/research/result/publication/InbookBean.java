package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import bibtex.dom.BibtexEntry;

public class InbookBean extends ResultPublicationBean implements Serializable {
    private String address;

    private String volume;

    private String series;

    private String edition;

    private Integer firstPage;

    private Integer lastPage;

    private String chapter;

    public InbookBean() {
        this.setPublicationType(ResultPublicationType.Inbook);
        this.setActiveSchema("result.publication.create.Inbook");
        this.setParticipationSchema("resultParticipation.simpleWithRole");
    }

    public InbookBean(BookPart bookPart) {
        this();
        fillCommonFields(bookPart);
        fillSpecificFields(bookPart);
    }
    
    public InbookBean(ResultPublicationBean bean) {
	this();
	fillCommonBeanFields(bean);
    }
    
    public InbookBean(BibtexEntry bibtexEntry) {
	this();
	fillBibTeXFields(bibtexEntry);
    }

    @Override
    protected void fillSpecificFields(ResultPublication publication) {
	this.setChapter(((BookPart)publication).getChapter());
	this.setFirstPage(((BookPart)publication).getFirstPage());
	this.setLastPage(((BookPart)publication).getLastPage());
	this.setAddress(((BookPart)publication).getAddress());
	this.setVolume(((BookPart)publication).getVolume());
	this.setSeries(((BookPart)publication).getSeries());
	this.setEdition(((BookPart)publication).getEdition());
    }
    
    @Override
    protected void fillBibTeXFields(BibtexEntry bibtexEntry) {
	setUnitFromBibtexEntry("publisher", bibtexEntry);
        setYearFromBibtexEntry(bibtexEntry);
        setMonthFromBibtexEntry(bibtexEntry);

        setTitle(getStringValueFromBibtexEntry("title", bibtexEntry));
        setChapter(getStringValueFromBibtexEntry("chapter", bibtexEntry));
        setVolume(getStringValueFromBibtexEntry("volume", bibtexEntry));
        setSeries(getStringValueFromBibtexEntry("series", bibtexEntry));
        setAddress(getStringValueFromBibtexEntry("address", bibtexEntry));
        setEdition(getStringValueFromBibtexEntry("edition", bibtexEntry));
        setNote(getStringValueFromBibtexEntry("note", bibtexEntry));
        if (getFirstPageFromBibtexEntry(bibtexEntry) != null) {
            setFirstPage(getFirstPageFromBibtexEntry(bibtexEntry));
            setLastPage(getLastPageFromBibtexEntry(bibtexEntry));
        }
    }
    
    @Override
    public ResultPublicationBean convertTo(ResultPublicationType type) {
        return ResultPublicationBeanConversions.inbookTo(this, type);
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
