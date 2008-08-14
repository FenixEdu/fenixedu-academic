/*
 * Created on 17/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.gesdis;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.gesdis.CourseHistoric;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class InfoCourseHistoricWithInfoCurricularCourse extends InfoCourseHistoric {
    /*
     * (non-Javadoc)
     * 
     * @see
     * net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain
     * (Dominio.DomainObject)
     */
    public void copyFromDomain(CourseHistoric courseHistoric) {
	super.copyFromDomain(courseHistoric);
	if (courseHistoric != null) {
	    setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(courseHistoric.getCurricularCourse()));
	}
    }

    public static InfoCourseHistoric newInfoFromDomain(CourseHistoric courseHistoric) {
	InfoCourseHistoricWithInfoCurricularCourse infoCourseHistoric = null;
	if (courseHistoric != null) {
	    infoCourseHistoric = new InfoCourseHistoricWithInfoCurricularCourse();
	    infoCourseHistoric.copyFromDomain(courseHistoric);
	}
	return infoCourseHistoric;
    }
}