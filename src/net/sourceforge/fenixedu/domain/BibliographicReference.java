package net.sourceforge.fenixedu.domain;

/**
 * @author PTRLV
 *  
 */
public class BibliographicReference extends BibliographicReference_Base {

    public String toString() {
        String result = "[REFERENCIA BIBLIOGRAFICA";
        result += ", codInt=" + getIdInternal();
        result += ", disciplinaExecucao=" + getExecutionCourse();
        result += ", titulo=" + getTitle();
        result += ", autor=" + getAuthors();
        result += ", ano=" + getYear();
        result += ", referencia=" + getReference();
        result += ", facultativo=" + getOptional();
        result += "]";
        return result;
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
    }

}
