package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberOfAcumulatedEnrollmentsRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberOfCurricularCoursesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PrecedencesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PreviousYearsCurricularCourseEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.SpecificLEICEnrollmentRule;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.AreaType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author David Santos in Jun 25, 2004
 */

public class DegreeCurricularPlanLEIC extends DegreeCurricularPlan implements IDegreeCurricularPlan {

    public DegreeCurricularPlanLEIC() {
        ojbConcreteClass = getClass().getName();
    }

    public List getSpecialListOfCurricularCourses() {

        List allDegreeCurricularPlans = this.getDegree().getDegreeCurricularPlans();

        IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) CollectionUtils.find(
                allDegreeCurricularPlans, new Predicate() {
                    public boolean evaluate(Object obj) {
                        IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) obj;
                        return degreeCurricularPlan.getName().equals("LEIC - Currículo Antigo");
                    }
                });

        List curricularCourses = new ArrayList();

        int curricularCoursesSize = degreeCurricularPlan.getCurricularCourses().size();
        for (int i = 0; i < curricularCoursesSize; i++) {
            ICurricularCourse curricularCourse = (ICurricularCourse) degreeCurricularPlan
                    .getCurricularCourses().get(i);

            int curricularCourseScopesSize = curricularCourse.getScopes().size();
            for (int j = 0; j < curricularCourseScopesSize; j++) {
                ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) curricularCourse
                        .getScopes().get(j);
                if ((curricularCourseScope.getCurricularSemester().getCurricularYear().getYear()
                        .intValue() == 5)
                        && (!curricularCourses.contains(curricularCourse))) {
                    curricularCourses.add(curricularCourse);
                }
            }
        }

        return curricularCourses;
    }
    
    public List getListOfEnrollmentRules(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {

        List result = new ArrayList();

        //result.add(new SecretaryEnrollmentRule(studentCurricularPlan));
        result.add(new MaximumNumberOfAcumulatedEnrollmentsRule(studentCurricularPlan, executionPeriod));
        result.add(new MaximumNumberOfCurricularCoursesEnrollmentRule(studentCurricularPlan,
                executionPeriod));
        result.add(new PrecedencesEnrollmentRule(studentCurricularPlan, executionPeriod));
        result.add(new PreviousYearsCurricularCourseEnrollmentRule(studentCurricularPlan,
                executionPeriod));
        result.add(new SpecificLEICEnrollmentRule(studentCurricularPlan, executionPeriod));

        return result;
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
    
    public List getSecundaryAreas() {
        List sec = super.getSecundaryAreas();
        sec.addAll(getSpecializationAreas());
        return sec;
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
}