/*
 * Created on Dec 22, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 *
 */
public class InfoSenior extends InfoObject {
    
    private String name;
    private String address;
    private String areaCode;
    private String areaCodeArea;
    private String phone;
    private String mobilePhone;
    private String email;
    private Boolean availablePhoto;
    private Integer personID;
    private Date expectedDegreeTermination;
	private Integer expectedDegreeAverageGrade;
	private String specialtyField;
	private String professionalInterests;
	private String languageSkills;
	private String informaticsSkills;
	private String extracurricularActivities;
	private String professionalExperience;
	private Date lastModificationDate;
	
	public InfoSenior(){
	    
	}
	
    /**
     * @param name
     * @param address
     * @param areaCode
     * @param areaCodeArea
     * @param phone
     * @param mobilePhone
     * @param email
     * @param expectedDegreeTermination
     * @param expectedDegreeAverageGrade
     * @param specialtyField
     * @param professionalInterests
     * @param languageSkills
     * @param informaticsSkills
     * @param extracurricularActivities
     * @param professionalExperience
     * @param lastModificationDate
     */
    public InfoSenior(String name, String address, String areaCode, String areaCodeArea, String phone, String mobilePhone, String email,
            Date expectedDegreeTermination, Integer expectedDegreeAverageGrade, String specialtyField,
            String professionalInterests, String languageSkills, String informaticsSkills,
            String extracurricularActivities, String professionalExperience, Date lastModificationDate) {
        this.name = name;
        this.address = address;
        this.areaCode = areaCode;
        this.areaCodeArea = areaCodeArea;
        this.phone = phone;
        this.mobilePhone = mobilePhone;
        this.email = email;
        this.expectedDegreeTermination = expectedDegreeTermination;
        this.expectedDegreeAverageGrade = expectedDegreeAverageGrade;
        this.specialtyField = specialtyField;
        this.professionalInterests = professionalInterests;
        this.languageSkills = languageSkills;
        this.informaticsSkills = informaticsSkills;
        this.extracurricularActivities = extracurricularActivities;
        this.professionalExperience = professionalExperience;
        this.lastModificationDate = lastModificationDate;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "Senior :\n";
        result += "\n  - Name : " + name;
        result += "\n  - Address : " + address;
        result += "\n  - AreaCode : " + areaCode;
        result += "\n  - AreaCodeArea : " + areaCodeArea;
        result += "\n  - Phone : " + phone;
        result += "\n  - MobilePhone : " + mobilePhone;
        result += "\n  - Expected Degree Termination : " + expectedDegreeTermination;
        result += "\n  - Expected Degree Average Grade : " + expectedDegreeAverageGrade;
        result += "\n  - Specialty Field : " + specialtyField;
        result += "\n  - Professional Interests : " + professionalInterests;
        result += "\n  - Language Skills : " + languageSkills;
        result += "\n  - Informatics Skills : " + informaticsSkills;
        result += "\n  - Extracurricular Activities : " + extracurricularActivities;
        result += "\n  - Professional Experience : " + professionalExperience;
        result += "\n  - Last Modification Date : " + lastModificationDate.toGMTString();
        return result;
    }
    
