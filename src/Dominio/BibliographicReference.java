package Dominio;

/**
 * @author PTRLV
 *  
 */
public class BibliographicReference extends DomainObject implements IBibliographicReference {

    protected String title;

    protected String authors;

    protected String reference;

    protected Boolean optional;

    protected IExecutionCourse executionCourse;

    protected String year;

    private Integer keyExecutionCourse;

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

    public Integer getKeyExecutionCourse() {
        return keyExecutionCourse;
    }

    public void setKeyExecutionCourse(Integer keyExecutionCourse) {
        this.keyExecutionCourse = keyExecutionCourse;
    }

    public IExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public String getYear() {
        return year;
    }

    public String getAuthors() {
        return authors;
    }

    public Boolean getOptional() {
        return optional;
    }

    public String getReference() {
        return reference;
    }

    public String getTitle() {
        return title;
    }

    public void setExecutionCourse(IExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setAuthors(String author) {
        this.authors = author;
    }

    public void setOptional(Boolean facultative) {
        this.optional = facultative;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setTitle(String title) {
        this.title = title;
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