/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.factoryExecutors;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.joda.time.YearMonthDay;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentCurricularPlanFactoryExecutor {

	@SuppressWarnings("serial")
	public static class StudentCurricularPlanCreator implements FactoryExecutor, Serializable {

		private Registration registration;

		private DegreeCurricularPlan degreeCurricularPlan;

		private CycleType cycleType;

		public StudentCurricularPlanCreator(Registration registration) {
			super();
			this.registration = registration;
		}

		@Override
		public Object execute() {

			if (getRegistration().getStudentCurricularPlan(getDegreeCurricularPlan()) != null) {
				throw new DomainException("error.registrationAlreadyHasSCPWithGivenDCP");
			}

			return StudentCurricularPlan.createBolonhaStudentCurricularPlan(getRegistration(), getDegreeCurricularPlan(),
					new YearMonthDay(), ExecutionSemester.readActualExecutionSemester(), getCycleType());
		}

		public DegreeCurricularPlan getDegreeCurricularPlan() {
			return this.degreeCurricularPlan;
		}

		public Registration getRegistration() {
			return this.registration;
		}

		public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
			this.degreeCurricularPlan = degreeCurricularPlan;
		}

		public void setRegistration(Registration registration) {
			this.registration = registration;
		}

		public Degree getDegree() {
			return getRegistration().getDegree();
		}

		public CycleType getCycleType() {
			return cycleType;
		}

		public void setCycleType(CycleType cycleType) {
			this.cycleType = cycleType;
		}

	}

}
