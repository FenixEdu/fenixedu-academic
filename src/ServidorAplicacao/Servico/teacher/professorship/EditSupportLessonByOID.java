/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.professorship;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import DataBeans.InfoObject;
import DataBeans.teacher.professorship.InfoSupportLesson;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.IExecutionPeriod;
import Dominio.IProfessorship;
import Dominio.ISupportLesson;
import Dominio.ITeacher;
import Dominio.Professorship;
import ServidorAplicacao.Servico.credits.validator.CreditsValidator;
import ServidorAplicacao.Servico.credits.validator.OverlappingSupportLessonPeriod;
import ServidorAplicacao.Servico.credits.validator.PeriodType;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.teacher.professorship.IPersistentSupportLesson;
import Util.DiaSemana;

/**
 * @author jpvl
 */
public class EditSupportLessonByOID extends EditDomainObjectService
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
    private static EditSupportLessonByOID service = new EditSupportLessonByOID();

    /**
	 * The singleton access method of this class.
	 */
    public static EditSupportLessonByOID getService()
    {
        return service;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
	 */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentSupportLesson();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(DataBeans.InfoObject)
	 */
    protected IDomainObject clone2DomainObject(InfoObject infoObject)
    {
        return Cloner.copyInfoSupportLesson2ISupportLesson((InfoSupportLesson) infoObject);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "EditSupportLessonByOID";
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#readObjectByUnique(Dominio.IDomainObject,
	 *          ServidorPersistente.ISuportePersistente)
	 */
    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
            throws ExcepcaoPersistencia
    {
        IPersistentSupportLesson supportLessonDAO = sp.getIPersistentSupportLesson();

        ISupportLesson supportLesson = supportLessonDAO.readByUnique((ISupportLesson) domainObject);
        return supportLesson;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#doBeforeLock(Dominio.IDomainObject,
	 *          DataBeans.InfoObject, ServidorPersistente.ISuportePersistente)
	 */
    protected void doBeforeLock(IDomainObject domainObjectToLock, InfoObject infoObject,
            ISuportePersistente sp) throws FenixServiceException
    {
        InfoSupportLesson infoSupportLesson = (InfoSupportLesson) infoObject;

        Calendar begin = Calendar.getInstance();
        begin.setTime(infoSupportLesson.getStartTime());
        Calendar end = Calendar.getInstance();
        end.setTime(infoSupportLesson.getEndTime());

        if (end.before(begin))
            throw new InvalidPeriodException();

        IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();

        IProfessorship professorship = (IProfessorship) professorshipDAO.readByOId(new Professorship(
                infoSupportLesson.getInfoProfessorship().getIdInternal()),
                false);

        ITeacher teacher = professorship.getTeacher();
        IExecutionPeriod executionPeriod = professorship.getExecutionCourse().getExecutionPeriod();
        DiaSemana weekDay = infoSupportLesson.getWeekDay();
        Date startTime = infoSupportLesson.getStartTime();
        Date endTime = infoSupportLesson.getEndTime();

        CreditsValidator.validatePeriod(teacher, executionPeriod, weekDay, startTime, endTime,
                PeriodType.SUPPORT_LESSON_PERIOD);

        IPersistentSupportLesson supportLessonDAO = sp.getIPersistentSupportLesson();

        try
        {
            List supportLessonList = supportLessonDAO.readOverlappingPeriod(teacher, executionPeriod,
                    weekDay, startTime, endTime);
            boolean ok = true;
            if (!supportLessonList.isEmpty())
            {
                if (supportLessonList.size() == 1)
                {
                    ISupportLesson supportLesson = (ISupportLesson) supportLessonList.get(0);
                    if (!supportLesson.getIdInternal().equals(infoSupportLesson.getIdInternal()))
                    {
                        ok = false;
                    }

                }
                else
                {
                    ok = false;
                }
            }
            if (!ok)
            {
                throw new OverlappingSupportLessonPeriod();
            }
        }
        catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems on database!", e);
        }

        super.doBeforeLock(domainObjectToLock, infoObject, sp);
    }
}