/*
 * Created on 29/Jun/2004
 *
 */
package DataBeans;

import Dominio.ICurricularCourse;

/**
 * @author Tânia Pousão
 *
 */
public class InfoCurricularCourseWithInfoDegreeCurricularPlan extends
        InfoCurricularCourse {

    /* (non-Javadoc)
     * @see DataBeans.InfoCurricularCourse#copyFromDomain(Dominio.ICurricularCourse)
     */
    public void copyFromDomain(ICurricularCourse curricularCourse) {
        super.copyFromDomain(curricularCourse);
        if(curricularCourse != null) {
            setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan.newInfoFromDomain(curricularCourse.getDegreeCurricularPlan()));
        }
    }
    
    public static InfoCurricularCourse newInfoFromDomain(ICurricularCourse curricularCourse) {
        InfoCurricularCourseWithInfoDegreeCurricularPlan infoCurricularCourse = null;
        if(curricularCourse != null) {
            infoCurricularCourse = new InfoCurricularCourseWithInfoDegreeCurricularPlan();
            infoCurricularCourse.copyFromDomain(curricularCourse);
        }
        
        return infoCurricularCourse;
    }    
}
