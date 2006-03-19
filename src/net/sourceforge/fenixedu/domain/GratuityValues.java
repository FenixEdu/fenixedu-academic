/*
 * Created on 9/Jan/2004
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * @author T�nia Pous�o
 */
public class GratuityValues extends GratuityValues_Base {

    public GratuityValues() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public Double calculateTotalValueForMasterDegree() {
        Double totalValue = 0.0;

        Double annualValue = this.getAnualValue();

        if ((annualValue != null) && (annualValue > 0)) {
            // we have data to calculate using annual value
            totalValue = annualValue;
        } else {
            // we have to use the components information (scholarship + final
            // proof)
            totalValue = this.getScholarShipValue()
                    + (this.getFinalProofValue() == null ? 0.0 : this.getFinalProofValue());
        }

        return totalValue;
    }

    public Double calculateTotalValueForSpecialization(StudentCurricularPlan studentCurricularPlan) {

        ExecutionYear executionYear = this.getExecutionDegree().getExecutionYear();
        Double totalValue = 0.0;

        if ((this.getCourseValue() != null) && (this.getCourseValue() > 0)) {

            // calculate using value per course
            double valuePerCourse = this.getCourseValue();

            for (Enrolment enrolment : studentCurricularPlan.getEnrolments()) {
                if (enrolment.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
                    totalValue += valuePerCourse;
                }
            }

        } else if ((this.getCreditValue() != null) && (this.getCreditValue() > 0)) {

            // calculate using value per credit
            double valuePerCredit = this.getCreditValue().doubleValue();
            double totalCredits = 0;

            for (Enrolment enrolment : studentCurricularPlan.getEnrolments()) {
                if (enrolment.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
                    totalCredits += enrolment.getCurricularCourse().getCredits();
                }
            }

            totalValue = totalCredits * valuePerCredit;

        }

        return totalValue;
    }

}