/*
 * Created on 24/Jul/2003
 *
 */
package ServidorAplicacao.strategy.groupEnrolment.strategys;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.ITurno;
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

public abstract class GroupEnrolmentStrategy implements IGroupEnrolmentStrategy {

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

	public boolean checkNumberOfGroups(IGroupProperties groupProperties, ITurno shift) {
		boolean result = false;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentStudentGroup persistentStudentGroup = sp.getIPersistentStudentGroup();
			List groups = persistentStudentGroup.readAllStudentGroupByGroupPropertiesAndShift(groupProperties, shift);
			int numberOfGroups = groups.size();
			if(groupProperties.getGroupMaximumNumber()== null)
				return true;
			
			if (numberOfGroups < groupProperties.getGroupMaximumNumber().intValue())
					result = true;

		} catch (ExcepcaoPersistencia e) {
		}
		return result;
	}

	public boolean checkEnrolmentDate(IGroupProperties groupProperties, Calendar actualDate) {
		Long actualDateInMills = new Long(actualDate.getTimeInMillis());
		Long enrolmentBeginDayInMills = null;
		Long enrolmentEndDayInMills = null;

		if (groupProperties.getEnrolmentBeginDay() != null)
			enrolmentBeginDayInMills = new Long(groupProperties.getEnrolmentBeginDay().getTimeInMillis());

		if (groupProperties.getEnrolmentEndDay() != null)
			enrolmentEndDayInMills = new Long(groupProperties.getEnrolmentEndDay().getTimeInMillis());

		if (enrolmentBeginDayInMills == null && enrolmentEndDayInMills == null)
			return true;

		if (enrolmentBeginDayInMills != null && enrolmentEndDayInMills == null) {
			if (actualDateInMills.compareTo(enrolmentBeginDayInMills) > 0)
				return true;
		}

		if (enrolmentBeginDayInMills == null && enrolmentEndDayInMills != null) {
			if (actualDateInMills.compareTo(enrolmentEndDayInMills) < 0)
				return true;
		}

		if (actualDateInMills.compareTo(enrolmentBeginDayInMills) > 0 && actualDateInMills.compareTo(enrolmentEndDayInMills) < 0)
			return true;

		return false;
	}

	public boolean checkShiftType(IGroupProperties groupProperties, ITurno shift) {
		boolean result = false;
		if (shift.getTipo().equals(groupProperties.getShiftType()))
			result = true;

		return result;
	}

	public List checkShiftsType(IGroupProperties groupProperties, List shifts) {
		Iterator iterShift = shifts.iterator();
		ITurno shift = null;
		List allShifts = new ArrayList();
		while (iterShift.hasNext()) {
			shift = (ITurno) iterShift.next();
			if (shift.getTipo().equals(groupProperties.getShiftType()))
				allShifts.add(shift);
		}
		return allShifts;
	}

	public boolean checkAlreadyEnroled(IGroupProperties groupProperties, List usernames) throws ExcepcaoPersistencia {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentStudent persistentStudent = sp.getIPersistentStudent();
			IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
			IPersistentStudentGroup persistentStudentGroup = sp.getIPersistentStudentGroup();
			IPersistentStudentGroupAttend persistentStudentGroupAttend = sp.getIPersistentStudentGroupAttend();

			Iterator iterUsernames = usernames.iterator();
			List allAttend = new ArrayList();
			IStudent student = null;
			IFrequenta attend = null;
			while (iterUsernames.hasNext()) {
				student = persistentStudent.readByUsername((String) iterUsernames.next());
				attend = persistentAttend.readByAlunoAndDisciplinaExecucao(student, groupProperties.getExecutionCourse());
				allAttend.add(attend);
			}

			List allStudentGroup = persistentStudentGroup.readAllStudentGroupByGroupProperties(groupProperties);
			Iterator iterStudentGroup = allStudentGroup.iterator();
			IStudentGroup group = null;
			List allStudentGroupAttendByGroup = new ArrayList();
			while (iterStudentGroup.hasNext()) {
				group = (IStudentGroup) iterStudentGroup.next();
				allStudentGroupAttendByGroup = persistentStudentGroupAttend.readAllByStudentGroup(group);
				Iterator iterStudentGroupAttend = allStudentGroupAttendByGroup.iterator();
				IStudentGroupAttend studentGroupAttend = null;
				while (iterStudentGroupAttend.hasNext()) {
					studentGroupAttend = (IStudentGroupAttend) iterStudentGroupAttend.next();
					if (allAttend.contains(studentGroupAttend.getAttend()))
						return false;
				}
			}

		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}

		return true;
	}

	public boolean checkPossibleToEnrolInExistingGroup(IGroupProperties groupProperties, IStudentGroup studentGroup, ITurno shift)
		throws ExcepcaoPersistencia {
		boolean result = false;

		List listStudentGroupAttend = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			listStudentGroupAttend = sp.getIPersistentStudentGroupAttend().readAllByStudentGroup(studentGroup);

		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}
		int nrOfElements = listStudentGroupAttend.size();
		Integer maximumCapacity = groupProperties.getMaximumCapacity();
		if(maximumCapacity==null)
			return true;
		if (nrOfElements < maximumCapacity.intValue())
			return true;

		return result;

	}

	public boolean checkIfStudentGroupIsEmpty(IStudentGroupAttend studentGroupAttend, IStudentGroup studentGroup)
		throws ExcepcaoPersistencia {

		boolean result = false;
		List allStudentGroupAttend = new ArrayList();
		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentStudentGroupAttend persistentStudentGroupAttend = persistentSuport.getIPersistentStudentGroupAttend();

			allStudentGroupAttend = (List) persistentStudentGroupAttend.readAllByStudentGroup(studentGroup);

		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}

		if (allStudentGroupAttend.size() == 1 && allStudentGroupAttend.contains(studentGroupAttend))
			result = true;

		return result;
	}

	public abstract boolean enrolmentPolicyNewGroup(IGroupProperties groupProperties, int numberOfStudentsToEnrole, ITurno shift);

	public abstract boolean checkNumberOfGroupElements(IGroupProperties groupProperties, IStudentGroup studentGroup)
		throws ExcepcaoPersistencia;

}
