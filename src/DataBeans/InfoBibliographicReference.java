/*
 * ReferenciaBibliograficaView.java
 * 
 * Created on 6 de Janeiro de 2003, 20:28
 */
package DataBeans;

import Dominio.IBibliographicReference;

/**
 * @author EP 15
 */
public class InfoBibliographicReference extends InfoObject implements
        ISiteComponent {

    protected String title;

    protected String authors;

    protected String reference;

    protected String year;

    protected Boolean optional;

    protected InfoExecutionCourse infoExecutionCourse;

    public InfoBibliographicReference() {
    }

    /** Creates a new instance of InfoBilbiographicReference */
    public InfoBibliographicReference(String title, String authors,
            String reference, String year, Boolean optional) {
        this.title = title;
        this.authors = authors;
        this.reference = reference;
        this.year = year;
        this.optional = optional;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj != null && obj instanceof InfoBibliographicReference) {
            resultado = getTitle().equals(
                    ((InfoBibliographicReference) obj).getTitle())
                    && getAuthors().equals(
                            ((InfoBibliographicReference) obj).getAuthors())
                    && getReference().equals(
                            ((InfoBibliographicReference) obj).getReference())
                    && getYear() == (((InfoBibliographicReference) obj)
                            .getYear())
                    && getOptional() == ((InfoBibliographicReference) obj)
                            .getOptional();
        }
        return resultado;
    }

    /**
     * @return String
     */
    public String getAuthors() {
        return authors;
    }

    /**
     * @return Boolean
     */
    public Boolean getOptional() {
        return optional;
    }

    /**
     * @return String
     */
    public String getReference() {
        return reference;
    }

    /**
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return Integer
     */
    public String getYear() {
        return year;
    }

    /**
     * Sets the authors.
     * 
     * @param authors
     *            The authors to set
     */
    public void setAuthors(String authors) {
        this.authors = authors;
    }

    /**
     * Sets the optional.
     * 
     * @param optional
     *            The optional to set
     */
    public void setOptional(Boolean optional) {
        this.optional = optional;
    }

    /**
     * Sets the reference.
     * 
     * @param reference
     *            The reference to set
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * Sets the title.
     * 
     * @param title
     *            The title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public InfoExecutionCourse getInfoExecutionCourse() {
        return infoExecutionCourse;
    }

    public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
        this.infoExecutionCourse = infoExecutionCourse;
    }

    /**
     * Sets the year.
     * 
     * @param year
     *            The year to set
     */
    public void setYear(String year) {
        this.year = year;
    }

    public void copyFromDomain(IBibliographicReference bibliographicReference) {
        super.copyFromDomain(bibliographicReference);
        if (bibliographicReference != null) {
            setAuthors(bibliographicReference.getAuthors());
            setOptional(bibliographicReference.getOptional());
            setReference(bibliographicReference.getReference());
            setTitle(bibliographicReference.getTitle());
            setYear(bibliographicReference.getYear());
        }
    }

    public static InfoBibliographicReference newInfoFromDomain(
            IBibliographicReference bibliographicReference) {
        InfoBibliographicReference infoBibliographicReference = null;
        if (bibliographicReference != null) {
            infoBibliographicReference = new InfoBibliographicReference();
            infoBibliographicReference.copyFromDomain(bibliographicReference);
        }
        return infoBibliographicReference;
    }

}