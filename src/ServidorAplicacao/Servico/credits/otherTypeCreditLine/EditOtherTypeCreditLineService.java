/*
 * Created on 29/Fev/2004
 */
package ServidorAplicacao.Servico.credits.otherTypeCreditLine;

import DataBeans.InfoObject;
import DataBeans.credits.InfoOtherTypeCreditLine;
import DataBeans.util.Cloner;
import Dominio.Credits;
import Dominio.ICredits;
import Dominio.IDomainObject;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.credits.IOtherTypeCreditLine;
import ServidorAplicacao.Servico.credits.calcutation.CreditsCalculator;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.credits.IPersistentCredits;

/**
 * @author jpvl
 */
public class EditOtherTypeCreditLineService extends EditDomainObjectService
{

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(DataBeans.InfoObject)
     */
    protected IDomainObject clone2DomainObject(InfoObject infoObject)
    {
        return Cloner.copyInfoOtherTypeCreditLine2IOtherCreditLine(
            (InfoOtherTypeCreditLine) infoObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentOtherTypeCreditLine();
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
        IOtherTypeCreditLine otherTypeCreditLine =
            (IOtherTypeCreditLine) domainObjectLocked;
        
        CreditsCalculator creditsCalculator = CreditsCalculator.getInstance();
        
        ITeacher teacher =otherTypeCreditLine.getTeacher();
        IExecutionPeriod executionPeriod =otherTypeCreditLine.getExecutionPeriod();
        
        try
        {
            Double otherTypeCreditLineCredits = creditsCalculator.calcuteOtherTypeCreditLineAfterInsert(teacher, executionPeriod, otherTypeCreditLine, sp);
            IPersistentCredits creditsDAO = sp.getIPersistentCredits();
            ICredits credits = creditsDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);
            if (credits == null)
            {
                credits = new Credits();
            }
            creditsDAO.simpleLockWrite(credits);
            credits.setExecutionPeriod(executionPeriod);
            credits.setTeacher(teacher);
            credits.setOtherTypeCredits(otherTypeCreditLineCredits);
            
        } catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace();
            throw new FenixServiceException("Problems on database!", e);
        }
    }       
}
