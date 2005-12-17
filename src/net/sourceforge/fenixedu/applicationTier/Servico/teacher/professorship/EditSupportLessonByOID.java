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
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ISupportLesson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
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
	protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
		return sp.getIPersistentSupportLesson();
	}

	protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
			throws ExcepcaoPersistencia {

		ISupportLesson supportLessonDomainObject = (ISupportLesson) domainObject;
		IPersistentSupportLesson supportLessonDAO = sp.getIPersistentSupportLesson();
		ISupportLesson supportLesson = supportLessonDAO.readByUnique(supportLessonDomainObject
				.getProfessorship().getIdInternal(), supportLessonDomainObject.getWeekDay(),
				supportLessonDomainObject.getStartTime(), supportLessonDomainObject.getEndTime());
		return supportLesson;
	}

	@Override
	protected void doBeforeLock(IDomainObject domainObjectToLock, InfoObject infoObject,
			ISuportePersistente sp) throws Exception {
		InfoSupportLesson infoSupportLesson = (InfoSupportLesson) infoObject;

		Calendar begin = Calendar.getInstance();
		begin.setTime(infoSupportLesson.getStartTime());
		Calendar end = Calendar.getInstance();
		end.setTime(infoSupportLesson.getEndTime());

		if (end.before(begin)) {
			throw new InvalidPeriodException();
		}
		IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();

		IProfessorship professorship;
		professorship = (IProfessorship) professorshipDAO.readByOID(Professorship.class,
				infoSupportLesson.getInfoProfessorship().getIdInternal());
		ITeacher teacher = professorship.getTeacher();
		IExecutionPeriod executionPeriod = professorship.getExecutionCourse().getExecutionPeriod();
		DiaSemana weekDay = infoSupportLesson.getWeekDay();
		Date startTime = infoSupportLesson.getStartTime();
		Date endTime = infoSupportLesson.getEndTime();

		CreditsValidator.validatePeriod(teacher.getIdInternal(), executionPeriod.getIdInternal(),
				weekDay, startTime, endTime, PeriodType.SUPPORT_LESSON_PERIOD);

		IPersistentSupportLesson supportLessonDAO = sp.getIPersistentSupportLesson();

		List supportLessonList = supportLessonDAO.readOverlappingPeriod(teacher.getIdInternal(),
				executionPeriod.getIdInternal(), weekDay, startTime, endTime);
		boolean ok = true;
		if (!supportLessonList.isEmpty()) {
			if (supportLessonList.size() == 1) {
				ISupportLesson supportLesson = (ISupportLesson) supportLessonList.get(0);
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

		super.doBeforeLock(domainObjectToLock, infoObject, sp);
	}

	@Override
	protected void copyInformationFromInfoToDomain(ISuportePersistente sp, InfoObject infoObject,
			IDomainObject domainObject) throws ExcepcaoPersistencia {
		InfoSupportLesson infoSupportLesson = (InfoSupportLesson) infoObject;
		ISupportLesson supportLesson = (ISupportLesson) domainObject;
		supportLesson.setEndTime(infoSupportLesson.getEndTime());

		IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
		IProfessorship professorship = (IProfessorship) persistentProfessorship.readByOID(
				Professorship.class, infoSupportLesson.getInfoProfessorship().getIdInternal());
		supportLesson.setProfessorship(professorship);

		supportLesson.setPlace(infoSupportLesson.getPlace());

		supportLesson.setStartTime(infoSupportLesson.getStartTime());
		supportLesson.setWeekDay(infoSupportLesson.getWeekDay());
	}

	@Override
	protected IDomainObject createNewDomainObject(InfoObject infoObject) {
		return DomainFactory.makeSupportLesson();
	}

	@Override
	protected Class getDomainObjectClass() {
		return SupportLesson.class;
	}

}
