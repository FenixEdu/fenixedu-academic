/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.factoryExecutors;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.joda.time.YearMonthDay;

import com.sun.tools.xjc.reader.RawTypeSet.Ref;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentCurricularPlanFactoryExecutor {

    @SuppressWarnings("serial")
    public static class StudentCurricularPlanCreator implements FactoryExecutor, Serializable {

	private DomainReference<Registration> registration;

	private DomainReference<DegreeCurricularPlan> degreeCurricularPlan;

	public StudentCurricularPlanCreator(Registration registration) {
	    super();
	    this.registration = new DomainReference<Registration>(registration);
	}

	public Object execute() {
	    return StudentCurricularPlan.createBolonhaStudentCurricularPlan(getRegistration(),
		    getDegreeCurricularPlan(), StudentCurricularPlanState.ACTIVE, new YearMonthDay(),
		    ExecutionPeriod.readActualExecutionPeriod());
	}

	public DegreeCurricularPlan getDegreeCurricularPlan() {
	    return this.degreeCurricularPlan != null ? this.degreeCurricularPlan.getObject() : null;
	}

	public Registration getRegistration() {
	    return this.registration != null ? this.registration.getObject() : null;
	}

	public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
	    this.degreeCurricularPlan = degreeCurricularPlan != null ? new DomainReference<DegreeCurricularPlan>(
		    degreeCurricularPlan)
		    : null;
	}

	public void setRegistration(Registration registration) {
	    this.registration = registration != null ? new DomainReference<Registration>(registration)
		    : null;
	}

	public Degree getDegree() {
	    return getRegistration().getDegree();
	}

    }

}
