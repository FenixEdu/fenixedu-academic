/*
 * Created on 1/Ago/2003, 21:13:05
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.dataTransferObject.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.Seminaries.ICourseEquivalency;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 1/Ago/2003, 21:13:05
 *  
 */
public class InfoEquivalency extends InfoObject {

    private InfoCurricularCourse curricularCourse;

    private InfoModality modality;

    private String seminaryName;

    private Boolean hasTheme;

    private Boolean hasCaseStudy;

    private List themes;

    private InfoSeminary infoSeminary;

    /**
     * @return
     */
    public InfoCurricularCourse getCurricularCourse() {
        return curricularCourse;
    }

    /**
     * @return
     */
    public InfoModality getModality() {
        return modality;
    }

    /**
     * @return
     */
    public Integer getSeminaryIdInternal() {
        return getInfoSeminary().getIdInternal();
    }

    /**
     * @param course
     */
    public void setCurricularCourse(InfoCurricularCourse course) {
        curricularCourse = course;
    }

    /**
     * @param modality
     */
    public void setModality(InfoModality modality) {
        this.modality = modality;
    }

    public String toString() {
        String result = "[InfoEquivalency:";
        result += "IdInternal: " + this.getIdInternal() + ";";
        result += "CurricularCourse" + this.getCurricularCourse() + ";";
        result += "CurricularCourseInternal " + this.getCurricularCourse().getIdInternal() + ";";
        result += "Seminary:" + this.getSeminaryIdInternal() + ";";
        result += "Modality:" + this.getModality() + "]";
        return result;
    }

    /**
     * @return
     */
    public List getThemes() {
        return themes;
    }

    /**
     * @param list
     */
    public void setThemes(List list) {
        themes = list;
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
    public Boolean getHasCaseStudy() {
        return hasCaseStudy;
    }

    /**
     * @return
     */
    public Boolean getHasTheme() {
        return hasTheme;
    }

    /**
     * @param boolean1
     */
    public void setHasCaseStudy(Boolean boolean1) {
        hasCaseStudy = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setHasTheme(Boolean boolean1) {
        hasTheme = boolean1;
    }

    public void copyFromDomain(ICourseEquivalency courseEquivalency) {
        super.copyFromDomain(courseEquivalency);
        if (courseEquivalency != null) {
            setCurricularCourse(InfoCurricularCourse.newInfoFromDomain(courseEquivalency
                    .getCurricularCourse()));
            setModality(InfoModality.newInfoFromDomain(courseEquivalency.getModality()));

            if (courseEquivalency.getSeminary() != null) {
                setInfoSeminary(InfoSeminary.newInfoFromDomain(courseEquivalency.getSeminary()));
                setSeminaryName(courseEquivalency.getSeminary().getName());
                setHasTheme(courseEquivalency.getSeminary().getHasTheme());
                setHasCaseStudy(courseEquivalency.getSeminary().getHasCaseStudy());
            }
        }
    }

    public static InfoEquivalency newInfoFromDomain(ICourseEquivalency courseEquivalency) {
        InfoEquivalency infoEquivalency = null;
        if (courseEquivalency != null) {
            infoEquivalency = new InfoEquivalency();
            infoEquivalency.copyFromDomain(courseEquivalency);
        }
        return infoEquivalency;
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
}