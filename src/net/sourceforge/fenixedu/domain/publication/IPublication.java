/*
 * Created on May 7, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.domain.publication;

import java.util.List;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public interface IPublication extends IDomainObject {
    /**
     * @return Returns the publicationString.
     */
    public abstract String getPublicationString();

    /**
     * @param publicationString
     *            The publicationString to set.
     */
    public abstract void setPublicationString(String publicationString);

    /**
     * @return Returns the country.
     */
    public abstract String getCountry();

    /**
     * @return Returns the criticizedAuthor.
     */
    public abstract String getCriticizedAuthor();

    /**
     * @return Returns the edition.
     */
    public abstract Integer getEdition();

    /**
     * @return Returns the editor.
     */
    public abstract String getEditor();

    /**
     * @return Returns the editorCity.
     */
    public abstract String getEditorCity();

    /**
     * @return Returns the fascicle.
     */
    public abstract Integer getFascicle();

    /**
     * @return Returns the firstPage.
     */
    public abstract Integer getFirstPage();

    /**
     * @return Returns the format.
     */
    public abstract String getFormat();

    /**
     * @return Returns the journalName.
     */
    public abstract String getJournalName();

    /**
     * @return Returns the keyPublicationType.
     */
    public abstract Integer getKeyPublicationType();

    /**
     * @return Returns the language.
     */
    public abstract String getLanguage();

    /**
     * @return Returns the lastPage.
     */
    public abstract Integer getLastPage();

    /**
     * @return Returns the local.
     */
    public abstract String getLocal();

    /**
     * @return Returns the numberPages.
     */
    public abstract Integer getNumberPages();

    /**
     * @return Returns the observation.
     */
    public abstract String getObservation();

    /**
     * @return Returns the originalLanguage.
     */
    public abstract String getOriginalLanguage();

    /**
     * @return Returns the publicationType.
     */
    public abstract String getPublicationType();

    /**
     * @return Returns the scope.
     */
    public abstract String getScope();

    /**
     * @return Returns the serie.
     */
    public abstract Integer getSerie();

    /**
     * @return Returns the subType.
     */
    public abstract String getSubType();

    /**
     * @return Returns the teacher.
     */
    public abstract List getPublicationTeachers();



    /**
     * @return Returns the title.
     */
    public abstract String getTitle();

    /**
     * @return Returns the translatedAuthor.
     */
    public abstract String getTranslatedAuthor();

    /**
     * @return Returns the university.
     */
    public abstract String getUniversity();

    /**
     * @return Returns the url.
     */
    public abstract String getUrl();

    /**
     * @return Returns the volume.
     */
    public abstract String getVolume();

    /**
     * @param country
     *            The country to set.
     */
    public abstract void setCountry(String country);

    /**
     * @param criticizedAuthor
     *            The criticizedAuthor to set.
     */
    public abstract void setCriticizedAuthor(String criticizedAuthor);

    /**
     * @param edition
     *            The edition to set.
     */
    public abstract void setEdition(Integer edition);

    /**
     * @param editor
     *            The editor to set.
     */
    public abstract void setEditor(String editor);

    /**
     * @param editorCity
     *            The editorCity to set.
     */
    public abstract void setEditorCity(String editorCity);

    /**
     * @param fascicle
     *            The fascicle to set.
     */
    public abstract void setFascicle(Integer fascicle);

    /**
     * @param firstPage
     *            The firstPage to set.
     */
    public abstract void setFirstPage(Integer firstPage);

    /**
     * @param format
     *            The format to set.
     */
    public abstract void setFormat(String format);

    /**
     * @param journalName
     *            The journalName to set.
     */
    public abstract void setJournalName(String journalName);

    /**
     * @param keyPublicationType
     *            The keyPublicationType to set.
     */
    public abstract void setKeyPublicationType(Integer keyPublicationType);

    /**
     * @param keyTeacher
     *            The keyTeacher to set.
     */

    public abstract void setLanguage(String language);

    /**
     * @param lastPage
     *            The lastPage to set.
     */
    public abstract void setLastPage(Integer lastPage);

    /**
     * @param local
     *            The local to set.
     */
    public abstract void setLocal(String local);

    /**
     * @param numberPages
     *            The numberPages to set.
     */
    public abstract void setNumberPages(Integer numberPages);

    /**
     * @param observation
     *            The observation to set.
     */
    public abstract void setObservation(String observation);

    /**
     * @param originalLanguage
     *            The originalLanguage to set.
     */
    public abstract void setOriginalLanguage(String originalLanguage);

    /**
     * @param publicationType
     *            The publicationType to set.
     */
    public abstract void setPublicationType(String publicationType);

    /**
     * @param scope
     *            The scope to set.
     */
    public abstract void setScope(String scope);

    /**
     * @param serie
     *            The serie to set.
     */
    public abstract void setSerie(Integer serie);

    /**
     * @param subType
     *            The subType to set.
     */
    public abstract void setSubType(String subType);

    /**
     * @param teacher
     *            The teacher to set.
     */
    public abstract void setTitle(String title);

    /**
     * @param translatedAuthor
     *            The translatedAuthor to set.
     */
    public abstract void setTranslatedAuthor(String translatedAuthor);

    /**
     * @param university
     *            The university to set.
     */
    public abstract void setUniversity(String university);

    /**
     * @param url
     *            The url to set.
     */
    public abstract void setUrl(String url);

    /**
     * @param volume
     *            The volume to set.
     */
    public abstract void setVolume(String volume);

    /**
     * @return Returns the type.
     */
    public abstract IPublicationType getType();

    /**
     * @param type
     *            The type to set.
     */
    public abstract void setType(IPublicationType type);

    public abstract String toString();

    /**
     * @return Returns the conference.
     */
    public abstract String getConference();

    /**
     * @return Returns the didatic.
     */
    public abstract Integer getDidatic();

    /**
     * @return Returns the instituition.
     */
    public abstract String getInstituition();

    /**
     * @return Returns the isbn.
     */
    public abstract Integer getIsbn();

    /**
     * @return Returns the issn.
     */
    public abstract Integer getIssn();

    /**
     * @return Returns the month.
     */
    public abstract String getMonth();

    /**
     * @return Returns the month_end.
     */
    public abstract String getMonth_end();

    /**
     * @return Returns the number.
     */
    public abstract Integer getNumber();

    /**
     * @return Returns the year.
     */
    public abstract Integer getYear();

    /**
     * @return Returns the year_end.
     */
    public abstract Integer getYear_end();

    /**
     * @param conference
     *            The conference to set.
     */
    public abstract void setConference(String conference);

    /**
     * @param didatic
     *            The didatic to set.
     */
    public abstract void setDidatic(Integer didatic);

    /**
     * @param instituition
     *            The instituition to set.
     */
    public abstract void setInstituition(String instituition);

    /**
     * @param isbn
     *            The isbn to set.
     */
    public abstract void setIsbn(Integer isbn);

    /**
     * @param issn
     *            The issn to set.
     */
    public abstract void setIssn(Integer issn);

    /**
     * @param month
     *            The month to set.
     */
    public abstract void setMonth(String month);

    /**
     * @param month_end
     *            The month_end to set.
     */
    public abstract void setMonth_end(String month_end);

    /**
     * @param number
     *            The number to set.
     */
    public abstract void setNumber(Integer number);

    /**
     * @param year
     *            The year to set.
     */
    public abstract void setYear(Integer year);

    /**
     * @param year_end
     *            The year_end to set.
     */
    public abstract void setYear_end(Integer year_end);

    public abstract void setPublicationTeachers(List publicationTeachers);

	  /**
	  * @return a list of PublicationAuthors (Note: Publications Authors != Authors)
	  */
	  public abstract List getPublicationAuthors();    
        
	  public abstract void setPublicationAuthors(List publicationAuthors);
    
    public List getAuthors();
    
    public void setAuthors(List authors);

    public boolean equals(Object object);
    
    public Integer getOrderForAuthor(IAuthor author)throws ExcepcaoInexistente;
}