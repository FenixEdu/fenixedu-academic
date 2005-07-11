/*
 * Section.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.fileSuport.FileSuport;
import net.sourceforge.fenixedu.fileSuport.IFileSuport;
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

    public void insertItem(String itemName, String itemInformation, Boolean itemUrgent,
            Integer insertItemOrder) throws DomainException {

        if (itemName == null || insertItemOrder == null || itemUrgent == null || insertItemOrder == null) {
            throw new NullPointerException();
        }

        for (IItem item : (List<IItem>) this.getAssociatedItems()) {
            if (item.getName().equals(itemName))
                throw new DomainException(this.getClass().getName(), "");
        }

        IItem item = new Item();
        item.setInformation(itemInformation);
        item.setName(itemName);        
        item.setUrgent(itemUrgent);
        Integer itemOrder = new Integer(organizeExistingItemsOrder(insertItemOrder.intValue()));
        item.setItemOrder(itemOrder);
        
        item.setSection(this);     
    }

    private int organizeExistingItemsOrder(int insertItemOrder) {

        Iterator items = this.getAssociatedItemsIterator();

        if (this.getAssociatedItems() != null) {
            int itemOrder;

            if (insertItemOrder == -1) {
                insertItemOrder = this.getAssociatedItemsCount();
            }

            for (; items.hasNext() ; ) {
                IItem item = (IItem) items.next();
                itemOrder = item.getItemOrder().intValue();
                if (itemOrder >= insertItemOrder)
                    item.setItemOrder(new Integer(itemOrder + 1));
            }
        }
        return insertItemOrder;
    }

    public void edit(String newSectionName, Integer newOrder) {

        if (newSectionName == null || newOrder == null) {
            throw new NullPointerException();
        }
        
        if (newOrder != this.getSectionOrder()) {
            newOrder = organizeSectionsOrder(newOrder, this.getSectionOrder(),
                    this.getSuperiorSection(), this.getSite());
        }

        this.setName(newSectionName);
        this.setSectionOrder(newOrder);
    }

    private Integer organizeSectionsOrder(Integer newOrder, Integer oldOrder, ISection superiorSection,
            ISite site) {

        List<ISection> sectionsList = getSections(superiorSection, this.getSite());

        if (newOrder.intValue() == -2) {
            newOrder = new Integer(sectionsList.size() - 1);
        }

        if (newOrder.intValue() - oldOrder.intValue() > 0) {
            for (ISection section : sectionsList) {
                int sectionOrder = section.getSectionOrder().intValue();
                if (sectionOrder > oldOrder.intValue() && sectionOrder <= newOrder.intValue()) {
                    section.setSectionOrder(new Integer(sectionOrder - 1));
                }
            }
        } else {
            for (ISection section : sectionsList) {
                int sectionOrder = section.getSectionOrder().intValue();
                if (sectionOrder >= newOrder.intValue() && sectionOrder < oldOrder.intValue()) {
                    section.setSectionOrder(new Integer(sectionOrder + 1));
                }
            }
        }
        return newOrder;
    }

    public void delete() throws DomainException {        
        
        IFileSuport fileSuport = FileSuport.getInstance();
        long size = 1;
        size = fileSuport.getDirectorySize(this.getSlideName());
        if (size > 0) {
            throw new DomainException(this.getClass().getName(), "");
        }

        ISection superiorSection = this.getSuperiorSection();
        ISite sectionSite = this.getSite();
        Integer sectionToDeleteOrder = this.getSectionOrder();
             
        //Delete Associated Items       
        if (this.getAssociatedItemsCount() != 0) {
            List<IItem> items = new ArrayList();
            items.addAll(this.getAssociatedItems());            
            for (IItem item : items) {
                item.delete();
            }            
        }
        
        //Delete Associated Sections
        if (this.getAssociatedSectionsCount() != 0) {
            List<ISection> sections = new ArrayList();
            sections.addAll(this.getAssociatedSections());
            for (ISection section : sections) {                
                section.delete();
            }
        }
        
        //Delete Associations with Site 
        this.setSite(null);
        
        //Delete Associations with Superior Section if exists
        if(superiorSection != null){           
            this.setSuperiorSection(null);
        }                     
                
        //ReOrder Sections                        
        List<ISection> sectionsReordered = getSections(superiorSection, sectionSite);
        
        for(ISection section : sectionsReordered) {
            Integer sectionOrder = section.getSectionOrder();
            if (sectionOrder.intValue() > sectionToDeleteOrder.intValue()) {
                section.setSectionOrder(new Integer(sectionOrder.intValue() - 1));
            }
        }
    }

    private List<ISection> getSections(ISection superiorSection, ISite site) {
        
        List<ISection> sections = new ArrayList();        
       
        if (superiorSection != null) {
            sections = superiorSection.getAssociatedSections();
        
        } else {
            List<ISection> sectionListAux = site.getAssociatedSections();
            for (ISection section : sectionListAux) {
                if (section.getSuperiorSection() == null)
                    sections.add(section);
            }
        }
        return sections;
    }
}
