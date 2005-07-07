/*
 * Created on Jun 29, 2005
 *	by mrsp and jdnf
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.notAuthorizedServiceDeleteException;

import junit.framework.TestCase;

public class ItemTest extends DomainTestBase {

    ISection section;
    ISection sectionSuperior;
    ISite site;
    IItem item, item2, item3;
    IExecutionCourse executionCourse;
    IExecutionPeriod executionPeriod;
    IExecutionYear executionYear;

    protected void setUp() throws Exception {
        super.setUp();
        DomainObject.turnOffLockMode();
        
        executionYear = new ExecutionYear();
        
        executionPeriod = new ExecutionPeriod();
        executionPeriod.setExecutionYear(executionYear);
                
        executionCourse = new ExecutionCourse();
        executionCourse.setIdInternal(0);
        executionCourse.setExecutionPeriod(executionPeriod);
        
        site = new Site();            
        site.setIdInternal(0);
        site.setExecutionCourse(executionCourse);

        section = new Section();
        section.setIdInternal(0);
        section.setAssociatedItems(new ArrayList());
        section.setName("Section");
        section.setSite(site);
        
        item = new Item();
        item.setIdInternal(0);
        item.setName("ItemName");
        item.setInformation("ItemInformation");
        item.setUrgent(true);
        item.setItemOrder(0);
        item.setSection(section);
        
        item2 = new Item();
        item2.setIdInternal(0);
        item2.setName("ItemName2");
        item2.setInformation("ItemInformation2");
        item2.setUrgent(false);
        item2.setItemOrder(1);
        item2.setSection(section);

        item3 = new Item();
        item3.setIdInternal(0);
        item3.setName("ItemName3");
        item3.setInformation("ItemInformation3");
        item3.setUrgent(true);
        item3.setItemOrder(2);
        item3.setSection(section);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDeleteItem() {
        
        section.getAssociatedItems().add(item);  
        
        try {
            item.deleteItem();
            
        } catch (notAuthorizedServiceDeleteException e) {
            assertEquals("Size Unexpected", 1, section.getAssociatedItems().size());
        }
        
        assertEquals("Size Unexpected", 0, section.getAssociatedItems().size());
        assertNull("Section Unexpected", item.getSection());
    }

    public void testEditItem() {

        section.getAssociatedItems().add(item);
        section.getAssociatedItems().add(item2);
        section.getAssociatedItems().add(item3);
        
        item2.editItem("ItemName2New", "ItemInformation2New", false, 2);
        
        assertEquals("Name Unexpected", "ItemName2New", item2.getName());
        assertEquals("Information Unexpectfalseed", "ItemInformation2New", item2.getInformation());
        assertEquals("Urgent Unexpected", false, item2.getUrgent().booleanValue());
        assertEquals("Order Unexpected", 2, item2.getItemOrder().intValue());
        
        // Test Organize Items Order
        assertEquals("Order Unexpected", 0, item.getItemOrder().intValue());
        assertEquals("Order Unexpected", 2, item2.getItemOrder().intValue());   
        assertEquals("Order Unexpected", 1, item3.getItemOrder().intValue());   
    }
}
