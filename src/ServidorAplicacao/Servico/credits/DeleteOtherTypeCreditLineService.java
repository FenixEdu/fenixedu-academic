/*
 * Created on 29/Fev/2004
 */
package ServidorAplicacao.Servico.credits;

import Dominio.Credits;
import Dominio.ICredits;
import Dominio.IDomainObject;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.credits.IOtherTypeCreditLine;
import Dominio.credits.OtherTypeCreditLine;
import Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent;
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
public class DeleteOtherTypeCreditLineService extends DeleteDomainObjectService
{

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass()
    {
        return OtherTypeCreditLine.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentOtherTypeCreditLine();
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
        IOtherTypeCreditLine otherTypeCreditLine = (IOtherTypeCreditLine) domainObject;
        CreditsCalculator creditsCalculator = CreditsCalculator.getInstance();

        ITeacher teacher = otherTypeCreditLine.getTeacher();
        IExecutionPeriod executionPeriod = otherTypeCreditLine.getExecutionPeriod();

        Double otherTypeCredits =
            creditsCalculator.calcuteOtherTypeCreditLineAfterDelete(
                teacher,
                executionPeriod,
                otherTypeCreditLine,
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
        credits.setOtherTypeCredits(otherTypeCredits);

    }

}
