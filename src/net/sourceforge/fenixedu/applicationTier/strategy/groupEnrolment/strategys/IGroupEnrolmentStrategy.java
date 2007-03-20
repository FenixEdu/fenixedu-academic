/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author scpo and asnr
 *
 */
public interface IGroupEnrolmentStrategy {
    	
    public boolean checkNumberOfGroups(Grouping grouping,Shift shift);
    public boolean checkEnrolmentDate(Grouping grouping,Calendar actualDate);
    public boolean checkShiftType(Grouping grouping,Shift shift);
    public boolean checkNumberOfGroupElements(Grouping grouping,StudentGroup studentGroup) throws ExcepcaoPersistencia;
    public boolean checkIfStudentGroupIsEmpty(Attends attend, StudentGroup studentGroup);
    public List checkShiftsType(Grouping groupProperties,List shifts);
    public boolean checkPossibleToEnrolInExistingGroup(Grouping grouping,StudentGroup studentGroup)throws ExcepcaoPersistencia;
    public Integer enrolmentPolicyNewGroup(Grouping grouping,int numberOfStudentsToEnrole,Shift shift);
    public boolean checkAlreadyEnroled(Grouping grouping, String username);
    public boolean checkNotEnroledInGroup(Grouping grouping,StudentGroup studentGroup, String username) throws ExcepcaoPersistencia;
    public boolean checkStudentInGrouping (Grouping grouping, String username)throws ExcepcaoPersistencia;
    public boolean checkHasShift(Grouping grouping);
    public boolean checkStudentsUserNamesInGrouping(List<String> studentUsernames, Grouping grouping);
}
