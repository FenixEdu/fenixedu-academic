/*
 * Created on 5/Ago/2003, 16:08:50
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package DataBeans.Seminaries;

import java.util.List;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseWithInfoDegree;
import DataBeans.InfoObject;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentWithInfoPerson;
import Dominio.Seminaries.ICandidacy;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 5/Ago/2003, 16:08:50
 *  
 */
public class InfoCandidacy extends InfoObject {

    private List caseStudyChoices;

    private String motivation;

    private String seminaryName;

    private InfoTheme theme;

    private InfoModality infoModality;

    private InfoStudent infoStudent;

    private InfoCurricularCourse curricularCourse;

    private InfoSeminary infoSeminary;

    private Boolean approved;

    /**
     * @return
     */
    public List getCaseStudyChoices() {
        return caseStudyChoices;
    }

    /**
     * @param list
     */
    public void setCaseStudyChoices(List list) {
        caseStudyChoices = list;
    }

    /**
     * @return
     */
    public String getMotivation() {
        return motivation;
    }

    /**
     * @param string
     */
    public void setMotivation(String string) {
        motivation = string;
    }

    public String toString() {
        String result = "[InfoCandidacy:";
        result += "Theme=" + this.theme.getIdInternal() + ";";
        result += "IdInternal=" + super.getIdInternal() + ";";
        result += "Motivation=" + this.motivation + ";";
        result += "Student=" + this.getInfoStudent().getIdInternal() + ";";
        result += "CaseStudyChoices" + this.caseStudyChoices + ";";
        result += "CurricularCourse=" + this.curricularCourse.getIdInternal() + ";";
        result += "Seminary:=" + this.infoSeminary.getIdInternal() + ";";
        result += "Modality=" + this.getInfoModality().getIdInternal() + "]";
        return result;
    }

    /**
     * @return
     */
    public String getSeminaryName() {
        return seminaryName;
    }

    /**
     * @param string
     */
    public void setSeminaryName(String string) {
        seminaryName = string;
    }

    /**
     * @return
     */
    public Boolean getApproved() {
        return approved;
    }

    /**
     * @param boolean1
     */
    public void setApproved(Boolean boolean1) {
        approved = boolean1;
    }

    public void copyFromDomain(ICandidacy candidacy) {
        super.copyFromDomain(candidacy);
        if (candidacy != null) {
            setApproved(candidacy.getApproved());
            setCurricularCourse(InfoCurricularCourseWithInfoDegree.newInfoFromDomain(candidacy
                    .getCurricularCourse()));
            setInfoModality(InfoModality.newInfoFromDomain(candidacy.getModality()));
            setMotivation(candidacy.getMotivation());
            setInfoSeminary(InfoSeminary.newInfoFromDomain(candidacy.getSeminary()));
            setInfoStudent(InfoStudentWithInfoPerson.newInfoFromDomain(candidacy.getStudent()));
            setTheme(InfoTheme.newInfoFromDomain(candidacy.getTheme()));
        }
    }

    public static InfoCandidacy newInfoFromDomain(ICandidacy candidacy) {
        InfoCandidacy infoCandidacy = null;
        if (candidacy != null) {
            infoCandidacy = new InfoCandidacy();
            infoCandidacy.copyFromDomain(candidacy);
        }
        return infoCandidacy;
    }

    /**
     * @return Returns the infoSeminary.
     */
    public InfoSeminary getInfoSeminary() {
        return infoSeminary;
    }

    /**
     * @param infoSeminary
     *            The infoSeminary to set.
     */
    public void setInfoSeminary(InfoSeminary infoSeminary) {
        this.infoSeminary = infoSeminary;
    }

    /**
     * @return Returns the infoStudent.
     */
    public InfoStudent getInfoStudent() {
        return infoStudent;
    }

    /**
     * @param infoStudent
     *            The infoStudent to set.
     */
    public void setInfoStudent(InfoStudent infoStudent) {
        this.infoStudent = infoStudent;
    }

    /**
     * @return Returns the infoModality.
     */
    public InfoModality getInfoModality() {
        return infoModality;
    }

    /**
     * @param infoModality
     *            The infoModality to set.
     */
    public void setInfoModality(InfoModality infoModality) {
        this.infoModality = infoModality;
    }

    /**
     * @return Returns the curricularCourse.
     */
    public InfoCurricularCourse getCurricularCourse() {
        return curricularCourse;
    }

    /**
     * @param curricularCourse
     *            The curricularCourse to set.
     */
    public void setCurricularCourse(InfoCurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }

    /**
     * @return Returns the theme.
     */
    public InfoTheme getTheme() {
        return theme;
    }

    /**
     * @param theme
     *            The theme to set.
     */
    public void setTheme(InfoTheme theme) {
        this.theme = theme;
    }
}