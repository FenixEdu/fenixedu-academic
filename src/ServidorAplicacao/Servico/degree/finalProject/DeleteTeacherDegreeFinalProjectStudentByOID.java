/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.degree.finalProject;

import Dominio.Credits;
import Dominio.ICredits;
import Dominio.IDomainObject;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import Dominio.degree.finalProject.TeacherDegreeFinalProjectStudent;
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
public class DeleteTeacherDegreeFinalProjectStudentByOID extends DeleteDomainObjectService
{
    private static DeleteTeacherDegreeFinalProjectStudentByOID service =
        new DeleteTeacherDegreeFinalProjectStudentByOID();

    /**
     * The singleton access method of this class.
     */
    public static DeleteTeacherDegreeFinalProjectStudentByOID getService()
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
        return TeacherDegreeFinalProjectStudent.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentTeacherDegreeFinalProjectStudent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome()
    {
        return "DeleteTeacherDegreeFinalProjectStudentByOID";
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
        ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent =
            (ITeacherDegreeFinalProjectStudent) domainObject;
        CreditsCalculator creditsCalculator = CreditsCalculator.getInstance();

        ITeacher teacher = teacherDegreeFinalProjectStudent.getTeacher();
        IExecutionPeriod executionPeriod = teacherDegreeFinalProjectStudent.getExecutionPeriod();

        Double teacherDegreeFinalProjectStudentsCredits;
        teacherDegreeFinalProjectStudentsCredits =
            creditsCalculator.calcuteDegreeFinalProjectStudentAfterDelete(
                teacher,
                executionPeriod,
                teacherDegreeFinalProjectStudent,
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
        credits.setDegreeFinalProjectStudents(teacherDegreeFinalProjectStudentsCredits);

    }

}
