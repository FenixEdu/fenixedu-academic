/*
 * Created on 28/Jul/2003
 *
 */
package ServidorAplicacao.strategy.groupEnrolment.strategys;

import java.util.Calendar;

import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;

/**
 * @author scpo and asnr
 *
 */
public interface IGroupEnrolmentStrategy {
	
	public boolean checkNumberOfGroups(IGroupProperties groupProperties);
	public boolean checkEnrolmentDate(IGroupProperties groupProperties,Calendar actualDate);
	public boolean checkShiftType(IGroupProperties groupProperties,ITurno shift);
	public boolean enrolmentPolicy(IGroupProperties groupProperties,int numberOfStudentsToEnrole,IStudentGroup studentGroup);
	
}
