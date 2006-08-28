/*
 * Created on Jun 29, 2005
 *	by mrsp and jdnf
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.MultiLanguageString;


public class ItemTest extends DomainTestBase {

    private Section section;
    private Site site;
    private Item item, item2, item3;

    protected void setUp() throws Exception {
        super.setUp();
                        
        site = new Site();            
        site.setIdInternal(0);        

        section = new Section();
        section.setIdInternal(0);
        section.setName(new MultiLanguageString(Language.pt, "Section"));
        section.setSite(site);
        
        item = new Item();
        item.setIdInternal(0);
        item.setName(new MultiLanguageString(Language.pt, "ItemName"));
        item.setInformation(new MultiLanguageString(Language.pt, "ItemInformation"));
        item.setSection(section);
        item.setItemOrder(0);      
        
        item2 = new Item();
        item2.setIdInternal(1);
        item2.setName(new MultiLanguageString(Language.pt, "ItemName2"));
        item2.setInformation(new MultiLanguageString(Language.pt, "ItemInformation2"));
        item2.setSection(section);
        item2.setItemOrder(1);        

        item3 = new Item();
        item3.setIdInternal(2);
        item3.setName(new MultiLanguageString(Language.pt, "ItemName3"));
        item3.setInformation(new MultiLanguageString(Language.pt, "ItemInformation3"));
        item3.setSection(section);
        item3.setItemOrder(2);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDelete() {
                                                      
        item.delete();            
        
        assertEquals("Size Unexpected", 2, section.getAssociatedItemsCount());
        assertNull("Section Unexpected", item.getSection());  
        
        // Test Organize Items Order
        assertEquals("Order Unexpected", 0, item2.getItemOrder().intValue());
        assertEquals("Order Unexpected", 1, item3.getItemOrder().intValue());        
        assertEquals("Order Unexpected", 0, section.getAssociatedItems().get(0).getItemOrder().intValue());
        assertEquals("Order Unexpected", 1, section.getAssociatedItems().get(1).getItemOrder().intValue());               
        
        item2.delete();          
                
        assertEquals("Size Unexpected", 1, section.getAssociatedItemsCount());
        assertNull("Section Unexpected", item2.getSection());
        
        //Test Organize Items Order
        assertEquals("Order Unexpected", 0, item3.getItemOrder().intValue());        
        assertEquals("Order Unexpected", 0, section.getAssociatedItems().get(0).getItemOrder().intValue());
                
        item3.delete();
            
        assertEquals("Size Unexpected", 0, section.getAssociatedItemsCount());
        assertNull("Section Unexpected", item3.getSection());                
    }

    public void testEdit() {
          
        try{
            item2.edit((MultiLanguageString) null, null, null);
            fail("Expected NullPointerException");
        }
        catch(NullPointerException e){
            assertEquals("Name Unexpected", "ItemName2", item2.getName());
            assertEquals("Information Unexpected", "ItemInformation2", item2.getInformation());
        }
        
        item2.edit("ItemName2New", "ItemInformation2New", 2);
        
        testEditInformation("ItemName2New","ItemInformation2New" , false);       
        testItemsOrder(0, 2, 1);            
        
        item2.edit("ItemName22New", "ItemInformation22New", 0);
        
        testEditInformation("ItemName22New","ItemInformation22New" , false);              
        testItemsOrder(1, 0, 2);        
        
        item2.edit("ItemName222New", "ItemInformation222New", 1);
        
        testEditInformation("ItemName222New","ItemInformation222New" , true);                       
        testItemsOrder(0, 1, 2);        
    }
    
    private void testEditInformation(String name, String information, boolean urgent){
        assertEquals("Name Unexpected", name, item2.getName());
        assertEquals("Information Unexpected", information, item2.getInformation());
    }
    
    private void testItemsOrder(int value1, int value2, int value3){
        assertEquals("Order Unexpected", value1, item.getItemOrder().intValue());
        assertEquals("Order Unexpected", value2, item2.getItemOrder().intValue());   
        assertEquals("Order Unexpected", value3, item3.getItemOrder().intValue());   
        
        assertEquals("Order Unexpected", value1, section.getAssociatedItems().get(0).getItemOrder().intValue());
        assertEquals("Order Unexpected", value2, section.getAssociatedItems().get(1).getItemOrder().intValue());   
        assertEquals("Order Unexpected", value3, section.getAssociatedItems().get(2).getItemOrder().intValue());
    }
}
