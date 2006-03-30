package net.sourceforge.fenixedu.domain;

/**
 * @author PTRLV
 *  
 */
public class BibliographicReference extends BibliographicReference_Base {

    public BibliographicReference() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public void edit(final String title, final String authors, final String reference, final String year, final Boolean optional) {
        if (title == null || authors == null || reference == null || year == null || optional == null)
            throw new NullPointerException();
        
        setTitle(title);
        setAuthors(authors);
        setReference(reference);
        setYear(year);
        setOptional(optional);
    }
    
    public void delete() {
        setExecutionCourse(null);        
        removeRootDomainObject();
        super.deleteDomainObject();
    }

}
