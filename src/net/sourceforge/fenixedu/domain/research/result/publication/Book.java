package net.sourceforge.fenixedu.domain.research.result.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;

/**
 * A book with an explicit publisher.
 * Required fields: author or editor, title, publisher, year.
 * Optional fields: volume, series, address, edition, month, note.
 * 
 * Extra from previous publications: isbn, numberPages, country, language, scope
 */
public class Book extends Book_Base {
    
    public Book() {
        super();
    }
    
    //constructor with required fields
    public Book(Person participator, ResultParticipationRole participatorRole, String title, Unit publisher, Integer year) {
        super();
        if((participator == null) || (participatorRole == null) || (title == null) || (title.length() == 0) || (publisher == null) || (year == null))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setParticipation(participator, participatorRole);
        setTitle(title);
        setPublisher(publisher);
        setYear(year);
    }
    
    //edit with required fields
    public void edit(String title, Unit publisher, Integer year) {
        if((title == null) || (title.length() == 0) || (publisher == null) || (year == null))
            throw new DomainException("error.publication.missingRequiredFields");
        
        setTitle(title);
        setPublisher(publisher);
        setYear(year);
    }
    
    public List<ResultParticipation> getAuthors() {
    	ArrayList<ResultParticipation> authors = new ArrayList<ResultParticipation>();
    	for (ResultParticipation participation : this.getResultParticipations()) {
			if(participation.getResultParticipationRole().equals(ResultParticipationRole.Author))
				authors.add(participation);
		}
    	return authors;
    }
    
    public List<ResultParticipation> getEditors() {
    	ArrayList<ResultParticipation> editors = new ArrayList<ResultParticipation>();
    	for (ResultParticipation participation : this.getResultParticipations()) {
			if(participation.getResultParticipationRole().equals(ResultParticipationRole.Editor))
				editors.add(participation);
		}
    	return  editors;
    }
}
