package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.PrescriptionEnum;

public class PrescriptionBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private PrescriptionEnum prescriptionEnum = PrescriptionEnum.MOMENT1;

    public PrescriptionBean(PrescriptionEnum prescriptionEnum) {
        super();
        this.prescriptionEnum = prescriptionEnum;
    }

    public void setSelectedPrescriptionEnum(PrescriptionEnum prescriptionEnum) {
        this.prescriptionEnum = prescriptionEnum;
    }

    public PrescriptionEnum getSelectedPrescriptionEnum() {
        return prescriptionEnum;
    }
}
