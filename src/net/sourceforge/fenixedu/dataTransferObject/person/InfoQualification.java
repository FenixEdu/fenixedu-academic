/*
 * Created on 05/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.person;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountryEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.QualificationType;

/**
 * @author Barbosa
 * @author Pica
 */

public class InfoQualification extends InfoObject implements ISiteComponent {
    private String mark;

    private String school;

    private String title;

    private String degree;

    private InfoPerson infoPerson;

    private Date date;

    private String branch;

    private String specializationArea;

    private String degreeRecognition;

    private Date equivalenceDate;

    private String equivalenceSchool;

    private InfoCountryEditor infoCountry;
    
    private String year;
    
    private QualificationType type;
    


    public InfoQualification() {
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj != null && obj instanceof InfoQualification) {
            result = getSchool().equals(((InfoQualification) obj).getSchool())
                    && getDate().equals(((InfoQualification) obj).getDate())
                    && getInfoPerson().equals(((InfoQualification) obj).getInfoPerson());
        }
        return result;
    }

    /**
     * @return InfoPerson
     */
    public InfoPerson getInfoPerson() {
        return infoPerson;
    }

    /**
     * @return String
     */
    public String getMark() {
        return mark;
    }

    /**
     * @return String
     */
    public String getSchool() {
        return school;
    }

    /**
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the personInfo.
     * 
     * @param infoPerson
     *            The personInfo to set
     */
    public void setInfoPerson(InfoPerson infoPerson) {
        this.infoPerson = infoPerson;
    }

    /**
     * Sets the mark of the qualification
     * 
     * @param mark.
     */
    public void setMark(String mark) {
        this.mark = mark;
    }

    /**
     * Sets the school of qualification
     * 
     * @param school;
     *            The school to set
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * Sets the title of qualification
     * 
     * @param title;
     *            The title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Returns the degree.
     */
    public String getDegree() {
        return degree;
    }

    /**
     * @param degree
     *            The degree to set.
     */
    public void setDegree(String degree) {
        this.degree = degree;
    }

    /**
     * @return Returns the branch.
     */
    public String getBranch() {
        return branch;
    }

    /**
     * @param branch
     *            The branch to set.
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }

    /**
     * @return Returns the degreeRecognition.
     */
    public String getDegreeRecognition() {
        return degreeRecognition;
    }

    /**
     * @param degreeRecognition
     *            The degreeRecognition to set.
     */
    public void setDegreeRecognition(String degreeRecognition) {
        this.degreeRecognition = degreeRecognition;
    }

    /**
     * @return Returns the equivalenceDate.
     */
    public Date getEquivalenceDate() {
        return equivalenceDate;
    }

    /**
     * @param equivalenceDate
     *            The equivalenceDate to set.
     */
    public void setEquivalenceDate(Date equivalenceDate) {
        this.equivalenceDate = equivalenceDate;
    }

    /**
     * @return Returns the equivalenceSchool.
     */
    public String getEquivalenceSchool() {
        return equivalenceSchool;
    }

    /**
     * @param equivalenceSchool
     *            The equivalenceSchool to set.
     */
    public void setEquivalenceSchool(String equivalenceSchool) {
        this.equivalenceSchool = equivalenceSchool;
    }

    /**
     * @return Returns the infoCountry.
     */
    public InfoCountryEditor getInfoCountry() {
        return infoCountry;
    }

    /**
     * @param infoCountry
     *            The infoCountry to set.
     */
    public void setInfoCountry(InfoCountryEditor infoCountry) {
        this.infoCountry = infoCountry;
    }

    /**
     * @return Returns the qualificationDate.
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param qualificationDate
     *            The qualificationDate to set.
     */
    public void setDate(Date qualificationDate) {
        this.date = qualificationDate;
    }

    /**
     * @return Returns the specializationArea.
     */
    public String getSpecializationArea() {
        return specializationArea;
    }

    /**
     * @param specializationArea
     *            The specializationArea to set.
     */
    public void setSpecializationArea(String specializationArea) {
        this.specializationArea = specializationArea;
    }


    public void copyFromDomain(Qualification qualification) {
        super.copyFromDomain(qualification);
        if (qualification != null) {
            setTitle(qualification.getTitle());
            setType(qualification.getType());
            setDate(qualification.getDate());
            setMark(qualification.getMark());
            setSchool(qualification.getSchool());
            setSpecializationArea(qualification.getSpecializationArea());
            setBranch(qualification.getBranch());
            setDegree(qualification.getDegree());
            setDegreeRecognition(qualification.getDegreeRecognition());
            setEquivalenceDate(qualification.getEquivalenceDate());
            setEquivalenceSchool(qualification.getEquivalenceSchool());            
            if(qualification.getYear() != null){
                String[] strings = qualification.getYear().split("/");
                if(strings.length == 2){
                    setYear(strings[1]);
                }
                else if(strings.length == 1){
                    setYear(strings[0]);
                }
            }
        }
    }

    public static InfoQualification newInfoFromDomain(Qualification qualification) {
        InfoQualification infoQualification = null;
        if (qualification != null) {
            infoQualification = new InfoQualification();
            infoQualification.copyFromDomain(qualification);
        }
        return infoQualification;
    }
    

    public QualificationType getType() {
        return type;
    }

    public void setType(QualificationType type) {
        this.type = type;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
