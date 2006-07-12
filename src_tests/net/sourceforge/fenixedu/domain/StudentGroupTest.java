/*
 * Created on Sep 6, 2005
 *	by mrsp and jdnf
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class StudentGroupTest extends DomainTestBase {

    Grouping grouping;
    StudentGroup studentGroup;
    Attends attend;
    Shift shift, shift2;
    
    protected void setUp() throws Exception {
        super.setUp();
    
//        shift = new Shift();
//        shift.setIdInternal(0);
//        
//        shift2 = new Shift();
        shift2.setIdInternal(0);
        shift2.setTipo(ShiftType.LABORATORIAL);
        
        attend = new Attends();
        attend.setIdInternal(0);
                       
        studentGroup = new StudentGroup();
        studentGroup.setIdInternal(0);        
        studentGroup.addAttends(attend);
        studentGroup.setShift(shift);
        
        grouping = new Grouping();
        grouping.setIdInternal(0);
        grouping.addStudentGroups(studentGroup);
        grouping.setShiftType(ShiftType.TEORICA);
        
    }

    public void testDelete() {
        
        try{
            studentGroup.delete();
            fail("Expected DomainException");
        }
        catch(DomainException e){
            assertEquals("Size UnExpected", 1, grouping.getStudentGroupsCount());
            assertEquals("UnExpected StudentGroup", studentGroup, grouping.getStudentGroups().get(0));
            assertEquals("Size UnExpected", 1, shift.getAssociatedStudentGroupsCount());
            assertEquals("UnExpected StudentGroup", studentGroup, shift.getAssociatedStudentGroups().get(0));
         
        }
        
        studentGroup.removeAttends(attend);
        
        try{
            studentGroup.delete();                     
        }
        catch(DomainException e){
            fail("UnExpected DomainException");
        }
        
        assertEquals("Size UnExpected", 0, grouping.getStudentGroupsCount());
        assertNull("UnExpected StudentGroup", grouping.getStudentGroups().get(0));
        
        assertEquals("Size UnExpected", 0, shift.getAssociatedStudentGroupsCount());
        assertNull("UnExpected StudentGroup", shift.getAssociatedStudentGroups().get(0));
        
    }

    public void testEditShift() {
        try{
            studentGroup.editShift(shift2);
            fail("Expected DomainException");
        }
        catch(DomainException e){
            assertEquals("UnExpected Shift", shift, studentGroup.getShift());
        }
        
        shift2.setTipo(ShiftType.TEORICA);
        
        try{
            studentGroup.editShift(shift2);            
        }
        catch(DomainException e){
            fail("Expected DomainException");
        }
        
        assertEquals("UnExpected Shift", shift2, studentGroup.getShift());        
    }
}
