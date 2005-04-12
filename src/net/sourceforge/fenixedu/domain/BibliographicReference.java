package net.sourceforge.fenixedu.domain;

/**
 * @author PTRLV
 *  
 */
public class BibliographicReference extends BibliographicReference_Base {

    /** Creates a new instance of ReferenciaBibliografica */
    public BibliographicReference() {
    }

    public BibliographicReference(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public BibliographicReference(IExecutionCourse executionCourse, String reference, Boolean facultative) {
        setExecutionCourse(executionCourse);
        setReference(reference);
        setOptional(facultative);
    }

    public BibliographicReference(IExecutionCourse executionCourse, String title, String authors,
            String reference, String year, Boolean facultative) {
        setTitle(title);
        setAuthors(authors);
        setReference(reference);
        setYear(year);
        setOptional(facultative);
        setExecutionCourse(executionCourse);
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IBibliographicReference) {
            IBibliographicReference refBiblio = (IBibliographicReference) obj;
            result = getTitle().equals(refBiblio.getTitle())
                    && getAuthors().equals(refBiblio.getAuthors())
                    && getReference().equals(refBiblio.getReference())
                    && (getYear() == refBiblio.getYear());
        }
        return result;
    }

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
}