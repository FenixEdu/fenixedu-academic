/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.workingTime;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InfoTeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.workTime.ITeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.applicationTier.Servico.credits.validator.CreditsValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.credits.validator.OverlappingInstitutionWorkingPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.credits.validator.PeriodType;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;
import net.sourceforge.fenixedu.util.DiaSemana;

/**
 * @author jpvl
 */
public class EditTeacherInstitutionWorkingTimeByOID extends EditDomainObjectService {
    /**
     * @author jpvl
     */
    public class InvalidPeriodException extends FenixServiceException {

        /**
         *  
         */
        public InvalidPeriodException() {
            super();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(DataBeans.InfoObject)
     */
    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
        return Cloner
                .copyInfoTeacherInstitutionWorkingTime2ITeacherInstitutionWorkTime((InfoTeacherInstitutionWorkTime) infoObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#doBeforeLock(Dominio.IDomainObject,
     *      DataBeans.InfoObject, ServidorPersistente.ISuportePersistente)
     */
    protected void doBeforeLock(IDomainObject domainObjectToLock, InfoObject infoObject,
            ISuportePersistente sp) throws FenixServiceException {
        super.doBeforeLock(domainObjectToLock, infoObject, sp);

        InfoTeacherInstitutionWorkTime infoTeacherInstitutionWorkTime = (InfoTeacherInstitutionWorkTime) infoObject;

        Calendar begin = Calendar.getInstance();
        begin.setTime(infoTeacherInstitutionWorkTime.getStartTime());
        Calendar end = Calendar.getInstance();
        end.setTime(infoTeacherInstitutionWorkTime.getEndTime());

        if (end.before(begin))
            throw new InvalidPeriodException();

        ITeacher teacher = new Teacher(infoTeacherInstitutionWorkTime.getInfoTeacher().getIdInternal());
        IExecutionPeriod executionPeriod = new ExecutionPeriod(infoTeacherInstitutionWorkTime
                .getInfoExecutionPeriod().getIdInternal());
        DiaSemana weekDay = infoTeacherInstitutionWorkTime.getWeekDay();
        Date startTime = infoTeacherInstitutionWorkTime.getStartTime();
        Date endTime = infoTeacherInstitutionWorkTime.getEndTime();

        CreditsValidator.validatePeriod(teacher, executionPeriod, weekDay, startTime, endTime,
                PeriodType.INSTITUTION_WORKING_TIME_PERIOD);
        IPersistentTeacherInstitutionWorkingTime teacherInstitutionWorkingTimeDAO = sp
                .getIPersistentTeacherInstitutionWorkingTime();

        try {
            List teacherInstitutionWorkingTime = teacherInstitutionWorkingTimeDAO.readOverlappingPeriod(
                    teacher, executionPeriod, weekDay, startTime, endTime);
            boolean ok = true;
            if (!teacherInstitutionWorkingTime.isEmpty()) {
                if (teacherInstitutionWorkingTime.size() == 1) {
                    ITeacherInstitutionWorkTime teacherInstitutionWorkTime = (ITeacherInstitutionWorkTime) teacherInstitutionWorkingTime
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
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException("Problems on database!", e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentTeacherInstitutionWorkingTime();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#readObjectByUnique(Dominio.IDomainObject,
     *      ServidorPersistente.ISuportePersistente)
     */
    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentTeacherInstitutionWorkingTime teacherInstitutionWorkingTimeDAO = sp
                .getIPersistentTeacherInstitutionWorkingTime();
        ITeacherInstitutionWorkTime teacherInstitutionWorkTime = (ITeacherInstitutionWorkTime) domainObject;
        teacherInstitutionWorkTime = teacherInstitutionWorkingTimeDAO
                .readByUnique(teacherInstitutionWorkTime);
        return teacherInstitutionWorkTime;
    }
}