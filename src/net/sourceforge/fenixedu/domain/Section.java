/*
 * Section.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.fileSuport.INode;

/**
 * @author Ivo Brandão
 */
public class Section extends Section_Base {

    public String toString() {
        String result = "[SECTION";
        result += ", codInt=" + getIdInternal();
        result += ", sectionOrder=" + getSectionOrder();
        result += ", name=" + getName();
        result += ", lastModifiedDate=" + getLastModifiedDate();
        result += ", site=" + getSite();
        result += ", superiorSection=" + getSuperiorSection();

        result += "]";

        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ISection) {
            final ISection section = (ISection) obj;
            return this.getIdInternal().equals(section.getIdInternal());
        }
        return false;
    }

    public String getSlideName() {
        String result = getParentNode().getSlideName() + "/S" + getIdInternal();
        return result;
    }

    public INode getParentNode() {
        if (getSuperiorSection() == null) {
            ISite site = getSite();
            IExecutionCourse executionCourse = site.getExecutionCourse();
            return executionCourse;
        }
        ISection section = getSuperiorSection();
        return section;
    }

    public IItem insertItem(String itemName, String itemInformation, Boolean itemUrgent,
            Integer insertItemOrder) throws ExistingServiceException {

        if (itemName == null || insertItemOrder == null || itemUrgent == null || insertItemOrder == null) {
            throw new NullPointerException();
        }

        for (IItem item : this.getAssociatedItems()) {
            if (item.getName().equals(itemName))
                throw new ExistingServiceException();
        }

        IItem item = new Item();
        item.setInformation(itemInformation);
        item.setName(itemName);
        item.setSection(this);
        item.setUrgent(itemUrgent);
        Integer itemOrder = new Integer(organizeExistingItemsOrder(insertItemOrder.intValue()));
        item.setItemOrder(itemOrder);

        if (this.getAssociatedItems() == null)
            this.setAssociatedItems(new ArrayList());

        this.getAssociatedItems().add(item);

        return item;
    }

    private int organizeExistingItemsOrder(int insertItemOrder) {

        List<IItem> items = this.getAssociatedItems();

        if (items != null) {
            int itemOrder;

            if (insertItemOrder == -1) {
                insertItemOrder = items.size();
            }

            for (IItem item : items) {
                itemOrder = item.getItemOrder().intValue();
                if (itemOrder >= insertItemOrder)
                    item.setItemOrder(new Integer(itemOrder + 1));
            }
        }
        return insertItemOrder;
    }
}
