package net.sourceforge.fenixedu.domain.publication;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.publication.PublicationDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Publication extends Publication_Base {

    static {
        PublicationAuthorship.addListener(new PublicationAuthorshipListener());
    }

    public Publication() {
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
    } 

    /********************************************************************
     *                        BUSINESS SERVICES                         *
     ********************************************************************/
    
    public Publication (PublicationDTO publicationDTO, PublicationType publicationType, List<Person> authors) {
    	this();
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
        setKeyType(publicationDTO.getKeyPublicationType());
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

    private static class PublicationAuthorshipListener extends dml.runtime.RelationAdapter<Publication,Authorship> {
        
	/*
	 * This method is responsible for, after removing an authorship from a publication, having all 
	 * the others authorships associated with the same publication have their order rearranged.
	 * @param publicationAuthorship the authorship being removed from the publication
	 * @param publication the publication from whom the authorship will be removed
	 * @see relations.PublicationAuthorship_Base#remove(net.sourceforge.fenixedu.domain.publication.Authorship, net.sourceforge.fenixedu.domain.publication.Publication)
	 */
        @Override
        public void afterRemove(Publication publication, Authorship removedAuthorship) {
            if ((removedAuthorship != null) && (publication != null)) {
                int removedOrder = removedAuthorship.getOrder();
                for(Authorship authorship : publication.getPublicationAuthorships()) {
                    if (authorship.getOrder() > removedOrder) {
                        authorship.setOrder(authorship.getOrder()-1);
                    }
                }
            }
        }
    }
}
