/*
 * Created on Mar 29, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package Dominio.publication;

import java.util.Iterator;
import java.util.List;

import Dominio.DomainObject;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class Publication extends DomainObject implements IPublication {

    private Integer keyPublicationType;

    private String subType;

    private String journalName;

    private String title;

    private String volume;

    private String editor;

    private Integer firstPage;

    private Integer lastPage;

    private String language;

    private String country;

    private Integer issn;

    private String format;

    private String scope;

    private String observation;

    private Integer edition;

    private String editorCity;

    private Integer numberPages;

    private Integer fascicle;

    private Integer serie;

    private String url;

    private String university;

    private String local;

    private String originalLanguage;

    private String translatedAuthor;

    private String criticizedAuthor;

    private String publicationType;

    private String month;

    private Integer year;

    private String month_end;

    private Integer year_end;

    private String conference;

    private String instituition;

    private Integer isbn;

    private Integer number;

    private Integer didatic;

    private String publicationString;

    private List publicationTeachers;

    private List publicationAuthors;

    private IPublicationType type;

    /**
     * @return Returns the publicationString.
     */
    public String getPublicationString() {
        return publicationString;
    }

    /**
     * @param publicationString
     *            The publicationString to set.
     */
    public void setPublicationString(String publicationString) {
        this.publicationString = publicationString;
    }

    public Publication() {
        super();
    }

    /**
     * @return Returns the country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return Returns the criticizedAuthor.
     */
    public String getCriticizedAuthor() {
        return criticizedAuthor;
    }

    /**
     * @return Returns the edition.
     */
    public Integer getEdition() {
        return edition;
    }

    /**
     * @return Returns the editor.
     */
    public String getEditor() {
        return editor;
    }

    /**
     * @return Returns the editorCity.
     */
    public String getEditorCity() {
        return editorCity;
    }

    /**
     * @return Returns the fascicle.
     */
    public Integer getFascicle() {
        return fascicle;
    }

    /**
     * @return Returns the firstPage.
     */
    public Integer getFirstPage() {
        return firstPage;
    }

    /**
     * @return Returns the format.
     */
    public String getFormat() {
        return format;
    }

    /**
     * @return Returns the journalName.
     */
    public String getJournalName() {
        return journalName;
    }

    /**
     * @return Returns the keyPublicationType.
     */
    public Integer getKeyPublicationType() {
        return keyPublicationType;
    }

    /**
     * @return Returns the language.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @return Returns the lastPage.
     */
    public Integer getLastPage() {
        return lastPage;
    }

    /**
     * @return Returns the local.
     */
    public String getLocal() {
        return local;
    }

    /**
     * @return Returns the numberPages.
     */
    public Integer getNumberPages() {
        return numberPages;
    }

    /**
     * @return Returns the observation.
     */
    public String getObservation() {
        return observation;
    }

    /**
     * @return Returns the originalLanguage.
     */
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    /**
     * @return Returns the publicationType.
     */
    public String getPublicationType() {
        return publicationType;
    }

    /**
     * @return Returns the scope.
     */
    public String getScope() {
        return scope;
    }

    /**
     * @return Returns the serie.
     */
    public Integer getSerie() {
        return serie;
    }

    /**
     * @return Returns the subType.
     */
    public String getSubType() {
        return subType;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return Returns the translatedAuthor.
     */
    public String getTranslatedAuthor() {
        return translatedAuthor;
    }

    /**
     * @return Returns the university.
     */
    public String getUniversity() {
        return university;
    }

    /**
     * @return Returns the url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return Returns the volume.
     */
    public String getVolume() {
        return volume;
    }

    /**
     * @param country
     *            The country to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @param criticizedAuthor
     *            The criticizedAuthor to set.
     */
    public void setCriticizedAuthor(String criticizedAuthor) {
        this.criticizedAuthor = criticizedAuthor;
    }

    /**
     * @param edition
     *            The edition to set.
     */
    public void setEdition(Integer edition) {
        this.edition = edition;
    }

    /**
     * @param editor
     *            The editor to set.
     */
    public void setEditor(String editor) {
        this.editor = editor;
    }

    /**
     * @param editorCity
     *            The editorCity to set.
     */
    public void setEditorCity(String editorCity) {
        this.editorCity = editorCity;
    }

    /**
     * @param fascicle
     *            The fascicle to set.
     */
    public void setFascicle(Integer fascicle) {
        this.fascicle = fascicle;
    }

    /**
     * @param firstPage
     *            The firstPage to set.
     */
    public void setFirstPage(Integer firstPage) {
        this.firstPage = firstPage;
    }

    /**
     * @param format
     *            The format to set.
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @param journalName
     *            The journalName to set.
     */
    public void setJournalName(String journalName) {
        this.journalName = journalName;
    }

    /**
     * @param keyPublicationType
     *            The keyPublicationType to set.
     */
    public void setKeyPublicationType(Integer keyPublicationType) {
        this.keyPublicationType = keyPublicationType;
    }

    /**
     * @param language
     *            The language to set.
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @param lastPage
     *            The lastPage to set.
     */
    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }

    /**
     * @param local
     *            The local to set.
     */
    public void setLocal(String local) {
        this.local = local;
    }

    /**
     * @param numberPages
     *            The numberPages to set.
     */
    public void setNumberPages(Integer numberPages) {
        this.numberPages = numberPages;
    }

    /**
     * @param observation
     *            The observation to set.
     */
    public void setObservation(String observation) {
        this.observation = observation;
    }

    /**
     * @param originalLanguage
     *            The originalLanguage to set.
     */
    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    /**
     * @param publicationType
     *            The publicationType to set.
     */
    public void setPublicationType(String publicationType) {
        this.publicationType = publicationType;
    }

    /**
     * @param scope
     *            The scope to set.
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * @param serie
     *            The serie to set.
     */
    public void setSerie(Integer serie) {
        this.serie = serie;
    }

    /**
     * @param subType
     *            The subType to set.
     */
    public void setSubType(String subType) {
        this.subType = subType;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @param translatedAuthor
     *            The translatedAuthor to set.
     */
    public void setTranslatedAuthor(String translatedAuthor) {
        this.translatedAuthor = translatedAuthor;
    }

    /**
     * @param university
     *            The university to set.
     */
    public void setUniversity(String university) {
        this.university = university;
    }

    /**
     * @param url
     *            The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @param volume
     *            The volume to set.
     */
    public void setVolume(String volume) {
        this.volume = volume;
    }

    /**
     * @return Returns the type.
     */
    public IPublicationType getType() {
        return type;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(IPublicationType type) {
        this.type = type;
    }

    /**
     * @return Returns the conference.
     */
    public String getConference() {
        return conference;
    }

    /**
     * @return Returns the didatic.
     */
    public Integer getDidatic() {
        return didatic;
    }

    /**
     * @return Returns the instituition.
     */
    public String getInstituition() {
        return instituition;
    }

    /**
     * @return Returns the isbn.
     */
    public Integer getIsbn() {
        return isbn;
    }

    /**
     * @return Returns the issn.
     */
    public Integer getIssn() {
        return issn;
    }

    /**
     * @return Returns the month.
     */
    public String getMonth() {
        return month;
    }

    /**
     * @return Returns the month_end.
     */
    public String getMonth_end() {
        return month_end;
    }

    /**
     * @return Returns the number.
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @return Returns the year.
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @return Returns the year_end.
     */
    public Integer getYear_end() {
        return year_end;
    }

    /**
     * @param conference
     *            The conference to set.
     */
    public void setConference(String conference) {
        this.conference = conference;
    }

    /**
     * @param didatic
     *            The didatic to set.
     */
    public void setDidatic(Integer didatic) {
        this.didatic = didatic;
    }

    /**
     * @param instituition
     *            The instituition to set.
     */
    public void setInstituition(String instituition) {
        this.instituition = instituition;
    }

    /**
     * @param isbn
     *            The isbn to set.
     */
    public void setIsbn(Integer isbn) {
        this.isbn = isbn;
    }

    /**
     * @param issn
     *            The issn to set.
     */
    public void setIssn(Integer issn) {
        this.issn = issn;
    }

    /**
     * @param month
     *            The month to set.
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * @param month_end
     *            The month_end to set.
     */
    public void setMonth_end(String month_end) {
        this.month_end = month_end;
    }

    /**
     * @param number
     *            The number to set.
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * @param year
     *            The year to set.
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * @param year_end
     *            The year_end to set.
     */
    public void setYear_end(Integer year_end) {
        this.year_end = year_end;
    }

    public String toString() {
        String publication;
        publication = "";

        /*
         * if (getType() != null) { if (getType() .getPublicationType()
         * .equalsIgnoreCase("Outra Publicacao")) { publication =
         * getPublicationType().toUpperCase() + " : '\n'"; } else { publication =
         * getType().getPublicationType().toUpperCase() + " : '\n'"; } }
         */

        publication += getTitle();

        if(getType().getPublicationType().equalsIgnoreCase("Unstructured"))
            return publication;

        String str = "";
        if (getType().getPublicationType().equalsIgnoreCase("translation"))
            str = " Translation";
        else if (getType().getPublicationType().equalsIgnoreCase("critique"))
            str = " Critique";

        if (getSubType() != null && getSubType().length() != 0) {
            publication = publication + " (" + getSubType() + str + ")";
        }

        publication += " - ";

        Iterator iteratorAuthors = publicationAuthors.iterator();
        while (iteratorAuthors.hasNext()) {
            IAuthor author = (IAuthor) iteratorAuthors.next();
            if (author.getKeyPerson() != null && author.getKeyPerson().intValue() != 0) {
                publication += author.getPerson().getNome();
            } else {
                publication += author.getAuthor() + ", ";
            }
            //publication += ", ";
        }

        if (getJournalName() != null && getJournalName().length() != 0) {
            publication = publication + ", " + getJournalName();
        }

        if (getCriticizedAuthor() != null && getCriticizedAuthor().length() != 0) {
            publication = publication + ", Original Author: " + getCriticizedAuthor();
        }

        if (getConference() != null && getConference().length() != 0) {
            publication = publication + ", " + getConference();
        }

        if (getOriginalLanguage() != null && getOriginalLanguage().length() != 0
                && getLanguage() != null && getLanguage().length() != 0) {
            publication = publication + ", Translation " + getOriginalLanguage() + " - " + getLanguage();
        }

        if (getTranslatedAuthor() != null && getTranslatedAuthor().length() != 0) {
            publication = publication + ", Original Author: " + getTranslatedAuthor();
        }

        String ola = null;

        if (getEdition() != null) {
            switch (getEdition().intValue()) {
            case 1:
                ola = "st";
                break;
            case 2:
                ola = "nd";
                break;
            case 3:
                ola = "rd";
                break;
            default:
                ola = "th";
                break;
            }
        }

        if (getEdition() != null && getEdition().intValue() != 0) {
            publication = publication + " " + getEdition() + ola + " Edition";
        }

        if (getFascicle() != null && getFascicle().intValue() != 0) {
            publication = publication + ", Fasc. " + getFascicle();
        }

        if (getNumber() != null && getNumber().intValue() != 0) {
            publication = publication + ", No. " + getNumber();
        }

        if (getVolume() != null && getVolume().length() != 0) {
            publication = publication + ", Vol. " + getVolume();
        }

        if (getFirstPage() != null && getFirstPage().intValue() != 0) {
            publication = publication + " (Pag. " + getFirstPage();
        }

        if (getLastPage() != null && getLastPage().intValue() != 0) {
            publication = publication + " - " + getLastPage() + ")";
        }

        /*
         * if (getNumberPages() != null && getNumberPages().intValue() != 0) {
         * publication = publication + ", Number Pages = " + getNumberPages(); }
         */

        if (getSerie() != null && getSerie().intValue() != 0) {
            publication = publication + ", Serie " + getSerie();
        }

        if (getEditor() != null && getEditor().length() != 0) {
            publication = publication + ", " + getEditor();
        }

        /*
         * if (getIssn() != null && getIssn().intValue() != 0) { publication =
         * publication + ", ISSN = " + getIssn(); }
         * 
         * if (getIsbn() != null && getIsbn().intValue() != 0) { publication =
         * publication + ", ISBN = " + getIsbn(); }
         */

        /*
         * if (getScope() != null && getScope().length() != 0) { publication =
         * publication + ", Scope : " + getScope(); }
         */

        if (getLocal() != null && getLocal().length() != 0) {
            publication = publication + ", " + getLocal();
        }

        if (getUniversity() != null && getUniversity().length() != 0) {
            publication = publication + ", " + getUniversity();
        }

        if (getInstituition() != null && getInstituition().length() != 0) {
            publication = publication + ", " + getInstituition();
        }

        if (getMonth() != null && getMonth().length() != 0) {
            publication = publication + " " + getMonth();
        }

        if (getYear() != null && getYear().intValue() != 0) {
            publication = publication + " " + getYear();
        }

        if (getMonth_end() != null && getMonth_end().length() != 0) {
            publication = publication + " a " + getMonth_end();
        }

        if (getYear_end() != null && getYear_end().intValue() != 0) {
            publication = publication + " " + getYear_end();
        }

        if (getEditorCity() != null && getEditorCity().length() != 0) {
            publication = publication + " " + getEditorCity();
        }

        if (getCountry() != null && getCountry().length() != 0) {
            publication = publication + ", " + getCountry();
        }

        /*
         * if (getObservation() != null && getObservation().length() != 0) {
         * publication = publication + ", Observation : " + getObservation(); }
         */
        //publication = publication + ".";
        if (getFormat() != null && getFormat().length() != 0) {
            publication = publication + ", (" + getFormat() + " format)";
        }

        if (getUrl() != null && getUrl().length() != 0) {
            publication = publication + " " + getUrl();
        }

        return publication;
    }

    /**
     * @return Returns the publicationPersons.
     */
    public List getPublicationTeachers() {
        return publicationTeachers;
    }

    /**
     * @param publicationPersons
     *            The publicationPersons to set.
     */
    public void setPublicationTeachers(List publicationTeachers) {
        this.publicationTeachers = publicationTeachers;
    }

    /**
     * @return Returns the publicationAuthors.
     */
    public List getPublicationAuthors() {
        return publicationAuthors;
    }

    /**
     * @param publicationAuthors
     *            The publicationAuthors to set.
     */
    public void setPublicationAuthors(List publicationAuthors) {
        this.publicationAuthors = publicationAuthors;
    }

    //	public boolean equals(Object object) {
    //		if (object == null) {
    //			return false;
    //		}
    //		if (!(object instanceof IPublication)) {
    //			return false;
    //		} else {
    //			if(this.publicationString != null && this.publicationString.length()!=
    // 0){
    //				return
    // this.publicationString.equals(((IPublication)object).getPublicationString());
    //			}
    //			else{
    //				return
    // this.getIdInternal().equals(((IPublication)object).getIdInternal());
    //			}
    //
    //		}
    //
    //	}

}