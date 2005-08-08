/**
 * 
 */
package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Ricardo Rodrigues
 *
 */

public class LETOptionalExclusiveEnrollmentRule implements IEnrollmentRule {
    
    private static final String COMPLEMENTOS_INVESTIGACAO_OPERACIONAL_CODE = "AFG";
    private static final String AVALIACAO_PROJECTOS_DECISAO_PUBLICA_CODE = "ON";

    public LETOptionalExclusiveEnrollmentRule() {
    }

    public List apply(List curricularCoursesToBeEnrolledIn) {
        
        if(curricularCourseExists(curricularCoursesToBeEnrolledIn,COMPLEMENTOS_INVESTIGACAO_OPERACIONAL_CODE)){
            removeCurricularCourse(curricularCoursesToBeEnrolledIn,AVALIACAO_PROJECTOS_DECISAO_PUBLICA_CODE);
        }
        else
            if(curricularCourseExists(curricularCoursesToBeEnrolledIn,AVALIACAO_PROJECTOS_DECISAO_PUBLICA_CODE)){
                removeCurricularCourse(curricularCoursesToBeEnrolledIn,COMPLEMENTOS_INVESTIGACAO_OPERACIONAL_CODE);
            }
        return curricularCoursesToBeEnrolledIn;        
    }

    private void removeCurricularCourse(List curricularCourses2Enroll, final String curricular_corse_code) {
        CollectionUtils.filter(curricularCourses2Enroll, new Predicate(){

            public boolean evaluate(Object arg0) {
                CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) arg0;
                return curricularCourse2Enroll.getCurricularCourse().getCode().equals(curricular_corse_code);
            }});        
    }

    private boolean curricularCourseExists(List curricularCourses2Enroll, final String curricular_corse_code) {
       return CollectionUtils.exists(curricularCourses2Enroll, new Predicate(){

            public boolean evaluate(Object arg0) {
                CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) arg0;
                return curricularCourse2Enroll.getCurricularCourse().getCode().equals(curricular_corse_code);
            }});
    }
}


