/*
 * Created on Jul 28, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * @author João Mota
 *  
 */
public class InfoCurricularCourseGroupWithCoursesToAdd extends InfoObject {

    private InfoCurricularCourseGroup infoCurricularCourseGroup;

    private List infoCurricularCourses;

    private List infoCurricularCoursesToAdd;

    public InfoCurricularCourseGroup getInfoCurricularCourseGroup() {
        return infoCurricularCourseGroup;
    }

    public void setInfoCurricularCourseGroup(InfoCurricularCourseGroup infoCurricularCourseGroup) {
        this.infoCurricularCourseGroup = infoCurricularCourseGroup;
    }

    public List getInfoCurricularCourses() {
        return infoCurricularCourses;
    }

    public void setInfoCurricularCourses(List infoCurricularCourses) {
        this.infoCurricularCourses = infoCurricularCourses;
    }

    public List getInfoCurricularCoursesToAdd() {
        return infoCurricularCoursesToAdd;
    }

    public void setInfoCurricularCoursesToAdd(List infoCurricularCoursesToAdd) {
        this.infoCurricularCoursesToAdd = infoCurricularCoursesToAdd;
    }

}