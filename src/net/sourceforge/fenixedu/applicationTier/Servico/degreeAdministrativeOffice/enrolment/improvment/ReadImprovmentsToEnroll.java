/*
 * Created on Nov 18, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.improvment;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.InfoImprovmentEnrolmentContext;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author nmgo
 */
public class ReadImprovmentsToEnroll implements IService  {

    public Object run(Integer studentNumber) throws FenixServiceException{
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            List previousExecPeriodAprovedEnrol = new ArrayList();
            List beforePreviousExecPeriodAprovedEnrol = new ArrayList();
            List beforeBeforePreviousExecPeriodAprovedEnrol = new ArrayList();
            //Read Execution Periods
            IExecutionPeriod actualExecPeriod = sp
                    .getIPersistentExecutionPeriod()
                    .readActualExecutionPeriod();
            IExecutionPeriod previousExecPeriod = actualExecPeriod
                    .getPreviousExecutionPeriod();
            IExecutionPeriod beforePreviousExecPeriod = previousExecPeriod
                    .getPreviousExecutionPeriod();
            IExecutionPeriod beforeBeforePreviousExecPeriod = beforePreviousExecPeriod.getPreviousExecutionPeriod();

            //Read Student
            IStudent student = sp.getIPersistentStudent()
                    .readStudentByNumberAndDegreeType(studentNumber, DegreeType.DEGREE);
            
            if(student == null) {
                throw new InvalidArgumentsServiceException("error.student.notExist");
            }
            //Read Aproved Enrolments by Execution Period
            List studentCurricularPlans = student.getStudentCurricularPlans();

            Iterator iterator = studentCurricularPlans.iterator();
            while (iterator.hasNext()) {
                IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator
                        .next();

                if (previousExecPeriod != null) {
                    previousExecPeriodAprovedEnrol.addAll(studentCurricularPlan.getAprovedEnrolmentsInExecutionPeriod(previousExecPeriod));
                }

                if (beforePreviousExecPeriod != null) {
                    beforePreviousExecPeriodAprovedEnrol.addAll(studentCurricularPlan.getAprovedEnrolmentsInExecutionPeriod(beforePreviousExecPeriod));
                }
                
                if (beforeBeforePreviousExecPeriod != null) {
                    beforeBeforePreviousExecPeriodAprovedEnrol.addAll(studentCurricularPlan.getAprovedEnrolmentsInExecutionPeriod(beforeBeforePreviousExecPeriod));
                }
            }

            //Remove Enrolments From Equivalences
            removeEquivalenceEnrolment(previousExecPeriodAprovedEnrol);
            removeEquivalenceEnrolment(beforePreviousExecPeriodAprovedEnrol);
            removeEquivalenceEnrolment(beforeBeforePreviousExecPeriodAprovedEnrol);
            
            //Remove Enrolments Already Improved and get Improvment Enrolments of this Execution Period
            List alreadyImprovedEnrolmentsInCurrentExecutionPeriod = new ArrayList(); 
            alreadyImprovedEnrolmentsInCurrentExecutionPeriod.addAll(removeImprovedEnrolmentAndGetImprovmentsOfCurrentPeriod(actualExecPeriod, beforePreviousExecPeriodAprovedEnrol));
            alreadyImprovedEnrolmentsInCurrentExecutionPeriod.addAll(removeImprovedEnrolmentAndGetImprovmentsOfCurrentPeriod(actualExecPeriod, previousExecPeriodAprovedEnrol));
            alreadyImprovedEnrolmentsInCurrentExecutionPeriod.addAll(removeImprovedEnrolmentAndGetImprovmentsOfCurrentPeriod(actualExecPeriod, beforeBeforePreviousExecPeriodAprovedEnrol));

            //From Before Before Previous Period remove the ones with scope in Previous Period
            removeFromBeforeBeforePreviousPeriod(beforeBeforePreviousExecPeriodAprovedEnrol, previousExecPeriod);
            
            //From previous Period remove the ones that not take place in the
            // Current Period
            previousExecPeriodAprovedEnrol = removeNotInCurrentExecutionPeriod(previousExecPeriodAprovedEnrol, actualExecPeriod);
            
            List res = (List) CollectionUtils.union(beforePreviousExecPeriodAprovedEnrol, previousExecPeriodAprovedEnrol);
            
            res = (List) CollectionUtils.union(beforeBeforePreviousExecPeriodAprovedEnrol, res);
            
            return buildResult(student, actualExecPeriod, res, alreadyImprovedEnrolmentsInCurrentExecutionPeriod);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    private void removeFromBeforeBeforePreviousPeriod(List beforeBeforePreviousExecPeriodAprovedEnrol, final IExecutionPeriod previousExecPeriod) {
        CollectionUtils.filter(beforeBeforePreviousExecPeriodAprovedEnrol, new Predicate () {

            public boolean evaluate(Object arg0) {
                IEnrolment enrolment = (IEnrolment) arg0;
                List executionCourses = enrolment.getCurricularCourse().getAssociatedExecutionCourses();
                for(Iterator iterator = executionCourses.iterator(); iterator.hasNext();) {
                    IExecutionCourse executionCourse = (IExecutionCourse) iterator.next();
                    if(executionCourse.getExecutionPeriod().equals(previousExecPeriod)) {
                        return false;
                    }
                }
                return true;
            }
        
        });
        
    }

    /**
     * @param actualExecPeriod
     * @param previousExecPeriodAprovedEnrol
     * @return
     */
    private List removeImprovedEnrolmentAndGetImprovmentsOfCurrentPeriod(IExecutionPeriod actualExecPeriod, List enrolments) {
        List improvments = (List) CollectionUtils.select(enrolments, new Predicate() {

            public boolean evaluate(Object arg0) {
                IEnrolment enrollment = (IEnrolment) arg0;
                if(CollectionUtils.find(enrollment.getEvaluations(), new Predicate() {

                    public boolean evaluate(Object arg0) {
                        IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) arg0;
                        if(enrolmentEvaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.IMPROVEMENT))
                            return true;
                        return false;
                    }
                    
                })!= null)
                    return true;
                return false;
            }
        });
        enrolments.removeAll(improvments);
        
        
        return (List) CollectionUtils.select(improvments, new Predicate() {

            public boolean evaluate(Object arg0) {
                IEnrolment enrollment = (IEnrolment) arg0;
                IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) CollectionUtils.find(enrollment.getEvaluations(), new Predicate() {

                    public boolean evaluate(Object arg0) {
                        IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) arg0;
                        if(enrolmentEvaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.IMPROVEMENT))
                            return true;
                        return false;
                    }
                    
                });
                
