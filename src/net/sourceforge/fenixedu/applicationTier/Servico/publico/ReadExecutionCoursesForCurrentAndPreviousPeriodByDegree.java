package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseView;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * 
 * @author Luis Cruz
 */
public class ReadExecutionCoursesForCurrentAndPreviousPeriodByDegree extends Service
{

    public Object run(Integer degreeOID) throws ExcepcaoPersistencia
    {
        List executionCourseViews = new ArrayList();

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport
        		.getIPersistentExecutionPeriod();
        ICursoPersistente persistentDegree = persistentSupport.getICursoPersistente();

        ExecutionPeriod currentExecutionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
        ExecutionPeriod previouseExecutionPeriod = currentExecutionPeriod.getPreviousExecutionPeriod();

        Degree degree = (Degree) persistentDegree.readByOID(Degree.class, degreeOID);
        List<DegreeCurricularPlan> degreeCurricularPlans = degree.getDegreeCurricularPlans();

        Set processedExecutionCourses = new HashSet();

        for (DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
            if (degreeCurricularPlan.getCurricularStage().equals(CurricularStage.OLD)) {
                List curricularCourses = degreeCurricularPlan.getCurricularCourses();

                for (Iterator iteratorCC = curricularCourses.iterator(); iteratorCC.hasNext();) {
                    CurricularCourse curricularCourse = (CurricularCourse) iteratorCC.next();
                    List executionCourses = curricularCourse.getAssociatedExecutionCourses();
                   

                    for (Iterator iteratorEC = executionCourses.iterator(); iteratorEC.hasNext();) {
                        ExecutionCourse executionCourse = (ExecutionCourse) iteratorEC.next();
                        ExecutionPeriod executionPeriodFromExecutionCourse = executionCourse.getExecutionPeriod();
                       
                        if (executionPeriodFromExecutionCourse.getIdInternal().equals(currentExecutionPeriod.getIdInternal())
                                || (previouseExecutionPeriod != null && executionPeriodFromExecutionCourse.getIdInternal().equals(previouseExecutionPeriod.getIdInternal()))) {
                            for (Iterator iteratorCCS = curricularCourse.getScopes().iterator(); iteratorCCS.hasNext();) {
                                CurricularCourseScope curricularCourseScope = (CurricularCourseScope) iteratorCCS.next();

                                String key = generateExecutionCourseKey(executionCourse, curricularCourseScope);

                                if (!processedExecutionCourses.contains(key)) {
                                    ExecutionCourseView executionCourseView = new ExecutionCourseView();
                                    executionCourseView.setExecutionCourseOID(executionCourse.getIdInternal());
                                    executionCourseView.setExecutionCourseName(executionCourse.getNome());
                                    executionCourseView.setSemester(executionCourse.getExecutionPeriod().getSemester());                        
                                    executionCourseView.setCurricularYear(curricularCourseScope.getCurricularSemester().getCurricularYear().getYear());
                                    executionCourseView.setExecutionPeriodOID(executionCourse.getExecutionPeriod().getIdInternal());
                                    executionCourseView.setAnotation(curricularCourseScope.getAnotation());
                                    executionCourseView.setDegreeCurricularPlanAnotation(degreeCurricularPlan.getAnotation());
                                    executionCourseViews.add(executionCourseView);
                                    processedExecutionCourses.add(key);
                                }
                            }
                        }
                    }
                }                
            }
        }

        return executionCourseViews;

    }

    private String generateExecutionCourseKey(ExecutionCourse executionCourse, CurricularCourseScope curricularCourseScope) {
        StringBuilder key = new StringBuilder();

        key.append(curricularCourseScope.getCurricularSemester().getCurricularYear().getYear());
        key.append('-');
        key.append(executionCourse.getIdInternal());

        return key.toString();
    }

}