/*
 * Created on 24/Jul/2003
 *
 */
package ServidorAplicacao.strategy.groupEnrolment.strategys;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */

public abstract class GroupEnrolmentStrategy implements IGroupEnrolmentStrategy{

	private IGroupProperties groupProperties = null;
	private int numberOfStudentsToEnrole;
	private IStudentGroup studentGroup = null;
	private ITurno shift = null;
	
	public IGroupProperties getGroupProperties() {
		return groupProperties;
	}

	public void setGroupProperties(IGroupProperties groupProperties) {
		this.groupProperties = groupProperties;
	}

	public int getNumberOfStudentsToEnrole() {
		return numberOfStudentsToEnrole;
	}

	public void setNumberOfStudentsToEnrole(int numberOfStudentsToEnrole) {
		this.numberOfStudentsToEnrole = numberOfStudentsToEnrole;
	}

	public IStudentGroup getStudentGroup() {
		return studentGroup;
	}

	public void setStudentGroup(IStudentGroup studentGroup) {
		this.studentGroup = studentGroup;
	}
	
	public ITurno getShift() {
		return shift;
	}

	public void setShift(ITurno shift) {
		this.shift = shift;
	}
		
	public boolean checkNumberOfGroups(IGroupProperties groupProperties,ITurno shift)
	{
		boolean result = false;
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentStudentGroup persistentStudentGroup = sp.getIPersistentStudentGroup();
			List groups = persistentStudentGroup.readAllStudentGroupByGroupPropertiesAndShift(groupProperties,shift);
			int numberOfGroups = groups.size();
			if(numberOfGroups < groupProperties.getGroupMaximumNumber().intValue())
				result = true;
		
		}catch (ExcepcaoPersistencia e) {
		 }
		return result;
	}
		
	public boolean checkEnrolmentDate(IGroupProperties groupProperties,Calendar actualDate)
	{
		Long actualDateInMills = new Long(actualDate.getTimeInMillis());
		Long enrolmentBeginDayInMills = null;
		Long enrolmentEndDayInMills = null;
		
		if(groupProperties.getEnrolmentBeginDay()!=null)
			enrolmentBeginDayInMills = new Long(groupProperties.getEnrolmentBeginDay().getTimeInMillis());
		
		if(groupProperties.getEnrolmentEndDay()!=null)
			enrolmentEndDayInMills =new Long(groupProperties.getEnrolmentEndDay().getTimeInMillis());
		
		if(enrolmentBeginDayInMills==null && enrolmentEndDayInMills==null)
		 	return true;
		
		if(enrolmentBeginDayInMills!=null && enrolmentEndDayInMills==null)
		{	
			if(actualDateInMills.compareTo(enrolmentBeginDayInMills)>0)		
				return true;
		}
		
		if(enrolmentBeginDayInMills==null && enrolmentEndDayInMills!=null)
		{	
			if(actualDateInMills.compareTo(enrolmentEndDayInMills)<0)		
				return true;
		}
		
		if(actualDateInMills.compareTo(enrolmentBeginDayInMills)>0 && actualDateInMills.compareTo(enrolmentEndDayInMills)<0 )
			return true;
		
		return false;
	}
		
	
	public boolean checkShiftType(IGroupProperties groupProperties,ITurno shift)
	{
		boolean result = false;
		if(shift.getTipo().equals(groupProperties.getShiftType()))
			result = true;
		
		return result;
	}
	
	
	public boolean checkNumberOfGroupElements(IGroupProperties groupProperties,IStudentGroup studentGroup) throws ExcepcaoPersistencia {
		
		boolean result = false;
				
		List allStudentGroupAttend = new ArrayList();
		try {
		
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentStudentGroupAttend persistentStudentGroupAttend = persistentSuport.getIPersistentStudentGroupAttend();
		
		allStudentGroupAttend = (List) persistentStudentGroupAttend.readAllByStudentGroup(studentGroup);
		
		} catch (ExcepcaoPersistencia ex){
			throw ex;
		}	
		
		int numberOfGroupElements = allStudentGroupAttend.size();
		
//		se o nr de elementos do grupo for maior que o nr minimo,entao podemos desinscrever o aluno,caso contrario nao
		if(numberOfGroupElements > groupProperties.getMinimumCapacity().intValue())
			result = true;
		
		return result;
	}

	
	public abstract boolean enrolmentPolicy(IGroupProperties groupProperties,int numberOfStudentsToEnrole,IStudentGroup studentGroup,ITurno shift);
}
