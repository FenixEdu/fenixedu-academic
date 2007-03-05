package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.Manual;
import net.sourceforge.fenixedu.domain.research.result.publication.OtherPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.domain.research.result.publication.Unstructured;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import bibtex.dom.BibtexAbstractValue;
import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexMacroReference;
import bibtex.dom.BibtexString;

public abstract class ResultPublicationBean implements Serializable {
    public enum ResultPublicationType {
	/* Types based on BibTex */
	Article, Book, Inbook, Incollection, Inproceedings, Proceedings, Thesis, Manual, TechnicalReport, OtherPublication;

	public static ResultPublicationType getDefaultType() {
	    return Article;
	}

	public static ResultPublicationType getTypeFromPublication(ResearchResultPublication publication) {
	    String typeStr = publication.getClass().getSimpleName();
	    if (typeStr.equals("Unstructured"))
		return null;
	    // add bookPart type
	    if (typeStr.equals("BookPart")) {
		typeStr = ((BookPart) publication).getBookPartType().toString();
	    }
	    return ResultPublicationType.valueOf(typeStr);
	}

	public static ResultPublicationType getTypeFromBibTeX(BibtexEntry entry) {
	    String type = entry.getEntryType();

	    if (type.equalsIgnoreCase("book"))
		return Book;
	    if (type.equalsIgnoreCase("inbook"))
		return Inbook;
	    if (type.equalsIgnoreCase("incollection"))
		return Incollection;
	    else if (type.equalsIgnoreCase("article"))
		return Article;
	    else if (type.equalsIgnoreCase("inproceedings") || type.equalsIgnoreCase("conference"))
		return Inproceedings;
	    else if (type.equalsIgnoreCase("proceedings") || type.equalsIgnoreCase("collection"))
		return Proceedings;
	    else if (type.equalsIgnoreCase("mastersthesis") || type.equalsIgnoreCase("phdthesis"))
		return Thesis;
	    else if (type.equalsIgnoreCase("manual"))
		return Manual;
	    else if (type.equalsIgnoreCase("techreport"))
		return TechnicalReport;
	    else if (type.equalsIgnoreCase("booklet") || type.equalsIgnoreCase("misc")
		    || type.equalsIgnoreCase("unpublished"))
		return OtherPublication;
	    else
		return null;
	}
    }

    private DomainReference<Person> person;

    private DomainReference<Unit> publisher;

    private DomainReference<Unit> organization;

    private DomainReference<Country> country;

    private ResultPublicationType publicationType;

    private String activeSchema;

    private String participationSchema;

    private ResultParticipationRole role = ResultParticipationRole.Author;

    private String publisherName;

    private String organizationName;

    private String countryName;

    private String title;

    private MultiLanguageString note;

    private String url;

    private Integer year;

    private Month month;

    private Integer idInternal;

    private Boolean createEvent = Boolean.FALSE;

    private MultiLanguageString keywords;

    private Boolean createJournal = Boolean.FALSE;
    
    public abstract ResultPublicationBean convertTo(ResultPublicationType type);

    protected abstract void fillSpecificFields(ResearchResultPublication publication);

    protected abstract void fillBibTeXFields(BibtexEntry bibtexEntry);

    public static ResultPublicationBean getBeanToCreate(ResultPublicationType type) {
	switch (type) {
	case Article:
	    return new ArticleBean();
	case Book:
	    return new BookBean();
	case Inbook:
	    return new InbookBean();
	case Incollection:
	    return new IncollectionBean();
	case Inproceedings:
	    return new InproceedingsBean();
	case Manual:
	    return new ManualBean();
	case OtherPublication:
	    return new OtherPublicationBean();
	case Proceedings:
	    return new ProceedingsBean();
	case TechnicalReport:
	    return new TechnicalReportBean();
	case Thesis:
	    return new ThesisBean();
	default:
	    return null;
	}
    }

    public static ResultPublicationBean getBeanToEdit(ResearchResultPublication publication) {
	if (publication instanceof Unstructured)
	    return new UnstructuredBean((Unstructured) publication);

	switch (ResultPublicationType.getTypeFromPublication(publication)) {
	case Article:
	    return new ArticleBean((Article) publication);
	case Book:
	    return new BookBean((Book) publication);
	case Inbook:
	    return new InbookBean((BookPart) publication);
	case Incollection:
	    return new IncollectionBean((BookPart) publication);
	case Inproceedings:
	    return new InproceedingsBean((Inproceedings) publication);
	case Manual:
	    return new ManualBean((Manual) publication);
	case OtherPublication:
	    return new OtherPublicationBean((OtherPublication) publication);
	case Proceedings:
	    return new ProceedingsBean((Proceedings) publication);
	case TechnicalReport:
	    return new TechnicalReportBean((TechnicalReport) publication);
	case Thesis:
	    return new ThesisBean((Thesis) publication);
	default:
	    return null;
	}
    }

    public static ResultPublicationBean getBeanToImport(BibtexEntry bibtexEntry) {
	switch (ResultPublicationType.getTypeFromBibTeX(bibtexEntry)) {
	case Article:
	    return new ArticleBean(bibtexEntry);
	case Book:
	    return new BookBean(bibtexEntry);
	case Inbook:
	    return new InbookBean(bibtexEntry);
	case Incollection:
	    return new IncollectionBean(bibtexEntry);
	case Inproceedings:
	    return new InproceedingsBean(bibtexEntry);
	case Manual:
	    return new ManualBean(bibtexEntry);
	case OtherPublication:
	    return new OtherPublicationBean(bibtexEntry);
	case Proceedings:
	    return new ProceedingsBean(bibtexEntry);
	case TechnicalReport:
	    return new TechnicalReportBean(bibtexEntry);
	case Thesis:
	    return new ThesisBean(bibtexEntry);
	default:
	    return null;
	}
    }

    protected void fillCommonFields(ResearchResultPublication publication) {
	this.setIdInternal(publication.getIdInternal());
	this.setTitle(publication.getTitle());
	this.setNote(publication.getNote());
	this.setUrl(publication.getUrl());
	this.setYear(publication.getYear());
	this.setMonth(publication.getMonth());
	this.setPublisher(publication.getPublisher());
	this.setOrganization(publication.getOrganization());
	this.setCountry(publication.getCountry());
	this.setKeywords(publication.getKeywords());
    }

    protected void fillCommonBeanFields(ResultPublicationBean bean) {
	this.setIdInternal(bean.getIdInternal());
	this.setTitle(bean.getTitle());
	this.setNote(bean.getNote());
	this.setUrl(bean.getUrl());
	this.setYear(bean.getYear());
	this.setMonth(bean.getMonth());
	this.setPublisher(bean.getPublisher());
	this.setOrganization(bean.getOrganization());
	this.setCountry(bean.getCountry());
	this.setPerson(bean.getPerson());
	this.setKeywords(bean.getKeywords());
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

    public MultiLanguageString getNote() {
	return note;
    }

    public void setNote(String note) {
    	this.note = new MultiLanguageString(note);
    }
    
    public void setNote(MultiLanguageString note) {
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

    public Boolean getCreateJournal() {
	return this.createJournal;
    }
    
    public void setCreateJournal(Boolean createJournal) {
	this.createJournal = createJournal;
    }
    public String getPublicationTypeString() {
	if (publicationType != null)
	    return publicationType.toString();
	else
	    return this.getClass().getSimpleName().replace("Bean", "");
    }

	public MultiLanguageString getKeywords() {
		return keywords;
	}

	public void setKeywords(MultiLanguageString keywords) {
		this.keywords = keywords;
	}
}
