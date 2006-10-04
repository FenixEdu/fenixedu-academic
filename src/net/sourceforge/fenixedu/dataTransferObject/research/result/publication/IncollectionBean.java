package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import bibtex.dom.BibtexEntry;

public class IncollectionBean extends ResultPublicationBean implements Serializable {
    private String address;

    private Integer firstPage;

    private Integer lastPage;

    private String bookTitle;

    public IncollectionBean() {
	this.setPublicationType(ResultPublicationType.Incollection);
	this.setActiveSchema("result.publication.create.Incollection");
	this.setParticipationSchema("resultParticipation.simpleWithRole");
    }

    public IncollectionBean(BookPart bookPart) {
	this();
	fillCommonFields(bookPart);
	fillSpecificFields(bookPart);
    }

    public IncollectionBean(ResultPublicationBean bean) {
	this();
	fillCommonBeanFields(bean);
    }

    public IncollectionBean(BibtexEntry bibtexEntry) {
	this();
	fillBibTeXFields(bibtexEntry);
	this.setActiveSchema("result.publication.import.Incollection");
    }

    @Override
    protected void fillSpecificFields(ResultPublication publication) {
	this.setBookTitle(((BookPart) publication).getBookTitle());
	this.setFirstPage(((BookPart) publication).getFirstPage());
	this.setLastPage(((BookPart) publication).getLastPage());
	this.setAddress(((BookPart) publication).getAddress());
    }

    @Override
    protected void fillBibTeXFields(BibtexEntry bibtexEntry) {
	setUnitFromBibtexEntry("publisher", bibtexEntry);
	setUnitFromBibtexEntry("organization", bibtexEntry);
	setYearFromBibtexEntry(bibtexEntry);
	setMonthFromBibtexEntry(bibtexEntry);

	setTitle(getStringValueFromBibtexEntry("title", bibtexEntry));
	setBookTitle(getStringValueFromBibtexEntry("booktitle", bibtexEntry));
	setAddress(getStringValueFromBibtexEntry("address", bibtexEntry));
	setNote(getStringValueFromBibtexEntry("note", bibtexEntry));
	if (getFirstPageFromBibtexEntry(bibtexEntry) != null) {
	    setFirstPage(getFirstPageFromBibtexEntry(bibtexEntry));
	    setLastPage(getLastPageFromBibtexEntry(bibtexEntry));
	}
    }

    @Override
    public ResultPublicationBean convertTo(ResultPublicationType type) {
	return ResultPublicationBeanConversions.incollectionTo(this, type);
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
