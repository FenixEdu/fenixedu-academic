/*
 * Created on Jun 29, 2005
 *	by mrsp and jdnf
 */
package net.sourceforge.fenixedu.domain;


public class ItemTest extends DomainTestBase {

    private ISection section;
    private ISection sectionSuperior;
    private ISite site;
    private IItem item, item2, item3;

    protected void setUp() throws Exception {
        super.setUp();
                        
        site = new Site();            
        site.setIdInternal(0);        

        section = new Section();
        section.setIdInternal(0);
        section.setName("Section");
        section.setSite(site);
        
        item = new Item();
        item.setIdInternal(0);
        item.setName("ItemName");
        item.setInformation("ItemInformation");
        item.setUrgent(true);
        item.setSection(section);
        item.setItemOrder(0);      
        
        item2 = new Item();
        item2.setIdInternal(1);
        item2.setName("ItemName2");
        item2.setInformation("ItemInformation2");
        item2.setUrgent(false);
        item2.setSection(section);
        item2.setItemOrder(1);        

        item3 = new Item();
        item3.setIdInternal(2);
        item3.setName("ItemName3");
        item3.setInformation("ItemInformation3");
        item3.setUrgent(true);
        item3.setSection(section);
        item3.setItemOrder(2);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDeleteItem() {
                                                      
        item.delete();            
        
        assertEquals("Size Unexpected", 2, section.getAssociatedItemsCount());
        assertNull("Section Unexpected", item.getSection());  
        
        // Test Organize Items Order
        assertEquals("Order Unexpected", 0, item2.getItemOrder().intValue());
        assertEquals("Order Unexpected", 1, item3.getItemOrder().intValue());        
        assertEquals("Order Unexpected", 0, section.getAssociatedItems(0).getItemOrder().intValue());
        assertEquals("Order Unexpected", 1, section.getAssociatedItems(1).getItemOrder().intValue());               
        
        item2.delete();          
                
        assertEquals("Size Unexpected", 1, section.getAssociatedItemsCount());
        assertNull("Section Unexpected", item2.getSection());
        
        //Test Organize Items Order
        assertEquals("Order Unexpected", 0, item3.getItemOrder().intValue());        
        assertEquals("Order Unexpected", 0, section.getAssociatedItems(0).getItemOrder().intValue());
                
        item3.delete();
            
        assertEquals("Size Unexpected", 0, section.getAssociatedItemsCount());
        assertNull("Section Unexpected", item3.getSection());                
    }

    public void testEditItem() {
          
        try{
            item2.edit(null, null, null, null);
            fail("Expected NullPointerException");
        }
        catch(NullPointerException e){
            assertEquals("Name Unexpected", "ItemName2", item2.getName());
            assertEquals("Information Unexpected", "ItemInformation2", item2.getInformation());
        }
        
        item2.edit("ItemName2New", "ItemInformation2New", false, 2);
        
        assertEquals("Name Unexpected", "ItemName2New", item2.getName());
        assertEquals("Information Unexpected", "ItemInformation2New", item2.getInformation());
        assertEquals("Urgent Unexpected", false, item2.getUrgent().booleanValue());
        
        // Test Organize Items Order
        assertEquals("Order Unexpected", 0, item.getItemOrder().intValue());
        assertEquals("Order Unexpected", 2, item2.getItemOrder().intValue());   
        assertEquals("Order Unexpected", 1, item3.getItemOrder().intValue());   
        
        assertEquals("Order Unexpected", 0, section.getAssociatedItems(0).getItemOrder().intValue());
        assertEquals("Order Unexpected", 2, section.getAssociatedItems(1).getItemOrder().intValue());   
        assertEquals("Order Unexpected", 1, section.getAssociatedItems(2).getItemOrder().intValue());
        
        item2.edit("ItemName22New", "ItemInformation22New", false, 0);
        
        assertEquals("Name Unexpected", "ItemName22New", item2.getName());
        assertEquals("Information Unexpectfalseed", "ItemInformation22New", item2.getInformation());
        assertEquals("Urgent Unexpected", false, item2.getUrgent().booleanValue());
        
        // Test Organize Items Order
        assertEquals("Order Unexpected", 1, item.getItemOrder().intValue());
        assertEquals("Order Unexpected", 0, item2.getItemOrder().intValue());   
        assertEquals("Order Unexpected", 2, item3.getItemOrder().intValue());   
        
        assertEquals("Order Unexpected", 1, section.getAssociatedItems(0).getItemOrder().intValue());
        assertEquals("Order Unexpected", 0, section.getAssociatedItems(1).getItemOrder().intValue());   
        assertEquals("Order Unexpected", 2, section.getAssociatedItems(2).getItemOrder().intValue());
        
        item2.edit("ItemName222New", "ItemInformation222New", true, 1);
        
        assertEquals("Name Unexpected", "ItemName222New", item2.getName());
        assertEquals("Information Unexpectfalseed", "ItemInformation222New", item2.getInformation());
        assertEquals("Urgent Unexpected", true, item2.getUrgent().booleanValue());
        
        // Test Organize Items Order
        assertEquals("Order Unexpected", 0, item.getItemOrder().intValue());
        assertEquals("Order Unexpected", 1, item2.getItemOrder().intValue());   
        assertEquals("Order Unexpected", 2, item3.getItemOrder().intValue());   
        
        assertEquals("Order Unexpected", 0, section.getAssociatedItems(0).getItemOrder().intValue());
        assertEquals("Order Unexpected", 1, section.getAssociatedItems(1).getItemOrder().intValue());   
        assertEquals("Order Unexpected", 2, section.getAssociatedItems(2).getItemOrder().intValue());
    }
}
