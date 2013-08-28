package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PrescriptionEnum;

public class PrescriptionBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private PrescriptionEnum prescriptionEnum = PrescriptionEnum.MOMENT1;
    private ExecutionYear executionYear;

    public PrescriptionBean(PrescriptionEnum prescriptionEnum) {
        super();
        this.prescriptionEnum = prescriptionEnum;
    }

    public void setSelectedPrescriptionEnum(PrescriptionEnum prescriptionEnum) {
        this.prescriptionEnum = prescriptionEnum;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public PrescriptionEnum getSelectedPrescriptionEnum() {
        return prescriptionEnum;
    }
}
