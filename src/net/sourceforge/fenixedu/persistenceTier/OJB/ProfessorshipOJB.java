/*
 * Created on 26/Mar/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author João Mota
 * 
 *  
 */
public class ProfessorshipOJB extends PersistentObjectOJB implements IPersistentProfessorship {

    public IProfessorship readByTeacherAndExecutionCourse(Integer teacherID, Integer executionCourseID)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacherID);
        criteria.addEqualTo("keyExecutionCourse", executionCourseID);
        return (IProfessorship) queryObject(Professorship.class, criteria);
    }

    public List readByTeacherNumber(Integer teacherNumber) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.teacherNumber", teacherNumber);
        return queryList(Professorship.class, criteria);
    }
    
    public List readByTeacher(Integer teacherID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacherID);
        return queryList(Professorship.class, criteria);
    }

    public List readByExecutionCourse(Integer executionCourseID) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("executionCourse.idInternal", executionCourseID);
        return queryList(Professorship.class, crit);
    }

    public List readByTeacherAndExecutionPeriod(Integer teacherID, Integer executionPeriodID)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacherID);
        criteria.addEqualTo("executionCourse.executionPeriod.idInternal", executionPeriodID);
        return queryList(Professorship.class, criteria);
    }

    public List readByDegreeCurricularPlansAndExecutionYearAndBasic(List degreeCurricularPlanIDs,
            Integer executionYearID, Boolean basic) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addIn("executionCourse.associatedCurricularCourses.degreeCurricularPlan.idInternal",
                degreeCurricularPlanIDs);
        if (executionYearID != null) {
            criteria.addEqualTo("executionCourse.executionPeriod.executionYear.idInternal",
                    executionYearID);
        }
        criteria.addEqualTo("executionCourse.associatedCurricularCourses.basic", basic);
        return queryList(Professorship.class, criteria, true);
    }

    public List readByDegreeCurricularPlanAndExecutionYear(Integer degreeCurricularPlanID,
            Integer executionYearID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(
                "executionCourse.associatedCurricularCourses.degreeCurricularPlan.idInternal",
                degreeCurricularPlanID);
        criteria.addEqualTo("executionCourse.executionPeriod.executionYear.idInternal", executionYearID);
        return queryList(Professorship.class, criteria, true);
    }

    public List readByDegreeCurricularPlanAndExecutionPeriod(Integer degreeCurricularPlanID,
            Integer executionPeriodID) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo(
                "executionCourse.associatedCurricularCourses.degreeCurricularPlan.idInternal",
                degreeCurricularPlanID);
        criteria.addEqualTo("executionCourse.executionPeriod.idInternal", executionPeriodID);

        return queryList(Professorship.class, criteria, true);
    }

    public List readByDegreeCurricularPlanAndBasic(Integer degreeCurricularPlanID,
            Integer executionYearID, Boolean basic) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(
                "executionCourse.associatedCurricularCourses.degreeCurricularPlan.idInternal",
                degreeCurricularPlanID);
        criteria.addEqualTo("executionCourse.executionPeriod.executionYear.idInternal", executionYearID);
        criteria.addEqualTo("executionCourse.associatedCurricularCourses.basic", basic);
        return queryList(Professorship.class, criteria, true);
    }

    public List readByTeacherAndExecutionYear(Integer teacherID, Integer executionYearID)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacherID);
        criteria.addEqualTo("executionCourse.executionPeriod.executionYear.idInternal", executionYearID);
        return queryList(Professorship.class, criteria);
    }

    public List readByDegreeCurricularPlansAndExecutionYear(List degreeCurricularPlanIDs,
            Integer executionYearID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addIn("executionCourse.associatedCurricularCourses.degreeCurricularPlan.idInternal",
                degreeCurricularPlanIDs);
        if (executionYearID != null) {
            criteria.addEqualTo("executionCourse.executionPeriod.executionYear.idInternal",
                    executionYearID);
        }
        return queryList(Professorship.class, criteria, true);
    }
}