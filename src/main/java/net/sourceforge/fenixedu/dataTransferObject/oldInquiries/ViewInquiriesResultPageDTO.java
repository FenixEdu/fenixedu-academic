package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ViewInquiriesResultPageDTO extends SearchInquiriesResultPageDTO {

    private String degreeCurricularPlanID;

    public String getDegreeCurricularPlanID() {
        return degreeCurricularPlanID;
    }

    public void setDegreeCurricularPlanID(String degreeCurricularPlanID) {
        this.degreeCurricularPlanID = degreeCurricularPlanID;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        final String degreeCurricularPlanID = getDegreeCurricularPlanID();
        return AbstractDomainObject.fromExternalId(degreeCurricularPlanID);
    }

}
