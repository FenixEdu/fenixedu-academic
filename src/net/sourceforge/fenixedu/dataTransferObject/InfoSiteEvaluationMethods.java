/*
 * Created on 4/Ago/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * @author João Mota
 * 
 * 4/Ago/2003 fenix-head DataBeans
 *  
 */
public class InfoSiteEvaluationMethods extends DataTranferObject implements ISiteComponent {
    private InfoEvaluationMethod infoEvaluationMethod;

    private List infoEvaluations; //unnecessary

    private List infoCurricularCourses; //unnecessary

    public int getSize() {
        return infoEvaluations.size();
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
    public List getInfoEvaluations() {
        return infoEvaluations;
    }

    /**
     * @param infoEvaluations
     */
    public void setInfoEvaluations(List infoEvaluations) {
        this.infoEvaluations = infoEvaluations;
    }

    public InfoEvaluationMethod getInfoEvaluationMethod() {
        return infoEvaluationMethod;
    }

    public void setInfoEvaluationMethod(InfoEvaluationMethod infoEvaluationMethod) {
        this.infoEvaluationMethod = infoEvaluationMethod;
    }

}