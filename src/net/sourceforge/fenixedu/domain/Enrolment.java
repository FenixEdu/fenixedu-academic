package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.log.EnrolmentLog;
import net.sourceforge.fenixedu.domain.log.IEnrolmentLog;
import net.sourceforge.fenixedu.util.EnrolmentAction;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

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
				attends.delete();
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
			
			List<IEquivalentEnrolmentForEnrolmentEquivalence> equivalentRestrictions = equivalence.getEquivalenceRestrictions();
			
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

}
