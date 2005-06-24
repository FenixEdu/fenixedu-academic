/*
 * Created on 24/Jun/2005
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;

import junit.framework.TestCase;

public class SchoolClassTest extends TestCase {

    ISchoolClass schoolClass;

    IShift shift;

    protected void setUp() throws Exception {
        super.setUp();
        
        schoolClass = new SchoolClass();
        schoolClass.setIdInternal(0);
        schoolClass.setAssociatedShifts(new ArrayList());
        shift = new Shift();
        shift.setIdInternal(0);
        shift.setAssociatedClasses(new ArrayList());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAssociateShift() {
        try {
            schoolClass.associateShift(null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected!", 0, schoolClass.getAssociatedShifts().size());
        }
        
        schoolClass.associateShift(shift);
        assertEquals("Size unexpected!", 1, schoolClass.getAssociatedShifts().size());
        assertEquals("Size unexpected!", 1, shift.getAssociatedClasses().size());
        
        schoolClass.associateShift(shift);
        assertEquals("Size unexpected!", 1, schoolClass.getAssociatedShifts().size());
        assertEquals("Size unexpected!", 1, shift.getAssociatedClasses().size());
        
        
    }

}
