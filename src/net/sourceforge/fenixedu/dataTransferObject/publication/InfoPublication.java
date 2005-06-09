/*
 * Created on Mar 29, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.dataTransferObject.publication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.publication.Author;
import net.sourceforge.fenixedu.domain.publication.IAuthor;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationAuthor;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author TJBF & PFON
 * @author Carlos Pereira & Francisco Passos
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class InfoPublication extends InfoObject {
	
	protected String objConcreteClass;
	
	protected Integer keyPublicationType;
	
	protected String subType;
	protected String journalName;
	protected String title;
	protected String volume;
	protected String editor;
	protected Integer firstPage;
	protected Integer lastPage;
	protected String language;
	protected String country;
	protected Integer issn;
	protected String format;
	protected String scope;
	protected String observation;
	protected Integer edition;
	protected String editorCity;
	protected Integer numberPages;
	protected Integer fascicle;
	protected Integer serie;
	protected String url;
	protected String university;
	protected String local;
	protected String originalLanguage;
	protected String translatedAuthor;
	protected String criticizedAuthor;
	protected String publicationType;
	protected String month;
	protected String year;
	protected String month_end;
	protected String year_end;
	protected String conference;
	protected String instituition;
	protected Integer isbn;
	protected Integer number;
	protected Integer didatic;
	
	protected String publicationString;
	
	protected List infoPublicationTeachers;
	
	protected List infoPublicationAuthors;
	
	public static InfoPublication newInfoFromDomain(IPublication publication) {
        InfoPublication infoPublication = null;
        if (publication != null) {
            infoPublication = new InfoPublication();
            infoPublication.copyFromDomain(publication);
        }
        return infoPublication;
    }

    public void copyFromDomain(IPublication publication) {
        
        super.copyFromDomain(publication);
        if (publication != null) {
        	if (getInfoPublicationType() == null) {
        		setInfoPublicationType(new InfoPublicationType());
        	}
        	getInfoPublicationType().setIdInternal(publication.getKeyPublicationType());
            setConference(publication.getConference());
            setCountry(publication.getCountry());
            setCriticizedAuthor(publication.getCriticizedAuthor());
            setEdition(publication.getEdition());
            setEditor(publication.getEditor());
            setEditorCity(publication.getEditorCity());
            setFascicle(publication.getFascicle());
            setFirstPage(publication.getFirstPage());
            setFormat(publication.getFormat());
            setIdInternal(publication.getIdInternal());
            
            List unsortedAuthorsList = new ArrayList(publication.getPublicationAuthors());
            Collections.sort(unsortedAuthorsList, new BeanComparator("order"));
            List authorsList = (List)CollectionUtils.collect(unsortedAuthorsList,
                    new Transformer() {
                        public Object transform(Object object) {
                            IPublicationAuthor publicationAuthor = (IPublicationAuthor) object;
                            return publicationAuthor.getAuthor();
            }});
            //tranform the authors list into an infoAuthors list
            Iterator it1 = authorsList.iterator();
            List infoAuthorsList = new ArrayList();        
            while (it1.hasNext()){
                IAuthor author = (IAuthor) it1.next();
                InfoAuthor infoAuthor = new InfoAuthor();
                infoAuthor.copyFromDomain(author);
                infoAuthorsList.add(infoAuthor);
            }

            setInfoPublicationAuthors(infoAuthorsList);

            List teachers = (List) CollectionUtils.collect(publication.getPublicationTeachers(), new Transformer() {
                public Object transform(Object object) {
                    IPublicationTeacher publicationTeacher = (IPublicationTeacher) object;
                    return publicationTeacher.getTeacher();
                }
            });
                    
            
            //tranform the teachers list into an infoTeachers list
            Iterator it2 = teachers.iterator();
            List infoTeachersList = new ArrayList();        
            while (it2.hasNext()){
                Teacher teacher = (Teacher) it2.next();
                InfoTeacher infoTeacher = new InfoTeacher();
                infoTeacher.copyFromDomain(teacher);
                infoTeachersList.add(infoTeacher);
            }
            setInfoPublicationTeachers(infoTeachersList);
            //DOME this is unwell
            
            
            //tranform the publicationType into an infoPublicationType
            InfoPublicationType infoPublicationType = new InfoPublicationType();
            infoPublicationType.setPublicationType(publication.getPublicationType());
            
            
            setInfoPublicationType(infoPublicationType);
            setInstituition(publication.getInstituition());
            setIsbn(publication.getIsbn());
            setIssn(publication.getIssn());
            setJournalName(publication.getJournalName());
            setKeyPublicationType(publication.getKeyPublicationType());
            setLanguage(publication.getLanguage());
            setLastPage(publication.getLastPage());
            setLocal(publication.getLocal());
            setMonth(publication.getMonth());
            setMonth_end(publication.getMonth_end());
            setNumber(publication.getNumber());
            setNumberPages(publication.getNumberPages());
            setObservation(publication.getObservation());
            setOriginalLanguage(publication.getOriginalLanguage());
            setPublicationString(publication.getPublicationString());
            setPublicationType(publication.getPublicationType());
            setScope(publication.getScope());
            setSerie(publication.getSerie());
            setSubType(publication.getSubType());
            setTitle(publication.getTitle());
            setTranslatedAuthor(publication.getTranslatedAuthor());
            setUniversity(publication.getUniversity());
            setUrl(publication.getUrl());
            setVolume(publication.getVolume());
            setYear(String.valueOf(publication.getYear()));
            setYear_end(String.valueOf(publication.getYear_end()));
            
        }
    }	

    public void copyToDomain(InfoPublication infoPublication, IPublication publication){
        if (infoPublication != null && publication != null) {
            super.copyToDomain(infoPublication, publication);
            publication.setConference(infoPublication.getConference());
            publication.setCountry(infoPublication.getCountry());
            publication.setCriticizedAuthor(infoPublication.getCriticizedAuthor());
            publication.setEdition(infoPublication.getEdition());
            publication.setEditor(infoPublication.getEditor());
            publication.setEditorCity(infoPublication.getEditorCity());
            publication.setFascicle(infoPublication.getFascicle());
            publication.setFirstPage(infoPublication.getFirstPage());
            publication.setFormat(infoPublication.getFormat());
            publication.setInstituition(infoPublication.getInstituition());
            publication.setIsbn(infoPublication.getIsbn());
            publication.setIssn(infoPublication.getIssn());
            publication.setJournalName(infoPublication.getJournalName());
            //publication.setKeyPublicationType(infoPublication.getKeyPublicationType());
            publication.setLanguage(infoPublication.getLanguage());
            publication.setLastPage(infoPublication.getLastPage());
            publication.setLocal(infoPublication.getLocal());
            publication.setMonth(infoPublication.getMonth());
            publication.setMonth_end(infoPublication.getMonth_end());
            publication.setNumber(infoPublication.getNumber());
            publication.setNumberPages(infoPublication.getNumberPages());
            publication.setObservation(infoPublication.getObservation());
            publication.setOriginalLanguage(infoPublication.getOriginalLanguage());
            //It is not possible to fill (in this method) the Publication Authors List since each
            //author also has a list of its own Publications, and therefore it would enter in an
            //infinite cycle
            //publication.setPublicationAuthors()
            publication.setPublicationString(infoPublication.getPublicationString());
            //DOME Verify this
            //It is not possible to fill (in this method) the Publication Teachers List since each
            //teacher also has a list of its own Publications, and therefore it would enter in an
            //infinite cycle
            //publication.setPublicationTeachers()
            
            publication.setPublicationType(infoPublication.getInfoPublicationType().getPublicationType());
            if(publication.getPublicationType() == null) {
            	publication.setPublicationType(infoPublication.getPublicationType());
            }
            publication.setScope(infoPublication.getScope());
            publication.setSerie(infoPublication.getSerie());
            publication.setSubType(infoPublication.getSubType());
            publication.setTitle(infoPublication.getTitle());
            publication.setTranslatedAuthor(infoPublication.getTranslatedAuthor());
            //publication.setType(infoPublication.getPublicationType());
            publication.setUniversity(infoPublication.getUniversity());
            publication.setUrl(infoPublication.getUrl());
            publication.setVolume(infoPublication.getVolume());
            try {
	            if (infoPublication.getYear() != null && !infoPublication.getYear().equals(""))
	                publication.setYear(Integer.valueOf(infoPublication.getYear()));
            } catch (NumberFormatException nfe) {
            	//nothing to be done, therefore empty catch clause
            }
            try {
	            if (infoPublication.getYear_end() != null && !infoPublication.getYear_end().equals(""))
	                publication.setYear_end(Integer.valueOf(infoPublication.getYear_end()));
            } catch (NumberFormatException nfe) {
	        	//nothing to be done, therefore empty catch clause
	        }

        }
    }
    
	public static List copyAuthorsFromInfo(InfoPublication infoPublication){
        
        Iterator it = infoPublication.getInfoPublicationAuthors().iterator();
        List authorsList = new ArrayList();
        while(it.hasNext()){
            InfoAuthor infoAuthor = (InfoAuthor) it.next();
            IAuthor author = new Author();
            infoAuthor.copyToDomain(infoAuthor, author);
            authorsList.add(author);
        }
        return authorsList;
    }
	
	private InfoPublicationType infoPublicationType;
	
	/**
	 * @return Returns the infoPublicationType.
	 */
	public InfoPublicationType getInfoPublicationType() {
		return infoPublicationType;
	}

	/**
	 * @param infoPublicationType The infoPublicationType to set.
	 */
	public void setInfoPublicationType(InfoPublicationType infoPublicationType) {
		this.infoPublicationType = infoPublicationType;
	}

	/**
	 * @return Returns the publicationString.
	 */
	public String getPublicationString() {
		return publicationString;
	}

	/**
	 * @param publicationString The publicationString to set.
	 */
	public void setPublicationString(String publicationString) {
		this.publicationString = publicationString;
	}

	public InfoPublication() {
		super();
		this.objConcreteClass = this.getClass().getName();
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
	 * @return Returns the objConcreteClass.
	 */
	public String getObjConcreteClass() {
		return objConcreteClass;
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
	 * @param country The country to set.
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @param criticizedAuthor The criticizedAuthor to set.
	 */
	public void setCriticizedAuthor(String criticizedAuthor) {
		this.criticizedAuthor = criticizedAuthor;
	}


	/**
	 * @param edition The edition to set.
	 */
	public void setEdition(Integer edition) {
		this.edition = edition;
	}

	/**
	 * @param editor The editor to set.
	 */
	public void setEditor(String editor) {
		this.editor = editor;
	}

	/**
	 * @param editorCity The editorCity to set.
	 */
	public void setEditorCity(String editorCity) {
		this.editorCity = editorCity;
	}

	/**
	 * @param fascicle The fascicle to set.
	 */
	public void setFascicle(Integer fascicle) {
		this.fascicle = fascicle;
	}

	/**
	 * @param firstPage The firstPage to set.
	 */
	public void setFirstPage(Integer firstPage) {
		this.firstPage = firstPage;
	}

	/**
	 * @param format The format to set.
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @param journalName The journalName to set.
	 */
	public void setJournalName(String journalName) {
		this.journalName = journalName;
	}

	/**
	 * @param keyPublicationType The keyPublicationType to set.
	 */
	public void setKeyPublicationType(Integer keyPublicationType) {
		this.keyPublicationType = keyPublicationType;
	}


	/**
	 * @param language The language to set.
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @param lastPage The lastPage to set.
	 */
	public void setLastPage(Integer lastPage) {
		this.lastPage = lastPage;
	}

	/**
	 * @param local The local to set.
	 */
	public void setLocal(String local) {
		this.local = local;
	}

	/**
	 * @param numberPages The numberPages to set.
	 */
	public void setNumberPages(Integer numberPages) {
		this.numberPages = numberPages;
	}

	/**
	 * @param objConcreteClass The objConcreteClass to set.
	 */
	public void setObjConcreteClass(String objConcreteClass) {
		this.objConcreteClass = objConcreteClass;
	}

	/**
	 * @param observation The observation to set.
	 */
	public void setObservation(String observation) {
		this.observation = observation;
	}

	/**
	 * @param originalLanguage The originalLanguage to set.
	 */
	public void setOriginalLanguage(String originalLanguage) {
		this.originalLanguage = originalLanguage;
	}

	/**
	 * @param publicationType The publicationType to set.
	 */
	public void setPublicationType(String publicationType) {
		this.publicationType = publicationType;
	}

	/**
	 * @param scope The scope to set.
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * @param serie The serie to set.
	 */
	public void setSerie(Integer serie) {
		this.serie = serie;
	}

	/**
	 * @param subType The subType to set.
	 */
	public void setSubType(String subType) {
		this.subType = subType;
	}


	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param translatedAuthor The translatedAuthor to set.
	 */
	public void setTranslatedAuthor(String translatedAuthor) {
		this.translatedAuthor = translatedAuthor;
	}

	/**
	 * @param university The university to set.
	 */
	public void setUniversity(String university) {
		this.university = university;
	}

	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @param volume The volume to set.
	 */
	public void setVolume(String volume) {
		this.volume = volume;
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
	public String getYear() {
		return year;
	}

	/**
	 * @return Returns the year_end.
	 */
	public String getYear_end() {
		return year_end;
	}

	/**
	 * @param conference The conference to set.
	 */
	public void setConference(String conference) {
		this.conference = conference;
	}

	/**
	 * @param didatic The didatic to set.
	 */
	public void setDidatic(Integer didatic) {
		this.didatic = didatic;
	}

	/**
	 * @param instituition The instituition to set.
	 */
	public void setInstituition(String instituition) {
		this.instituition = instituition;
	}

	/**
	 * @param isbn The isbn to set.
	 */
	public void setIsbn(Integer isbn) {
		this.isbn = isbn;
	}

	/**
	 * @param issn The issn to set.
	 */
	public void setIssn(Integer issn) {
		this.issn = issn;
	}

	/**
	 * @param month The month to set.
	 */
	public void setMonth(String month) {
		this.month = month;
	}

	/**
	 * @param month_end The month_end to set.
	 */
	public void setMonth_end(String month_end) {
		this.month_end = month_end;
	}

	/**
	 * @param number The number to set.
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * @param year The year to set.
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * @param year_end The year_end to set.
	 */
	public void setYear_end(String year_end) {
		this.year_end = year_end;
	}



	/**
	 * @return Returns the infoPublicationAuthors.
	 */
	public List getInfoPublicationAuthors() {
		return infoPublicationAuthors;
	}

	/**
	 * @return Returns the infoPublicationTeachers.
	 */
	public List getInfoPublicationTeachers() {
		return infoPublicationTeachers;
	}

	/**
	 * @param infoPublicationAuthors The infoPublicationAuthors to set.
	 */
	public void setInfoPublicationAuthors(List infoPublicationAuthors) {
		this.infoPublicationAuthors = infoPublicationAuthors;
	}

	/**
	 * @param infoPublicationTeachers The infoPublicationTeachers to set.
	 */
	public void setInfoPublicationTeachers(List infoPublicationTeachers) {
		this.infoPublicationTeachers = infoPublicationTeachers;
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getTitle()+", "+getYear()+"\n";
    }
}