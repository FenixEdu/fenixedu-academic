package net.sourceforge.fenixedu.applicationTier.utils;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;

import org.apache.commons.collections.Predicate;

public class EnrollmentPredicates {
    
    /* devolve um Predicate que aceita IEnrollments que tenham o estado como APROVED (sic) */
    public static Predicate getApprovedPredicate() {
	return new Predicate() {
	    public boolean evaluate(Object object) {
		if (object instanceof Enrolment) {
		    Enrolment en = (Enrolment) object;
		    if (en.isEnrolmentStateApproved()
			    || en.isEnroled()
			    || en.isTemporarilyEnroled())
			return true;
		}

		return false;
	    }
	};
    }

    /* devolve um Predicate que aceita IEnrollments que tenham o estado como ENROLLED */
    public static Predicate getEnrolledPredicate() {
	return new Predicate() {
	    public boolean evaluate(Object object) {
		if (object instanceof Enrolment) {
		    Enrolment en = (Enrolment) object;
		    if (en.isEnroled()
			    || en.isTemporarilyEnroled())
			return true;
		}

		return false;
	    }
	};
    }

    /* devolve um Predicate que aceita quaisquer IEnrollments */
    public static Predicate getAllPredicate() {
	return new Predicate() {
	    public boolean evaluate(Object object) {
		if (object instanceof Enrolment) {
		    return true;
		}

		return false;
	    }
	};
    }

    /* devolve um Predicate que nao aceita nenhum Enrolment */
    public static Predicate getNonePredicate() {
	return new Predicate() {
	    public boolean evaluate(Object object) {
		return false;
	    }
	};
    }

}
