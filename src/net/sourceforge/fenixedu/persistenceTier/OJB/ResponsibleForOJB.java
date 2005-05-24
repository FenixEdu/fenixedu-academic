/*
 * Created on 26/Mar/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.ResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author João Mota
 * 
 */
public class ResponsibleForOJB extends PersistentObjectOJB implements IPersistentResponsibleFor {

    public List readByExecutionDegree(Integer degreeCurricularPlanID,
            Integer executionYearID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(
                "executionCourse.associatedCurricularCourses.degreeCurricularPlan.idInternal",
                degreeCurricularPlanID);
        criteria.addEqualTo("executionCourse.executionPeriod.executionYear.idInternal",
                executionYearID);
        return queryList(ResponsibleFor.class, criteria);
    }

    public List readByTeacherNumber(Integer teacherNumber) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.teacherNumber", teacherNumber);
        return queryList(ResponsibleFor.class, criteria);
    }
    
    public List readByTeacher(Integer teacherID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacherID);
        return queryList(ResponsibleFor.class, criteria);
    }

    public List readByExecutionCourse(Integer executionCourseID) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("executionCourse.idInternal", executionCourseID);
        return queryList(ResponsibleFor.class, crit);
    }

    public IResponsibleFor readByTeacherAndExecutionCourse(Integer teacherID, Integer executionCourseID)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacherID);
        criteria.addEqualTo("keyExecutionCourse", executionCourseID);

        return (IResponsibleFor) queryObject(ResponsibleFor.class, criteria);
    }

    public List readByTeacherAndExecutionPeriod(Integer teacherID, Integer executionPeriodID)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacherID);
        criteria.addEqualTo("executionCourse.executionPeriod.idInternal", executionPeriodID);
        return queryList(ResponsibleFor.class, criteria);
    }

    public List readByTeacherAndExecutionYear(Integer teacherID, Integer executionYearID)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacherID);
        criteria.addEqualTo("executionCourse.executionPeriod.executionYear.idInternal", executionYearID);
        return queryList(ResponsibleFor.class, criteria);
    }

    public List readByTeacherAndExecutionCourseIds(Integer teacherID, List executionCourseIds)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacherID);
        criteria.addIn("executionCourse.idInternal", executionCourseIds);
        return queryList(ResponsibleFor.class, criteria);
    }

}