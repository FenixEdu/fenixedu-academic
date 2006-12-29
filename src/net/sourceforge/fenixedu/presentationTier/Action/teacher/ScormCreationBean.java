package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.io.InputStream;

import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Language;

import org.joda.time.YearMonthDay;

import pt.linkare.scorm.utils.ScormMetaDataHash;

/**
 * 
 * @author pcma
 * 
 * This bean is responsabile to hold information about a ready to create scorm
 * package. Field values along enums created here are based on the following
 * paper:
 * 
 * "Manual de Apoio para a utilização de Meta-dados na catalogação de conteúdos
 * com próposito educativo"
 */
public class ScormCreationBean extends FileItemCreationBean {

	private static final String metaMetadataSchema = "ADL SCORM";
	private String generalTitle="";
	private String generalDescription="";
	private CatalogType generalCatalog;
	private String generalEntry="";
	private Language generalLanguage;
	private String generalAggregationLevel="1";
	private String personName;
	  
	private ScormContributionRecommendedRoles contributeRole;
	private YearMonthDay contributeDate;
	
	private EducationalInteractivityType educationalInteractivityType;
    
    
    private Language educationalLanguage;
    
    private Boolean rightsCost=null;
    private Boolean rightsCopyRightOtherRestrictions=null;
    private String rightsDescription="";
    
    transient private InputStream vCardFile;
    private String vCardFilename;
    private long vCardSize;
    private String vcardContent="";
    
    private String keywords="";
    
    private String technicalLocation="";
    
	public ScormCreationBean(Item item) {
		super(item);
	}

    public ScormMetaDataHash getMetaInformation() {
    	ScormMetaDataHash scormMetaHasher = new ScormMetaDataHash();

		scormMetaHasher.put("title", null, getGeneralLanguageAsString(), getGeneralTitle());
		scormMetaHasher.put("description", null, getGeneralLanguageAsString(), getGeneralDescription());
		scormMetaHasher.put("identifier", getCatalogTypeAsString(), getGeneralLanguageAsString(), getGeneralEntry());
		
		scormMetaHasher.put("date", "created", null, getCurrentDateAsString());
		scormMetaHasher.put("contributor", "author", null, getCourseName());
		
		scormMetaHasher.put("version", null, null, "v1.0");
		scormMetaHasher.put("metadatascheme", null, null, metaMetadataSchema);
		scormMetaHasher.put("location", "uri", null, getTechnicalLocation());
		scormMetaHasher.put("rights", "cost", null, getBooleanValue(getRightsCost()));
		scormMetaHasher.put("rights", "copyright", null, getBooleanValue(getRightsCost()));
		scormMetaHasher.put("subject", null, getGeneralLanguageAsString(), getKeywordsArray());
		scormMetaHasher.put("description", null, getGeneralLanguageAsString(),getGeneralDescription());
		return scormMetaHasher;		
    }
    
    private String[] getKeywordsArray() {
    	String[] keys = getKeywords().split(",");
    	for(int i=0;i<keys.length;i++) {
    		keys[i].trim();
    	}
    	return keys;
    }
    private String getCurrentDateAsString() {
    	YearMonthDay current = new YearMonthDay();
    	return YearMonthDayAsString(current);
	}

	private String YearMonthDayAsString(YearMonthDay current) {
		return current.getDayOfMonth() + "-" + current.getMonthOfYear() + "-" + current.getYear();

	}

	public boolean isValid() {
    	return getDisplayName()!=null && getFile()!=null &&  getPermittedGroup()!=null && getGeneralTitle()!=null && 
    	getRightsCost()!=null &&  getRightsCopyRightOtherRestrictions()!=null && getGeneralCatalog()!=null && 
    	getGeneralEntry()!=null && getKeywords()!=null;
    }

	private String getEducationalLanguageAsString() {
		Language language= getEducationalLanguage();
		return (language==null) ? "" : language.toString();
	}

