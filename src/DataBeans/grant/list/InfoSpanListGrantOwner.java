/*
 * Created on Jun 23, 2004
 *
 */
package DataBeans.grant.list;

import DataBeans.InfoObject;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoSpanListGrantOwner extends InfoObject {
    private Integer spanNumber;
    private Integer numberOfElementsInSpan;
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
     * @param ascendingOrder The ascendingOrder to set.
     */
    public void setAscendingOrder(Boolean ascendingOrder) {
        this.ascendingOrder = ascendingOrder;
    }
    /**
     * @return Returns the numberOfElementsInSpan.
     */
    public Integer getNumberOfElementsInSpan() {
        return numberOfElementsInSpan;
    }
    /**
     * @param numberOfElementsInSpan The numberOfElementsInSpan to set.
     */
    public void setNumberOfElementsInSpan(Integer numberOfElementsInSpan) {
        this.numberOfElementsInSpan = numberOfElementsInSpan;
    }
    /**
     * @return Returns the orderBy.
     */
    public String getOrderBy() {
        return orderBy;
    }
    /**
     * @param orderBy The orderBy to set.
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
     * @param spanNumber The spanNumber to set.
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
     * @param totalElements The totalElements to set.
     */
    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }
    
    public Integer getNumberOfSpans()
    {
        if(this.totalElements != null && 
           this.numberOfElementsInSpan != null && 
           !this.numberOfElementsInSpan.equals(new Integer(0)))
            return new Integer((this.totalElements.intValue() / this.numberOfElementsInSpan.intValue()) + 1);
        else
            return new Integer(-1);
    }
    public boolean hasBeforeSpan()
    {
        if(this.spanNumber.intValue() > 1)
            return true;
        else
            return false;
    }
    public Integer getBeforeSpan()
    {
        return new Integer(this.spanNumber.intValue() - 1);
    }
    public boolean hasAfterSpan()
    {
        if(this.spanNumber.intValue() < getNumberOfSpans().intValue())
            return true;
        else
            return false;
    }
    public Integer getAfterSpan()
    {
        return new Integer(this.spanNumber.intValue() + 1);
    }
}
