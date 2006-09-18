package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart.BookPartType;

public class BookPartBean extends ResultPublicationBean implements Serializable{
    private String address;
    private String volume;
    private String series;
    private String edition;
    private Integer firstPage;
    private Integer lastPage;
    private BookPartType bookPartType;
    private String chapter;
    private String bookTitle;

    private BookPartBean() {
	this.setPublicationType(ResultPublicationType.BookPart);
	this.setBookPartType(BookPartType.Inbook);
	this.setActiveSchema("result.publication.create.BookPart.Inbook");
	this.setParticipationSchema("resultParticipation.simpleWithRole");
    }

    public BookPartBean(BookPart bookPart) {
	this();
	if(bookPart!=null) {
            this.fillCommonFields(bookPart);
            this.setPublicationType(ResultPublicationType.BookPart);
            this.setBookPartType(bookPart.getBookPartType());
            this.setActiveSchema("result.publication.create.BookPart." + this.getBookPartType());
            this.setChapter(bookPart.getChapter());
            this.setBookTitle(bookPart.getBookTitle());
            this.setFirstPage(bookPart.getFirstPage());
            this.setLastPage(bookPart.getLastPage());
            this.setAddress(bookPart.getAddress());
            this.setVolume(bookPart.getVolume());
            this.setSeries(bookPart.getSeries());
            this.setEdition(bookPart.getEdition());
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
    public BookPartType getBookPartType() {
        return bookPartType;
    }
    public void setBookPartType(BookPartType bookPartType) {
        this.bookPartType = bookPartType;
    }
    public String getChapter() {
        return chapter;
    }
    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
    public String getBookTitle() {
        return bookTitle;
    }
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
}
