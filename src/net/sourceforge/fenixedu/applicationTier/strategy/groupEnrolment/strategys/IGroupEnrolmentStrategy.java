/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author scpo and asnr
 *
 */
public interface IGroupEnrolmentStrategy {
	
	public boolean checkNumberOfGroups(IGroupProperties groupProperties,IShift shift);
	public boolean checkEnrolmentDate(IGroupProperties groupProperties,Calendar actualDate);
	public boolean checkShiftType(IGroupProperties groupProperties,IShift shift);
	public boolean checkNumberOfGroupElements(IGroupProperties groupProperties,IStudentGroup studentGroup) throws ExcepcaoPersistencia;
	public boolean checkIfStudentGroupIsEmpty(IStudentGroupAttend studentGroupAttend,IStudentGroup studentGroup) throws ExcepcaoPersistencia;
	public List checkShiftsType(IGroupProperties groupProperties,List shifts);
	public boolean checkPossibleToEnrolInExistingGroup(IGroupProperties groupProperties,IStudentGroup studentGroup,IShift shift)throws ExcepcaoPersistencia; 
	public Integer enrolmentPolicyNewGroup(IGroupProperties groupProperties,int numberOfStudentsToEnrole,IShift shift);
	public boolean checkAlreadyEnroled(IGroupProperties groupProperties, String username )throws ExcepcaoPersistencia;
	public boolean checkNotEnroledInGroup(IGroupProperties groupProperties,IStudentGroup studentGroup, String username) throws ExcepcaoPersistencia;
	public boolean checkStudentInAttendsSet (IGroupProperties groupProperties, String username)throws ExcepcaoPersistencia;
	public boolean checkStudentsInAttendsSet (List studentCodes, IGroupProperties groupProperties)throws ExcepcaoPersistencia;
	public boolean checkHasShift(IGroupProperties groupProperties);
	public boolean checkStudentsUserNamesInAttendsSet (List studentUsernames, IGroupProperties groupProperties)throws ExcepcaoPersistencia;
}
