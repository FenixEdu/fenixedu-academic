package net.sourceforge.fenixedu.domain;

/**
 * @author PTRLV
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */
public interface IBibliographicReference extends IDomainObject {
    String getTitle();

    String getAuthors();

    String getReference();

    String getYear();

    IExecutionCourse getExecutionCourse();

    Boolean getOptional();

    void setTitle(String title);

    void setAuthors(String author);

    void setReference(String reference);

    void setYear(String year);

    void setOptional(Boolean optional);

    void setExecutionCourse(IExecutionCourse executionCourse);
}