/*
 * Item.java
 *
 * Created on 19 de Agosto de 2002, 13:21
 */

package Dominio;

import fileSuport.INode;

/**
 * 
 * @author ars
 */

public class Item extends DomainObject implements IItem {

    private String information;

    private String name;

    private Integer itemOrder;

    private ISection section;

    private Boolean urgent;

    private Integer keySection;

    /**
     * Construtor
     */
    public Item() {
    }

    /**
     * Construtor
     */
    public Item(Integer idInternal) {
        setIdInternal(idInternal);
    }

    /**
     * Construtor
     */
    public Item(String name, ISection section, Integer itemOrder, String information, Boolean urgent) {
        setName(name);
        setSection(section);
        setItemOrder(itemOrder);
        setInformation(information);
        setUrgent(urgent);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof Item) {
            Item item = (Item) obj;
            resultado = (getName().equals(item.getName())) && getSection().equals(item.getSection());
        }
        return resultado;
    }

    public String toString() {
        String result = "[ITEM";
        result += ", codInt=" + getIdInternal();
        result += ", nome=" + name;
        result += ", seccao=" + section;
        result += ", ordem=" + itemOrder;
        result += ", informacao=" + information;
        result += ", urgente=" + urgent;
        result += ", chaveSeccao=" + keySection;
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
     * @return Integer
     */
    public Integer getKeySection() {
        return keySection;
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
     * @return ISection
     */
    public ISection getSection() {
        return section;
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
     * Sets the keySection.
     * 
     * @param keySection
     *            The keySection to set
     */
    public void setKeySection(Integer keySection) {
        this.keySection = keySection;
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
     * Sets the order.
     * 
     * @param order
     *            The order to set
     */
    public void setItemOrder(Integer order) {
        this.itemOrder = order;
    }

    /**
     * Sets the section.
     * 
     * @param section
     *            The section to set
     */
    public void setSection(ISection section) {
        this.section = section;
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

    /*
     * (non-Javadoc)
     * 
     * @see fileSuport.INode#getSlideName()
     */
    public String getSlideName() {
        String result = getParentNode().getSlideName() + "/I" + getIdInternal();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fileSuport.INode#getParentNode()
     */
    public INode getParentNode() {
        ISection section = getSection();
        return section;
    }

}