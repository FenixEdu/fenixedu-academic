/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.workingTime;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.credits.validator.CreditsValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.credits.validator.OverlappingInstitutionWorkingPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.credits.validator.PeriodType;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InfoTeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.workTime.TeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;
import net.sourceforge.fenixedu.util.DiaSemana;

/**
 * @author jpvl
 */
public class EditTeacherInstitutionWorkingTimeByOID extends EditDomainObjectService {

	public class InvalidPeriodException extends FenixServiceException {
		public InvalidPeriodException() {
			super();
		}
	}

	@Override
	protected void doBeforeLock(DomainObject domainObjectToLock, InfoObject infoObject) throws Exception {
		super.doBeforeLock(domainObjectToLock, infoObject);

		InfoTeacherInstitutionWorkTime infoTeacherInstitutionWorkTime = (InfoTeacherInstitutionWorkTime) infoObject;

		Calendar begin = Calendar.getInstance();
		begin.setTime(infoTeacherInstitutionWorkTime.getStartTime());
		Calendar end = Calendar.getInstance();
		end.setTime(infoTeacherInstitutionWorkTime.getEndTime());

		if (end.before(begin))
			throw new InvalidPeriodException();

		DiaSemana weekDay = infoTeacherInstitutionWorkTime.getWeekDay();
		Date startTime = infoTeacherInstitutionWorkTime.getStartTime();
		Date endTime = infoTeacherInstitutionWorkTime.getEndTime();

		CreditsValidator.validatePeriod(infoTeacherInstitutionWorkTime.getInfoTeacher().getIdInternal(),
				infoTeacherInstitutionWorkTime.getInfoExecutionPeriod().getIdInternal(), weekDay,
				startTime, endTime, PeriodType.INSTITUTION_WORKING_TIME_PERIOD);
		IPersistentTeacherInstitutionWorkingTime teacherInstitutionWorkingTimeDAO = persistentSupport
				.getIPersistentTeacherInstitutionWorkingTime();

		List teacherInstitutionWorkingTime = teacherInstitutionWorkingTimeDAO.readOverlappingPeriod(
				infoTeacherInstitutionWorkTime.getInfoTeacher().getIdInternal(),
				infoTeacherInstitutionWorkTime.getInfoExecutionPeriod().getIdInternal(), weekDay,
				startTime, endTime);
		boolean ok = true;
		if (!teacherInstitutionWorkingTime.isEmpty()) {
			if (teacherInstitutionWorkingTime.size() == 1) {
				TeacherInstitutionWorkTime teacherInstitutionWorkTime = (TeacherInstitutionWorkTime) teacherInstitutionWorkingTime
						.get(0);
				if (!teacherInstitutionWorkTime.getIdInternal().equals(
						infoTeacherInstitutionWorkTime.getIdInternal())) {
					ok = false;
				}

			} else {
				ok = false;
			}
		}
		if (!ok) {
			throw new OverlappingInstitutionWorkingPeriod();
		}
	}

	@Override
	protected IPersistentObject getIPersistentObject() {
		return persistentSupport.getIPersistentTeacherInstitutionWorkingTime();
	}

	protected DomainObject readObjectByUnique(DomainObject domainObject)
			throws ExcepcaoPersistencia {
		IPersistentTeacherInstitutionWorkingTime teacherInstitutionWorkingTimeDAO = persistentSupport
				.getIPersistentTeacherInstitutionWorkingTime();
		TeacherInstitutionWorkTime teacherInstitutionWorkTime = (TeacherInstitutionWorkTime) domainObject;
		teacherInstitutionWorkTime = teacherInstitutionWorkingTimeDAO
				.readByUnique(teacherInstitutionWorkTime);
		return teacherInstitutionWorkTime;
	}

	@Override
	protected void copyInformationFromInfoToDomain(InfoObject infoObject,
			DomainObject domainObject) throws ExcepcaoPersistencia {
		InfoTeacherInstitutionWorkTime infoTeacherInstitutionWorkTime = (InfoTeacherInstitutionWorkTime) infoObject;
		TeacherInstitutionWorkTime teacherInstitutionWorkTime = (TeacherInstitutionWorkTime) domainObject;
		teacherInstitutionWorkTime.setEndTime(infoTeacherInstitutionWorkTime.getEndTime());

		IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
		ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentExecutionPeriod.readByOID(
				ExecutionPeriod.class, infoTeacherInstitutionWorkTime.getInfoExecutionPeriod()
						.getIdInternal());
		teacherInstitutionWorkTime.setExecutionPeriod(executionPeriod);

		IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
		Teacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class,
				infoTeacherInstitutionWorkTime.getInfoTeacher().getIdInternal());
		teacherInstitutionWorkTime.setKeyTeacher(teacher.getIdInternal());
		teacherInstitutionWorkTime.setStartTime(infoTeacherInstitutionWorkTime.getStartTime());
		teacherInstitutionWorkTime.setTeacher(teacher);

		teacherInstitutionWorkTime.setWeekDay(infoTeacherInstitutionWorkTime.getWeekDay());
	}

	@Override
	protected DomainObject createNewDomainObject(InfoObject infoObject) {
		return DomainFactory.makeTeacherInstitutionWorkTime();
	}

	@Override
	protected Class getDomainObjectClass() {
		return TeacherInstitutionWorkTime.class;
	}

}
