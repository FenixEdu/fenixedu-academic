/*
 * Created on Oct 3, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

public enum FunctionType {
    
    PRESIDENT,
    ASSISTANCE_PRESIDENT,
    VICE_PRESIDENT,
    SECRETARY,
    CHIEF,       
    DIRECTOR,
    COORDINATOR,
    EMPLOYEE,    
    NON_TEACHING_EMPLOYEE,
    TEACHER_MEMBER,
    STUDENT_MEMBER,
    EMPLOYEE_MEMBER,
    REPRESENTATIVE,
    VOWEL,
    TEACER_VOWEL;
         
    public String getName() {
        return name();
    }    
}
