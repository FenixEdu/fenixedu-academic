package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;
import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.util.Month;

public abstract class ResultPublicationBean implements Serializable {

    private ResultPublicationType publicationType;

    private String activeSchema;

    private String participationSchema;

    private ArrayList<PublicationParticipatorBean> participators = new ArrayList<PublicationParticipatorBean>();

    private DomainReference<Person> person;

    private ResultParticipationRole role = ResultParticipationRole.Author;

    private DomainReference<Unit> publisher;

    private String publisherName;

    private DomainReference<Unit> organization;

    private String organizationName;

    private DomainReference<Country> country;

    private String countryName;

    private String title;

    private String note;

    private String url;

    private Integer year;

    private Month month;

    private Integer idInternal;

    private Boolean createEvent = false;

    public enum ResultPublicationType {
	/*Types based on BibTex*/
	Book, BookPart, Article, Inproceedings, Proceedings, Thesis, Manual, TechnicalReport, Booklet, Misc, Unpublished;

	public static ResultPublicationType getDefaultType() {
	    return Book;
	}
    }
    
    public void fillCommonFields(ResultPublication publication) {
	this.setIdInternal(publication.getIdInternal());
	this.setTitle(publication.getTitle());
	this.setNote(publication.getNote());
	this.setUrl(publication.getUrl());
	this.setYear(publication.getYear());
	this.setMonth(publication.getMonth());
	this.setPublisher(publication.getPublisher());
	this.setOrganization(publication.getOrganization());
	this.setCountry(publication.getCountry());
    }

    public Person getPerson() {
	return (this.person == null) ? null : this.person.getObject();
    }

    public void setPerson(Person person) {
	this.person = (person != null) ? new DomainReference<Person>(person) : null;
    }

    public Unit getPublisher() {
	return (this.publisher == null) ? null : this.publisher.getObject();
    }

    public void setPublisher(Unit publisher) {
	this.publisher = (publisher != null) ? new DomainReference<Unit>(publisher) : null;
    }

    public String getPublisherName() {
	return publisherName;
    }

    public void setPublisherName(String publisherName) {
	this.publisherName = publisherName;
    }

    public Unit getOrganization() {
	return (this.organization == null) ? null : this.organization.getObject();
    }

    public void setOrganization(Unit organization) {
	this.organization = (organization != null) ? new DomainReference<Unit>(organization) : null;
    }

    public String getOrganizationName() {
	return organizationName;
    }

    public void setOrganizationName(String organizationName) {
	this.organizationName = organizationName;
    }

    public Country getCountry() {
	return (this.country == null) ? null : this.country.getObject();
    }

    public void setCountry(Country country) {
	this.country = (country != null) ? new DomainReference<Country>(country) : null;
    }

    public String getCountryName() {
	return countryName;
    }

    public void setCountryName(String countryName) {
	this.countryName = countryName;
    }

    public Month getMonth() {
	return month;
    }

    public void setMonth(Month month) {
	this.month = month;
    }

    public String getNote() {
	return note;
    }

    public void setNote(String note) {
	this.note = note;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public Integer getYear() {
	return year;
    }

    public void setYear(Integer year) {
	this.year = year;
    }

    public String getActiveSchema() {
	return activeSchema;
    }

    public void setActiveSchema(String activeSchema) {
	this.activeSchema = activeSchema;
    }

    public ResultPublicationType getPublicationType() {
	return publicationType;
    }

    public void setPublicationType(ResultPublicationType publicationType) {
	this.publicationType = publicationType;
    }

    public Integer getIdInternal() {
	return idInternal;
    }

    public void setIdInternal(Integer idInternal) {
	this.idInternal = idInternal;
    }

    public ResultParticipationRole getRole() {
	return role;
    }

    public void setRole(ResultParticipationRole role) {
	this.role = role;
    }

    public String getParticipationSchema() {
	return participationSchema;
    }

    public void setParticipationSchema(String participationSchema) {
	this.participationSchema = participationSchema;
    }

    public Boolean getCreateEvent() {
	return createEvent;
    }

    public void setCreateEvent(Boolean createEvent) {
	this.createEvent = createEvent;
    }

    public ArrayList<PublicationParticipatorBean> getParticipators() {
	return participators;
    }

    public void setParticipators(ArrayList<PublicationParticipatorBean> participators) {
	this.participators = participators;
    }
}
