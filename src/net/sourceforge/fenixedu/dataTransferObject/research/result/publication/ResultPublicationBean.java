package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import bibtex.dom.BibtexAbstractValue;
import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexMacroReference;
import bibtex.dom.BibtexString;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.Unstructured;
import net.sourceforge.fenixedu.util.Month;

public abstract class ResultPublicationBean implements Serializable {

    private ResultPublicationType publicationType;

    private String activeSchema;

    private String participationSchema;

    private DomainReference<Person> person;

    private ResultParticipationRole role = ResultParticipationRole.Author;

    private DomainReference<Unit> publisher;

    private String publisherName;

    private DomainReference<Unit> organization;

    private String organizationName;

    private DomainReference<Country> country;

    private String countryName;

    private DomainReference<Unstructured> unstructuredPublication;

    private String title;

    private String note;

    private String url;

    private Integer year;

    private Month month;

    private Integer idInternal;

    private Boolean createEvent = false;

    public enum ResultPublicationType {
        /* Types based on BibTex */
        Book, Inbook, Incollection, Article, Inproceedings, Proceedings, Thesis, Manual, TechnicalReport, Booklet, Misc, Unpublished;


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

    protected String getStringValueFromBibtexEntry(String field, BibtexEntry entry) {
        BibtexAbstractValue value = (BibtexAbstractValue) entry.getFieldValue(field);
        if (value == null)
            return null;
        String string = null;
        if (value instanceof BibtexString)
            string = ((BibtexString) value).getContent();
        else if (value instanceof BibtexMacroReference)
            string = ((BibtexMacroReference) value).getKey();
        else
            return null;
        string = string.trim();
        if (string.length() == 0)
            return null;
        return string;
    }

    protected Integer getFirstPageFromBibtexEntry(BibtexEntry entry) {
        String pages = getStringValueFromBibtexEntry("pages", entry);
        if (pages == null || pages.length() == 0)
            return null;
        int separatorPos = pages.indexOf("-");
        Integer first = null;
        try {
            first = Integer.valueOf(pages.substring(0, separatorPos).trim());
        } catch (Exception e) {
        }
        return first;
    }

    protected Integer getLastPageFromBibtexEntry(BibtexEntry entry) {
        String pages = getStringValueFromBibtexEntry("pages", entry);
        if (pages == null || pages.length() == 0)
            return null;
        int separatorPos = pages.lastIndexOf("-");
        Integer last = null;
        try {
            last = Integer.valueOf(pages.substring(separatorPos + 1).trim());            
        } catch (Exception e) {
        }
        return last;
    }

    protected void setUnitFromBibtexEntry(String unitType, BibtexEntry entry) {
        String unitString = getStringValueFromBibtexEntry(unitType, entry);
        if (unitString == null)
            return;

        Unit unit = UnitUtils.readExternalInstitutionUnitByName(unitString);
        if (unit != null) {
            if (unitType.compareToIgnoreCase("publisher") == 0)
                setPublisher(unit);
            else if (unitType.compareToIgnoreCase("organization") == 0
                    || unitType.compareToIgnoreCase("school") == 0)
                setOrganization(unit);
        } else {
            // new organization
            if (unitType.compareToIgnoreCase("publisher") == 0)
                setPublisherName(unitString);
            if (unitType.compareToIgnoreCase("organization") == 0
                    || unitType.compareToIgnoreCase("school") == 0)
                setOrganizationName(unitString);
        }
    }

    protected void setYearFromBibtexEntry(BibtexEntry entry) {
        if (getStringValueFromBibtexEntry("year", entry) != null) {
            try {
                setYear(Integer.valueOf(getStringValueFromBibtexEntry("year", entry)));
            } catch (Exception e) {
            }
        }
    }

    protected void setMonthFromBibtexEntry(BibtexEntry entry) {
        BibtexAbstractValue value = (BibtexAbstractValue) entry.getFieldValue("month");
        if (value == null)
            return;

        String monthString = null;
        if (value instanceof BibtexString)
            monthString = ((BibtexString) value).getContent();
        else if (value instanceof BibtexMacroReference)
            monthString = ((BibtexMacroReference) value).getKey();
        else
            return;

        if (monthString.compareToIgnoreCase("JANUARY") == 0)
            month = Month.JANUARY;
        else if (monthString.compareToIgnoreCase("FEBRUARY") == 0)
            month = Month.FEBRUARY;
        else if (monthString.compareToIgnoreCase("MARCH") == 0)
            month = Month.MARCH;
        else if (monthString.compareToIgnoreCase("APRIL") == 0)
            month = Month.APRIL;
        else if (monthString.compareToIgnoreCase("MAY") == 0)
            month = Month.MAY;
        else if (monthString.compareToIgnoreCase("JUNE") == 0)
            month = Month.JUNE;
        else if (monthString.compareToIgnoreCase("JULY") == 0)
            month = Month.JULY;
        else if (monthString.compareToIgnoreCase("AUGUST") == 0)
            month = Month.AUGUST;
        else if (monthString.compareToIgnoreCase("SEPTEMBER") == 0)
            month = Month.SEPTEMBER;
        else if (monthString.compareToIgnoreCase("OCTOBER") == 0)
            month = Month.OCTOBER;
        else if (monthString.compareToIgnoreCase("NOVEMBER") == 0)
            month = Month.NOVEMBER;
        else if (monthString.compareToIgnoreCase("DECEMBER") == 0)
            month = Month.DECEMBER;
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

    public Unstructured getUnstructuredPublication() {
        return (this.unstructuredPublication == null) ? null : this.unstructuredPublication.getObject();
    }

    public void setUnstructuredPublication(Unstructured unstructuredPublication) {
        this.unstructuredPublication = (unstructuredPublication != null) ? new DomainReference<Unstructured>(
                unstructuredPublication)
                : null;
    }
}
