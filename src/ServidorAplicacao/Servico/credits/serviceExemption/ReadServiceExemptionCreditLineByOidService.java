/*
 * Created on 29/Fev/2004
 */
package ServidorAplicacao.Servico.credits.serviceExemption;

import DataBeans.InfoObject;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.credits.IManagementPositionCreditLine;
import Dominio.credits.IServiceExemptionCreditLine;
import Dominio.credits.ManagementPositionCreditLine;
import Dominio.credits.ServiceExemptionCreditLine;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class ReadServiceExemptionCreditLineByOidService extends ReadDomainObjectService
{

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass()
    {
        return ServiceExemptionCreditLine.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentServiceExemptionCreditLine();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#clone2InfoObject(Dominio.IDomainObject)
     */
    protected InfoObject clone2InfoObject(IDomainObject domainObject)
    {
        return Cloner.copyIServiceExemptionCreditLine2InfoServiceExemptionCreditLine(
            (IServiceExemptionCreditLine) domainObject);
    }

}
