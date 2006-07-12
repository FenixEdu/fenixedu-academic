/*
 * Created on 24/Jun/2005
 */
package net.sourceforge.fenixedu.domain;


public class SchoolClassTest extends DomainTestBase {

    SchoolClass schoolClass;

    Shift shift;

    protected void setUp() throws Exception {
        super.setUp();
        
//        schoolClass = new SchoolClass();
//        schoolClass.setIdInternal(0);        
//        shift = new Shift();
        shift.setIdInternal(0);
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
