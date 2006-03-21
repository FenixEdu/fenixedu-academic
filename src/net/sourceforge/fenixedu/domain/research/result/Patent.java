 package net.sourceforge.fenixedu.domain.research.result;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.Partial;

public class Patent extends Patent_Base {
    
    public  Patent() {
        super();
    }
 
    public Patent(MultiLanguageString title, MultiLanguageString description, Integer patentNumber,
    		PatentType patentType, PatentStatus patentStatus, Partial registrationDate, Partial approvalDate, 
    		Country country, String local, Integer numberPages, PatentFormat format, String language, String url,
    	    List<Person> authors) {
    	this();
    	
    	if (title == null || patentType == null || patentStatus == null || registrationDate == null || approvalDate == null ||
    			country == null || format == null)
    		throw new NullPointerException();
    	if( authors == null || authors.size() == 0)
            throw new DomainException("error.result.createPatentWithoutAuthors");
    	
    	setTitle(title);
    	setDescription(description);
    	setPatentNumber(patentNumber);
    	setType(patentType);
    	setStatus(patentStatus);
    	setRegistrationDate(registrationDate);
    	setApprovalDate(approvalDate);
    	setCountry(country);
    	setLocal(local);
    	setNumberPages(numberPages);
    	setFormat(format);
    	setLanguage(language);
    	setUrl(url);
    	
    	setAuthorships(authors);
		
    }
    
    public void edit(MultiLanguageString title, MultiLanguageString description, Integer patentNumber,
    		PatentType patentType, PatentStatus patentStatus, Partial registrationDate, Partial approvalDate, 
    		Country country, String local, Integer numberPages, PatentFormat format, String language, String url,
    	    List<Person> authors) {
    	
    	if (title == null || patentType == null || patentStatus == null || registrationDate == null || approvalDate == null ||
    			country == null || format == null)
    		throw new NullPointerException();
    	if( authors == null || authors.size() == 0)
            throw new DomainException("error.result.createPatentWithoutAuthors");
    	
    	setTitle(title);
    	setDescription(description);
    	setPatentNumber(patentNumber);
    	setType(patentType);
    	setStatus(patentStatus);
    	setRegistrationDate(registrationDate);
    	setApprovalDate(approvalDate);
    	setCountry(country);
    	setLocal(local);
    	setNumberPages(numberPages);
    	setFormat(format);
    	setLanguage(language);
    	setUrl(url);
    	
    	setAuthorships(authors);
		
    }
    
    public void delete() {
        
        for (Iterator<Authorship> iterator = getResultAuthorshipsIterator(); iterator.hasNext(); ) {
            Authorship authorship = iterator.next();
            iterator.remove();
            authorship.delete();
        }
    	super.deleteDomainObject();
    }
    
}
