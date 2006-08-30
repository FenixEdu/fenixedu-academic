package net.sourceforge.fenixedu.domain.research.result.publication;

import java.util.List;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.util.Month;

/**
 * Inbook A part of a book, which may be a chapter and/or a range of pages.
 * Required fields: author or editor, title, chapter and/or pages, publisher,
 * year. Optional fields: volume, series, address, edition, month, note.
 * Incollection A part of a book with its own title. Required fields: author,
 * title, booktitle, publisher, year. Optional fields: editor, pages,
 * organization, publisher, address, month, note.
 */
public class BookPart extends BookPart_Base {

    public enum BookPartType {
	Inbook, Incollection;
    }

    public BookPart() {
	super();
    }

    /**
     * BookPart - Inbook constructor
     */
    public BookPart(Person participator, ResultParticipationRole participatorRole,
	    BookPartType bookPartType, String title, String chapter, Integer firstPage,
	    Integer lastPage, Unit publisher, Integer year, String volume, String series,
	    String edition, Country country, String address, String note, Month month, String url) {
	this();
	checkInbookRequiredParameters(bookPartType, title, chapter, firstPage, lastPage, publisher, year);
	super.setParticipation(participator, participatorRole);
	fillAllInbookAttributes(bookPartType, title, chapter, firstPage, lastPage, publisher, year,
		volume, series, edition, country, address, note, month, url);
    }
    
