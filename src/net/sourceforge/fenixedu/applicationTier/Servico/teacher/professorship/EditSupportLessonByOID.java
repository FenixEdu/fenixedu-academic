/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.InfoSupportLesson;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ISupportLesson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.applicationTier.Servico.credits.validator.CreditsValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.credits.validator.OverlappingSupportLessonPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.credits.validator.PeriodType;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
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

    public EditSupportLessonByOID() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentSupportLesson();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(DataBeans.InfoObject)
     */
    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
        return Cloner.copyInfoSupportLesson2ISupportLesson((InfoSupportLesson) infoObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "EditSupportLessonByOID";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#readObjectByUnique(Dominio.IDomainObject,
     *      ServidorPersistente.ISuportePersistente)
     */
    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentSupportLesson supportLessonDAO = sp.getIPersistentSupportLesson();
        ISupportLesson supportLesson = supportLessonDAO.readByUnique((ISupportLesson) domainObject);
        return supportLesson;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#doBeforeLock(Dominio.IDomainObject,
     *      DataBeans.InfoObject, ServidorPersistente.ISuportePersistente)
     */
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
        try {
            professorship = (IProfessorship) professorshipDAO.readByOID(Professorship.class,
                    infoSupportLesson.getInfoProfessorship().getIdInternal());
        } catch (ExcepcaoPersistencia e1) {
            throw new FenixServiceException(e1);
        }
        ITeacher teacher = professorship.getTeacher();
        IExecutionPeriod executionPeriod = professorship.getExecutionCourse().getExecutionPeriod();
        DiaSemana weekDay = infoSupportLesson.getWeekDay();
        Date startTime = infoSupportLesson.getStartTime();
        Date endTime = infoSupportLesson.getEndTime();

        CreditsValidator.validatePeriod(teacher, executionPeriod, weekDay, startTime, endTime,
                PeriodType.SUPPORT_LESSON_PERIOD);

        IPersistentSupportLesson supportLessonDAO = sp.getIPersistentSupportLesson();

        try {
            List supportLessonList = supportLessonDAO.readOverlappingPeriod(teacher, executionPeriod,
                    weekDay, startTime, endTime);
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
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException("Problems on database!", e);
        }

        super.doBeforeLock(domainObjectToLock, infoObject, sp);
    }
}