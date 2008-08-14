/*
 * InfoItem.java Mar 11, 2003
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Item;
import pt.utl.ist.fenix.tools.util.i18n.Language;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.fileupload.FileItem;

/**
 * @author Ivo Brandão
 */
public class InfoItem extends InfoObject implements Comparable {

    // Serializable
    private String information;

    private String name;

    private Integer itemOrder;

    private InfoSection infoSection;

    private List<InfoFileContent> infoFileItems;

    /**
     * Constructor
     */
    public InfoItem() {
    }

    /**
     * Constructor
     */
    public InfoItem(String information, String name, Integer itemOrder, InfoSection infoSection) {

	this.information = information;
	this.name = name;
	this.itemOrder = itemOrder;
	this.infoSection = infoSection;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
	boolean resultado = false;
	if (obj instanceof InfoItem) {
	    InfoItem infoItem = (InfoItem) obj;
	    resultado = getInformation().equals(infoItem.getInformation()) && getName().equals(infoItem.getName())
		    && getItemOrder().equals(infoItem.getItemOrder()) && getInfoSection().equals(infoItem.getInfoSection());
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
     * Sets the infoSection.
     * 
     * @param infoSection
     *            The infoSection to set
     */
    public void setInfoSection(InfoSection infoSection) {
	this.infoSection = infoSection;
    }

    public int compareTo(Object arg0) {

	return this.getItemOrder().intValue() - ((InfoItem) arg0).getItemOrder().intValue();
    }

    public void copyFromDomain(Item item) {
	super.copyFromDomain(item);
	if (item != null) {
	    setInformation(item.getBody().getContent(Language.pt));
	    setItemOrder(item.getItemOrder());
	    setName(item.getName().getContent(Language.pt));

	    List<InfoFileContent> infoFileItems = new ArrayList<InfoFileContent>();

	    for (FileContent fileItem : item.getFileItems()) {
		infoFileItems.add(InfoFileContent.newInfoFromDomain(fileItem));
	    }

	    Collections.sort(infoFileItems, new BeanComparator("displayName"));
	    setInfoFileItems(infoFileItems);
	}
    }

    /**
     * @param item
     * @return
     */
    public static InfoItem newInfoFromDomain(Item item) {
	InfoItem infoItem = null;
	if (item != null) {
	    infoItem = new InfoItem();
	    infoItem.copyFromDomain(item);
	}
	return infoItem;
    }

    public void setInfoFileItems(List<InfoFileContent> infoFileItems) {
	this.infoFileItems = infoFileItems;
    }

    public List<InfoFileContent> getInfoFileItems() {
	return this.infoFileItems;
    }
}