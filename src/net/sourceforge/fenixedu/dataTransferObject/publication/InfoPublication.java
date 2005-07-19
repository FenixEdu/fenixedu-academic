package net.sourceforge.fenixedu.dataTransferObject.publication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.publication.IAuthorship;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class InfoPublication extends PublicationDTO {
	
	protected List infoPublicationTeachers;
	
	protected List infoPublicationAuthors;
    
    private InfoPublicationType infoPublicationType;
	
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
            
            setConference(publication.getConference());
            setCountry(publication.getCountry());
            setCriticizedAuthor(publication.getCriticizedAuthor());
            setEdition(publication.getEdition());
            setEditor(publication.getEditor());
            setEditorCity(publication.getEditorCity());
            setFascicle(publication.getFascicle());
            setFirstPage(publication.getFirstPage());
            setFormat(publication.getFormat());
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
            setPublicationString(publication.toString());
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
            
        	if (getInfoPublicationType() == null) {
        		setInfoPublicationType(new InfoPublicationType());
        	}
        	getInfoPublicationType().setIdInternal(publication.getKeyPublicationType());
            
            List unsortedAuthorsList = new ArrayList(publication.getPublicationAuthorships());
            Collections.sort(unsortedAuthorsList, new BeanComparator("order"));
            List authorsList = (List)CollectionUtils.collect(unsortedAuthorsList,
                    new Transformer() {
                        public Object transform(Object object) {
                            IAuthorship publicationAuthor = (IAuthorship) object;
                            return publicationAuthor.getAuthor();
            }});
            //tranform the authors list into an infoAuthors list
            Iterator it1 = authorsList.iterator();
            List infoAuthorsList = new ArrayList();        
            while (it1.hasNext()){
                IPerson author = (IPerson) it1.next();
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
                ITeacher teacher = (ITeacher) it2.next();
                InfoTeacher infoTeacher = new InfoTeacher();
                infoTeacher.copyFromDomain(teacher);
                infoTeachersList.add(infoTeacher);
            }
            setInfoPublicationTeachers(infoTeachersList);
            
            
            //tranform the publicationType into an infoPublicationType
            InfoPublicationType infoPublicationType = new InfoPublicationType();
            infoPublicationType.setPublicationType(publication.getPublicationType());
            setInfoPublicationType(infoPublicationType);

        }
    }	

    public void copyToDomain(InfoPublication infoPublication, IPublication publication){
        super.copyToDomain(infoPublication,publication);
        
        if (infoPublication != null && publication != null) {
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
            publication.setLanguage(infoPublication.getLanguage());
            publication.setLastPage(infoPublication.getLastPage());
            publication.setLocal(infoPublication.getLocal());
            publication.setMonth(infoPublication.getMonth());
            publication.setMonth_end(infoPublication.getMonth_end());
            publication.setNumber(infoPublication.getNumber());
            publication.setNumberPages(infoPublication.getNumberPages());
            publication.setObservation(infoPublication.getObservation());
            publication.setOriginalLanguage(infoPublication.getOriginalLanguage());
            publication.setPublicationString(infoPublication.getPublicationString());
            
            publication.setScope(infoPublication.getScope());
            publication.setSerie(infoPublication.getSerie());
            publication.setSubType(infoPublication.getSubType());
            publication.setTitle(infoPublication.getTitle());
            publication.setTranslatedAuthor(infoPublication.getTranslatedAuthor());
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

            publication.setPublicationType(infoPublication.getInfoPublicationType().getPublicationType());
            if(publication.getPublicationType() == null) {
            	publication.setPublicationType(infoPublication.getPublicationType());
            }
        }
    }
    
	public static List copyAuthorsFromInfo(InfoPublication infoPublication) throws ExcepcaoPersistencia{
        
        Iterator it = infoPublication.getInfoPublicationAuthors().iterator();
        List authorsList = new ArrayList();
        while(it.hasNext()){
            InfoAuthor infoAuthor = (InfoAuthor) it.next();
            
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPerson author = (IPerson) sp.getIPessoaPersistente().readByOID(Person.class,infoAuthor.getInfoPessoa().getIdInternal());
            
            infoAuthor.copyToDomain(infoAuthor, author);
            authorsList.add(author);
        }
        return authorsList;
    }
	
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
	 * @return Returns the infoPublicationAuthors.
	 */
    
	public List<InfoPublicationAuthor> getInfoPublicationAuthors() {
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
	public void setInfoPublicationAuthors(List<InfoPublicationAuthor> infoPublicationAuthors) {
		this.infoPublicationAuthors = infoPublicationAuthors;
	}

	/**
	 * @param infoPublicationTeachers The infoPublicationTeachers to set.
	 */
	public void setInfoPublicationTeachers(List infoPublicationTeachers) {
		this.infoPublicationTeachers = infoPublicationTeachers;
	}

}