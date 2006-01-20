/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.credits.validator.CreditsValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.credits.validator.OverlappingSupportLessonPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.credits.validator.PeriodType;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.InfoSupportLesson;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentSupportLesson;
import net.sourceforge.fenixedu.util.DiaSemana;

/**
 * @author jpvl
 */
public class EditSupportLessonByOID extends EditDomainObjectService {

	public class InvalidPeriodException extends FenixServiceException {
		public InvalidPeriodException() {
			super();
		}
	}

	@Override
	protected IPersistentObject getIPersistentObject() {
		return persistentSupport.getIPersistentSupportLesson();
	}

	protected DomainObject readObjectByUnique(DomainObject domainObject)
			throws ExcepcaoPersistencia {

		SupportLesson supportLessonDomainObject = (SupportLesson) domainObject;
		IPersistentSupportLesson supportLessonDAO = persistentSupport.getIPersistentSupportLesson();
		SupportLesson supportLesson = supportLessonDAO.readByUnique(supportLessonDomainObject
				.getProfessorship().getIdInternal(), supportLessonDomainObject.getWeekDay(),
				supportLessonDomainObject.getStartTime(), supportLessonDomainObject.getEndTime());
		return supportLesson;
	}

	@Override
	protected void doBeforeLock(DomainObject domainObjectToLock, InfoObject infoObject)
            throws Exception {
		InfoSupportLesson infoSupportLesson = (InfoSupportLesson) infoObject;

		Calendar begin = Calendar.getInstance();
		begin.setTime(infoSupportLesson.getStartTime());
		Calendar end = Calendar.getInstance();
		end.setTime(infoSupportLesson.getEndTime());

		if (end.before(begin)) {
			throw new InvalidPeriodException();
		}
		IPersistentProfessorship professorshipDAO = persistentSupport.getIPersistentProfessorship();

		Professorship professorship = (Professorship) professorshipDAO.readByOID(Professorship.class,
				infoSupportLesson.getInfoProfessorship().getIdInternal());
		Teacher teacher = professorship.getTeacher();
		ExecutionPeriod executionPeriod = professorship.getExecutionCourse().getExecutionPeriod();
		DiaSemana weekDay = infoSupportLesson.getWeekDay();
		Date startTime = infoSupportLesson.getStartTime();
		Date endTime = infoSupportLesson.getEndTime();

		CreditsValidator.validatePeriod(teacher.getIdInternal(), executionPeriod.getIdInternal(),
				weekDay, startTime, endTime, PeriodType.SUPPORT_LESSON_PERIOD);

		IPersistentSupportLesson supportLessonDAO = persistentSupport.getIPersistentSupportLesson();

		List supportLessonList = supportLessonDAO.readOverlappingPeriod(teacher.getIdInternal(),
				executionPeriod.getIdInternal(), weekDay, startTime, endTime);
		boolean ok = true;
		if (!supportLessonList.isEmpty()) {
			if (supportLessonList.size() == 1) {
				SupportLesson supportLesson = (SupportLesson) supportLessonList.get(0);
				if (!supportLesson.getIdInternal().equals(infoSupportLesson.getIdInternal())) {
					ok = false;
				}

			} else {
				ok = false;
			}
		}
		if (!ok) {
			throw new OverlappingSupportLessonPeriod();
		}

		super.doBeforeLock(domainObjectToLock, infoObject);
	}

	@Override
	protected void copyInformationFromInfoToDomain(InfoObject infoObject, DomainObject domainObject)
            throws ExcepcaoPersistencia {
		InfoSupportLesson infoSupportLesson = (InfoSupportLesson) infoObject;
		SupportLesson supportLesson = (SupportLesson) domainObject;
		supportLesson.setEndTime(infoSupportLesson.getEndTime());

		IPersistentProfessorship persistentProfessorship = persistentSupport.getIPersistentProfessorship();
		Professorship professorship = (Professorship) persistentProfessorship.readByOID(
				Professorship.class, infoSupportLesson.getInfoProfessorship().getIdInternal());
		supportLesson.setProfessorship(professorship);

		supportLesson.setPlace(infoSupportLesson.getPlace());

		supportLesson.setStartTime(infoSupportLesson.getStartTime());
		supportLesson.setWeekDay(infoSupportLesson.getWeekDay());
	}

	@Override
	protected DomainObject createNewDomainObject(InfoObject infoObject) {
		return DomainFactory.makeSupportLesson();
	}

	@Override
	protected Class getDomainObjectClass() {
		return SupportLesson.class;
	}

}
