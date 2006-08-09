package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.event.EventType;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.domain.research.result.publication.*;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart.BookPartType;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication.ScopeType;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis.ThesisType;

import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class ResultPublicationCreationBean implements Serializable{

    private ResultPublicationType publicationType;
    private String activeSchema;
    private String participationSchema;
    
    private DomainReference<Person> person;
    private ResultParticipationRole resultParticipationRole;
    
    private DomainReference<Unit> publisher;
    private String publisherName;
    
    private DomainReference<Unit> organization;
    private String organizationName;
    
    private String title;
    private String address;
    private Integer year;
    private Month month;
    private String volume;
    private String series;
    private String edition;
    private Integer isbn;
    private Integer numberPages;
    private String language;
    private ScopeType scope;
    private String url;
    private String note;
    private String journal;
    private String number;
    private Integer firstPage;
    private Integer lastPage;
    private Integer issn;
    private BookPartType bookPartType;
    private String chapter;
    private String bookTitle;
    private ThesisType thesisType;
    private String technicalReportType;
    private String howPublished;
    private String otherPublicationType;
    private Integer idInternal;
    
    
    private DomainReference<Event> event;
    private String eventNameAutoComplete;
    private Boolean createEvent = false;
    
    //event creation needed to create a event
    private MultiLanguageString eventName;
    private EventType eventType;
    private String eventLocal;
    private YearMonthDay eventStartDate;
    private YearMonthDay eventEndDate;
    private Boolean eventFee;
    
     
    public enum ResultPublicationType {
        /*Types based on BibTex*/
        Book,
        BookPart,
        Article,
        Inproceedings,
        Proceedings,
        Thesis,
        Manual,
        TechnicalReport,
        Booklet,
        Misc,
        Unpublished;
              
        public static ResultPublicationType getDefaultResultPublicationType() {
            return Book;
        }
    }

    public void fillFromPublication(ResultPublication publication) {
        //common fields
        this.setIdInternal(publication.getIdInternal());
        this.setTitle(publication.getTitle());
        this.setNote(publication.getNote());
        this.setUrl(publication.getUrl());
        this.setYear(publication.getYear());
        this.setMonth(publication.getMonth());
        this.setPublisher(publication.getPublisher());
        this.setOrganization(publication.getOrganization());
        
        if(publication instanceof Book) {
            this.setPublicationType(ResultPublicationType.Book);
            Book book = (Book) publication;
            this.setAddress(book.getAddress());
            this.setVolume(book.getVolume());
            this.setSeries(book.getSeries());
            this.setEdition(book.getEdition());
            this.setIsbn(book.getIsbn());
            this.setNumberPages(book.getNumberPages());
            this.setLanguage(book.getLanguage());
            this.setScope(book.getScope());
        }
        else if(publication instanceof BookPart) {
            this.setPublicationType(ResultPublicationType.BookPart);
            BookPart bookPart = (BookPart) publication;
            this.setBookPartType(bookPart.getBookPartType());
            this.setChapter(bookPart.getChapter());
            this.setBookTitle(bookPart.getBookTitle());
            this.setFirstPage(bookPart.getFirstPage());
            this.setLastPage(bookPart.getLastPage());
            this.setAddress(bookPart.getAddress());
            this.setVolume(bookPart.getVolume());
            this.setSeries(bookPart.getSeries());
            this.setEdition(bookPart.getEdition());
        }
        else if(publication instanceof Article) {
            this.setPublicationType(ResultPublicationType.Article);
            Article article = (Article) publication;
            this.setJournal(article.getJournal());
            this.setVolume(article.getVolume());
            this.setNumber(article.getNumber());
            this.setFirstPage(article.getFirstPage());
            this.setLastPage(article.getLastPage());
            this.setIssn(article.getIssn());
            this.setLanguage(article.getLanguage());
            this.setScope(article.getScope());
        }
        else if(publication instanceof Inproceedings) {
            this.setPublicationType(ResultPublicationType.Inproceedings);
            Inproceedings inproceedings = (Inproceedings) publication;
            this.setBookTitle(inproceedings.getBookTitle());
            this.setAddress(inproceedings.getAddress());
            this.setFirstPage(inproceedings.getFirstPage());
            this.setLastPage(inproceedings.getLastPage());
            this.setLanguage(inproceedings.getLanguage());
            this.setEvent(inproceedings.getEvent());
        }
        else if(publication instanceof Proceedings) {
            this.setPublicationType(ResultPublicationType.Proceedings);
            Proceedings proceedings = (Proceedings) publication;
            this.setAddress(proceedings.getAddress());
            this.setEvent(proceedings.getEvent());
        }
        else if(publication instanceof Thesis) {
            this.setPublicationType(ResultPublicationType.Thesis);
            Thesis thesis = (Thesis) publication;
            this.setThesisType(thesis.getThesisType());
            this.setAddress(thesis.getAddress());
            this.setNumberPages(thesis.getNumberPages());
            this.setLanguage(thesis.getLanguage());
        }
        else if(publication instanceof Manual) {
            this.setPublicationType(ResultPublicationType.Manual);
            Manual manual = (Manual) publication;
            this.setAddress(manual.getAddress());
            this.setEdition(manual.getEdition());
        }
        else if(publication instanceof TechnicalReport) {
            this.setPublicationType(ResultPublicationType.TechnicalReport);
            TechnicalReport technicalReport = (TechnicalReport) publication;
            this.setTechnicalReportType(technicalReport.getTechnicalReportType());
            this.setNumber(technicalReport.getNumber());
            this.setAddress(technicalReport.getAddress());
            this.setNumberPages(technicalReport.getNumberPages());
            this.setLanguage(technicalReport.getLanguage());
        }
        else if(publication instanceof Booklet) {
            this.setPublicationType(ResultPublicationType.Booklet);
            Booklet booklet = (Booklet) publication;
            this.setHowPublished(booklet.getHowPublished());
            this.setAddress(booklet.getAddress());
        }
        else if(publication instanceof Misc) {
            this.setPublicationType(ResultPublicationType.Misc);
            Misc misc = (Misc) publication;
            this.setHowPublished(misc.getHowPublished());
            this.setOtherPublicationType(misc.getOtherPublicationType());
            this.setNumberPages(misc.getNumberPages());
            this.setLanguage(misc.getLanguage());
            this.setAddress(misc.getAddress());
        }
        else if(publication instanceof Unpublished) {
            this.setPublicationType(ResultPublicationType.Unpublished);
            //Unpublished has no more attributtes
        }
    }
    
    public Person getPerson() {
        return (this.person == null) ? null : this.person.getObject();
    }
    public void setPerson(Person person) {
        this.person = (person != null) ? new DomainReference<Person>(person) : null;
    }
    public Unit getPublisher() {
        return (this.publisher == null) ? null : this.publisher.getObject();
    }
    public void setPublisher(Unit publisher) {
        this.publisher = (publisher != null) ? new DomainReference<Unit>(publisher) : null;
    }
    public String getPublisherName() {
        return publisherName;
    }
    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
    public Unit getOrganization() {
        return (this.organization == null) ? null : this.organization.getObject();
    }
    public void setOrganization(Unit organization) {
        this.organization = (organization != null) ? new DomainReference<Unit>(organization) : null;
    }
    public String getOrganizationName() {
        return organizationName;
    }
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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
    public Month getMonth() {
        return month;
    }
    public void setMonth(Month month) {
        this.month = month;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
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
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getVolume() {
        return volume;
    }
    public void setVolume(String volume) {
        this.volume = volume;
    }
    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    public Integer getFirstPage() {
        return firstPage;
    }
    public void setFirstPage(Integer firstPage) {
        this.firstPage = firstPage;
    }
    public Integer getIssn() {
        return issn;
    }
    public void setIssn(Integer issn) {
        this.issn = issn;
    }
    public String getJournal() {
        return journal;
    }
    public void setJournal(String journal) {
        this.journal = journal;
    }
    public Integer getLastPage() {
        return lastPage;
    }
    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
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
    public String getActiveSchema() {
        return activeSchema;
    }
    public void setActiveSchema(String activeSchema) {
        this.activeSchema = activeSchema;
    }
    public String getBookTitle() {
        return bookTitle;
    }
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    public ResultPublicationType getPublicationType() {
        return publicationType;
    }
    public void setPublicationType(ResultPublicationType publicationType) {
        this.publicationType = publicationType;
    }
    public ThesisType getThesisType() {
        return thesisType;
    }
    public void setThesisType(ThesisType thesisType) {
        this.thesisType = thesisType;
    }
    public String getTechnicalReportType() {
        return technicalReportType;
    }
    public void setTechnicalReportType(String technicalReportType) {
        this.technicalReportType = technicalReportType;
    }
    public String getHowPublished() {
        return howPublished;
    }
    public void setHowPublished(String howPublished) {
        this.howPublished = howPublished;
    }
    public String getOtherPublicationType() {
        return otherPublicationType;
    }
    public void setOtherPublicationType(String otherPublicationType) {
        this.otherPublicationType = otherPublicationType;
    }
    public Integer getIdInternal() {
        return idInternal;
    }
    public void setIdInternal(Integer idInternal) {
        this.idInternal = idInternal;
    }

	public YearMonthDay getEventEndDate() {
		return eventEndDate;
	}
	public void setEventEndDate(YearMonthDay eventEndDate) {
		this.eventEndDate = eventEndDate;
	}
	public Boolean getEventFee() {
		return eventFee;
	}
	public void setEventFee(Boolean eventFee) {
		this.eventFee = eventFee;
	}
	public String getEventLocal() {
		return eventLocal;
	}
	public void setEventLocal(String eventLocal) {
		this.eventLocal = eventLocal;
	}
	public MultiLanguageString getEventName() {
		return eventName;
	}
	public void setEventName(MultiLanguageString eventName) {
		this.eventName = eventName;
	}
	public YearMonthDay getEventStartDate() {
		return eventStartDate;
	}
	public void setEventStartDate(YearMonthDay eventStartDate) {
		this.eventStartDate = eventStartDate;
	}
	public EventType getEventType() {
		return eventType;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	public String getEventNameAutoComplete() {
        return eventNameAutoComplete;
    }
    public void setEventNameAutoComplete(String name) {
        this.eventNameAutoComplete = name;
    }
    public Event getEvent() {
        return (this.event == null) ? null : this.event.getObject();
    }
    public void setEvent(Event event) {
        this.event = (event != null) ? new DomainReference<Event>(event) : null;
    }
	public Boolean getCreateEvent() {
		return createEvent;
	}
	public void setCreateEvent(Boolean createEvent) {
		this.createEvent = createEvent;
	}
	public ResultParticipationRole getResultParticipationRole() {
		return resultParticipationRole;
	}
	public void setResultParticipationRole(
			ResultParticipationRole resultParticipationRole) {
		this.resultParticipationRole = resultParticipationRole;
	}
	public String getParticipationSchema() {
		return participationSchema;
	}
	public void setParticipationSchema(String participationSchema) {
		this.participationSchema = participationSchema;
	}

}
