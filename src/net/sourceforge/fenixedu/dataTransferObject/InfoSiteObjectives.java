/*
 * Created on 5/Mai/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * @author João Mota
 * 
 *  
 */
public class InfoSiteObjectives extends DataTranferObject implements ISiteComponent {

    private List infoCurricularCourses;

    private List infoCurriculums;

    public int getSize() {
        return infoCurriculums.size();
    }

    /**
     * @return
     */
    public List getInfoCurricularCourses() {
        return infoCurricularCourses;
    }

    /**
     * @param infoCurricularCourses
     */
    public void setInfoCurricularCourses(List infoCurricularCourses) {
        this.infoCurricularCourses = infoCurricularCourses;
    }

    /**
     * @return
     */
    public List getInfoCurriculums() {
        return infoCurriculums;
    }

    /**
     * @param infoCurriculums
     */
    public void setInfoCurriculums(List infoCurriculums) {
        this.infoCurriculums = infoCurriculums;
    }
}