/*
 * Created on Jun 23, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.list;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.presentationTier.Action.grant.utils.SessionConstants;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoSpanListGrantOwner extends InfoObject {
    private Integer spanNumber;

    private String orderBy;

    private Integer totalElements;

    private Boolean ascendingOrder;

    /**
     * @return Returns the ascendingOrder.
     */
    public Boolean getAscendingOrder() {
        return ascendingOrder;
    }

    /**
     * @param ascendingOrder
     *            The ascendingOrder to set.
     */
    public void setAscendingOrder(Boolean ascendingOrder) {
        this.ascendingOrder = ascendingOrder;
    }

    /**
     * @return Returns the orderBy.
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * @param orderBy
     *            The orderBy to set.
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * @return Returns the spanNumber.
     */
    public Integer getSpanNumber() {
        return spanNumber;
    }

    /**
     * @param spanNumber
     *            The spanNumber to set.
     */
    public void setSpanNumber(Integer spanNumber) {
        this.spanNumber = spanNumber;
    }

    /**
     * @return Returns the totalElements.
     */
    public Integer getTotalElements() {
        return totalElements;
    }

    /**
     * @param totalElements
     *            The totalElements to set.
     */
    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getNumberOfSpans() {
        if (this.totalElements != null) {
            return new Integer(
                    (this.totalElements.intValue() / new Integer(100).intValue()) + 1);			//SessionConstants.NUMBER_OF_ELEMENTS_IN_SPAN
                           
        }
        return new Integer(-1);
    }

    public boolean hasBeforeSpan() {
        if (this.spanNumber.intValue() > 1) {
            return true;
        }
        return false;
    }

    public Integer getBeforeSpan() {
        return new Integer(this.spanNumber.intValue() - 1);
    }

    public boolean hasAfterSpan() {
        if (this.spanNumber.intValue() < getNumberOfSpans().intValue()) {
            return true;
        }
        return false;
    }

    public Integer getAfterSpan() {
        return new Integer(this.spanNumber.intValue() + 1);
    }
}