	private String getLearningResourceAsString() {
		
		EducationalResourceType type = getEducationalLearningResourceType(); 
		return (type==null) ? "" : type.getType();
	}

	private String getEducationalInteractivityTypeAsString() {
		EducationalInteractivityType type = getEducationalInteractivityType(); 
		return (type==null) ? "" : type.getType();
	}
    
    private String getCatalogTypeAsString() {
    	CatalogType type = getGeneralCatalog();
    	return (type==null) ? "" : type.getType();
    }
    
    private String getGeneralLanguageAsString() {
    	Language language =getGeneralLanguage(); 
    	return (language==null) ? "x-none" : language.toString();
    }
    
    private String getContributeRoleAsString() {
    	ScormContributionRecommendedRoles role = getContributeRole();
    	return (role==null) ? "" : role.getType();
    }
    
    private String getDateAsString() {
    	YearMonthDay date = getContributeDate();
    	return (date==null) ? "" : YearMonthDayAsString(date);
    }
    

    private String getCourseName() {
    	ExecutionCourseSite site = (ExecutionCourseSite)this.getItem().getSection().getSite();
		return site.getExecutionCourse().getNome();
    }
    public enum ScormContributionRecommendedRoles {
    	ROLE_AUTHOR ("author"),
    	ROLE_PUBLISHER ("publisher"),
    	ROLE_UNKNOWN ("unknown"),
// ROLE_INITIATOR ("initiator"),
// ROLE_TERMINATOR ("terminator"),
    	ROLE_VALIDATOR ("validator");
// ROLE_EDITOR ("editor"),
// ROLE_GRAPHICAL_DESIGNER ("graphical designer"),
// ROLE_TECHNICAL_IMPLEMENTER ("technical implementer"),
// ROLE_CONTENT_PROVIDER ("content provider"),
// ROLE_ROLE_TECHNICAL_VALIDATOR ("technical validator"),
// ROLE_EDUCATIONAL_VALIDATOR ("educational validator"),
// ROLE_SCRIPT_WRITER ("script writter"),
// ROLE_INSTRUCTIONAL_DESIGNER ("instructional designer");
    	
    	private String type;
    	
    	private ScormContributionRecommendedRoles(String type) {
    		this.type = type;
    	}
    	
    	public String getType() {
    		return type;
    	}
    }
    
    public enum EducationalInteractivityType {
    	ACTIVE ("active"),
    	EXPOSITIVE("expositive"),
    	MIXER("mixed"),
    	UNDEFINED("undefined");
    	
    	private String type;
    	
    	private EducationalInteractivityType(String type) {
    		this.type = type;
    	}
    	
    	public String getType() {
    		return type;
    	}
    }
    
    public enum CatalogType {
    	ISBN ("isbn"),
    	ISSN("issn"),
    	SICI("sici"),
    	ISMN("ismn"),
    	URI("uri"),
    	OTHER("other");
    	;
    	
    	private String type;
    	
    	private CatalogType(String type) {
    		this.type = type;
    	}
    	
    	public String getType() {
    		return type;
    	}
    }
    
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getVcardContent() {
		return vcardContent;
	}

	public void setVcardContent(String vcardContent) {
		this.vcardContent = vcardContent;
	}

	public InputStream getVirtualCardFile() {
		return vCardFile;
	}

	public void setVirtualCardFile(InputStream cardFile) {
		vCardFile = cardFile;
	}
	public YearMonthDay getContributeDate() {
		return contributeDate;
	}

	public void setContributeDate(YearMonthDay contributeDate) {
		this.contributeDate = contributeDate;
	}

	public ScormContributionRecommendedRoles getContributeRole() {
		return contributeRole;
	}

	public void setContributeRole(ScormContributionRecommendedRoles contributeRole) {
		this.contributeRole = contributeRole;
	}

	public String getGeneralAggregationLevel() {
		return generalAggregationLevel;
	}

