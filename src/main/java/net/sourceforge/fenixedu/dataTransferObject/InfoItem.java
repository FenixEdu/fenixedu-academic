/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * InfoItem.java Mar 11, 2003
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Item;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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
    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoItem) {
            InfoItem infoItem = (InfoItem) obj;
            resultado =
                    getInformation().equals(infoItem.getInformation()) && getName().equals(infoItem.getName())
                            && getItemOrder().equals(infoItem.getItemOrder())
                            && getInfoSection().equals(infoItem.getInfoSection());
        }
        return resultado;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
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

    @Override
    public int compareTo(Object arg0) {

        return this.getItemOrder().intValue() - ((InfoItem) arg0).getItemOrder().intValue();
    }

    public void copyFromDomain(Item item) {
        super.copyFromDomain(item);
        if (item != null) {
            setInformation(item.getBody().getContent(MultiLanguageString.pt));
            setItemOrder(item.getOrder());
            setName(item.getName().getContent(MultiLanguageString.pt));

            List<InfoFileContent> infoFileItems = new ArrayList<InfoFileContent>();

            for (FileContent fileItem : item.getFileContentSet()) {
                infoFileItems.add(InfoFileContent.newInfoFromDomain(fileItem));
            }

            Collections.sort(infoFileItems, InfoFileContent.COMPARATOR_BY_DISPLAY_NAME);
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