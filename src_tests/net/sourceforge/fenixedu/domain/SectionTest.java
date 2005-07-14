/*
 * Created on Jun 29, 2005
 *	by mrsp and jdnf
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class SectionTest extends DomainTestBase {

    ISection section, section2, section3, section4, section5, section6;
    ISite site;
    IItem item, item2;
       
    protected void setUp() throws Exception {
        super.setUp();
                     
        site = new Site();
        site.setIdInternal(0);        
        
        //Secção Superior
        section = new Section();
        section.setIdInternal(0);
        section.setName("SectionName");
        section.setSectionOrder(0);
        section.setSite(site);        

        //Secção Inferior de Secção       
        section2 = new Section();
        section2.setIdInternal(1);
        section2.setName("SectionName2");        
        section2.setSectionOrder(0);
        section2.setSite(site);
        section2.setSuperiorSection(section);
        
        //Secção Inferior de Secção
        section3 = new Section();
        section3.setIdInternal(2);
        section3.setName("SectionName3");
        section3.setSectionOrder(1);
        section3.setSite(site);
        section3.setSuperiorSection(section);
        
        //Secção Inferior de Secção
        section4 = new Section();
        section4.setIdInternal(3);
        section4.setName("SectionName4");
        section4.setSectionOrder(2);
        section4.setSite(site);
        section4.setSuperiorSection(section);
        
        //Secção Superior
        section5 = new Section();
        section5.setIdInternal(4);
        section5.setName("SectionName5");
        section5.setSectionOrder(1);
        section5.setSite(site);  
        
        //Secção Superior
        section6 = new Section();
        section6.setIdInternal(5);
        section6.setName("SectionName6");
        section6.setSectionOrder(2);
        section6.setSite(site);  
               
        //Items associated to section3
        item = new Item();
        item.setIdInternal(0);
        item.setName("ItemName");
        item.setInformation("ItemInformation");
        item.setUrgent(true);
        item.setItemOrder(0);
        item.setSection(section3);
        
        item2 = new Item();
        item2.setIdInternal(1);
        item2.setName("ItemName2");
        item2.setInformation("ItemInformation2");
        item2.setUrgent(false);
        item2.setItemOrder(1);
        item2.setSection(section3);             
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInsertItem() {
        try {
            section.insertItem(null, null, null, null);
            fail("Expected NullPointerException");
            
        } catch (NullPointerException e) {
            assertEquals("Size Unexpected", 0, section.getAssociatedItemsCount());
        } catch (DomainException e) {
            assertEquals("Size Unexpected", 0, section.getAssociatedItemsCount());
        }
        
        try {
            section.insertItem("ItemName1", "ItemInformation1", true, 0);
          
            assertEquals("Size Unexpected", 1, section.getAssociatedItemsCount());
            assertEquals("Name Unexpected", "ItemName1", section.getAssociatedItems(0).getName());
            assertEquals("Information Unexpected", "ItemInformation1", section.getAssociatedItems(0).getInformation());
            assertEquals("Urgent Unexpected", true, section.getAssociatedItems(0).getUrgent().booleanValue());
            assertEquals("Order Unexpected", 0, section.getAssociatedItems(0).getItemOrder().intValue());       
       
        } catch (DomainException e) {
            assertEquals("Size Unexpected", 0, section.getAssociatedItemsCount());
        }      
        
        try {
            section.insertItem("ItemName2", "ItemInformation2", false, 1);
            
            assertEquals("Size Unexpected", 2, section.getAssociatedItemsCount());
            assertEquals("Name Unexpected", "ItemName2", section.getAssociatedItems(1).getName());
            assertEquals("Information Unexpected", "ItemInformation2", section.getAssociatedItems(1).getInformation());
            assertEquals("Urgent Unexpected", false, section.getAssociatedItems(1).getUrgent().booleanValue());
            assertEquals("Order Unexpected", 1, section.getAssociatedItems(1).getItemOrder().intValue());
        
        } catch (DomainException e) {
            assertEquals("Size Unexpected", 1, section.getAssociatedItemsCount());
        }
        
              
        try {
            section.insertItem("ItemName3", "ItemInformation3", true, 1);
            
            assertEquals("Size Expected", 3, section.getAssociatedItemsCount());
            assertEquals("Name Expected", "ItemName3", section.getAssociatedItems(2).getName());
            assertEquals("Information Expected", "ItemInformation3", section.getAssociatedItems(2).getInformation());
            assertEquals("Urgent Expected", true, section.getAssociatedItems(2).getUrgent().booleanValue());
            assertEquals("Order Expected", 1, section.getAssociatedItems(2).getItemOrder().intValue());           
        
        } catch (DomainException e) {
            assertEquals("Size Unexpected", 2, section.getAssociatedItemsCount());
        }
        
        try {
            section.insertItem("ItemName4", "ItemInformation4", false, 2);
            
            assertEquals("Size Unexpected", 4, section.getAssociatedItemsCount());
            assertEquals("Name Unexpected", "ItemName4", section.getAssociatedItems(3).getName());
            assertEquals("Information Unexpected", "ItemInformation4", section.getAssociatedItems(3).getInformation());
            assertEquals("Urgent Unexpected", false, section.getAssociatedItems(3).getUrgent().booleanValue());
            assertEquals("Order Unexpected", 2, section.getAssociatedItems(3).getItemOrder().intValue());
        
        } catch (DomainException e) {
            assertEquals("Size Unexpected", 3, section.getAssociatedItemsCount());
        }        
        
        // Test: Organize Existing Items Order
        assertEquals("Order Unexpected", 0, section.getAssociatedItems(0).getItemOrder().intValue());
        assertEquals("Order Unexpected", 3, section.getAssociatedItems(1).getItemOrder().intValue());
        assertEquals("Order Unexpected", 1, section.getAssociatedItems(2).getItemOrder().intValue());
        assertEquals("Order Unexpected", 2, section.getAssociatedItems(3).getItemOrder().intValue());
    }  
    
    public void testEditSection(){
        try{
            section.edit(null, null);
            fail("Expected NullPointerException");
        }
        catch(NullPointerException e){
            assertEquals("Name Unexpected", "SectionName", section.getName());
            assertEquals("Order Unexpected", 0, section.getSectionOrder().intValue());
        }
              
        section.edit("NewSectionName", 1);
        
        assertEquals("Name Unexpected", "NewSectionName", section.getName());
        assertEquals("Order Unexpected", 1, section.getSectionOrder().intValue());        
        
        // Test: Organize Superior Sections 
        assertEquals("Order Unexpected", 0, site.getAssociatedSections(4).getSectionOrder().intValue());
        assertEquals("Order Unexpected", 1, site.getAssociatedSections(0).getSectionOrder().intValue());
        assertEquals("Order Unexpected", 2, site.getAssociatedSections(5).getSectionOrder().intValue());
        
        
        // Test: Organize SubSections Order
        section2.edit("NewSectionName2", 1);                             
        
        assertEquals("Name Unexpected", "NewSectionName2", section2.getName());
        assertEquals("Order Unexpected", 1, section2.getSectionOrder().intValue());
        
        assertEquals("Order Unexpected", 0, section.getAssociatedSections(1).getSectionOrder().intValue());
        assertEquals("Order Unexpected", 1, section.getAssociatedSections(0).getSectionOrder().intValue());
        assertEquals("Order Unexpected", 2, section.getAssociatedSections(2).getSectionOrder().intValue());
    }
    
    public void testDeleteSection(){
        
        section3.delete();
                           
        // Test: Remove Items
        assertEquals("Size Unexpected", 0, section3.getAssociatedItemsCount());              
        assertNull("Section Unexpected", item.getSection());
        assertNull("Section Unexpected", item2.getSection());
        
        assertNull("Site Unexpected", section3.getSite());
        assertNull("Superior Section Unexpected", section3.getSuperiorSection());
                
        assertEquals("Size Unexpected", 2, section.getAssociatedSectionsCount());
        assertEquals("Size Unexpected", 5, site.getAssociatedSectionsCount());
        
        assertEquals("Name Unexpected", "SectionName2", section.getAssociatedSections(0).getName());
        assertEquals("Order Unexpected", 0, section.getAssociatedSections(0).getSectionOrder().intValue());
        
        assertEquals("Name Unexpected", "SectionName4", section.getAssociatedSections(1).getName());
        assertEquals("Order Unexpected", 1, section.getAssociatedSections(1).getSectionOrder().intValue());        
        
        assertEquals("Order Unexpected", 0, site.getAssociatedSections(0).getSectionOrder().intValue());
        assertEquals("Order Unexpected", 1, site.getAssociatedSections(3).getSectionOrder().intValue());
        assertEquals("Order Unexpected", 2, site.getAssociatedSections(4).getSectionOrder().intValue());
               
        section.delete();
        
        assertEquals("Size Unexpected", 2, site.getAssociatedSectionsCount());
        assertEquals("Size Unexpected", 0, section.getAssociatedSectionsCount());
        
        assertEquals("Name Unexpected", "SectionName5", site.getAssociatedSections(0).getName());
        assertEquals("Name Unexpected", "SectionName6", site.getAssociatedSections(1).getName());
        
        assertNull("Superior Section Unexpected", section2.getSuperiorSection());
        assertNull("Superior Section Unexpected", section4.getSuperiorSection());
        
        assertNull("Site Unexpected", section.getSite());
        assertNull("Site Unexpected", section2.getSite());
        assertNull("Site Unexpected", section4.getSite());        
    }
}
