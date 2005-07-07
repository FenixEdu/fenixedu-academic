/*
 * Created on Jun 29, 2005
 *	by mrsp and jdnf
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;

public class SectionTest extends DomainTestBase {

    ISection section;
    
    protected void setUp() throws Exception {
        super.setUp();
        DomainObject.turnOffLockMode();
        
        section = new Section();
        section.setIdInternal(0);
        section.setAssociatedItems(new ArrayList());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInsertItem() {
        try {
            section.insertItem(null, null, null, null);
            fail("Expected NullPointerException");
            
        } catch (NullPointerException e) {
            assertEquals("Size Unexpected", 0, section.getAssociatedItems().size());
        } catch (ExistingServiceException e) {
            assertEquals("Size Unexpected", 0, section.getAssociatedItems().size());
        }
        
        try {
            section.insertItem("ItemName1", "ItemInformation1", true, 0);
            assertEquals("Size Unexpected", 1, section.getAssociatedItems().size());
            assertEquals("Name Unexpected", "ItemName1", ((IItem)section.getAssociatedItems().get(0)).getName());
            assertEquals("Information Unexpected", "ItemInformation1", ((IItem)section.getAssociatedItems().get(0)).getInformation());
            assertEquals("Urgent Unexpected", true, ((IItem)section.getAssociatedItems().get(0)).getUrgent().booleanValue());
            assertEquals("Order Unexpected", 0, ((IItem)section.getAssociatedItems().get(0)).getItemOrder().intValue());       
        } catch (ExistingServiceException e) {
            assertEquals("Size Unexpected", 0, section.getAssociatedItems().size());
        }      
        
        try {
            section.insertItem("ItemName2", "ItemInformation2", false, 1);
            assertEquals("Size Unexpected", 2, section.getAssociatedItems().size());
            assertEquals("Name Unexpected", "ItemName2", ((IItem)section.getAssociatedItems().get(1)).getName());
            assertEquals("Information Unexpected", "ItemInformation2", ((IItem)section.getAssociatedItems().get(1)).getInformation());
            assertEquals("Urgent Unexpected", false, ((IItem)section.getAssociatedItems().get(1)).getUrgent().booleanValue());
            assertEquals("Order Unexpected", 1, ((IItem)section.getAssociatedItems().get(1)).getItemOrder().intValue());
        } catch (ExistingServiceException e) {
            assertEquals("Size Unexpected", 1, section.getAssociatedItems().size());
        }
        
              
        try {
            section.insertItem("ItemName3", "ItemInformation3", true, 1);
            assertEquals("Size Expected", 3, section.getAssociatedItems().size());
            assertEquals("Name Expected", "ItemName3", ((IItem)section.getAssociatedItems().get(2)).getName());
            assertEquals("Information Expected", "ItemInformation3", ((IItem)section.getAssociatedItems().get(2)).getInformation());
            assertEquals("Urgent Expected", true, ((IItem)section.getAssociatedItems().get(2)).getUrgent().booleanValue());
            assertEquals("Order Expected", 1, ((IItem)section.getAssociatedItems().get(2)).getItemOrder().intValue());           
        } catch (ExistingServiceException e) {
            assertEquals("Size Unexpected", 2, section.getAssociatedItems().size());
        }
        
        try {
            section.insertItem("ItemName4", "ItemInformation4", false, 2);
            assertEquals("Size Unexpected", 4, section.getAssociatedItems().size());
            assertEquals("Name Unexpected", "ItemName4", ((IItem)section.getAssociatedItems().get(3)).getName());
            assertEquals("Information Unexpected", "ItemInformation4", ((IItem)section.getAssociatedItems().get(3)).getInformation());
            assertEquals("Urgent Unexpected", false, ((IItem)section.getAssociatedItems().get(3)).getUrgent().booleanValue());
            assertEquals("Order Unexpected", 2, ((IItem)section.getAssociatedItems().get(3)).getItemOrder().intValue());
        } catch (ExistingServiceException e) {
            assertEquals("Size Unexpected", 3, section.getAssociatedItems().size());
        }        
        
        // Test: Organize Existing Items Order
        assertEquals("Order Unexpected", 0, ((IItem)section.getAssociatedItems().get(0)).getItemOrder().intValue());
        assertEquals("Order Unexpected", 3, ((IItem)section.getAssociatedItems().get(1)).getItemOrder().intValue());
        assertEquals("Order Unexpected", 1, ((IItem)section.getAssociatedItems().get(2)).getItemOrder().intValue());
        assertEquals("Order Unexpected", 2, ((IItem)section.getAssociatedItems().get(3)).getItemOrder().intValue());
    }  
}