    /**
     * BookPart - Incollection constructor
     */
    public BookPart(Person participator, ResultParticipationRole participatorRole,
	    BookPartType bookPartType, String title, String bookTitle, Unit publisher, Integer year,
	    Integer firstPage, Integer lastPage, Unit organization, Country country, String address,
	    String note, Month month, String url) {
	this();
	checkIncollectionRequiredParameters(bookPartType, title, bookTitle, publisher, year);
	super.setParticipation(participator, participatorRole);
	fillAllIncollectionAttributes(bookPartType, title, bookTitle, publisher, year, firstPage,
		lastPage, organization, country, address, note, month, url);
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAllInbook(BookPartType bookPartType, String title, String chapter,
	    Integer firstPage, Integer lastPage, Unit publisher, Integer year, String volume,
	    String series, String edition, Country country, String address, String note, Month month,
	    String url) {
	checkInbookRequiredParameters(bookPartType, title, chapter, firstPage, lastPage, publisher, year);
	checkBookPartTypeChange(bookPartType);
	fillAllInbookAttributes(bookPartType, title, chapter, firstPage, lastPage, publisher, year,
		volume, series, edition, country, address, note, month, url);
	super.setModifyedByAndDate();
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAllIncollection(BookPartType bookPartType, String title, String bookTitle,
	    Unit publisher, Integer year, Integer firstPage, Integer lastPage, Unit organization,
	    Country country, String address, String note, Month month, String url) {
	checkIncollectionRequiredParameters(bookPartType, title, bookTitle, publisher, year);
	checkBookPartTypeChange(bookPartType);
	fillAllIncollectionAttributes(bookPartType, title, bookTitle, publisher, year, firstPage,
		lastPage, organization, country, address, note, month, url);
	super.setModifyedByAndDate();
    }

    private void fillAllInbookAttributes(BookPartType bookPartType, String title, String chapter,
	    Integer firstPage, Integer lastPage, Unit publisher, Integer year, String volume,
	    String series, String edition, Country country, String address, String note, Month month,
	    String url) {
	super.setBookPartType(bookPartType);
	super.setTitle(title);
	super.setPublisher(publisher);
	super.setYear(year);
	super.setChapter(chapter);
	super.setFirstPage(firstPage);
	super.setLastPage(lastPage);
	super.setVolume(volume);
	super.setSeries(series);
	super.setEdition(edition);
	super.setCountry(country);
	super.setAddress(address);
	super.setNote(note);
	super.setMonth(month);
	super.setUrl(url);
    }

    private void checkInbookRequiredParameters(BookPartType bookPartType, String title, String chapter,
	    Integer firstPage, Integer lastPage, Unit publisher, Integer year) {
	if (!bookPartType.equals(BookPartType.Inbook))
	    throw new DomainException("error.researcher.BookPart.wrongType");
	checkCommonRequired(bookPartType, title, publisher, year);
	if ((chapter == null) || (chapter.length() == 0)) {
	    if ((firstPage == null) || (firstPage <= 0) || (lastPage == null) || (lastPage <= 0))
		throw new DomainException("error.researcher.BookPart.needChapterOrPages");
	}
    }



    private void fillAllIncollectionAttributes(BookPartType bookPartType, String title,
	    String bookTitle, Unit publisher, Integer year, Integer firstPage, Integer lastPage,
	    Unit organization, Country country, String address, String note, Month month, String url) {
	super.setBookPartType(bookPartType);
	super.setTitle(title);
	super.setBookTitle(bookTitle);
	super.setPublisher(publisher);
	super.setYear(year);
	super.setFirstPage(firstPage);
	super.setLastPage(lastPage);
	super.setOrganization(organization);
	super.setCountry(country);
	super.setAddress(address);
	super.setNote(note);
	super.setMonth(month);
	super.setUrl(url);
    }

    private void checkIncollectionRequiredParameters(BookPartType bookPartType, String title,
	    String bookTitle, Unit publisher, Integer year) {
	if (!bookPartType.equals(BookPartType.Incollection))
	    throw new DomainException("error.researcher.BookPart.wrongType");
	checkCommonRequired(bookPartType, title, publisher, year);
	if (bookTitle == null || bookTitle.length() == 0)
	    throw new DomainException("error.researcher.BookPart.bookTitle.null");
    }

    private void checkCommonRequired(BookPartType bookPartType, String title, Unit publisher, Integer year) {
	if (bookPartType == null)
	    throw new DomainException("error.researcher.BookPart.bookPartType.null");
	if (title == null || title.length() == 0)
	    throw new DomainException("error.researcher.BookPart.title.null");
	if (publisher == null)
	    throw new DomainException("error.researcher.BookPart.publisher.null");
	if (year == null)
	    throw new DomainException("error.researcher.BookPart.year.null");
    }

    private void checkBookPartTypeChange(BookPartType bookPartType) {
	// if changing type of BookPart remove fields that don't belong to the new type
	if (!this.getBookPartType().equals(bookPartType)) {
	    switch (bookPartType) {
	    case Inbook:
		super.setBookTitle(null);
		super.setOrganization(null);
		break;

	    case Incollection:
		super.setChapter(null);
		super.setVolume(null);
		super.setSeries(null);
		super.setEdition(null);
		break;

	    default:
		break;
	    }
	}
    }
    
    @Override
    public void setBookPartType(BookPartType bookPartType) {
	throw new DomainException("error.researcher.BookPart.call","setBookPartType");
    }
    
    @Override
    public void setTitle(String title) {
	throw new DomainException("error.researcher.BookPart.call","setTitle");
    }
    
    @Override
    public void setPublisher(Unit publisher) {
	throw new DomainException("error.researcher.BookPart.call","setPublisher");
    }
    
    @Override
    public void setYear(Integer year) {
	throw new DomainException("error.researcher.BookPart.call","setYear");
    }
    
    @Override
    public void setChapter(String chapter) {
	throw new DomainException("error.researcher.BookPart.call","setChapter");
    }
    
    @Override
    public void setFirstPage(Integer firstPage) {
	throw new DomainException("error.researcher.BookPart.call","setFirstPage");
    }
    
    @Override
    public void setLastPage(Integer lastPage) {
	throw new DomainException("error.researcher.BookPart.call","setLastPage");
    }
    
    @Override
    public void setVolume(String volume) {
	throw new DomainException("error.researcher.BookPart.call","setVolume");
    }
    
    @Override
    public void setSeries(String series) {
	throw new DomainException("error.researcher.BookPart.call","setSeries");
    }
    
    @Override
    public void setEdition(String edition) {
	throw new DomainException("error.researcher.BookPart.call","setEdition");
    }
    
    @Override
    public void setCountry(Country country) {
	throw new DomainException("error.researcher.BookPart.call","setCountry");
    }
    
    @Override
    public void setAddress(String address) {
	throw new DomainException("error.researcher.BookPart.call","setTitle");
    }
    
    @Override
    public void setNote(String note) {
	throw new DomainException("error.researcher.BookPart.call","setNote");
    }
    
    @Override
    public void setMonth(Month month) {
	throw new DomainException("error.researcher.BookPart.call","setMonth");
    }
    
    @Override
    public void setUrl(String url) {
	throw new DomainException("error.researcher.BookPart.call","setUrl");
    }
    
    @Override
    public void setBookTitle(String bookTitle) {
	throw new DomainException("error.researcher.BookPart.call","setBookTitle");
    }
    
    @Override
    public void setOrganization(Unit organization) {
	throw new DomainException("error.researcher.BookPart.call","setOrganization");
    }
    
    @Override
    public void setParticipation(Person creator, ResultParticipationRole role) {
	throw new DomainException("error.researcher.BookPart.call","setParticipation");
    }
}
