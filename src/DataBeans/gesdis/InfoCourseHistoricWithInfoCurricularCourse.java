/*
 * Created on 17/Fev/2004
 *  
 */
package DataBeans.gesdis;

import DataBeans.InfoCurricularCourse;
import Dominio.gesdis.ICourseHistoric;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class InfoCourseHistoricWithInfoCurricularCourse extends InfoCourseHistoric
{
    /* (non-Javadoc)
     * @see DataBeans.InfoObject#copyFromDomain(Dominio.IDomainObject)
     */
    public void copyFromDomain(ICourseHistoric courseHistoric) {
        super.copyFromDomain(courseHistoric);
        if(courseHistoric != null) {
            setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(courseHistoric.getCurricularCourse()));
        }        
    }
    
    public static InfoCourseHistoric newInfoFromDomain(ICourseHistoric courseHistoric) {
        InfoCourseHistoricWithInfoCurricularCourse infoCourseHistoric = null;
        if(courseHistoric != null) {
            infoCourseHistoric = new InfoCourseHistoricWithInfoCurricularCourse();
            infoCourseHistoric.copyFromDomain(courseHistoric);
        }
        return infoCourseHistoric;
    }
}