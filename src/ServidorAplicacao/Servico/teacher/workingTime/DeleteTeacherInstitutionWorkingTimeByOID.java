/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.workingTime;

import Dominio.Credits;
import Dominio.ICredits;
import Dominio.IDomainObject;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.teacher.workTime.ITeacherInstitutionWorkTime;
import Dominio.teacher.workTime.TeacherInstitutionWorkTime;
import ServidorAplicacao.Servico.credits.calcutation.CreditsCalculator;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.DeleteDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.credits.IPersistentCredits;

/**
 * @author jpvl
 */
public class DeleteTeacherInstitutionWorkingTimeByOID extends DeleteDomainObjectService
{
    private static DeleteTeacherInstitutionWorkingTimeByOID service =
        new DeleteTeacherInstitutionWorkingTimeByOID();

    /**
     * The singleton access method of this class.
     */
    public static DeleteTeacherInstitutionWorkingTimeByOID getService()
    {
        return service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass()
    {
        return TeacherInstitutionWorkTime.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
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
        return "DeleteTeacherInstitutionWorkingTimeByOID";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#doBeforeDelete(Dominio.IDomainObject,
     *      ServidorPersistente.ISuportePersistente)
     */
    protected void doBeforeDelete(IDomainObject domainObject, ISuportePersistente sp)
        throws FenixServiceException, ExcepcaoPersistencia
    {
        ITeacherInstitutionWorkTime teacherInstitutionWorkTime =
            (ITeacherInstitutionWorkTime) domainObject;

        ITeacher teacher = teacherInstitutionWorkTime.getTeacher();
        IExecutionPeriod executionPeriod = teacherInstitutionWorkTime.getExecutionPeriod();

        CreditsCalculator creditsCalculator = CreditsCalculator.getInstance();
        Double teacherInstitutionWorkTimeCredits =
            creditsCalculator.calcuteTeacherInstitutionWorkingTimeAfterDelete(
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
    }

}
