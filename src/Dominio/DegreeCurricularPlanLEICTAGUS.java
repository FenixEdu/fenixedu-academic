package Dominio;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import Dominio.degree.enrollment.rules.MaximumNumberOfAcumulatedEnrollmentsRule;
import Dominio.degree.enrollment.rules.MaximumNumberOfCurricularCoursesEnrollmentRule;
import Dominio.degree.enrollment.rules.PrecedencesEnrollmentRule;
import Dominio.degree.enrollment.rules.PreviousYearsCurricularCourseEnrollmentRule;
import Dominio.degree.enrollment.rules.SecretaryEnrollmentRule;
import Dominio.degree.enrollment.rules.SpecificLEICEnrollmentRule;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.AreaType;

/**
 * @author João Mota in Aug 10, 2004
 */

public class DegreeCurricularPlanLEICTAGUS extends DegreeCurricularPlan implements IDegreeCurricularPlan {

    public DegreeCurricularPlanLEICTAGUS() {
        ojbConcreteClass = getClass().getName();
    }

    public List getCurricularCoursesFromArea(IBranch area, AreaType areaType) {

        List curricularCourses = new ArrayList();

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport
                    .getIPersistentCurricularCourseGroup();

            List groups = curricularCourseGroupDAO.readByBranchAndAreaType(area, areaType);

            int groupsSize = groups.size();

            for (int i = 0; i < groupsSize; i++) {
                ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) groups.get(i);

                List courses = curricularCourseGroup.getCurricularCourses();

                int coursesSize = courses.size();

                for (int j = 0; j < coursesSize; j++) {
                    ICurricularCourse curricularCourse = (ICurricularCourse) courses.get(j);

                    if (!curricularCourses.contains(curricularCourse)) {
                        curricularCourses.add(curricularCourse);
                    }
                }
            }

        } catch (ExcepcaoPersistencia e) {
            throw new RuntimeException(e);
        }

        return curricularCourses;
    }
    
    public List getCurricularCoursesFromAnyArea() {
        Set curricularCourses = new HashSet();
        
        List specializationAreas = getSpecializationAreas();
        List secundaryAreas = getSecundaryAreas();
        
        for (Iterator iter = specializationAreas.iterator(); iter.hasNext();) {
            IBranch branch = (IBranch) iter.next();
            curricularCourses.addAll(getCurricularCoursesFromArea(branch, AreaType.SPECIALIZATION_OBJ));
        }
        
        for (Iterator iter = secundaryAreas.iterator(); iter.hasNext();) {
            IBranch branch = (IBranch) iter.next();
            curricularCourses.addAll(getCurricularCoursesFromArea(branch, AreaType.SECONDARY_OBJ));
        }

        List result = new ArrayList();
        result.addAll(curricularCourses);
        return result;
    }

    public List getSecundaryAreas() {
        return getSpecializationAreas();
    }
    
    public List getListOfEnrollmentRules(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {

        List result = new ArrayList();

        result.add(new SecretaryEnrollmentRule(studentCurricularPlan));
        result.add(new MaximumNumberOfAcumulatedEnrollmentsRule(studentCurricularPlan, executionPeriod));
        result.add(new MaximumNumberOfCurricularCoursesEnrollmentRule(studentCurricularPlan,
                executionPeriod));
        result.add(new PrecedencesEnrollmentRule(studentCurricularPlan, executionPeriod));
        result.add(new PreviousYearsCurricularCourseEnrollmentRule(studentCurricularPlan,
                executionPeriod));
        result.add(new SpecificLEICEnrollmentRule(studentCurricularPlan, executionPeriod));

        return result;
    }

}