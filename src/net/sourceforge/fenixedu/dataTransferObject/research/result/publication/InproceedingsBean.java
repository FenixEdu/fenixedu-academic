package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import bibtex.dom.BibtexEntry;

public class InproceedingsBean extends ConferenceArticlesBean implements Serializable {
    private String address;

    private Integer firstPage;

    private Integer lastPage;

    private String language;

    public InproceedingsBean() {
        this.setPublicationType(ResultPublicationType.Inproceedings);
        this.setActiveSchema("result.publication.create.Inproceedings");
        this.setParticipationSchema("resultParticipation.simpleWithRole");
    }

    public InproceedingsBean(Inproceedings inproceedings) {
        this();
       	fillCommonFields(inproceedings);
       	fillSpecificFields(inproceedings);
    }
    
    public InproceedingsBean(ResultPublicationBean bean) {
	this();
        fillCommonBeanFields(bean);
    }
    
    public InproceedingsBean(BibtexEntry bibtexEntry) {
	this();
	fillBibTeXFields(bibtexEntry);
    }

    @Override
    protected void fillSpecificFields(ResultPublication publication) {
	this.setAddress(((Inproceedings)publication).getAddress());
	this.setFirstPage(((Inproceedings)publication).getFirstPage());
	this.setLastPage(((Inproceedings)publication).getLastPage());
	this.setLanguage(((Inproceedings)publication).getLanguage());
	this.setEvent(((Inproceedings)publication).getEvent());
    }
    
    @Override
    protected void fillBibTeXFields(BibtexEntry bibtexEntry) {
	setUnitFromBibtexEntry("publisher", bibtexEntry);
        setUnitFromBibtexEntry("organization", bibtexEntry);
        setYearFromBibtexEntry(bibtexEntry);
        setMonthFromBibtexEntry(bibtexEntry);

        setTitle(getStringValueFromBibtexEntry("title", bibtexEntry));
        //booktitle will be used to event name
        //setBookTitle(getStringValueFromBibtexEntry("booktitle", entry));
        setAddress(getStringValueFromBibtexEntry("address", bibtexEntry));
        setNote(getStringValueFromBibtexEntry("note", bibtexEntry));
        if (getFirstPageFromBibtexEntry(bibtexEntry) != null) {
            setFirstPage(getFirstPageFromBibtexEntry(bibtexEntry));
            setLastPage(getLastPageFromBibtexEntry(bibtexEntry));
        }
    }
    
    @Override
    public ResultPublicationBean convertTo(ResultPublicationType type) {
        return ResultPublicationBeanConversions.inproceedingsTo(this, type);
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
