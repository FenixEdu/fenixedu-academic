/*
 * Created on Dec 12, 2003 by jpvl
 *  
 */
package DataBeans.teacher.professorship;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.DataTranferObject;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoDegree;
import DataBeans.InfoProfessorship;

/**
 * @author jpvl
 */
public class DetailedProfessorship extends DataTranferObject {
    private InfoProfessorship infoProfessorship;

    private Boolean responsibleFor;

    private List executionCourseCurricularCoursesList;

    private Boolean masterDegreeOnly = Boolean.TRUE;

    /**
     * @return Returns the executionCourseCurricularCoursesList.
     */
    public List getExecutionCourseCurricularCoursesList() {
        return this.executionCourseCurricularCoursesList;
    }

    /**
     * @param executionCourseCurricularCoursesList
     *            The executionCourseCurricularCoursesList to set.
     */
    public void setExecutionCourseCurricularCoursesList(List executionCourseCurricularCoursesList) {
        this.executionCourseCurricularCoursesList = executionCourseCurricularCoursesList;
    }

    /**
     * @return Returns the infoProfessorship.
     */
    public InfoProfessorship getInfoProfessorship() {
        return this.infoProfessorship;
    }

    /**
     * @param infoProfessorship
     *            The infoProfessorship to set.
     */
    public void setInfoProfessorship(InfoProfessorship infoProfessorship) {
        this.infoProfessorship = infoProfessorship;
    }

    public List getInfoDegreeList() {

        List infoDegreeList = new ArrayList();
        Iterator iter = executionCourseCurricularCoursesList.iterator();
        while (iter.hasNext()) {
            InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iter.next();
            InfoDegree infoDegree = infoCurricularCourse.getInfoDegreeCurricularPlan().getInfoDegree();
            if (!infoDegreeList.contains(infoDegree)) {
                infoDegreeList.add(infoDegree);
            }
        }
        return infoDegreeList;
    }

    /**
     * @return Returns the responsibleFor.
     */
    public Boolean getResponsibleFor() {
        return this.responsibleFor;
    }

    /**
     * @param responsibleFor
     *            The responsibleFor to set.
     */
    public void setResponsibleFor(Boolean responsibleFor) {
        this.responsibleFor = responsibleFor;
    }

    /**
     * @return Returns the masterDegreeOnly.
     */
    public Boolean getMasterDegreeOnly() {
        return masterDegreeOnly;
    }

    /**
     * @param masterDegreeOnly
     *            The masterDegreeOnly to set.
     */
    public void setMasterDegreeOnly(Boolean masterDegreeOnly) {
        this.masterDegreeOnly = masterDegreeOnly;
    }
}