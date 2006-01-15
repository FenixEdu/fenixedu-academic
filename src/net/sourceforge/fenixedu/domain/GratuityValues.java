/*
 * Created on 9/Jan/2004
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * @author Tânia Pousão
 */
public class GratuityValues extends GratuityValues_Base {

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

    public String toString() {
        StringBuilder result = new StringBuilder();
        result = result.append("[GratuityValues: \n").append("idInternal= ").append(getIdInternal())
                .append("\nanualValue= ").append(getAnualValue()).append("\nscholarShipPart= ").append(
                        getScholarShipValue()).append("\nfinalProofValue= ")
                .append(getFinalProofValue()).append("\ncourseValue= ").append(getCourseValue()).append(
                        "\ncreditValue= ").append(getCreditValue()).append("\nproofRequestPayment= ")
                .append(getProofRequestPayment()).append("\nstartPayment= ").append(getStartPayment())
                .append("\nendPayment= ").append(getEndPayment()).append("]");
        return result.toString();
    }

}