/*
 * Created on 23/Jun/2005
 */
package net.sourceforge.fenixedu.domain;


public class ShiftTest extends DomainTestBase {

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

    public void testAssociateSchoolClass() {
        try {
            shift.associateSchoolClass(null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected!", 0, shift.getAssociatedClasses().size());
        }
        
        shift.associateSchoolClass(schoolClass);
        assertEquals("Size unexpected!", 1, shift.getAssociatedClasses().size());
        assertEquals("Size unexpected!", 1, schoolClass.getAssociatedShifts().size());
        
        shift.associateSchoolClass(schoolClass);
        assertEquals("Size unexpected!", 1, shift.getAssociatedClasses().size());
        assertEquals("Size unexpected!", 1, schoolClass.getAssociatedShifts().size());
    }

}
