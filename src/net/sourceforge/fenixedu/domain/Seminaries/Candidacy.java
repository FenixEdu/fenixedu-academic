/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IStudent;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at Jul 23, 2003, 9:49:19 AM
 *  
 */
public class Candidacy extends DomainObject implements ICandidacy {

    private List caseStudyChoices;

    private String motivation;

    private ISeminary seminary;

    private Integer themeIdInternal;

    private ITheme theme;

    private Integer modalityIdInternal;

    private IModality modality;

    private Integer seminaryIdInternal;

    private Integer studentIdInternal;

    private IStudent student;

    private Integer curricularCourseIdInternal;

    private ICurricularCourse curricularCourse;

    private Boolean approved;

    public Candidacy() {

    }

    public String toString() {
        String result = "[Candidacy:";
        result += "Theme=" + this.themeIdInternal + ";";
        result += "Motivation=" + this.motivation + ";";
        result += "Student=" + this.studentIdInternal + ";";
        result += "CaseStudyChoices" + this.caseStudyChoices + ";";
        result += "CurricularCourse=" + this.curricularCourseIdInternal + ";";
        result += "Seminary:=" + this.seminaryIdInternal + ";";
        result += "Modality=" + this.modalityIdInternal + "]";

        return result;
    }

    /**
     * @return
     */
    public List getCaseStudyChoices() {
        return caseStudyChoices;
    }

    /**
     * @return
     */
    public Integer getCurricularCourseIdInternal() {
        return curricularCourseIdInternal;
    }

    /**
     * @return
     */
    public Integer getModalityIdInternal() {
        return modalityIdInternal;
    }

    /**
     * @return
     */
    public Integer getSeminaryIdInternal() {
        return seminaryIdInternal;
    }

    /**
     * @return
     */
    public Integer getStudentIdInternal() {
        return studentIdInternal;
    }

    /**
     * @return
     */
    public Integer getThemeIdInternal() {
        return themeIdInternal;
    }

    /**
     * @param list
     */
    public void setCaseStudyChoices(List list) {
        caseStudyChoices = list;
    }

    /**
     * @param integer
     */
    public void setCurricularCourseIdInternal(Integer integer) {
        curricularCourseIdInternal = integer;
    }

    /**
     * @param integer
     */
    public void setModalityIdInternal(Integer integer) {
        modalityIdInternal = integer;
    }

    /**
     * @param integer
     */
    public void setSeminaryIdInternal(Integer integer) {
        seminaryIdInternal = integer;
    }

    /**
     * @param integer
     */
    public void setStudentIdInternal(Integer integer) {
        studentIdInternal = integer;
    }

    /**
     * @param integer
     */
    public void setThemeIdInternal(Integer integer) {
        themeIdInternal = integer;
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

    /**
     * @return Returns the seminary.
     */
    public ISeminary getSeminary() {
        return seminary;
    }

    /**
     * @param seminary
     *            The seminary to set.
     */
    public void setSeminary(ISeminary seminary) {
        this.seminary = seminary;
    }

    /**
     * @return Returns the student.
     */
    public IStudent getStudent() {
        return student;
    }

    /**
     * @param student
     *            The student to set.
     */
    public void setStudent(IStudent student) {
        this.student = student;
    }

    /**
     * @return Returns the modality.
     */
    public IModality getModality() {
        return modality;
    }

    /**
     * @param modality
     *            The modality to set.
     */
    public void setModality(IModality modality) {
        this.modality = modality;
    }

    /**
     * @return Returns the curricularCourse.
     */
    public ICurricularCourse getCurricularCourse() {
        return curricularCourse;
    }

    /**
     * @param curricularCourse
     *            The curricularCourse to set.
     */
    public void setCurricularCourse(ICurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }

    /**
     * @return Returns the theme.
     */
    public ITheme getTheme() {
        return theme;
    }

    /**
     * @param theme
     *            The theme to set.
     */
    public void setTheme(ITheme theme) {
        this.theme = theme;
    }
}