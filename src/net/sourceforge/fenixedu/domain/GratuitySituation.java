/*
 * Created on 5/Jan/2004
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.transactions.GratuityTransaction;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * @author T�nia Pous�o
 */
public class GratuitySituation extends GratuitySituation_Base {

	public GratuitySituation() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}
	
    public GratuitySituation(GratuityValues gratuityValues, StudentCurricularPlan studentCurricularPlan) {
    	this();
        setGratuityValues(gratuityValues);
        setStudentCurricularPlan(studentCurricularPlan);
        setWhen(Calendar.getInstance().getTime());
        Double totalValue = null;

        if (studentCurricularPlan.getSpecialization().equals(Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE)) {
            totalValue = gratuityValues.calculateTotalValueForMasterDegree();
        } else if (studentCurricularPlan.getSpecialization().equals(Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION)) {
            totalValue = gratuityValues.calculateTotalValueForSpecialization(studentCurricularPlan);
        }

        setRemainingValue(totalValue);
        setTotalValue(totalValue);
    }
    
    public void delete() {
        removeRootDomainObject();
        deleteDomainObject();
    }

    public void updateValues() {
        
        updateTotalValue();
        updateRemainingValue();
    }
    
    private void updateTotalValue() {

        double totalValue = 0.0;

        Specialization specialization = getStudentCurricularPlan().getSpecialization();
        if (specialization.equals(Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION)) {
            totalValue = this.getGratuityValues().calculateTotalValueForSpecialization(
                    this.getStudentCurricularPlan());
        } else if (specialization.equals(Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE)) {
            totalValue = this.getGratuityValues().calculateTotalValueForMasterDegree();
        }

        setTotalValue(totalValue);

    }
    
    private void updateRemainingValue() {
        
        double valueToSubtract = calculateValueToSubtract();
        this.setRemainingValue(getTotalValue() - valueToSubtract);
        
    }
    
    
    private double calculateValueToSubtract() {

        double exemptionValue = 0;

        if (this.getExemptionPercentage() != null) {
            exemptionValue = this.getTotalValue().doubleValue()
                    * (this.getExemptionPercentage().doubleValue() / 100.0);
        }

        if (this.getExemptionValue() != null) {
            exemptionValue += this.getExemptionValue().doubleValue();
        }

        double payedValue = 0;
        double reimbursedValue = 0;

        for (GratuityTransaction gratuityTransaction : this.getTransactionList()) {

            payedValue += gratuityTransaction.getValue();

            if (gratuityTransaction.getGuideEntry() != null) {

                List<ReimbursementGuideEntry> reimbursementGuideEntries = gratuityTransaction
                        .getGuideEntry().getReimbursementGuideEntries();
                for (ReimbursementGuideEntry reimbursementGuideEntry : reimbursementGuideEntries) {
                    if (reimbursementGuideEntry.getReimbursementGuide()
                            .getActiveReimbursementGuideSituation().getReimbursementGuideState().equals(
                                    ReimbursementGuideState.PAYED)) {

                        reimbursedValue += reimbursementGuideEntry.getValue();
                    }
                }
            }
        }

        return (exemptionValue + payedValue - reimbursedValue);
    }

}