	public void setGeneralAggregationLevel(String generalAggregationLevel) {
		this.generalAggregationLevel = generalAggregationLevel;
	}

	public CatalogType getGeneralCatalog() {
		return generalCatalog;
	}

	public void setGeneralCatalog(CatalogType generalCatalog) {
		this.generalCatalog = generalCatalog;
	}

	public String getGeneralDescription() {
		return generalDescription;
	}

	public void setGeneralDescription(String generalDescription) {
		this.generalDescription = generalDescription;
	}

	public String getGeneralEntry() {
		return generalEntry;
	}

	public void setGeneralEntry(String generalEntry) {
		this.generalEntry = generalEntry;
	}

	public String getGeneralTitle() {
		return generalTitle;
	}

	public void setGeneralTitle(String generalTitle) {
		this.generalTitle = generalTitle;
	}

	public Boolean getRightsCopyRightOtherRestrictions() {
		return rightsCopyRightOtherRestrictions;
	}

	public void setRightsCopyRightOtherRestrictions(Boolean rightsCopyRightOtherRestrictions) {
		this.rightsCopyRightOtherRestrictions = rightsCopyRightOtherRestrictions;
	}

	public Boolean getRightsCost() {
		return rightsCost;
	}

	public void setRightsCost(Boolean rightsCost) {
		this.rightsCost = rightsCost;
	}

	public String getRightsDescription() {
		return rightsDescription;
	}

	public void setRightsDescription(String rightsDescription) {
		this.rightsDescription = rightsDescription;
	}

	private String getBooleanValue(Boolean bool) {
		return (bool) ? "yes" : "no"; 
	}

	public String getVirtualCardFilename() {
		return vCardFilename;
	}

	public void setVirtualCardFilename(String cardFilename) {
		vCardFilename = cardFilename;
	}

	public long getVirtualCardSize() {
		return vCardSize;
	}

	public void setVirtualCardSize(long cardSize) {
		vCardSize = cardSize;
	}
	
	public String getTechnicalLocation() {
		return technicalLocation;
	}

	public void setTechnicalLocation(String technicalLocation) {
		this.technicalLocation = technicalLocation;
	}

	public Language getEducationalLanguage() {
		return educationalLanguage;
	}

	public void setEducationalLanguage(Language educationalLanguage) {
		this.educationalLanguage = educationalLanguage;
	}

	public Language getGeneralLanguage() {
		return generalLanguage;
	}

	public void setGeneralLanguage(Language generalLanguage) {
		this.generalLanguage = generalLanguage;
	}

	  public EducationalInteractivityType getEducationalInteractivityType() {
			return educationalInteractivityType;
		}

		public void setEducationalInteractivityType(EducationalInteractivityType educationalInteractivityType) {
			this.educationalInteractivityType = educationalInteractivityType;
		}

	public void copyValuesFrom(ScormCreationBean possibleBean) {
		this.setContributeDate(possibleBean.getContributeDate());
		this.setContributeRole(possibleBean.getContributeRole());
		this.setDisplayName(possibleBean.getDisplayName());
		this.setEducationalInteractivityType(possibleBean.getEducationalInteractivityType());
		this.setEducationalLanguage(possibleBean.getEducationalLanguage());
		this.setEducationalLearningResourceType(possibleBean.getEducationalLearningResourceType());
		this.setGeneralAggregationLevel(possibleBean.getGeneralAggregationLevel());
		this.setGeneralCatalog(possibleBean.getGeneralCatalog());
		this.setGeneralDescription(possibleBean.getGeneralDescription());
		this.setGeneralEntry(possibleBean.getGeneralEntry());
		this.setGeneralLanguage(possibleBean.getGeneralLanguage());
		this.setGeneralTitle(possibleBean.getGeneralTitle());
		this.setKeywords(possibleBean.getKeywords());
		this.setPermittedGroup(possibleBean.getPermittedGroup());
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}
}
