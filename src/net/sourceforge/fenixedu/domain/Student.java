package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.util.EntryPhase;
import net.sourceforge.fenixedu.util.StudentState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class Student extends Student_Base {

    public Student() {
        this.setSpecialSeason(Boolean.FALSE);
    }
	
	public Student(IPerson person, Integer studentNumber, IStudentKind studentKind, StudentState state, 
			Boolean payedTuition, Boolean enrolmentForbidden, EntryPhase entryPhase, DegreeType degreeType) {

		this();
		
		setPayedTuition(payedTuition);
        setEnrollmentForbidden(enrolmentForbidden);
        setEntryPhase(entryPhase);
        setDegreeType(degreeType);
        setPerson(person);
        setState(state);
		setNumber(studentNumber);
		setStudentKind(studentKind);
	}

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "internalCode = " + this.getIdInternal() + "; ";
        result += "number = " + this.getNumber() + "; ";
        result += "state = " + this.getState() + "; ";
        result += "degreeType = " + this.getDegreeType() + "; ";
        result += "studentKind = " + this.getStudentKind() + "; ";
        return result;
    }

    public IStudentCurricularPlan getActiveStudentCurricularPlan() {
        List curricularPlans = getStudentCurricularPlans();
        IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) CollectionUtils.find(
                curricularPlans, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) arg0;
                        return studentCurricularPlan.getCurrentState().equals(
                                StudentCurricularPlanState.ACTIVE);
                    }
                });

        if (studentCurricularPlan == null) {
            studentCurricularPlan = (IStudentCurricularPlan) curricularPlans.get(0);
        }
        return studentCurricularPlan;
    }

}
