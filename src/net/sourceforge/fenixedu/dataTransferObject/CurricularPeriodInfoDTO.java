/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CurricularPeriodInfoDTO extends DataTranferObject {

    private AcademicPeriod periodType;

    private Integer order;

    public CurricularPeriodInfoDTO(Integer order, AcademicPeriod type) {
	super();
	this.order = order;
	this.periodType = type;
    }

    public Integer getOrder() {
	return order;
    }

    public AcademicPeriod getPeriodType() {
	return periodType;
    }

}
