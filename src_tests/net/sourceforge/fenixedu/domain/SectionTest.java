/*
 * Created on Jun 29, 2005
 *	by mrsp and jdnf
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class SectionTest extends DomainTestBase {

    Section section, section2, section3, section4, section5, section6;
    Site site;
    Item item, item2;
       
    protected void setUp() throws Exception {
        super.setUp();
                     
        site = new Site();
        site.setIdInternal(0);        
        
        //Secção Superior
        section = new Section();
        section.setIdInternal(0);
        section.setName(new MultiLanguageString(Language.pt, "SectionName"));
        section.setSectionOrder(0);
        section.setSite(site);        

        //Secção Inferior de Secção       
        section2 = new Section();
        section2.setIdInternal(1);
        section2.setName(new MultiLanguageString(Language.pt, "SectionName2"));        
        section2.setSectionOrder(0);
        section2.setSite(site);
        section2.setSuperiorSection(section);
        
        //Secção Inferior de Secção
        section3 = new Section();
        section3.setIdInternal(2);
        section3.setName(new MultiLanguageString(Language.pt, "SectionName3"));
        section3.setSectionOrder(1);
        section3.setSite(site);
        section3.setSuperiorSection(section);
        
        //Secção Inferior de Secção
        section4 = new Section();
        section4.setIdInternal(3);
        section4.setName(new MultiLanguageString(Language.pt, "SectionName4"));
        section4.setSectionOrder(2);
        section4.setSite(site);
        section4.setSuperiorSection(section);
        
        //Secção Superior
        section5 = new Section();
        section5.setIdInternal(4);
        section5.setName(new MultiLanguageString(Language.pt, "SectionName5"));
        section5.setSectionOrder(1);
        section5.setSite(site);  
        
        //Secção Superior
        section6 = new Section();
        section6.setIdInternal(5);
        section6.setName(new MultiLanguageString(Language.pt, "SectionName6"));
        section6.setSectionOrder(2);
        section6.setSite(site);  
               
        //Items associated to section3
        item = new Item();
        item.setIdInternal(0);
        item.setName(new MultiLanguageString(Language.pt, "ItemName"));
        item.setInformation(new MultiLanguageString(Language.pt, "ItemInformation"));
        item.setItemOrder(0);
        item.setSection(section3);
        
        item2 = new Item();
        item2.setIdInternal(1);
        item2.setName(new MultiLanguageString(Language.pt, "ItemName2"));
        item2.setInformation(new MultiLanguageString(Language.pt, "ItemInformation2"));
        item2.setItemOrder(1);
        item2.setSection(section3);             
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInsertItem() {
        try {
            section.insertItem(null, null, null);
            fail("Expected NullPointerException");
            
        } catch (NullPointerException e) {
            assertEquals("Size Unexpected", 0, section.getAssociatedItemsCount());
        } catch (DomainException e) {
            fail("Expected NullPointerException");
        }
        
        try {
            section.insertItem("ItemName1", "ItemInformation1", 0);          
            testInsertItemInformation(1, "ItemName1", "ItemInformation1", 0, 0);             
       
        } catch (DomainException e) {
            assertEquals("Size Unexpected", 0, section.getAssociatedItemsCount());
        }  
        
        //Test: insert an item with the same name of an existing one 
        try {
            section.insertItem("ItemName1", "ItemInformation111111", 0);
            fail("Expected DomainException");
          
        } catch (DomainException e) {            
            testInsertItemInformation(1, "ItemName1", "ItemInformation1", 0, 0);            
        }
        
        try {
            section.insertItem("ItemName2", "ItemInformation2", 1);           
            testInsertItemInformation(2, "ItemName2", "ItemInformation2", 1, 0);
                   
        } catch (DomainException e) {
            assertEquals("Size Unexpected", 1, section.getAssociatedItemsCount());
        }        
              
        try {
            section.insertItem("ItemName3", "ItemInformation3", 1);
            testInsertItemInformation(3, "ItemName3", "ItemInformation3", 1, 0);
                           
        } catch (DomainException e) {
            assertEquals("Size Unexpected", 2, section.getAssociatedItemsCount());
        }
        
        try {
            section.insertItem("ItemName4", "ItemInformation4", 2);            
            testInsertItemInformation(4, "ItemName4", "ItemInformation4", 2, 0);            
        
        } catch (DomainException e) {
            assertEquals("Size Unexpected", 3, section.getAssociatedItemsCount());
        }        
        
        // Test: Organize Existing Items Order
        assertEquals("Order Unexpected", 0, section.getAssociatedItems().get(3).getItemOrder().intValue());
        assertEquals("Order Unexpected", 3, section.getAssociatedItems().get(2).getItemOrder().intValue());
        assertEquals("Order Unexpected", 1, section.getAssociatedItems().get(1).getItemOrder().intValue());
        assertEquals("Order Unexpected", 2, section.getAssociatedItems().get(0).getItemOrder().intValue());
    }  
    
    public void testEdit(){
        try{
            section.edit((MultiLanguageString) null, Integer.valueOf(0));
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
        assertEquals("Order Unexpected", 0, site.getAssociatedSections().get(4).getSectionOrder().intValue());
        assertEquals("Order Unexpected", 1, site.getAssociatedSections().get(0).getSectionOrder().intValue());
        assertEquals("Order Unexpected", 2, site.getAssociatedSections().get(5).getSectionOrder().intValue());
        
        
        // Test: Organize SubSections Order
        section2.edit("NewSectionName2", 1);                             
        
        assertEquals("Name Unexpected", "NewSectionName2", section2.getName());
        assertEquals("Order Unexpected", 1, section2.getSectionOrder().intValue());
        
        assertEquals("Order Unexpected", 0, section.getAssociatedSections().get(1).getSectionOrder().intValue());
        assertEquals("Order Unexpected", 1, section.getAssociatedSections().get(0).getSectionOrder().intValue());
        assertEquals("Order Unexpected", 2, section.getAssociatedSections().get(2).getSectionOrder().intValue());
    }
    
    public void testDelete(){
        
        section3.delete();
                           
        // Test: Remove Items
        assertEquals("Size Unexpected", 0, section3.getAssociatedItemsCount());              
        assertNull("Section Unexpected", item.getSection());
        assertNull("Section Unexpected", item2.getSection());
        
        assertNull("Site Unexpected", section3.getSite());
        assertNull("Superior Section Unexpected", section3.getSuperiorSection());
                
        assertEquals("Size Unexpected", 2, section.getAssociatedSectionsCount());
        assertEquals("Size Unexpected", 5, site.getAssociatedSectionsCount());
        
        assertEquals("Name Unexpected", "SectionName2", section.getAssociatedSections().get(0).getName());
        assertEquals("Order Unexpected", 0, section.getAssociatedSections().get(0).getSectionOrder().intValue());
        
        assertEquals("Name Unexpected", "SectionName4", section.getAssociatedSections().get(1).getName());
        assertEquals("Order Unexpected", 1, section.getAssociatedSections().get(1).getSectionOrder().intValue());        
        
        assertEquals("Order Unexpected", 0, site.getAssociatedSections().get(0).getSectionOrder().intValue());
        assertEquals("Order Unexpected", 1, site.getAssociatedSections().get(3).getSectionOrder().intValue());
        assertEquals("Order Unexpected", 2, site.getAssociatedSections().get(4).getSectionOrder().intValue());
               
        section.delete();
        
        assertEquals("Size Unexpected", 2, site.getAssociatedSectionsCount());
        assertEquals("Size Unexpected", 0, section.getAssociatedSectionsCount());
        
        assertEquals("Name Unexpected", "SectionName5", site.getAssociatedSections().get(0).getName());
        assertEquals("Name Unexpected", "SectionName6", site.getAssociatedSections().get(1).getName());
        
        assertNull("Superior Section Unexpected", section2.getSuperiorSection());
        assertNull("Superior Section Unexpected", section4.getSuperiorSection());
        
        assertNull("Site Unexpected", section.getSite());
        assertNull("Site Unexpected", section2.getSite());
        assertNull("Site Unexpected", section4.getSite());        
    }
    
    private void testInsertItemInformation(int size, String name, String information, int order, int index){
        assertEquals("Size Unexpected", size, section.getAssociatedItemsCount());
        assertEquals("Name Unexpected", name, section.getAssociatedItems().get(index).getName());
        assertEquals("Information Unexpected", information, section.getAssociatedItems().get(index).getInformation());
        assertEquals("Order Unexpected", order, section.getAssociatedItems().get(index).getItemOrder().intValue());
    }
}
