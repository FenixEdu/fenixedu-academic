/*
 * Created on 29/Fev/2004
 */
package ServidorAplicacao.Servico.credits.managementPosition;

import DataBeans.InfoObject;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.credits.IManagementPositionCreditLine;
import Dominio.credits.ManagementPositionCreditLine;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class ReadManagementPositionCreditLineByOidService extends ReadDomainObjectService
{

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass()
    {
        return ManagementPositionCreditLine.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentManagementPositionCreditLine();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#clone2InfoObject(Dominio.IDomainObject)
     */
    protected InfoObject clone2InfoObject(IDomainObject domainObject)
    {
        return Cloner.copyIManagementPositionCreditLine2InfoManagementPositionCreditLine(
            (IManagementPositionCreditLine) domainObject);
    }

}
