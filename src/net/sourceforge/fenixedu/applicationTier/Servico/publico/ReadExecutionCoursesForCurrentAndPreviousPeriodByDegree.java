package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseView;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Luis Cruz
 */
public class ReadExecutionCoursesForCurrentAndPreviousPeriodByDegree implements IService
{

    public Object run(Integer degreeOID) throws FenixServiceException, ExcepcaoPersistencia
    {
        List executionCourseViews = new ArrayList();

        ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport
        		.getIPersistentExecutionPeriod();
        ICursoPersistente persistentDegree = persistentSupport.getICursoPersistente();

        IExecutionPeriod currentExecutionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
        IExecutionPeriod previouseExecutionPeriod = currentExecutionPeriod.getPreviousExecutionPeriod();

        IDegree degree = (IDegree) persistentDegree.readByOID(Degree.class, degreeOID);
        List degreeCurricularPlans = degree.getDegreeCurricularPlans();

        Set processedExecutionCourses = new HashSet();

        for (Iterator iteratorDCP = degreeCurricularPlans.iterator(); iteratorDCP.hasNext();) {
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iteratorDCP.next();
            List curricularCourses = degreeCurricularPlan.getCurricularCourses();

            for (Iterator iteratorCC = curricularCourses.iterator(); iteratorCC.hasNext();) {
                ICurricularCourse curricularCourse = (ICurricularCourse) iteratorCC.next();
                List executionCourses = curricularCourse.getAssociatedExecutionCourses();

                for (Iterator iteratorEC = executionCourses.iterator(); iteratorEC.hasNext();) {
                    IExecutionCourse executionCourse = (IExecutionCourse) iteratorEC.next();
                    IExecutionPeriod executionPeriodFromExecutionCourse = executionCourse.getExecutionPeriod();

                    if (executionPeriodFromExecutionCourse.getIdInternal().equals(currentExecutionPeriod.getIdInternal())
                            || executionPeriodFromExecutionCourse.getIdInternal().equals(previouseExecutionPeriod.getIdInternal())) {
                        for (Iterator iteratorCCS = curricularCourse.getScopes().iterator(); iteratorCCS.hasNext();) {
                            ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iteratorCCS.next();

                            String key = generateExecutionCourseKey(executionCourse, curricularCourseScope);

                            if (!processedExecutionCourses.contains(key)) {
                                ExecutionCourseView executionCourseView = new ExecutionCourseView();
                                executionCourseView.setExecutionCourseOID(executionCourse.getIdInternal());
                                executionCourseView.setExecutionCourseName(executionCourse.getNome());
                                executionCourseView.setSemester(executionCourse.getExecutionPeriod().getSemester());                        
                                executionCourseView.setCurricularYear(curricularCourseScope.getCurricularSemester().getCurricularYear().getYear());
                                executionCourseView.setExecutionPeriodOID(executionCourse.getExecutionPeriod().getIdInternal());
                                
                                executionCourseViews.add(executionCourseView);
                                processedExecutionCourses.add(key);
                            }
                        }
                    }
                }
            }
        }

        return executionCourseViews;

    }

    private String generateExecutionCourseKey(IExecutionCourse executionCourse, ICurricularCourseScope curricularCourseScope) {
        StringBuffer key = new StringBuffer();

        key.append(curricularCourseScope.getCurricularSemester().getCurricularYear().getYear());
        key.append('-');
        key.append(executionCourse.getIdInternal());

        return key.toString();
    }

}