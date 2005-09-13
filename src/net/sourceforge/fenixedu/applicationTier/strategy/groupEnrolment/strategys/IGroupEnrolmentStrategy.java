/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author scpo and asnr
 *
 */
public interface IGroupEnrolmentStrategy {
	
	public boolean checkNumberOfGroups(IGrouping grouping,IShift shift);
	public boolean checkEnrolmentDate(IGrouping grouping,Calendar actualDate);
	public boolean checkShiftType(IGrouping grouping,IShift shift);
	public boolean checkNumberOfGroupElements(IGrouping grouping,IStudentGroup studentGroup) throws ExcepcaoPersistencia;
    public boolean checkIfStudentGroupIsEmpty(IAttends attend, IStudentGroup studentGroup);
	public List checkShiftsType(IGrouping groupProperties,List shifts);
	public boolean checkPossibleToEnrolInExistingGroup(IGrouping grouping,IStudentGroup studentGroup)throws ExcepcaoPersistencia;
	public Integer enrolmentPolicyNewGroup(IGrouping grouping,int numberOfStudentsToEnrole,IShift shift);
	public boolean checkAlreadyEnroled(IGrouping grouping, String username);
	public boolean checkNotEnroledInGroup(IGrouping grouping,IStudentGroup studentGroup, String username) throws ExcepcaoPersistencia;
	public boolean checkStudentInGrouping (IGrouping grouping, String username)throws ExcepcaoPersistencia;
	public boolean checkHasShift(IGrouping grouping);
    public boolean checkStudentsUserNamesInGrouping(List<String> studentUsernames, IGrouping grouping);
}
