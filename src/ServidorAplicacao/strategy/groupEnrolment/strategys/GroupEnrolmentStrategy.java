/*
 * Created on 24/Jul/2003
 *
 */
package ServidorAplicacao.strategy.groupEnrolment.strategys;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import Dominio.IAttendsSet;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.ITurno;
import Dominio.Student;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */

public abstract class GroupEnrolmentStrategy implements IGroupEnrolmentStrategy
{

    public boolean checkNumberOfGroups(IGroupProperties groupProperties, ITurno shift)
    {
        boolean result = false;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentStudentGroup persistentStudentGroup = sp.getIPersistentStudentGroup();
            List groups = new ArrayList();
            if(shift!=null){
             groups =
                persistentStudentGroup.readAllStudentGroupByAttendsSetAndShift(
                    groupProperties.getAttendsSet(),
                    shift);
            }else{
            	groups = groupProperties.getAttendsSet().getStudentGroupsWithoutShift();
            }

            int numberOfGroups = groups.size();
            if (groupProperties.getGroupMaximumNumber() == null)
                return true;

            if (numberOfGroups < groupProperties.getGroupMaximumNumber().intValue())
            {
                result = true;
                System.out.println("numberOfGroups"+ numberOfGroups);
                System.out.println("groupProperties.getGroupMaximumNumber().intValue()"+ groupProperties.getGroupMaximumNumber().intValue());
            }
        } catch (ExcepcaoPersistencia e)
        {
        }
        return result;
    }

    public boolean checkEnrolmentDate(IGroupProperties groupProperties, Calendar actualDate)
    {
        Long actualDateInMills = new Long(actualDate.getTimeInMillis());
        Long enrolmentBeginDayInMills = null;
        Long enrolmentEndDayInMills = null;

        if (groupProperties.getEnrolmentBeginDay() != null)
            enrolmentBeginDayInMills =
                new Long(groupProperties.getEnrolmentBeginDay().getTimeInMillis());

        if (groupProperties.getEnrolmentEndDay() != null)
            enrolmentEndDayInMills = new Long(groupProperties.getEnrolmentEndDay().getTimeInMillis());

        if (enrolmentBeginDayInMills == null && enrolmentEndDayInMills == null)
            return true;

        if (enrolmentBeginDayInMills != null && enrolmentEndDayInMills == null)
        {
            if (actualDateInMills.compareTo(enrolmentBeginDayInMills) > 0)
                return true;
        }

        if (enrolmentBeginDayInMills == null && enrolmentEndDayInMills != null)
        {
            if (actualDateInMills.compareTo(enrolmentEndDayInMills) < 0)
                return true;
        }

        if (actualDateInMills.compareTo(enrolmentBeginDayInMills) > 0
            && actualDateInMills.compareTo(enrolmentEndDayInMills) < 0)
            return true;

        return false;
    }

    public boolean checkShiftType(IGroupProperties groupProperties, ITurno shift)
    {
        boolean result = false;
        if(shift!=null){
        if (shift.getTipo().equals(groupProperties.getShiftType()))
            result = true;
        }else{
        	if(groupProperties.getShiftType() == null)
        		return true;
        }

        return result;
    }

    public List checkShiftsType(IGroupProperties groupProperties, List shifts)
    {
    	if(groupProperties.getShiftType()!=null){
    	Iterator iterShift = shifts.iterator();
        ITurno shift = null;
        List allShifts = new ArrayList();
        while (iterShift.hasNext())
        {
            shift = (ITurno) iterShift.next();
            if (shift.getTipo().equals(groupProperties.getShiftType()))
                allShifts.add(shift);
        }
        return allShifts;
    	}else return null;
    }

    public boolean checkAlreadyEnroled(IGroupProperties groupProperties, String username)
        throws ExcepcaoPersistencia
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
            IPersistentStudentGroup persistentStudentGroup = sp.getIPersistentStudentGroup();
            IPersistentStudentGroupAttend persistentStudentGroupAttend =
                sp.getIPersistentStudentGroupAttend();

            IStudent student = persistentStudent.readByUsername(username);
            
            Iterator iterAttends = groupProperties.getAttendsSet().getAttends().iterator();
            boolean found = false;
            IFrequenta attend = null;
            while(iterAttends.hasNext() && !found){
            	attend = (IFrequenta)iterAttends.next();
            	if (attend.getAluno().equals(student)) {
            		found=true;
            		}
            	else
            	{
            		attend = null;
            	}
            }

            List allStudentGroup =
            	persistentStudentGroup.readAllStudentGroupByAttendsSet((IAttendsSet)groupProperties.getAttendsSet());
            Iterator iterStudentGroup = allStudentGroup.iterator();
            IStudentGroup group = null;
            List allStudentGroupAttendByGroup = new ArrayList();
            while (iterStudentGroup.hasNext())
            {
                group = (IStudentGroup) iterStudentGroup.next();
                allStudentGroupAttendByGroup = persistentStudentGroupAttend.readAllByStudentGroup(group);
                Iterator iterStudentGroupAttend = allStudentGroupAttendByGroup.iterator();
                IStudentGroupAttend studentGroupAttend = null;
                while (iterStudentGroupAttend.hasNext())
                {
                    studentGroupAttend = (IStudentGroupAttend) iterStudentGroupAttend.next();
                    if (attend.equals(studentGroupAttend.getAttend()))
                        return true;
                }
            }

        } catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace();
        }

        return false;
    }

    public boolean checkNotEnroledInGroup(
        IGroupProperties groupProperties,
        IStudentGroup studentGroup,
        String username)
        throws ExcepcaoPersistencia
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
            IPersistentStudentGroupAttend persistentStudentGroupAttend =
                sp.getIPersistentStudentGroupAttend();

            IStudent student = persistentStudent.readByUsername(username);
            
            Iterator iterAttends = groupProperties.getAttendsSet().getAttends().iterator();
            boolean found = false;
            IFrequenta attend = null;
            while(iterAttends.hasNext() && !found){
            	attend = (IFrequenta)iterAttends.next();
            	if (attend.getAluno().equals(student)) {
            		found=true;
            		}
            	else
            	{
            		attend = null;
            	}
            }

            List allStudentGroupAttendByGroup =
                persistentStudentGroupAttend.readAllByStudentGroup(studentGroup);
            Iterator iterStudentGroupAttend = allStudentGroupAttendByGroup.iterator();
            IStudentGroupAttend studentGroupAttend = null;
            while (iterStudentGroupAttend.hasNext())
            {
                studentGroupAttend = (IStudentGroupAttend) iterStudentGroupAttend.next();
                if (studentGroupAttend.getAttend().equals(attend))
                    return false;
            }

        } catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace();
        }

        return true;
    }

    public boolean checkPossibleToEnrolInExistingGroup(
        IGroupProperties groupProperties,
        IStudentGroup studentGroup,
        ITurno shift)
        throws ExcepcaoPersistencia
    {
        boolean result = false;

        List listStudentGroupAttend = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            listStudentGroupAttend =
                sp.getIPersistentStudentGroupAttend().readAllByStudentGroup(studentGroup);

        } catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace();
        }
        int nrOfElements = listStudentGroupAttend.size();
        Integer maximumCapacity = groupProperties.getMaximumCapacity();
        if (maximumCapacity == null)
            return true;
        if (nrOfElements < maximumCapacity.intValue())
            return true;

        return result;

    }

    public boolean checkIfStudentGroupIsEmpty(
        IStudentGroupAttend studentGroupAttend,
        IStudentGroup studentGroup)
        throws ExcepcaoPersistencia
    {

        boolean result = false;
        List allStudentGroupAttend = new ArrayList();
        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentStudentGroupAttend persistentStudentGroupAttend =
                persistentSuport.getIPersistentStudentGroupAttend();

            allStudentGroupAttend = persistentStudentGroupAttend.readAllByStudentGroup(studentGroup);

        } catch (ExcepcaoPersistencia ex)
        {
            throw ex;
        }

        if (allStudentGroupAttend.size() == 1 && allStudentGroupAttend.contains(studentGroupAttend))
            result = true;

        return result;
    }
    
    
    public boolean checkStudentInAttendsSet (IGroupProperties groupProperties, String username)
    throws ExcepcaoPersistencia
	{
    	boolean found = false;
    	try
		{
    		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
    		IPersistentStudent persistentStudent = sp.getIPersistentStudent();

    		IStudent student = persistentStudent.readByUsername(username);
    
    		Iterator iterAttends = groupProperties.getAttendsSet().getAttends().iterator();
  
    		IFrequenta attend = null;
    		while(iterAttends.hasNext() && !found){
    			attend = (IFrequenta)iterAttends.next();
    			if (attend.getAluno().equals(student)) {
    				found=true;
    			}
    		}
    
		} catch (ExcepcaoPersistencia ex)
		{
			ex.printStackTrace();
		}
		return found;
	}


    public boolean checkStudentsInAttendsSet (List studentCodes, IGroupProperties groupProperties)
    throws ExcepcaoPersistencia
	{
    	boolean found = true;
    	try {
    		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
    		IPersistentStudent persistentStudent = sp
			.getIPersistentStudent();
	
    		List attends = groupProperties.getAttendsSet().getAttends();
    		List students =  new ArrayList();
    		Iterator iterAttends = attends.iterator();
    		while(iterAttends.hasNext()){
    			IFrequenta frequenta = (IFrequenta)iterAttends.next();
    			students.add(frequenta.getAluno());
    		}
    		Iterator iterator = studentCodes.iterator();
    		while (iterator.hasNext()) {
    			IStudent student = (IStudent) persistentStudent.readByOID(
    					Student.class, (Integer) iterator.next());
    			if(!students.contains(student))return false;
    		}           		
    	} catch (ExcepcaoPersistencia ex)
		{
    		ex.printStackTrace();
		}
    	return found;
	}

    
    public boolean checkStudentsUserNamesInAttendsSet (List studentUsernames, IGroupProperties groupProperties)
    throws ExcepcaoPersistencia
	{
    	boolean found = true;
    	try {
    		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
    		IPersistentStudent persistentStudent = sp
			.getIPersistentStudent();
	
    		List attends = groupProperties.getAttendsSet().getAttends();
    		List students =  new ArrayList();
    		Iterator iterAttends = attends.iterator();
    		while(iterAttends.hasNext()){
    			IFrequenta frequenta = (IFrequenta)iterAttends.next();
    			students.add(frequenta.getAluno());
    		}
    		Iterator iterator = studentUsernames.iterator();
    		while (iterator.hasNext()) {
    			String userName = (String) iterator.next();
    			IStudent student = (IStudent) persistentStudent.readByUsername(userName);
    			if(!students.contains(student))return false;
    		}           		
    	} catch (ExcepcaoPersistencia ex)
		{
    		ex.printStackTrace();
		}
    	return found;
	}

    
    public boolean checkHasShift(IGroupProperties groupProperties){
    	if(groupProperties.getShiftType()!=null){
    		return true;
    	}else{
    		return false;
    	}
    }

    public abstract Integer enrolmentPolicyNewGroup(
        IGroupProperties groupProperties,
        int numberOfStudentsToEnrole,
        ITurno shift);

    public abstract boolean checkNumberOfGroupElements(
        IGroupProperties groupProperties,
        IStudentGroup studentGroup)
        throws ExcepcaoPersistencia;

}
