package net.sourceforge.fenixedu.domain.publication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.publication.PublicationDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class Publication extends Publication_Base {

    public Publication() {
    } 

    /********************************************************************
     *                        BUSINESS SERVICES                         *
     ********************************************************************/
    
    public Publication (PublicationDTO publicationDTO, PublicationType publicationType, List<Person> authors) {
        if( authors == null || authors.size() == 0)
            throw new DomainException("error.publication.createPublicationWithoutAuthors");
        setProperties(publicationDTO);
        
        setType(publicationType);
        
        setAuthorships(authors);
    }
    
    public void edit(PublicationDTO publicationDTO, PublicationType publicationType, List<Person> authors) {
        if( authors == null || authors.size() == 0)
            throw new DomainException("error.publication.editPublicationWithoutAuthors");

        setProperties(publicationDTO);
        
        setType(publicationType);
        
        removeAuthorships();
        
        setAuthorships(authors);
    }
    
    public void delete()
    {
        removeType();
        
        for (Iterator<PublicationTeacher> iterator = getPublicationTeachersIterator(); iterator.hasNext(); ) {
            PublicationTeacher publicationTeacher = iterator.next();
            iterator.remove();
            publicationTeacher.delete();
        }
        
        for (Iterator<Authorship> iterator = getPublicationAuthorshipsIterator(); iterator.hasNext(); ) {
            Authorship authorship = iterator.next();
            iterator.remove();
            authorship.delete();
        }
        
        super.deleteDomainObject();
    }

    
    /********************************************************************
     *                         PRIVATE METHODS                          *
     ********************************************************************/
    
    
    private void setProperties(PublicationDTO publicationDTO) {
        setConference(publicationDTO.getConference());
        setCountry(publicationDTO.getCountry());
        setCriticizedAuthor(publicationDTO.getCriticizedAuthor());
        setEdition(publicationDTO.getEdition());
        setEditor(publicationDTO.getEditor());
        setEditorCity(publicationDTO.getEditorCity());
        setFascicle(publicationDTO.getFascicle());
        setFirstPage(publicationDTO.getFirstPage());
        setFormat(publicationDTO.getFormat());
        setInstituition(publicationDTO.getInstituition());
        setIsbn(publicationDTO.getIsbn());
        setIssn(publicationDTO.getIssn());
        setJournalName(publicationDTO.getJournalName());
        setKeyPublicationType(publicationDTO.getKeyPublicationType());
        setLanguage(publicationDTO.getLanguage());
        setLastPage(publicationDTO.getLastPage());
        setLocal(publicationDTO.getLocal());
        setMonth(publicationDTO.getMonth());
        setMonth_end(publicationDTO.getMonth_end());
        setNumber(publicationDTO.getNumber());
        setNumberPages(publicationDTO.getNumberPages());
        setObservation(publicationDTO.getObservation());
        setOriginalLanguage(publicationDTO.getOriginalLanguage());
        setPublicationString(publicationDTO.getPublicationString());
        setPublicationType(publicationDTO.getPublicationType());
        setScope(publicationDTO.getScope());
        setSerie(publicationDTO.getSerie());
        setSubType(publicationDTO.getSubType());
        setTitle(publicationDTO.getTitle());
        setTranslatedAuthor(publicationDTO.getTranslatedAuthor());
        setUniversity(publicationDTO.getUniversity());
        setUrl(publicationDTO.getUrl());
        setVolume(publicationDTO.getVolume());
        try {
            if (publicationDTO.getYear() != null && !publicationDTO.getYear().equals(""))
                setYear(Integer.valueOf(publicationDTO.getYear()));
        } catch (NumberFormatException nfe) {
            //nothing to be done, therefore empty catch clause
        }
        try {
            if (publicationDTO.getYear_end() != null && !publicationDTO.getYear_end().equals(""))
                setYear_end(Integer.valueOf(publicationDTO.getYear_end()));
        } catch (NumberFormatException nfe) {
            //nothing to be done, therefore empty catch clause
        }
    }
    
    private void setAuthorships(List<Person> authors) {
        int i = 1;
        for (final Iterator iterator = authors.iterator(); iterator.hasNext(); i++) {
            final Person author = (Person) iterator.next();
            final Authorship authorship = new Authorship();

            authorship.setAuthor(author);
            authorship.setPublication(this);
            authorship.setOrder(new Integer(i));
        }
    }
    
    private void removeAuthorships() {

        for (Iterator<Authorship> iterator = getPublicationAuthorshipsIterator(); iterator.hasNext(); ) {
            Authorship authorship = iterator.next();
            iterator.remove();
            authorship.delete();
        }
        
    }

    
    /********************************************************************
     *                          OTHER METHODS                           *
     ********************************************************************/

    public String toString() {
        String publication;
        publication = "";

        publication += getTitle();

        String str = "";
        if (getType()!=null && getType().getPublicationType()!=null && getType().getPublicationType().equalsIgnoreCase("translation"))
            str = " Translation";
        else if (getType()!=null && getType().getPublicationType()!=null && getType().getPublicationType().equalsIgnoreCase("critique"))
            str = " Critique";

        if (getSubType() != null && getSubType().length() != 0) {
            publication = publication + " (" + getSubType() + str + ")";
        }

        publication += " - ";

        List publicationAuthorships = new ArrayList(this.getPublicationAuthorships());
        Collections.sort(publicationAuthorships, new BeanComparator("order"));
        
        List authors = (List) CollectionUtils.collect(publicationAuthorships, new Transformer() {
            public Object transform(Object obj){
                Authorship pa = (Authorship) obj;
                return pa.getAuthor();
            }
        });
        
        Iterator iteratorAuthors = authors.iterator();
        while (iteratorAuthors.hasNext()) {
            Person author = (Person) iteratorAuthors.next();

            publication += author.getNome()+", ";
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

        if (getSerie() != null && getSerie().intValue() != 0) {
            publication = publication + ", Serie " + getSerie();
        }

        if (getEditor() != null && getEditor().length() != 0) {
            publication = publication + ", " + getEditor();
        }

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

        if (getFormat() != null && getFormat().length() != 0) {
            publication = publication + ", (" + getFormat() + " format)";
        }

        if (getUrl() != null && getUrl().length() != 0) {
            publication = publication + " " + getUrl();
        }

        return publication;
    }

}