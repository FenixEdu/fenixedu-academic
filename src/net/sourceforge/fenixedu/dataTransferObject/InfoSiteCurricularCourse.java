/*
 * Created on 5/Mai/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author João Mota
 * 
 *  
 */
public class InfoSiteCurricularCourse extends DataTranferObject implements ISiteComponent {

    private InfoCurriculum infoCurriculum;

    private InfoCurricularCourse infoCurricularCourse;

    /**
     * @return
     */
    public InfoCurricularCourse getInfoCurricularCourse() {
        return infoCurricularCourse;
    }

    /**
     * @param infoCurricularCourse
     */
    public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
        this.infoCurricularCourse = infoCurricularCourse;
    }

    /**
     * @return
     */
    public InfoCurriculum getInfoCurriculum() {
        return infoCurriculum;
    }

    /**
     * @param infoCurriculum
     */
    public void setInfoCurriculum(InfoCurriculum infoCurriculum) {
        this.infoCurriculum = infoCurriculum;
    }

}