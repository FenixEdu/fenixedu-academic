/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.workingTime;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import DataBeans.InfoObject;
import DataBeans.teacher.workTime.InfoTeacherInstitutionWorkTime;
import DataBeans.util.Cloner;
import Dominio.Credits;
import Dominio.ExecutionPeriod;
import Dominio.ICredits;
import Dominio.IDomainObject;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.Teacher;
import Dominio.teacher.workTime.ITeacherInstitutionWorkTime;
import ServidorAplicacao.Servico.credits.calcutation.CreditsCalculator;
import ServidorAplicacao.Servico.credits.validator.CreditsValidator;
import ServidorAplicacao.Servico.credits.validator.OverlappingInstitutionWorkingPeriod;
import ServidorAplicacao.Servico.credits.validator.PeriodType;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.credits.IPersistentCredits;
import ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;
import Util.DiaSemana;

/**
 * @author jpvl
 */
public class EditTeacherInstitutionWorkingTimeByOID extends EditDomainObjectService
{
    /**
     * @author jpvl
     */
    public class InvalidPeriodException extends FenixServiceException
    {

        /**
         *  
         */
        public InvalidPeriodException()
        {
            super();
        }
    }

    private static EditTeacherInstitutionWorkingTimeByOID service =
        new EditTeacherInstitutionWorkingTimeByOID();

    /**
     * The singleton access method of this class.
     */
    public static EditTeacherInstitutionWorkingTimeByOID getService()
    {
        return service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(DataBeans.InfoObject)
     */
    protected IDomainObject clone2DomainObject(InfoObject infoObject)
    {
        return Cloner.copyInfoTeacherInstitutionWorkingTime2ITeacherInstitutionWorkTime(
            (InfoTeacherInstitutionWorkTime) infoObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#doBeforeLock(Dominio.IDomainObject,
     *      DataBeans.InfoObject, ServidorPersistente.ISuportePersistente)
     */
    protected void doBeforeLock(
        IDomainObject domainObjectToLock,
        InfoObject infoObject,
        ISuportePersistente sp)
        throws FenixServiceException
    {
        super.doBeforeLock(domainObjectToLock, infoObject, sp);

        InfoTeacherInstitutionWorkTime infoTeacherInstitutionWorkTime =
            (InfoTeacherInstitutionWorkTime) infoObject;

        Calendar begin = Calendar.getInstance();
        begin.setTime(infoTeacherInstitutionWorkTime.getStartTime());
        Calendar end = Calendar.getInstance();
        end.setTime(infoTeacherInstitutionWorkTime.getEndTime());

        if (end.before(begin))
            throw new InvalidPeriodException();

        ITeacher teacher = new Teacher(infoTeacherInstitutionWorkTime.getInfoTeacher().getIdInternal());
        IExecutionPeriod executionPeriod =
            new ExecutionPeriod(infoTeacherInstitutionWorkTime.getInfoExecutionPeriod().getIdInternal());
        DiaSemana weekDay = infoTeacherInstitutionWorkTime.getWeekDay();
        Date startTime = infoTeacherInstitutionWorkTime.getStartTime();
        Date endTime = infoTeacherInstitutionWorkTime.getEndTime();

        CreditsValidator.validatePeriod(
            teacher,
            executionPeriod,
            weekDay,
            startTime,
            endTime,
            PeriodType.INSTITUTION_WORKING_TIME_PERIOD);
        IPersistentTeacherInstitutionWorkingTime teacherInstitutionWorkingTimeDAO =
            sp.getIPersistentTeacherInstitutionWorkingTime();

        try
        {
            List teacherInstitutionWorkingTime =
                teacherInstitutionWorkingTimeDAO.readOverlappingPeriod(
                    teacher,
                    executionPeriod,
                    weekDay,
                    startTime,
                    endTime);
            boolean ok = true;
            if (!teacherInstitutionWorkingTime.isEmpty())
            {
                if (teacherInstitutionWorkingTime.size() == 1)
                {
                    ITeacherInstitutionWorkTime teacherInstitutionWorkTime =
                        (ITeacherInstitutionWorkTime) teacherInstitutionWorkingTime.get(0);
                    if (!teacherInstitutionWorkTime
                        .getIdInternal()
                        .equals(infoTeacherInstitutionWorkTime.getIdInternal()))
                    {
                        ok = false;
                    }

                } else
                {
                    ok = false;
                }
            }
            if (!ok)
            {
                throw new OverlappingInstitutionWorkingPeriod();
            }
        } catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems on database!", e);
        }

    }
    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentTeacherInstitutionWorkingTime();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome()
    {
        return "EditTeacherInstitutionWorkingTimeByOID";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#readObjectByUnique(Dominio.IDomainObject,
     *      ServidorPersistente.ISuportePersistente)
     */
    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentTeacherInstitutionWorkingTime teacherInstitutionWorkingTimeDAO =
            sp.getIPersistentTeacherInstitutionWorkingTime();
        ITeacherInstitutionWorkTime teacherInstitutionWorkTime =
            (ITeacherInstitutionWorkTime) domainObject;
        teacherInstitutionWorkTime =
            teacherInstitutionWorkingTimeDAO.readByUnique(teacherInstitutionWorkTime);
        return teacherInstitutionWorkTime;
    }
    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#doAfterLock(Dominio.IDomainObject,
     *      DataBeans.InfoObject, ServidorPersistente.ISuportePersistente)
     */
    protected void doAfterLock(
        IDomainObject domainObjectLocked,
        InfoObject infoObject,
        ISuportePersistente sp)
        throws FenixServiceException
    {
        ITeacherInstitutionWorkTime teacherInstitutionWorkTime =
            (ITeacherInstitutionWorkTime) domainObjectLocked;

        ITeacher teacher = teacherInstitutionWorkTime.getTeacher();
        IExecutionPeriod executionPeriod = teacherInstitutionWorkTime.getExecutionPeriod();

        CreditsCalculator creditsCalculator = CreditsCalculator.getInstance();
        try
        {
            Double teacherInstitutionWorkTimeCredits =
                creditsCalculator.calcuteTeacherInstitutionWorkingTimeAfterInsert(
                    teacher,
                    executionPeriod,
                    teacherInstitutionWorkTime,
                    sp);
            IPersistentCredits creditsDAO = sp.getIPersistentCredits();
            ICredits credits = creditsDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);
            if (credits == null)
            {
                credits = new Credits();
            }
            creditsDAO.simpleLockWrite(credits);
            credits.setExecutionPeriod(executionPeriod);
            credits.setTeacher(teacher);
            credits.setInstitutionWorkTime(teacherInstitutionWorkTimeCredits);
        } catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace();
            throw new FenixServiceException("Problems on database!", e);
        }

    }

}