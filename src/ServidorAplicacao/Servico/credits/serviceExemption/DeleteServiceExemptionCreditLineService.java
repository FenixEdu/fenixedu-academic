/*
 * Created on 29/Fev/2004
 */
package ServidorAplicacao.Servico.credits.serviceExemption;

import Dominio.IDomainObject;
import Dominio.credits.IServiceExemptionCreditLine;
import Dominio.credits.ServiceExemptionCreditLine;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.DeleteDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.credits.IPersistentServiceExemptionCreditLine;

/**
 * @author jpvl
 */
public class DeleteServiceExemptionCreditLineService extends DeleteDomainObjectService
{

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass()
    {
        return ServiceExemptionCreditLine.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentServiceExemptionCreditLine();
    }

}
