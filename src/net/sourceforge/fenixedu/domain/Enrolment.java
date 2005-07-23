package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.log.EnrolmentLog;
import net.sourceforge.fenixedu.domain.log.IEnrolmentLog;
import net.sourceforge.fenixedu.util.EnrolmentAction;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.commons.collections.Predicate;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerException;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class Enrolment extends Enrolment_Base {

    public Enrolment() {
        this.setOjbConcreteClass(this.getClass().getName());
    }
	
	public void initializeAsNew (IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod, EnrollmentCondition enrolmentCondition, String createdBy) {
		setCurricularCourse(curricularCourse);
		setEnrollmentState(EnrollmentState.ENROLLED);
		setExecutionPeriod(executionPeriod);
		setStudentCurricularPlan(studentCurricularPlan);
		setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
		setCreationDate(new Date());
		setCondition(enrolmentCondition);
		setCreatedBy(createdBy);
		
		createEnrollmentEvaluationWithoutGrade();
		createAttend(studentCurricularPlan.getStudent(), curricularCourse, executionPeriod);
	}

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "idInternal = " + super.getIdInternal() + "; ";
        result += "studentCurricularPlan = " + this.getStudentCurricularPlan() + "; ";
        result += "enrollmentState = " + this.getEnrollmentState() + "; ";
        result += "execution Period = " + this.getExecutionPeriod() + "; ";
        result += "curricularCourse = " + this.getCurricularCourse() + "]\n";
        return result;
    }

    private void createNewEnrolmentLog(EnrolmentAction action, PersistenceBroker arg0)
            throws PersistenceBrokerException {
        IEnrolmentLog enrolmentLog = new EnrolmentLog();
        enrolmentLog.setDate(new Date());
        enrolmentLog.setAction(action);
        enrolmentLog.setCurricularCourse(this.getCurricularCourse());
        enrolmentLog.setStudent(this.getStudentCurricularPlan().getStudent());
        arg0.store(enrolmentLog);
    }

    public void afterDelete(PersistenceBroker arg0) throws PersistenceBrokerException {
        //createNewEnrolmentLog(EnrolmentAction.UNENROL, arg0);
    }

    public void afterInsert(PersistenceBroker arg0) throws PersistenceBrokerException {
        createNewEnrolmentLog(EnrolmentAction.ENROL, arg0);
    }

    public void afterLookup(PersistenceBroker arg0) throws PersistenceBrokerException {
    }

    public void afterUpdate(PersistenceBroker arg0) throws PersistenceBrokerException {
    }

    public void beforeDelete(PersistenceBroker arg0) throws PersistenceBrokerException {
    }

    public void beforeInsert(PersistenceBroker arg0) throws PersistenceBrokerException {
    }

    public void beforeUpdate(PersistenceBroker arg0) throws PersistenceBrokerException {
    }
	
	
	
	

	
	
	
	public void unEnroll() throws DomainException {
		
		for (IEnrolmentEvaluation eval : getEvaluations()) {
			
			
			if (eval.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.NORMAL) &&
				eval.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.TEMPORARY_OBJ) &&
				(eval.getGrade() == null || eval.getGrade().equals("")))
				;
			else
				throw new DomainException(this.getClass().getName(), "ola mundo");
		}
		
		delete();
	}
	
	
	
	
	
	
	public void delete() {
		
		removeExecutionPeriod();
		removeStudentCurricularPlan();
		removeCurricularCourse();
			
		
		Iterator<IAttends> attendsIter = getAttendsIterator();
		while (attendsIter.hasNext()) {
			IAttends attends = attendsIter.next();
			
			try {
				attendsIter.remove();
				attends.removeEnrolment();
				//attends.delete();
			}
			catch (DomainException e) {}
		}
		
		
		Iterator<IEnrolmentEvaluation>  evalsIter = getEvaluationsIterator();
		while (evalsIter.hasNext()) {
			IEnrolmentEvaluation eval = evalsIter.next();
			evalsIter.remove();
			eval.delete();
		}
		
		
		
		Iterator<ICreditsInAnySecundaryArea> creditsInAnysecundaryAreaIterator = getCreditsInAnySecundaryAreasIterator();
		
		while (creditsInAnysecundaryAreaIterator.hasNext()) {
			ICreditsInAnySecundaryArea credits = creditsInAnysecundaryAreaIterator.next();
			creditsInAnysecundaryAreaIterator.remove();
			credits.delete();
		}
		

		
		
		Iterator<ICreditsInScientificArea> creditsInScientificAreaIterator = getCreditsInScientificAreasIterator();
		
		while (creditsInScientificAreaIterator.hasNext()) {
			ICreditsInScientificArea credits = creditsInScientificAreaIterator.next();
			creditsInScientificAreaIterator.remove();
			credits.delete();
		}
		
		
	
		
		
		Iterator<IEquivalentEnrolmentForEnrolmentEquivalence> equivalentEnrolmentIterator = getEquivalentEnrolmentForEnrolmentEquivalencesIterator();
		
		while (equivalentEnrolmentIterator.hasNext()) {
			IEquivalentEnrolmentForEnrolmentEquivalence equivalentEnrolment = equivalentEnrolmentIterator.next();
			equivalentEnrolmentIterator.remove();
			equivalentEnrolment.removeEquivalentEnrolment();
			
			IEnrolmentEquivalence equivalence = equivalentEnrolment.getEnrolmentEquivalence();
			IEnrolment enrolment = equivalence.getEnrolment();
			
			enrolment.delete();
			equivalence.removeEnrolment();
			equivalentEnrolment.removeEnrolmentEquivalence();
			
			((EquivalentEnrolmentForEnrolmentEquivalence)equivalentEnrolment).deleteDomainObject();
			((EnrolmentEquivalence)equivalence).deleteDomainObject();
		}

		
		Iterator<IEnrolmentEquivalence> equivalenceIterator = getEnrolmentEquivalencesIterator();
		
		while (equivalenceIterator.hasNext()) {
			IEnrolmentEquivalence equivalence = equivalenceIterator.next();
			equivalenceIterator.remove();
			equivalence.removeEnrolment();
			
			Iterator<IEquivalentEnrolmentForEnrolmentEquivalence> equivalentRestrictionIterator = equivalence.getEquivalenceRestrictionsIterator();
			
			while (equivalentRestrictionIterator.hasNext()) {
				IEquivalentEnrolmentForEnrolmentEquivalence equivalentRestriction = equivalentRestrictionIterator.next();
				equivalentRestriction.removeEquivalentEnrolment();
				equivalentRestrictionIterator.remove();
				equivalentRestriction.removeEnrolmentEquivalence();
				
				((EquivalentEnrolmentForEnrolmentEquivalence)equivalentRestriction).deleteDomainObject();
			}
			
			((EnrolmentEquivalence)equivalence).deleteDomainObject();
			
		}
		
		super.deleteDomainObject();

	}
	
	
	
	
	
	
	
	
	
	
	
	
	public IEnrolmentEvaluation getImprovementEvaluation() {
		
		for (IEnrolmentEvaluation evaluation : getEvaluations()) {
			if (evaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.IMPROVEMENT) &&
				evaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.TEMPORARY_OBJ))
				
				return evaluation;
		}
		
		return null;
	}

	
	public IEnrolmentEvaluation getEnrolmentEvaluationByEnrolmentEvaluationTypeAndGrade (final EnrolmentEvaluationType evaluationType, final String grade) {
		
		return (IEnrolmentEvaluation)CollectionUtils.find(getEvaluations(),new Predicate() {

			public boolean evaluate(Object o) {
				IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) o;
				String evaluationGrade = enrolmentEvaluation.getGrade();
				
				return enrolmentEvaluation.getEnrolmentEvaluationType().equals(evaluationType) &&
					((grade == null && evaluationGrade == null) || 
							(evaluationGrade != null && evaluationGrade.equals(grade)));
			}
			
		});
	}
	
	private IEnrolmentEvaluation getEnrolmentEvaluationByEnrolmentEvaluationStateAndType (final EnrolmentEvaluationState state, final EnrolmentEvaluationType type) {
		return (IEnrolmentEvaluation)CollectionUtils.find(getEvaluations(),new Predicate() {

			public boolean evaluate(Object o) {
				IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) o;
				return (enrolmentEvaluation.getEnrolmentEvaluationState().equals(state) &&
						enrolmentEvaluation.getEnrolmentEvaluationType().equals(type));
			}
			
		});
	}
	
	public IEnrolmentEvaluation submitEnrolmentEvaluation (EnrolmentEvaluationType enrolmentEvaluationType, IMark publishedMark, IEmployee employee, IPerson personResponsibleForGrade, Date evaluationDate, String observation) {
		
		IEnrolmentEvaluation enrolmentEvaluation = getEnrolmentEvaluationByEnrolmentEvaluationStateAndType(EnrolmentEvaluationState.TEMPORARY_OBJ, enrolmentEvaluationType);

		//There can be only one enrolmentEvaluation with Temporary State
		if (enrolmentEvaluation == null ) {
		    enrolmentEvaluation = new EnrolmentEvaluation();
			enrolmentEvaluation.setEnrolment(this);
		}
		
		//teacher responsible for execution course
		String grade = null;
		if ((publishedMark == null) || (publishedMark.getMark().length() == 0))
			grade = "NA";
		else
			grade = publishedMark.getMark().toUpperCase();
		
		enrolmentEvaluation.setGrade(grade);
		
		enrolmentEvaluation.setEnrolmentEvaluationType(enrolmentEvaluationType);
		enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
		enrolmentEvaluation.setObservation(observation);
		enrolmentEvaluation.setPersonResponsibleForGrade(personResponsibleForGrade);
		
		enrolmentEvaluation.setEmployee(employee);
		
		Calendar calendar = Calendar.getInstance();
		enrolmentEvaluation.setWhen(new Timestamp(calendar.getTimeInMillis()));
		enrolmentEvaluation.setGradeAvailableDate(calendar.getTime());
		if (evaluationDate != null) {
		    enrolmentEvaluation.setExamDate(evaluationDate);
		} else {
		    enrolmentEvaluation.setExamDate(calendar.getTime());
		}
		
		enrolmentEvaluation.setCheckSum("");
		
		return enrolmentEvaluation;
	}
	
    public void createEnrollmentEvaluationWithoutGrade() {
		
		IEnrolmentEvaluation enrolmentEvaluation = getEnrolmentEvaluationByEnrolmentEvaluationTypeAndGrade(EnrolmentEvaluationType.NORMAL, null);

        if (enrolmentEvaluation == null) {
            enrolmentEvaluation = new EnrolmentEvaluation();
            enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
            enrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
			
			addEvaluations(enrolmentEvaluation);
        }
    }
	
	public void createAttend(IStudent student, ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod) {

		List executionCourses = curricularCourse.getExecutionCoursesByExecutionPeriod(executionPeriod);
		
		IExecutionCourse executionCourse = null;
        if (executionCourses.size() > 1) {
            Iterator iterator = executionCourses.iterator();
            while (iterator.hasNext()) {
                IExecutionCourse executionCourse2 = (IExecutionCourse) iterator.next();
                if (executionCourse2.getExecutionCourseProperties() == null
                        || executionCourse2.getExecutionCourseProperties().isEmpty()) {
                    executionCourse = executionCourse2;
                }
            }
        } else if (executionCourses.size() == 1) {
            executionCourse = (IExecutionCourse) executionCourses.get(0);
        }

        if (executionCourse != null) {
			IAttends attend = executionCourse.getAttendsByStudent(student);

            if (attend != null) {
				addAttends(attend);
            } else {
                IAttends attendToWrite = new Attends(student,executionCourse);
				addAttends(attendToWrite);
            }
        }
    }
	
	
	public boolean isImprovementForExecutionCourse (IExecutionCourse executionCourse) {
		return !getExecutionPeriod().equals(executionCourse.getExecutionPeriod());
	}
}
