/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.professorship;

import Dominio.Credits;
import Dominio.ICredits;
import Dominio.IDomainObject;
import Dominio.IExecutionPeriod;
import Dominio.ISupportLesson;
import Dominio.ITeacher;
import Dominio.SupportLesson;
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
public class DeleteSupportLessonByOID extends DeleteDomainObjectService
{
    public DeleteSupportLessonByOID()
    {
    }
    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass()
    {
        // TODO Auto-generated method stub
        return SupportLesson.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentSupportLesson();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome()
    {
        return "DeleteSupportLessonByOID";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#doAfterDelete(Dominio.IDomainObject)
     */
    protected void doBeforeDelete(IDomainObject domainObject, ISuportePersistente sp)
        throws FenixServiceException, ExcepcaoPersistencia
    {
        ISupportLesson supportLesson = (ISupportLesson) domainObject;
        CreditsCalculator creditsCalculator = CreditsCalculator.getInstance();

        ITeacher teacher = supportLesson.getProfessorship().getTeacher();
        IExecutionPeriod executionPeriod =
            supportLesson.getProfessorship().getExecutionCourse().getExecutionPeriod();

        Double supportLessonsCredits;
        supportLessonsCredits =
            creditsCalculator.calculateSupportLessonsAfterDelete(
                teacher,
                executionPeriod,
                supportLesson,
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
        credits.setSupportLessons(supportLessonsCredits);

    }

}
