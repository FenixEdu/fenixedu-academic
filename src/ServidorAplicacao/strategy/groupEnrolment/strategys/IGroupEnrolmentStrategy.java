/*
 * Created on 28/Jul/2003
 *
 */
package ServidorAplicacao.strategy.groupEnrolment.strategys;

import java.util.Calendar;
import java.util.List;

import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.ITurno;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author scpo and asnr
 *
 */
public interface IGroupEnrolmentStrategy {
	
	public boolean checkNumberOfGroups(IGroupProperties groupProperties,ITurno shift);
	public boolean checkEnrolmentDate(IGroupProperties groupProperties,Calendar actualDate);
	public boolean checkShiftType(IGroupProperties groupProperties,ITurno shift);
	public boolean checkNumberOfGroupElements(IGroupProperties groupProperties,IStudentGroup studentGroup) throws ExcepcaoPersistencia;
	public boolean checkIfStudentGroupIsEmpty(IStudentGroupAttend studentGroupAttend,IStudentGroup studentGroup) throws ExcepcaoPersistencia;
	public List checkShiftsType(IGroupProperties groupProperties,List shifts);
	public boolean checkPossibleToEnrolInExistingGroup(IGroupProperties groupProperties,IStudentGroup studentGroup,ITurno shift)throws ExcepcaoPersistencia; 
	public boolean enrolmentPolicyNewGroup(IGroupProperties groupProperties,int numberOfStudentsToEnrole,ITurno shift);
	public boolean checkAlreadyEnroled(IGroupProperties groupProperties, List usernames )throws ExcepcaoPersistencia;
}
