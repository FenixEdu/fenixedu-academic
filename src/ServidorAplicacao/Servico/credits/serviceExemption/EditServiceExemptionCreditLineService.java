/*
 * Created on 29/Fev/2004
 */
package ServidorAplicacao.Servico.credits.serviceExemption;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import DataBeans.InfoObject;
import DataBeans.credits.InfoServiceExemptionCreditLine;
import DataBeans.util.Cloner;
import Dominio.Credits;
import Dominio.ICredits;
import Dominio.IDomainObject;
import Dominio.IExecutionPeriod;
import Dominio.credits.IServiceExemptionCreditLine;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.credits.IPersistentCredits;

/**
 * @author jpvl
 */
public class EditServiceExemptionCreditLineService extends EditDomainObjectService
{

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(DataBeans.InfoObject)
     */
    protected IDomainObject clone2DomainObject(InfoObject infoObject)
    {
        return (IDomainObject) Cloner.copyInfoServiceExemptionCreditLine2IServiceExemptionCreditLine(
            (InfoServiceExemptionCreditLine) infoObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentServiceExemptionCreditLine();
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
        IServiceExemptionCreditLine serviceExemptionCreditLine =
            (IServiceExemptionCreditLine) domainObjectLocked;

        IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

        try
        {
            List executionPeriods =
                executionPeriodDAO.readExecutionPeriodsInTimePeriod(
                    serviceExemptionCreditLine.getStart(),
                    serviceExemptionCreditLine.getEnd());

            if (!executionPeriods.isEmpty())
            {
                IPersistentCredits creditsDAO = sp.getIPersistentCredits();

                List creditsList =
                    creditsDAO.readByTeacherAndExecutionPeriods(
                        serviceExemptionCreditLine.getTeacher(),
                        executionPeriods);

                List executionPeriodsTreated = new ArrayList();

                for (int i = 0; i < creditsList.size(); i++)
                {
                    ICredits credits = (ICredits) creditsList.get(i);
                    creditsDAO.simpleLockWrite(credits);
                    credits.setContainsServiceExemptionsSituations(Boolean.TRUE);
                    executionPeriodsTreated.add(credits.getExecutionPeriod());
                }
                List executionPeriodsToTreat =
                    (List) CollectionUtils.subtract(executionPeriods, executionPeriodsTreated);

                for (int i = 0; i < executionPeriodsToTreat.size(); i++)
                {
                    IExecutionPeriod executionPeriod = (IExecutionPeriod) executionPeriodsToTreat.get(i);
                    ICredits credits = new Credits();
                    creditsDAO.simpleLockWrite(credits);
                    credits.setExecutionPeriod(executionPeriod);
                    credits.setTeacher(serviceExemptionCreditLine.getTeacher());
                    credits.setContainsServiceExemptionsSituations(Boolean.TRUE);
                }
            }
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException("Problems on database!", e);
        }

    }
}
