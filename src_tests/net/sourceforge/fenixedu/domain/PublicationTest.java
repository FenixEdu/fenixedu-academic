package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.publication.PublicationDTO;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.publication.Authorship;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.PublicationType;

public class PublicationTest extends DomainTestBase {

    Person person1;
    Person person2;
    Person person3;
    
    List<Person> authors;
    
    PublicationDTO inexistentPublicationDTO;
    PublicationDTO existentPublicationDTO;
    Publication existentPublication;
    PublicationType publicationType;
    PublicationType newPublicationType;
    
    String conference = "conference";
    String country = "country";
    String criticized_author = "criticized author";
    Integer edition = 1;
    String editor = "editor";
    String editor_city = "editor city";
    Integer fascicle = 2;
    Integer first_page = 1;
    String format = "Magnetic";
    String instituition = "Instituto Superior Tecnico";
    Integer isbn = 3333333;
    Integer issn = 44444444;
    String journal_name = "journal name";
    Integer key_publication_type = 1;
    String language = "english";
    Integer last_page = 999;
    String local = "Jardim de Quimica";
    String month = "Janeiro";
    String month_end = "Março";
    Integer number = 7;
    Integer number_pages = 999;
    String observation = "observation";
    String original_language = "english";
    String publication_string = "publication string";
    String publication_type = "Journal Article";
    String scope = "nacional";
    Integer serie = 6;
    String subtype = "";
    String title = "titulo";
    String translated_author = "translated author";
    String university = "university";
    String url = "http://university";
    String volume = "volume";
    String year = "1999";
    String year_end = "2000";
    
