/*
 * InfoItem.java Mar 11, 2003
 */
package DataBeans;

import java.util.List;

import Dominio.IItem;

/**
 * @author Ivo Brandão
 */
public class InfoItem extends InfoObject implements Comparable {

    //Serializable
    private String information;

    private String name;

    private Integer itemOrder;

    private InfoSection infoSection;

    private Boolean urgent;

    private List links;

    /**
     * Constructor
     */
    public InfoItem() {
    }

    /**
     * Constructor
     */
    public InfoItem(String information, String name, Integer itemOrder,
            InfoSection infoSection, Boolean urgent) {

        this.information = information;
        this.name = name;
        this.itemOrder = itemOrder;
        this.infoSection = infoSection;
        this.urgent = urgent;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoItem) {
            InfoItem infoItem = (InfoItem) obj;
            resultado = getInformation().equals(infoItem.getInformation())
                    && getName().equals(infoItem.getName())
                    && getItemOrder().equals(infoItem.getItemOrder())
                    && getInfoSection().equals(infoItem.getInfoSection())
                    && getUrgent().equals(infoItem.getUrgent());
        }
        return resultado;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[INFOITEM";
        result += ", name=" + name;
        result += ", itemOrder=" + itemOrder;
        result += ", infoSection=" + infoSection;
        result += ", urgent=" + urgent;
        result += "]";
        return result;
    }

    /**
     * @return String
     */
    public String getInformation() {
        return information;
    }

    /**
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * @return Integer
     */
    public Integer getItemOrder() {
        return itemOrder;
    }

    /**
     * @return InfoSection
     */
    public InfoSection getInfoSection() {
        return infoSection;
    }

    /**
     * @return Boolean
     */
    public Boolean getUrgent() {
        return urgent;
    }

    /**
     * Sets the information.
     * 
     * @param information
     *            The information to set
     */
    public void setInformation(String information) {
        this.information = information;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the itemOrder.
     * 
     * @param itemOrder
     *            The itemOrder to set
     */
    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }

    /**
     * Sets the urgent.
     * 
     * @param urgent
     *            The urgent to set
     */
    public void setUrgent(Boolean urgent) {
        this.urgent = urgent;
    }

    /**
     * Sets the infoSection.
     * 
     * @param infoSection
     *            The infoSection to set
     */
    public void setInfoSection(InfoSection infoSection) {
        this.infoSection = infoSection;
    }

    public int compareTo(Object arg0) {

        return this.getItemOrder().intValue()
                - ((InfoItem) arg0).getItemOrder().intValue();
    }

    /**
     * @return
     */
    public List getLinks() {
        return links;
    }

    /**
     * @param links
     */
    public void setLinks(List links) {
        this.links = links;
    }

    public void copyFromDomain(IItem item) {
        super.copyFromDomain(item);
        if (item != null) {
            setInformation(item.getInformation());
            setItemOrder(item.getItemOrder());
            setName(item.getName());
            setUrgent(item.getUrgent());
        }
    }

    /**
     * @param item
     * @return
     */
    public static InfoItem newInfoFromDomain(IItem item) {
        InfoItem infoItem = null;
        if (item != null) {
            infoItem = new InfoItem();
            infoItem.copyFromDomain(item);
        }
        return infoItem;
    }
}