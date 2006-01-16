/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CurricularPeriodInfoDTO extends DataTranferObject {

    private CurricularPeriodType periodType;

    private Integer order;

    public CurricularPeriodInfoDTO(Integer order, CurricularPeriodType type) {
        super();
        this.order = order;
        this.periodType = type;
    }

    public Integer getOrder() {
        return order;
    }

    public CurricularPeriodType getPeriodType() {
        return periodType;
    }

}