    protected void setUp() throws Exception {
        super.setUp();
        
        publicationType = new PublicationType();
        publicationType.setPublicationType("Journal Article");
        
        newPublicationType = new PublicationType();
        newPublicationType.setPublicationType("Book");
        
        person1 = new Person();
        person1.setName("Autor1");
        
        person2 = new Person();
        person2.setName("Autor2");
        
        person3 = new Person();
        person3.setName("Autor3");
        
        authors = new ArrayList<Person>();
        authors.add(person1);
        authors.add(person2);
        authors.add(person3);
        
        inexistentPublicationDTO = new PublicationDTO();
        inexistentPublicationDTO.setConference(conference);
        inexistentPublicationDTO.setCountry(country);
        inexistentPublicationDTO.setCriticizedAuthor(criticized_author);
        inexistentPublicationDTO.setEdition(edition);
        inexistentPublicationDTO.setEditor(editor);
        inexistentPublicationDTO.setEditorCity(editor_city);
        inexistentPublicationDTO.setFascicle(fascicle);
        inexistentPublicationDTO.setFirstPage(first_page);
        inexistentPublicationDTO.setFormat(format);
        inexistentPublicationDTO.setInstituition(instituition);
        inexistentPublicationDTO.setIsbn(isbn);
        inexistentPublicationDTO.setIssn(issn);
        inexistentPublicationDTO.setJournalName(journal_name);
        inexistentPublicationDTO.setKeyPublicationType(key_publication_type);
        inexistentPublicationDTO.setLanguage(language);
        inexistentPublicationDTO.setLastPage(last_page);
        inexistentPublicationDTO.setLocal(local);
        inexistentPublicationDTO.setMonth(month);
        inexistentPublicationDTO.setMonth_end(month_end);
        inexistentPublicationDTO.setNumber(number);
        inexistentPublicationDTO.setNumberPages(number_pages);
        inexistentPublicationDTO.setObservation(observation);
        inexistentPublicationDTO.setOriginalLanguage(language);
        inexistentPublicationDTO.setPublicationString(publication_string);
        inexistentPublicationDTO.setPublicationType(publication_type);
        inexistentPublicationDTO.setScope(scope);
        inexistentPublicationDTO.setSerie(serie);
        inexistentPublicationDTO.setSubType(subtype);
        inexistentPublicationDTO.setTitle(title);
        inexistentPublicationDTO.setTranslatedAuthor(translated_author);
        inexistentPublicationDTO.setUniversity(university);
        inexistentPublicationDTO.setUrl(url);
        inexistentPublicationDTO.setVolume(volume);
        inexistentPublicationDTO.setYear(year);
        inexistentPublicationDTO.setYear_end(year_end);
        
        existentPublicationDTO = new PublicationDTO();
        existentPublicationDTO.setConference(conference);
        existentPublicationDTO.setCountry(country);
        existentPublicationDTO.setCriticizedAuthor(criticized_author);
        existentPublicationDTO.setEdition(edition);
        existentPublicationDTO.setEditor(editor);
        existentPublicationDTO.setEditorCity(editor_city);
        existentPublicationDTO.setFascicle(fascicle);
        existentPublicationDTO.setFirstPage(first_page);
        existentPublicationDTO.setFormat(format);
        existentPublicationDTO.setInstituition(instituition);
        existentPublicationDTO.setIsbn(isbn);
        existentPublicationDTO.setIssn(issn);
        existentPublicationDTO.setJournalName(journal_name);
        existentPublicationDTO.setKeyPublicationType(key_publication_type);
        existentPublicationDTO.setLanguage(language);
        existentPublicationDTO.setLastPage(last_page);
        existentPublicationDTO.setLocal(local);
        existentPublicationDTO.setMonth(month);
        existentPublicationDTO.setMonth_end(month_end);
        existentPublicationDTO.setNumber(number);
        existentPublicationDTO.setNumberPages(number_pages);
        existentPublicationDTO.setObservation(observation);
        existentPublicationDTO.setOriginalLanguage(language);
        existentPublicationDTO.setPublicationString(publication_string);
        existentPublicationDTO.setPublicationType(publication_type);
        existentPublicationDTO.setScope(scope);
        existentPublicationDTO.setSerie(serie);
        existentPublicationDTO.setSubType(subtype);
        existentPublicationDTO.setTitle(title);
        existentPublicationDTO.setTranslatedAuthor(translated_author);
        existentPublicationDTO.setUniversity(university);
        existentPublicationDTO.setUrl(url);
        existentPublicationDTO.setVolume(volume);
        existentPublicationDTO.setYear(year);
        existentPublicationDTO.setYear_end(year_end);
        
        //all fields are different from existentPublication, except idInternal
        existentPublication = new Publication();
        existentPublication.setConference(conference+1);
        existentPublication.setCountry(country+1);
        existentPublication.setCriticizedAuthor(criticized_author+1);
        existentPublication.setEdition(edition+1);
        existentPublication.setEditor(editor+1);
        existentPublication.setEditorCity(editor_city+1);
        existentPublication.setFascicle(fascicle+1);
        existentPublication.setFirstPage(first_page+1);
        existentPublication.setFormat(format+1);
        existentPublication.setInstituition(instituition+1);
        existentPublication.setIsbn(isbn+1);
        existentPublication.setIssn(issn+1);
        existentPublication.setJournalName(journal_name+1);
        existentPublication.setKeyType(key_publication_type+1);
        existentPublication.setLanguage(language+1);
        existentPublication.setLastPage(last_page+1);
        existentPublication.setLocal(local+1);
        existentPublication.setMonth(month+1);
        existentPublication.setMonth_end(month_end+1);
        existentPublication.setNumber(number+1);
        existentPublication.setNumberPages(number_pages+1);
        existentPublication.setObservation(observation+1);
        existentPublication.setOriginalLanguage(language+1);
        //existentPublication.setPublicationString(publication_string+1);
        //existentPublication.setPublicationType(publication_type+1);
        existentPublication.setScope(scope+1);
        existentPublication.setSerie(serie+1);
        existentPublication.setSubType(subtype+1);
        existentPublication.setTitle(title+1);
        existentPublication.setTranslatedAuthor(translated_author+1);
        existentPublication.setUniversity(university+1);
        existentPublication.setUrl(url+1);
        existentPublication.setVolume(volume+1);
        existentPublication.setYear(Integer.valueOf(year+1));
        existentPublication.setYear_end(Integer.valueOf(year_end+1));
        
        Authorship authorship = new Authorship();
        authorship.setAuthor(person1);
        authorship.setAuthorOrder(1);
        authorship.setPublication(existentPublication);
        //existentPublication.getPublicationAuthorships().add(authorship);

    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    private void assertPublicationContent(Publication publication) {
        assertEquals(publication.getConference(),conference);
        assertEquals(publication.getCountry(),country);
        assertEquals(publication.getCriticizedAuthor(),criticized_author);
        assertEquals(publication.getEdition(),edition);
        assertEquals(publication.getEditor(),editor);
        assertEquals(publication.getEditorCity(),editor_city);
        assertEquals(publication.getFascicle(),fascicle);
        assertEquals(publication.getFirstPage(),first_page);
        assertEquals(publication.getFormat(),format);
        assertEquals(publication.getInstituition(),instituition);
        assertEquals(publication.getIsbn(),isbn);
        assertEquals(publication.getIssn(),issn);
        assertEquals(publication.getJournalName(),journal_name);
        assertEquals(publication.getKeyType(),key_publication_type);
        assertEquals(publication.getLanguage(),language);
        assertEquals(publication.getLastPage(),last_page);
        assertEquals(publication.getLocal(),local);
        assertEquals(publication.getMonth(),month);
        assertEquals(publication.getMonth_end(),month_end);
        assertEquals(publication.getNumber(),number);
        assertEquals(publication.getNumberPages(),number_pages);
        assertEquals(publication.getObservation(),observation);
        assertEquals(publication.getOriginalLanguage(),original_language);
        //assertEquals(publication.getPublicationString(),publication_string);
        //assertEquals(publication.getPublicationType(),publication_type);
        assertEquals(publication.getScope(),scope);
        assertEquals(publication.getSerie(),serie);
        assertEquals(publication.getSubType(),subtype);
        assertEquals(publication.getTitle(),title);
        assertEquals(publication.getTranslatedAuthor(),translated_author);
        assertEquals(publication.getUniversity(),university);
        assertEquals(publication.getUrl(),url);
        assertEquals(publication.getVolume(),volume);
        assertEquals(publication.getYear().toString(),year);
        assertEquals(publication.getYear_end().toString(),year_end);
    }

    public void testCreatePublication() {
        Publication publication = new Publication(inexistentPublicationDTO, publicationType, authors);
        
        assertNotNull(publication);
        
        List<Person> createdAuthors = new ArrayList<Person>(authors.size());
        for(Authorship authorship : publication.getPublicationAuthorships()) {
            createdAuthors.add(authorship.getAuthor());
        }
        
        if (createdAuthors.size() != authors.size() || !createdAuthors.containsAll(authors)) {
            fail();
        }
        
        assertEquals(publication.getType(),publicationType);

        assertPublicationContent(publication);
        
    }
    
    public void testCreatePublicationWithoutAuthors() {
        try {
            new Publication(inexistentPublicationDTO, publicationType, null);
            fail("The publication shouldn't have been created without authors");
        } catch (DomainException domainException) {
            //everything went as planned since a publication can't be created without authors
        }
        
        try {
            List authors1 = new ArrayList<Authorship>();
            new Publication(inexistentPublicationDTO, publicationType, authors1);
            fail("The publication shouldn't have been created without authors");
        } catch (DomainException domainException) {
            //everything went as planned since a publication can't be created without authors
        }        
    }

    public void testEditPublication() {
        
        existentPublication.edit(existentPublicationDTO, newPublicationType, authors);
        
        assertNotNull(existentPublication);
        
        List<Person> createdAuthors = new ArrayList<Person>(authors.size());
        for(Authorship authorship : existentPublication.getPublicationAuthorships()) {
            createdAuthors.add(authorship.getAuthor());
        }
        
        if (createdAuthors.size() != authors.size() || !createdAuthors.containsAll(authors)) {
            fail();
        }
        
        assertEquals(existentPublication.getType(),newPublicationType);
        
        assertPublicationContent(existentPublication);  
    }
    
    public void testEditPublicationWithoutAuthors() {
        try {
            existentPublication.edit(existentPublicationDTO, newPublicationType, null);
            fail("The publication shouldn't have been edited without authors");
        } catch (DomainException domainException) {
            //everything went as planned since a publication can't be edited without authors
        }
        
        try {
            List authors1 = new ArrayList<Authorship>();
            existentPublication.edit(existentPublicationDTO, newPublicationType, authors1);
            fail("The publication shouldn't have been edited without authors");
        } catch (DomainException domainException) {
            //everything went as planned since a publication can't be edited without authors
        }        
    }
    
    public void testDeletePublication() {
        
        existentPublication.delete();
        
        assertEquals("Authorships Size Unexpected", 0, existentPublication.getPublicationAuthorshipsCount());
        assertEquals("PublicationTeachers Size Unexpected", 0, existentPublication.getPublicationTeachersCount());
        assertNull("PublicationType Unexpected", existentPublication.getType());
        
    }

}