                if(enrolmentEvaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.TEMPORARY_OBJ))
                    return true;
                return false;
            }
            
        });
    }

    /**
     * @param student
     * @param actualExecPeriod
     * @param res
     * @param alreadyImprovedEnrolmentsInCurrentExecutionPeriod
     * @return
     */
    private InfoImprovmentEnrolmentContext buildResult(IStudent student, IExecutionPeriod actualExecPeriod, List res, List alreadyImprovedEnrolmentsInCurrentExecutionPeriod) {
        InfoImprovmentEnrolmentContext improvmentEnrolmentContext = new InfoImprovmentEnrolmentContext();
        improvmentEnrolmentContext.setInfoStudent(InfoStudentWithInfoPerson.newInfoFromDomain(student));
        improvmentEnrolmentContext.setInfoExecutionPeriod(InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(actualExecPeriod));
        
        improvmentEnrolmentContext.setImprovmentsToEnroll((List) CollectionUtils.collect(res, new Transformer() {

            public Object transform(Object arg0) {
                IEnrolment enrollment = (IEnrolment) arg0;
                return InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear.newInfoFromDomain(enrollment);
            }
            
        }));
        
        improvmentEnrolmentContext.setAlreadyEnrolled((List) CollectionUtils.collect(alreadyImprovedEnrolmentsInCurrentExecutionPeriod, new Transformer() {

            public Object transform(Object arg0) {
                IEnrolment enrollment = (IEnrolment) arg0;
                return InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear.newInfoFromDomain(enrollment);
            }
            
        }));
        
        return improvmentEnrolmentContext;
    }

    private void removeEquivalenceEnrolment(List enrolments) {
        CollectionUtils.filter(enrolments, new Predicate() {
            public boolean evaluate(Object obj) {
                IEnrolment enrollment = (IEnrolment) obj;
                if (enrollment.getEnrolmentEvaluationType().equals(
                        EnrolmentEvaluationType.EQUIVALENCE))
                    return false;
                return true;
            }
        });
    }
    
    private List removeNotInCurrentExecutionPeriod(List enrolments, final IExecutionPeriod currentExecutionPeriod) throws ExcepcaoPersistencia{
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentCurricularCourseScope persistentCurricularCourseScope = sp.getIPersistentCurricularCourseScope();
        List res = new ArrayList();
        Iterator iterator = enrolments.iterator();
        while(iterator.hasNext()) {
           IEnrolment enrolment = (IEnrolment) iterator.next();

           List scopes = persistentCurricularCourseScope.readCurricularCourseScopesByCurricularCourseInExecutionPeriod(enrolment.getCurricularCourse().getIdInternal(), currentExecutionPeriod.getBeginDate(), currentExecutionPeriod.getEndDate());
           if(scopes != null && !scopes.isEmpty()) {
               ICurricularCourseScope curricularCourseScope =  (ICurricularCourseScope) CollectionUtils.find(scopes, new Predicate() {

                public boolean evaluate(Object arg0) {
                    ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
                    if(curricularCourseScope.getCurricularSemester().getSemester().equals(currentExecutionPeriod.getSemester())
                            && (curricularCourseScope.getEndDate() == null || (curricularCourseScope.getEnd().compareTo(new Date())) >= 0))
                        return true;
                    return false;
                }                   
               });
               
               if(curricularCourseScope != null)
                   res.add(enrolment);
           }
               
        }
        return res;
    }
}
