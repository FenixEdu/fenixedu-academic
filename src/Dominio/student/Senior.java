/*
 * Created on Dec 10, 2004
 *
 */
package Dominio.student;

import java.util.Date;
import Dominio.DomainObject;
import Dominio.IStudent;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 *
 */
public class Senior extends DomainObject implements ISenior {

	private IStudent student;
    private Integer keyStudent;
	private Date expectedDegreeTermination;
	private Integer expectedDegreeAverageGrade;
	private String specialtyField;
	private String professionalInterests;
	private String languageSkills;
	private String informaticsSkills;
	private String extracurricularActivities;
	private String professionalExperience;
	private Date lastModificationDate;
    
    /* (non-Javadoc)
     * @see Dominio.student.ISenior#getStudent()
     */
    public IStudent getStudent() {
        return student;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#setStudent(IStudent)
     */
    public void setStudent(IStudent student) {
        this.student = student;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#getKeyStudent()
     */
    public Integer getKeyStudent() {
        return keyStudent;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#setKeyStudent(Integer)
     */
    public void setKeyStudent(Integer keyStudent) {
        this.keyStudent = keyStudent;
    }
    
    /* (non-Javadoc)
     * @see Dominio.student.ISenior#getExpectedDegreeTermination()
     */
    public Date getExpectedDegreeTermination() {
        return expectedDegreeTermination;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#setExpectedDegreeTermination(java.sql.Date)
     */
    public void setExpectedDegreeTermination(Date expectedDegreeTermination) {
        this.expectedDegreeTermination = expectedDegreeTermination;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#getExpectedDegreeAverageGrade()
     */
    public Integer getExpectedDegreeAverageGrade() {
        return expectedDegreeAverageGrade;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#setExpectedDegreeAverageGrade(Integer)
     */
    public void setExpectedDegreeAverageGrade(Integer expectedDegreeAverageGrade) {
        this.expectedDegreeAverageGrade = expectedDegreeAverageGrade;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#getSpecialtyField()
     */
    public String getSpecialtyField() {
        return specialtyField;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#setSpecialtyField(java.lang.String)
     */
    public void setSpecialtyField(String specialtyField) {
        this.specialtyField = specialtyField;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#getProfessionalInterests()
     */
    public String getProfessionalInterests() {
        return professionalInterests;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#setProfessionalInterests(java.lang.String)
     */
    public void setProfessionalInterests(String professionalInterests) {
        this.professionalInterests = professionalInterests;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#getLanguageSkills()
     */
    public String getLanguageSkills() {
        return languageSkills;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#setLanguageSkills(java.lang.String)
     */
    public void setLanguageSkills(String languageSkills) {
        this.languageSkills = languageSkills;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#getInformaticsSkills()
     */
    public String getInformaticsSkills() {
        return informaticsSkills;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#setInformaticsSkills(java.lang.String)
     */
    public void setInformaticsSkills(String informaticsSkills) {
        this.informaticsSkills = informaticsSkills;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#getExtracurricularActivities()
     */
    public String getExtracurricularActivities() {
        return extracurricularActivities;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#setExtracurricularActivities(java.lang.String)
     */
    public void setExtracurricularActivities(String extracurricularActivities) {
        this.extracurricularActivities = extracurricularActivities;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#getProfessionalExperience()
     */
    public String getProfessionalExperience() {
        return professionalExperience;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#setProfessionalExperience(java.lang.String)
     */
    public void setProfessionalExperience(String professionalExperience) {
        this.professionalExperience = professionalExperience;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#getLastModificationDate()
     */
    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    /* (non-Javadoc)
     * @see Dominio.student.ISenior#setLastModificationDate(java.sql.Date)
     */
    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

}