	/**
     * @return Returns the address.
     */
    public String getAddress() {
        return address;
    }
    /**
     * @param address The address to set.
     */
    public void setAddress(String address) {
        this.address = address;
    }	
    /**
     * @return Returns the areaCode.
     */
    public String getAreaCode() {
        return areaCode;
    }
    /**
     * @param address The areaCode to set.
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }	
    /**
     * @return Returns the areaCodeArea.
     */
    public String getAreaCodeArea() {
        return areaCodeArea;
    }
    /**
     * @param address The areaCodeArea to set.
     */
    public void setAreaCodeArea(String areaCodeArea) {
        this.areaCodeArea = areaCodeArea;
    }
    /**
     * @return Returns the email.
     */
    public String getEmail() {
        return email;
    }
    /**
     * @param email The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * @return Returns the availablePhoto.
     */
    public Boolean getAvailablePhoto() {
        return availablePhoto;
    }
    /**
     * @param availablePhoto The availablePhoto to set.
     */
    public void setAvailablePhoto(Boolean availablePhoto) {
        this.availablePhoto = availablePhoto;
    }
    /**
     * @return Returns the expectedDegreeAverageGrade.
     */
    /**
     * @return Returns the personID.
     */
    public Integer getPersonID() {
        return personID;
    }
    /**
     * @param personID The personID to set.
     */
    public void setPersonID(Integer personID) {
        this.personID = personID;
    }
    public Integer getExpectedDegreeAverageGrade() {
        return expectedDegreeAverageGrade;
    }
    /**
     * @param expectedDegreeAverageGrade The expectedDegreeAverageGrade to set.
     */
    public void setExpectedDegreeAverageGrade(Integer expectedDegreeAverageGrade) {
        this.expectedDegreeAverageGrade = expectedDegreeAverageGrade;
    }
    /**
     * @return Returns the expectedDegreeTermination.
     */
    public Date getExpectedDegreeTermination() {
        return expectedDegreeTermination;
    }
    /**
     * @param expectedDegreeTermination The expectedDegreeTermination to set.
     */
    public void setExpectedDegreeTermination(Date expectedDegreeTermination) {
        this.expectedDegreeTermination = expectedDegreeTermination;
    }
    /**
     * @return Returns the extracurricularActivities.
     */
    public String getExtracurricularActivities() {
        return extracurricularActivities;
    }
    /**
     * @param extracurricularActivities The extracurricularActivities to set.
     */
    public void setExtracurricularActivities(String extracurricularActivities) {
        this.extracurricularActivities = extracurricularActivities;
    }
    /**
     * @return Returns the informaticsSkills.
     */
    public String getInformaticsSkills() {
        return informaticsSkills;
    }
    /**
     * @param informaticsSkills The informaticsSkills to set.
     */
    public void setInformaticsSkills(String informaticsSkills) {
        this.informaticsSkills = informaticsSkills;
    }
    /**
     * @return Returns the languageSkills.
     */
    public String getLanguageSkills() {
        return languageSkills;
    }
    /**
     * @param languageSkills The languageSkills to set.
     */
    public void setLanguageSkills(String languageSkills) {
        this.languageSkills = languageSkills;
    }
    /**
     * @return Returns the lastModificationDate.
     */
    public Date getLastModificationDate() {
        return lastModificationDate;
    }
    /**
     * @param lastModificationDate The lastModificationDate to set.
     */
    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }
    /**
     * @return Returns the mobilePhone.
     */
    public String getMobilePhone() {
        return mobilePhone;
    }
    /**
     * @param mobilePhone The mobilePhone to set.
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the professionalExperience.
     */
    public String getProfessionalExperience() {
        return professionalExperience;
    }
    /**
     * @param professionalExperience The professionalExperience to set.
     */
    public void setProfessionalExperience(String professionalExperience) {
        this.professionalExperience = professionalExperience;
    }
    /**
     * @return Returns the professionalInterests.
     */
    public String getProfessionalInterests() {
        return professionalInterests;
    }
    /**
     * @param professionalInterests The professionalInterests to set.
     */
    public void setProfessionalInterests(String professionalInterests) {
        this.professionalInterests = professionalInterests;
    }
    /**
     * @return Returns the specialtyField.
     */
    public String getSpecialtyField() {
        return specialtyField;
    }
    /**
     * @param specialtyField The specialtyField to set.
     */
    public void setSpecialtyField(String specialtyField) {
        this.specialtyField = specialtyField;
    }
    /**
     * @return Returns the phone.
     */
    public String getPhone() {
        return phone;
    }
    /**
     * @param phone The phone to set.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
