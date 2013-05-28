package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ViewInquiriesResultPageDTO extends SearchInquiriesResultPageDTO {

    private Integer degreeCurricularPlanID;

    public Integer getDegreeCurricularPlanID() {
        return degreeCurricularPlanID;
    }

    public void setDegreeCurricularPlanID(Integer degreeCurricularPlanID) {
        this.degreeCurricularPlanID = degreeCurricularPlanID;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        final Integer degreeCurricularPlanID = getDegreeCurricularPlanID();
        return degreeCurricularPlanID == null ? null : AbstractDomainObject.fromExternalId(
                degreeCurricularPlanID);
    }

}
